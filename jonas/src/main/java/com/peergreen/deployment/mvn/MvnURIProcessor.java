/**
 * Copyright 2012 Peergreen S.A.S.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.peergreen.deployment.mvn;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;

import com.peergreen.deployment.Artifact;
import com.peergreen.deployment.DiscoveryPhasesLifecycle;
import com.peergreen.deployment.ProcessorContext;
import com.peergreen.deployment.ProcessorException;
import com.peergreen.deployment.processor.Discovery;
import com.peergreen.deployment.processor.Uri;
import com.peergreen.deployment.processor.Processor;

/**
 * mvn: URI processor
 * @author Florent Benoit
 */
@Processor
@Discovery(DiscoveryPhasesLifecycle.URI_FETCHER)
@Uri("mvn")
public class MvnURIProcessor {

    private final File cacheDir;

    public MvnURIProcessor() {
        String userDir = System.getProperty("user.dir");
        cacheDir = new File(userDir + File.separator + "mvn-cache");
    }

    public void handle(Artifact artifact, ProcessorContext processorContext) throws ProcessorException {

        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }

        String fileName = String.valueOf(artifact.uri().toString().hashCode()).concat(artifact.name().replace(":", "_"));

        // Dump inputstream if not exists
        File dumpedFile = new File(cacheDir, fileName);

        // File doesn't exists
        if (!dumpedFile.exists()) {

            synchronized (this) {

                // It's a mvn URI from requirements, so get the resource
                try (InputStream is = artifact.uri().toURL().openStream(); FileOutputStream fos = new FileOutputStream(dumpedFile)) {
                    System.out.println("Fetching uri " + artifact.uri() + "...");

                    int len;
                    byte[] b = new byte[4096];
                    // dump stream
                    while ((len = is.read(b, 0, b.length)) != -1) {
                        fos.write(b, 0, len);
                    }
                } catch (IOException e) {
                    throw new ProcessorException("Unable to read inputstream from '" + artifact.uri() + "'", e);
                }
            }
        }

        // Now, add the dumped artifact
        URI dumpedURI = dumpedFile.toURI();
        Artifact newArtifact = processorContext.build(artifact.name(), dumpedURI);
        processorContext.addArtifact(newArtifact);

    }


}

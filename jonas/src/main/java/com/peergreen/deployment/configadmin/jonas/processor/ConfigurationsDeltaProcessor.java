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
package com.peergreen.deployment.configadmin.jonas.processor;

import static java.lang.String.format;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.apache.felix.ipojo.annotations.Requires;

import com.peergreen.deployment.DeploymentContext;
import com.peergreen.deployment.ProcessorContext;
import com.peergreen.deployment.ProcessorException;
import com.peergreen.deployment.configadmin.jonas.ConfigAdmin;
import com.peergreen.deployment.configadmin.jonas.processor.delta.ConfigAdminDeltaService;
import com.peergreen.deployment.configadmin.jonas.processor.delta.Delta;
import com.peergreen.deployment.configadmin.jonas.processor.parser.ConfigAdminParser;
import com.peergreen.deployment.processor.Phase;
import com.peergreen.deployment.processor.Processor;

/**
 * Compute the diff between old content and new file content
 * @author Guillaume Sauthier
 */
@Processor
@Phase("compute-diff")
public class ConfigurationsDeltaProcessor {

    private final ConfigAdminParser parser;
    private final ConfigAdminDeltaService deltaService;

    public ConfigurationsDeltaProcessor(@Requires ConfigAdminParser parser,
                                        @Requires ConfigAdminDeltaService deltaService) {
        this.parser = parser;
        this.deltaService = deltaService;
    }

    public void handle(DeploymentContext context, ProcessorContext processorContext) throws ProcessorException {
        try {
            URI artifact = context.getArtifact().uri();
            ConfigAdmin latest = parser.parse(artifact.toURL());
            ConfigAdmin previous = context.getFacet(ConfigAdmin.class);

            List<Delta> deltas = deltaService.delta(previous, latest);
            processorContext.addFacet(Deltas.class, new Deltas(deltas));
        } catch (XMLStreamException e) {
            throw new ProcessorException(format("Cannot parse artifact's content for %s", context.getArtifact()), e);
        } catch (MalformedURLException e) {
            throw new ProcessorException(format("'%s' is not adaptable to a valid URL", context.getArtifact().uri()), e);
        }
    }


}

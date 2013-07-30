/**
 * Copyright 2013 Peergreen S.A.S.
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

import static org.mockito.Mockito.doReturn;

import java.net.URI;
import java.net.URISyntaxException;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.peergreen.deployment.Artifact;
import com.peergreen.deployment.ProcessorException;

public class TestMvnProcessor {

    private MvnURIProcessor mvnURIProcessor;

    @Mock
    private Artifact artifact;


    @BeforeMethod
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.mvnURIProcessor = new MvnURIProcessor();

        doReturn("test-mock").when(artifact).name();

    }


    @Test
    public void checkFileNameFromUri() throws URISyntaxException, ProcessorException {
        doReturn(new URI("mvn:com.peergreen.webconsole/web-console-community/1.0.0-SNAPSHOT")).when(artifact).uri();
        String fileName = mvnURIProcessor.getFileName(artifact);
        Assert.assertEquals(fileName, "com.peergreen.webconsole-web-console-community-1.0.0-SNAPSHOT.jar");
    }

    @Test
    public void checkFileNameFromUriWithType() throws URISyntaxException, ProcessorException {
        doReturn(new URI("mvn:com.peergreen.webconsole/web-console-community/1.0.0-SNAPSHOT/war")).when(artifact).uri();
        String fileName = mvnURIProcessor.getFileName(artifact);
        Assert.assertEquals(fileName, "com.peergreen.webconsole-web-console-community-1.0.0-SNAPSHOT.war");
    }

    @Test
    public void checkFileNameFromUriWithClassifier() throws URISyntaxException, ProcessorException {
        doReturn(new URI("mvn:com.peergreen.webconsole/web-console-community/1.0.0-SNAPSHOT/war/bin")).when(artifact).uri();
        String fileName = mvnURIProcessor.getFileName(artifact);
        Assert.assertEquals(fileName, "com.peergreen.webconsole-web-console-community-bin-1.0.0-SNAPSHOT.war");
    }


}

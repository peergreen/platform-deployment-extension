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

import java.io.InputStream;
import java.io.InputStreamReader;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;

import com.peergreen.deployment.DiscoveryPhasesLifecycle;
import com.peergreen.deployment.ProcessorContext;
import com.peergreen.deployment.ProcessorException;
import com.peergreen.deployment.configadmin.jonas.ConfigAdmin;
import com.peergreen.deployment.configadmin.jonas.processor.parser.ConfigAdminParser;
import com.peergreen.deployment.facet.content.Content;
import com.peergreen.deployment.facet.content.ContentException;
import com.peergreen.deployment.processor.Discovery;
import com.peergreen.deployment.processor.Uri;
import com.peergreen.deployment.processor.XmlNamespace;
import com.peergreen.deployment.processor.handler.Processor;

/**
 * Deployment plan scanner.
 * @author Florent Benoit
 */
@Component
@Instantiate
@Processor
@Discovery(DiscoveryPhasesLifecycle.DEPENDENCY_FINDER)
@Uri(extension = "xml")
@XmlNamespace(ConfigAdminParser.NAMESPACE)
public class ConfigurationsFacetScannerProcessor {

    private final XMLInputFactory factory;
    private final ConfigAdminParser parser;

    public ConfigurationsFacetScannerProcessor() {
        this(XMLInputFactory.newInstance(), new ConfigAdminParser());
    }

    public ConfigurationsFacetScannerProcessor(XMLInputFactory factory, ConfigAdminParser configAdminParser) {
        this.factory = factory;
        this.parser = configAdminParser;
    }

    /**
     * Needs to parse the XML file which is the deployment plan.
     */
    public void handle(Content content, ProcessorContext context) throws ProcessorException {

        // Parse the content of the XML file
        InputStream is = null;
        try {
            is = content.getInputStream();
        } catch (ContentException e) {
            throw new ProcessorException("Unable to get InputStream", e);
        }

        try {
            XMLEventReader reader = factory.createXMLEventReader(new InputStreamReader(is));
            // Parse + add facet
            context.addFacet(ConfigAdmin.class, parser.parse(reader));
        } catch (XMLStreamException e) {
            throw new ProcessorException("Unable to load configurations", e);
        }

    }


}

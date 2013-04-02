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

import java.io.IOException;

import org.osgi.service.cm.Configuration;

import com.peergreen.deployment.Processor;
import com.peergreen.deployment.ProcessorContext;
import com.peergreen.deployment.ProcessorException;
import com.peergreen.deployment.configadmin.jonas.ConfigAdmin;
import com.peergreen.deployment.configadmin.jonas.processor.parser.ConfigAdminParser;

/**
 * Install the OSGi bundles on the gateway.
 * @author Florent Benoit
 */
public class ConfigurationsUpdateProcessor implements Processor<ConfigAdmin> {

    private final ConfigAdminParser parser;

    public ConfigurationsUpdateProcessor(ConfigAdminParser parser) {
        this.parser = parser;
    }

    @Override
    public void handle(ConfigAdmin configAdmin, ProcessorContext context) throws ProcessorException {

        // Cannot access both the ConfigAdmin facet and the new Content
        //context.
        // Mayh be I have to use DeploymentContext
        try {
            for (Configuration c : configAdmin.getConfigurations()) {
                try {
                    c.delete();
                } catch (IOException e) {
                    // TODO Ignore and log something
                }
            }
        } finally {
            configAdmin.clear();
        }
    }

}
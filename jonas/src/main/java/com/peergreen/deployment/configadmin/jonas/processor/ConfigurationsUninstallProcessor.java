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

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.osgi.service.cm.Configuration;

import com.peergreen.deployment.ProcessorContext;
import com.peergreen.deployment.ProcessorException;
import com.peergreen.deployment.configadmin.jonas.ConfigAdmin;
import com.peergreen.deployment.processor.Phase;
import com.peergreen.deployment.processor.Processor;

/**
 * Delete the managed configurations from config admin service.
 * @author Guillaume Sauthier
 */
@Processor
@Phase("delete")
public class ConfigurationsUninstallProcessor {

    public void handle(ConfigAdmin configAdmin, ProcessorContext context) throws ProcessorException {

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

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
import org.apache.felix.ipojo.annotations.Requires;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import com.peergreen.deployment.ProcessorContext;
import com.peergreen.deployment.ProcessorException;
import com.peergreen.deployment.configadmin.jonas.ConfigAdmin;
import com.peergreen.deployment.configadmin.jonas.ConfigurationInfo;
import com.peergreen.deployment.processor.Phase;
import com.peergreen.deployment.processor.handler.Processor;

/**
 * Install the OSGi bundles on the gateway.
 * @author Florent Benoit
 */
@Component
@Instantiate
@Processor
@Phase("INSTALL")
public class ConfigurationsInstallProcessor {

    private ConfigurationAdmin configurationAdmin;

    public ConfigurationsInstallProcessor(@Requires ConfigurationAdmin configurationAdmin) {
        this.configurationAdmin = configurationAdmin;
    }

    public void handle(ConfigAdmin configAdmin, ProcessorContext context) throws ProcessorException {

        for (ConfigurationInfo info : configAdmin.getInfos()) {
            if (info.isFactory()) {
                try {
                    Configuration configuration = configurationAdmin.createFactoryConfiguration(info.getPid(), null);
                    configuration.update(info.getProperties());
                    configAdmin.associate(info, configuration);
                } catch (IOException e) {
                    // TODO Log a warning
                }
            } else {
                try {
                    Configuration c = configurationAdmin.getConfiguration(info.getPid(), null);
                    c.update(info.getProperties());
                    configAdmin.associate(info, c);
                } catch (IOException e) {
                    // Ignored ATM, continue
                }
            }
        }
    }

}

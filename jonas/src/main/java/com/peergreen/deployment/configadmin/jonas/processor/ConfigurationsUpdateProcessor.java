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

import org.apache.felix.ipojo.annotations.Requires;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import com.peergreen.deployment.Artifact;
import com.peergreen.deployment.ProcessorContext;
import com.peergreen.deployment.ProcessorException;
import com.peergreen.deployment.configadmin.jonas.ConfigAdmin;
import com.peergreen.deployment.configadmin.jonas.ConfigurationInfo;
import com.peergreen.deployment.configadmin.jonas.processor.delta.Delta;
import com.peergreen.deployment.processor.Phase;
import com.peergreen.deployment.processor.Processor;

/**
 * Apply the delta computed on earlier phases
 * @author Guillaume Sauthier
 */
@Processor
@Phase("update")
public class ConfigurationsUpdateProcessor {

    private ConfigurationAdmin configurationAdmin;

    public ConfigurationsUpdateProcessor(@Requires ConfigurationAdmin configurationAdmin) {
        this.configurationAdmin = configurationAdmin;
    }

    public void handle(Artifact artifact, ProcessorContext processorContext) throws ProcessorException {
        Deltas deltas = artifact.as(Deltas.class);
        ConfigAdmin configAdmin = artifact.as(ConfigAdmin.class);

        for (Delta delta : deltas) {
            switch (delta.getKind()) {
                case ADDED:
                    createConfiguration(configAdmin, delta.getActual());
                    break;
                case REMOVED:
                    deleteConfiguration(configAdmin, delta.getPrevious());
                    break;
                case CHANGED:
                    updateConfiguration(configAdmin, delta.getPrevious(), delta.getActual());
                    break;
                case UNCHANGED:
                    // do nothing
            }
        }

    }

    private void createConfiguration(final ConfigAdmin configAdmin, final ConfigurationInfo info) {
        try {
            // Add the model
            configAdmin.add(info);

            // Really create the OSGi Configuration
            Configuration configuration;
            if (info.isFactory()) {
                configuration = configurationAdmin.createFactoryConfiguration(info.getPid(), null);
            } else {
                configuration = configurationAdmin.getConfiguration(info.getPid(), null);
            }
            // Update the config and update the model
            configuration.update(info.getProperties());
            configAdmin.associate(info, configuration);
        } catch (IOException e) {
            // Ignored ATM
        }
    }

    private void deleteConfiguration(final ConfigAdmin configAdmin, final ConfigurationInfo info) {
        try {
            Configuration configuration = configAdmin.remove(info);
            configuration.delete();
        } catch (IOException e) {
            // Ignored ATM
        }
    }

    private void updateConfiguration(final ConfigAdmin configAdmin,
                                     final ConfigurationInfo previous,
                                     final ConfigurationInfo actual) {
        try {
            Configuration configuration = configAdmin.remove(previous);
            configuration.update(actual.getProperties());
            configAdmin.associate(actual, configuration);
        } catch (IOException e) {
            // Ignored ATM
        }
    }

}

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
package com.peergreen.deployment.configadmin.jonas.persistence;

import static java.lang.String.format;

import java.io.IOException;
import java.util.Collection;

import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import com.peergreen.deployment.Artifact;
import com.peergreen.deployment.PersistenceArtifactManager;
import com.peergreen.deployment.configadmin.jonas.Constants;

/**
 * Support transient Configuration of ConfigAdmin.
 * @author Guillaume Sauthier
 */
public class ConfigurationsPersistenceArtifactManager implements PersistenceArtifactManager {

    private final ConfigurationAdmin configurationAdmin;

    public ConfigurationsPersistenceArtifactManager(ConfigurationAdmin configurationAdmin) {
        this.configurationAdmin = configurationAdmin;
    }

    //FIXME : needs to send a report !!!
    @Override
    public void forget(Collection<Artifact> artifacts) {

        try {
            Configuration[] configurations = configurationAdmin.listConfigurations(
                    format("(%s=*)", Constants.TRANSIENT_PROPERTY)
            );
            if (configurations != null) {
                for (Configuration configuration : configurations) {
                    configuration.delete();
                }
            }
        } catch (IOException e) {
            // Ignore
        } catch (InvalidSyntaxException e) {
            // Should never happen
        }

    }

}

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
package com.peergreen.deployment.configadmin.jonas.persistence;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.osgi.service.cm.ConfigurationAdmin;

import com.peergreen.deployment.DelegatePersistenceArtifactManager;
import com.peergreen.deployment.ProcessorException;

@Component
@Provides
@Instantiate
public class ConfigurationsDelegatePersistenceArtifactManager extends DelegatePersistenceArtifactManager {

    public ConfigurationsDelegatePersistenceArtifactManager(@Requires ConfigurationAdmin service) throws ProcessorException {
        super(new ConfigurationsPersistenceArtifactManager(service));
    }

}

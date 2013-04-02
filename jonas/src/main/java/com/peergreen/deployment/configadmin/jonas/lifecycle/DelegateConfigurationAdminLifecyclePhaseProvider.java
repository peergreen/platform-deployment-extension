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
package com.peergreen.deployment.configadmin.jonas.lifecycle;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;

import com.peergreen.deployment.DelegateInternalLifeCyclePhaseProvider;
import com.peergreen.deployment.configadmin.jonas.ConfigAdmin;

@Component
@Provides
@Instantiate(name = "OW2 JOnAS Configuration Admin Lifecycle Provider")
public class DelegateConfigurationAdminLifecyclePhaseProvider extends DelegateInternalLifeCyclePhaseProvider<ConfigAdmin> {

    public DelegateConfigurationAdminLifecyclePhaseProvider() {
        super(new ConfigurationAdminLifeCyclePhaseProvider(), ConfigAdmin.class);
    }

}

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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.peergreen.deployment.DeploymentMode;
import com.peergreen.deployment.FacetLifeCyclePhaseProvider;
import com.peergreen.deployment.configadmin.jonas.ConfigAdmin;

public class ConfigurationAdminLifeCyclePhaseProvider implements FacetLifeCyclePhaseProvider<ConfigAdmin> {

    private static final List<String> DEPLOY_PHASES = Collections.singletonList("create");
    private static final List<String> UPDATE_PHASES = Arrays.asList("compute-diff", "update");
    private static final List<String> UNDEPLOY_PHASES = Collections.singletonList("delete");

    @Override
    public List<String> getLifeCyclePhases(DeploymentMode deploymentMode) {
        switch (deploymentMode) {
            case DEPLOY:
                return DEPLOY_PHASES;
            case UPDATE:
                return UPDATE_PHASES;
            case UNDEPLOY:
                return UNDEPLOY_PHASES;
            default:
                throw new IllegalStateException("Deployment mode '" + deploymentMode + "' not supported");
        }
    }



}

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

import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Validate;

import com.peergreen.deployment.DelegateHandlerProcessor;
import com.peergreen.deployment.DeploymentContext;
import com.peergreen.deployment.DiscoveryPhasesLifecycle;
import com.peergreen.deployment.ProcessorException;
import com.peergreen.deployment.configadmin.jonas.processor.parser.ConfigAdminParser;
import com.peergreen.deployment.facet.content.Content;
import com.peergreen.deployment.resource.builder.RequirementBuilder;

@Component
@Provides
@Instantiate(name="OW2 JOnAS Configuration Admin Facet Scanner")
public class ConfigurationsFacetScannerDelegateInternalProcessor extends DelegateHandlerProcessor<Content> {

    public ConfigurationsFacetScannerDelegateInternalProcessor() throws ProcessorException {
        super(new ConfigurationsFacetScannerProcessor(), Content.class);
    }


    @Override
    @Bind
    public void bindRequirementBuilder(RequirementBuilder requirementBuilder) {
        super.bindRequirementBuilder(requirementBuilder);
    }

    @Validate
    protected void addRequirements() {

        // Execute only on artifacts with XML path extension
        addRequirement(getRequirementBuilder().buildArtifactRequirement(this).setPathExtension("xml"));

        // Execute only on a specific XML namespace
        addRequirement(getRequirementBuilder().buildXMLContentRequirement(this).setNamespace(ConfigAdminParser.NAMESPACE));

        // Execute at the DEPENDENCY_FINDER lifecycle
        // FIXME This should be FACET_SCANNER
        addRequirement(getRequirementBuilder().buildPhaseRequirement(this, DiscoveryPhasesLifecycle.DEPENDENCY_FINDER.toString()));

    }

}

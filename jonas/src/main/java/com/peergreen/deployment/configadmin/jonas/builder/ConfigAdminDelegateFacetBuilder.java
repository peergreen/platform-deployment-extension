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

package com.peergreen.deployment.configadmin.jonas.builder;

import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.osgi.service.cm.ConfigurationAdmin;

import com.peergreen.deployment.DelegateFacetBuilder;
import com.peergreen.deployment.configadmin.jonas.ConfigAdmin;
import com.peergreen.deployment.facet.content.XMLContent;
import com.peergreen.deployment.resource.builder.CapabilityBuilder;
import com.peergreen.deployment.resource.builder.RequirementBuilder;


/**
 * Defines builder for ConfigAdmin
 * @author Guillaume Sauthier
 */
@Component
@Provides
@Instantiate
public class ConfigAdminDelegateFacetBuilder extends DelegateFacetBuilder<ConfigAdmin> {

    public ConfigAdminDelegateFacetBuilder(@Requires ConfigurationAdmin configurationAdmin) {
        super(new ConfigAdminFacetBuilder(configurationAdmin), ConfigAdmin.class);
    }

    @Override
    @Bind
    public void bindRequirementBuilder(RequirementBuilder requirementBuilder) {
        super.bindRequirementBuilder(requirementBuilder);
    }

    @Override
    @Bind
    public void bindCapabilityBuilder(CapabilityBuilder capabilityBuilder) {
        super.bindCapabilityBuilder(capabilityBuilder);
    }

    @Validate
    protected void addRequirementsAndCapabilities() {
        // Execute only on XML contents
        addRequirement(getRequirementBuilder().buildFacetRequirement(this, XMLContent.class));

        // Add capability of the facet
        super.postConstruct();
    }

}

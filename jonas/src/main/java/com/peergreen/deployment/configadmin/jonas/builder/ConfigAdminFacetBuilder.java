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

import static java.lang.String.format;

import java.util.Collections;
import java.util.Dictionary;

import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import com.peergreen.deployment.configadmin.jonas.ConfigAdmin;
import com.peergreen.deployment.configadmin.jonas.ConfigurationInfo;
import com.peergreen.deployment.configadmin.jonas.Constants;
import com.peergreen.deployment.facet.builder.BuilderContext;
import com.peergreen.deployment.facet.builder.FacetBuilder;
import com.peergreen.deployment.facet.builder.FacetBuilderException;


/**
 * Defines builder for ConfigAdmin
 * @author Guillaume Sauthier
 */
public class ConfigAdminFacetBuilder implements FacetBuilder<ConfigAdmin> {

    private final ConfigurationAdmin configurationAdmin;

    public ConfigAdminFacetBuilder(final ConfigurationAdmin configurationAdmin) {
        this.configurationAdmin = configurationAdmin;
    }

    @Override
    public void build(BuilderContext<ConfigAdmin> context) throws FacetBuilderException {

        // Fail fast if the XmlContent is not a config-admin file

        ConfigAdmin ca = new ConfigAdmin();
        String uri = context.getArtifact().uri().toString();
        try {
            Configuration[] configurations = configurationAdmin.listConfigurations(format("(%s=%s)", Constants.URI_PROPERTY, uri));
            if (configurations != null) {
                for (Configuration configuration : configurations) {
                    ConfigurationInfo info = buildConfigurationInfo(configuration);
                    ca.associate(info, configuration);
                }
            }
        } catch (Exception e) {
            throw new FacetBuilderException(
                    String.format("Cannot re-build ConfigAdmin facet for artifact %s",
                                  context.getArtifact().name()),
                    e);
        }

        // Add the rebuilt facet
        context.addFacet(ca);
    }

    private ConfigurationInfo buildConfigurationInfo(final Configuration configuration) {
        ConfigurationInfo info;

        String factoryPid = configuration.getFactoryPid();
        if (factoryPid != null) {
            // Re-load factory id (if any)
            String id = (String) configuration.getProperties().get(Constants.ID_PROPERTY);
            if (id != null) {
                info = new ConfigurationInfo(factoryPid, id, true);
            } else {
                info = new ConfigurationInfo(factoryPid, true);
            }
        } else {
            info = new ConfigurationInfo(configuration.getPid());
        }

        Dictionary<String, Object> properties = info.getProperties();
        for (String key : Collections.list(configuration.getProperties().keys())) {
            properties.put(key, configuration.getProperties().get(key));
        }

        return info;
    }

}

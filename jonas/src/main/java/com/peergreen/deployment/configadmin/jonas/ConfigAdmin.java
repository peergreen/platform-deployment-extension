package com.peergreen.deployment.configadmin.jonas;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.osgi.service.cm.Configuration;

/**
 * User: guillaume
 * Date: 27/03/13
 * Time: 21:23
 */
public class ConfigAdmin {
    private final Map<ConfigurationInfo, Configuration> configurations = new HashMap<>();

    public void add(ConfigurationInfo info) {
        configurations.put(info, null);
    }

    public Set<ConfigurationInfo> getInfos() {
        return configurations.keySet();
    }

    public void associate(ConfigurationInfo info, Configuration configuration) {
        configurations.put(info, configuration);
    }

    public Collection<Configuration> getConfigurations() {
        return configurations.values();
    }

    public void clear() {
        configurations.clear();
    }
}

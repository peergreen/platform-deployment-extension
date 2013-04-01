package com.peergreen.deployment.configadmin.jonas;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Properties;

/**
 * User: guillaume
 * Date: 27/03/13
 * Time: 21:55
 */
public class ConfigurationInfo {
    private final String pid;
    private final boolean factory;
    private final Dictionary<String, Object> properties = new Hashtable<>();

    public ConfigurationInfo(String pid) {
        this(pid, false);
    }

    public ConfigurationInfo(String pid, boolean factory) {
        this.pid = pid;
        this.factory = factory;
    }

    public void addProperty(String name, String value) {
        properties.put(name, value);
    }

    public String getPid() {
        return pid;
    }

    public boolean isFactory() {
        return factory;
    }

    public Dictionary<String, Object> getProperties() {
        return properties;
    }
}

package com.peergreen.deployment.configadmin.jonas;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * User: guillaume
 * Date: 27/03/13
 * Time: 21:55
 */
public class ConfigurationInfo {
    private final String pid;
    /**
     * Xml Identifier, may be null.
     */
    private final String id;

    private final boolean factory;
    private final Dictionary<String, Object> properties = new Hashtable<>();

    public ConfigurationInfo(String pid) {
        this(pid, pid, false);
    }

    /**
     * Usually this constructor is here to create a factory-configuration
     * It is equivalent to call {@link ConfigurationInfo(String, String, boolean)} with a {@literal null} id.
     * @param pid ManagedServiceFactory service.pid
     * @param factory is this a factory (should be true)
     */
    public ConfigurationInfo(String pid, boolean factory) {
        this(pid, null, factory);
    }

    public ConfigurationInfo(String pid, final String id, boolean factory) {
        this.pid = pid;
        this.id = id;
        this.factory = factory;
    }

    public ConfigurationInfo addProperty(String name, String value) {
        properties.put(name, value);
        return this;
    }

    public String getPid() {
        return pid;
    }

    public String getId() {
        return id;
    }

    public boolean isFactory() {
        return factory;
    }

    public Dictionary<String, Object> getProperties() {
        return properties;
    }
}

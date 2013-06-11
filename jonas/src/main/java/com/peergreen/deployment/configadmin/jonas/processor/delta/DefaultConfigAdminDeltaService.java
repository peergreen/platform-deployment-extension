package com.peergreen.deployment.configadmin.jonas.processor.delta;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;

import com.peergreen.deployment.configadmin.jonas.ConfigAdmin;
import com.peergreen.deployment.configadmin.jonas.ConfigurationInfo;

/**
 * User: guillaume
 * Date: 11/06/13
 * Time: 12:06
 */
@Component
@Provides
@Instantiate
public class DefaultConfigAdminDeltaService implements ConfigAdminDeltaService {
    @Override
    public List<Delta> delta(final ConfigAdmin previous, final ConfigAdmin actual) {
        // Change structure for easy element removal
        Deque<ConfigurationInfo> configurationsPrevious = new ArrayDeque<>(previous.getInfos());
        Deque<ConfigurationInfo> configurationsActual = new ArrayDeque<>(actual.getInfos());
        return computeDeltas(configurationsPrevious, configurationsActual);
    }

    private List<Delta> computeDeltas(final Deque<ConfigurationInfo> previous,
                                        final Deque<ConfigurationInfo> actual) {
        List<Delta> deltas = new ArrayList<>();
        while (!previous.isEmpty()) {
            ConfigurationInfo old = previous.pop();
            ConfigurationInfo match = findConfiguration(old, actual);
            if (match != null) {

                if (incompatibleChange(old, match)) {
                    deltas.add(new Delta(old, null, Kind.REMOVED));
                    deltas.add(new Delta(null, match, Kind.ADDED));
                } else {
                    // Were the configuration details changed ?
                    if (arePropertiesChanged(old, match)) {
                        // Configuration's properties have been modified
                        deltas.add(new Delta(old, match, Kind.CHANGED));
                    } else {
                        // No changes, configuration remains unchanged
                        deltas.add(new Delta(old, match, Kind.UNCHANGED));
                    }
                }
                actual.remove(match);
            } else {
                // Simple case: configuration was removed in actual info set
                deltas.add(new Delta(old, null, Kind.REMOVED));
            }
        }

        // Remaining items in actual are new Items
        while (!actual.isEmpty()) {
            deltas.add(new Delta(null, actual.pop(), Kind.ADDED));
        }

        return deltas;
    }

    private boolean incompatibleChange(final ConfigurationInfo previous,
                                       final ConfigurationInfo actual) {
        // Strange cases first
        // ------------------------------------
        // Same xml:id but target PID has changed
        if (!previous.getPid().equals(actual.getPid())) {
            return true;
        }
        // Same xml:id but configuration type has changed
        if (previous.isFactory() != actual.isFactory()) {
            return true;
        }
        return false;

    }

    private boolean arePropertiesChanged(final ConfigurationInfo previous,
                                         final ConfigurationInfo actual) {

        if (previous.getProperties().size() != actual.getProperties().size()) {
            // More or less properties in latest
            return true;
        }

        for (String key : Collections.list(previous.getProperties().keys())) {
            Object value = previous.getProperties().get(key);
            if (value != actual.getProperties().get(key)) {
                return true;
            }
        }

        return false;
    }

    private ConfigurationInfo findConfiguration(final ConfigurationInfo reference,
                                                final Deque<ConfigurationInfo> infos) {
        if (!reference.isFactory()) {
            Deque<ConfigurationInfo> copy = new ArrayDeque<>(infos);
            for (ConfigurationInfo info : copy) {
                if (info.getPid().equals(reference.getPid())) {
                    infos.remove(info);
                    return info;
                }
            }
        } else {
            if (reference.getId() == null) {
                // Cannot perform any matching without strict identifier
                return null;
            }
            Deque<ConfigurationInfo> copy = new ArrayDeque<>(infos);
            for (ConfigurationInfo info : copy) {
                if (reference.getId().equals(info.getId())) {
                    infos.remove(info);
                    return info;
                }
            }
        }
        return null;
    }

}

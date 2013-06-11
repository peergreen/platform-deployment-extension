package com.peergreen.deployment.configadmin.jonas.processor.delta;

import java.util.List;

import com.peergreen.deployment.configadmin.jonas.ConfigAdmin;

/**
 * User: guillaume
 * Date: 11/06/13
 * Time: 12:02
 */
public interface ConfigAdminDeltaService {
    List<Delta> delta(ConfigAdmin previous, ConfigAdmin actual);
}

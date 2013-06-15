package com.peergreen.deployment.configadmin.jonas.processor;

import com.peergreen.deployment.ProcessorContext;
import com.peergreen.deployment.configadmin.jonas.ConfigAdmin;
import com.peergreen.deployment.configadmin.jonas.ConfigurationInfo;
import com.peergreen.deployment.configadmin.jonas.Constants;
import com.peergreen.deployment.model.view.ArtifactModelPersistenceView;

/**
 * User: guillaume
 * Date: 14/06/13
 * Time: 14:33
 */
public abstract class AbstractConfigurationsProcessor {

    /**
     * Install the additional attributes required for configadmin persistence management
     * @param context gives access to the artifact model and the artifact itself
     * @param configAdmin instance to be modified
     */
    protected void managePersistence(final ProcessorContext context, final ConfigAdmin configAdmin) {

        // Manage persistence
        ArtifactModelPersistenceView persistenceView = context.getArtifactModel().as(ArtifactModelPersistenceView.class);
        if ((persistenceView != null) && (!persistenceView.isPersistent())) {
            // Transient configurations
            for (ConfigurationInfo info : configAdmin.getInfos()) {
                info.getProperties().put(Constants.TRANSIENT_PROPERTY, true);
            }
        } else {
            // Persisted configurations
            String uri = context.getArtifact().uri().toString();
            for (ConfigurationInfo info : configAdmin.getInfos()) {
                info.getProperties().put(Constants.URI_PROPERTY, uri);
            }
        }

        // Store id for factory configurations
        for (ConfigurationInfo info : configAdmin.getInfos()) {
            if (info.isFactory() && (info.getId() != null)) {
                info.getProperties().put(Constants.ID_PROPERTY, info.getId());
            }
        }
    }
}

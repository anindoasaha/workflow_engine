package com.anindoasaha.workflowengine.prianza.api;

import com.anindoasaha.workflowengine.prianza.bo.Entity;

public interface WorkflowInstanceEventListener extends Entity {

    /**
     * The workflow instance has just been created.
     *
     * @param event
     * @param <T>
     */
    default <T extends Event> void onInstanceCreate(T event) {
    }

    /**
     * A task in the workflow instance has finished execution.
     *
     * @param event
     * @param <T>
     */
    default <T extends Event> void onInstanceTaskExecutionFinish(T event) {
    }

    /**
     * The workflow instance has finished execution.
     *
     * @param event
     * @param <T>
     */
    default <T extends Event> void onInstanceFinish(T event) {
    }
}

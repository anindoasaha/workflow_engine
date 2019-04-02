package com.anindoasaha.workflowengine.prianza.bo;

public enum ActorType {
    /**
     * Workflow engine takes action on a task if the {@link ActorType} is SYSTEM.
     */
    SYSTEM,

    /**
     * User must take explicit action on a task if the {@link ActorType} is USER.
     */
    USER
}

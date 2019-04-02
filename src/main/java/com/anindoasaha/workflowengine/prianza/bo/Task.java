package com.anindoasaha.workflowengine.prianza.bo;

import java.util.Map;

public interface Task<K, V> extends Entity {

    @Deprecated
    default String getTaskType() {
        return this.getClass().getCanonicalName();
    }

    /**
     * The {@link ActorType} that must perform this task.
     *
     * @return
     */
    default ActorType getActorType() {
        return ActorType.USER;
    }

    Map<String, String>  getTaskVariables();

    void updateTaskVariables(Map<String, String> taskVariables);

    void setTaskVariables(Map<String, String> taskVariables);

    V beforeAction(final WorkflowInstance workflowInstance);
    V onAction(final WorkflowInstance workflowInstance);
    default V onAction(final K taskExecutionInput, final WorkflowInstance workflowInstance) {
        return null;
    }

    /**
     * Implementation must explicitly implement afterAction to alter
     * when workflowInstance should proceed.
     *
     * @param workflowInstance
     * @return
     */
    default V afterAction(final WorkflowInstance workflowInstance) {
        workflowInstance.proceed(getId());
        return null;
    }

    V onSuccess(final WorkflowInstance workflowInstance);
    V onError(final WorkflowInstance workflowInstance);
}

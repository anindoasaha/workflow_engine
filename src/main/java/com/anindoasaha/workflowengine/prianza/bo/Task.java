package com.anindoasaha.workflowengine.prianza.bo;

import java.util.Map;

public interface Task<V> {

    default String getTaskType() {
        return this.getClass().getCanonicalName();
    }

    String getId();
    void setId(String id);

    void updateTaskVariables(Map<String, String> taskVariables);

    void setTaskVariables(Map<String, String> taskVariables);

    V beforeAction(final WorkflowInstance workflowInstance);
    V onAction(final WorkflowInstance workflowInstance);
    V onSuccess(final WorkflowInstance workflowInstance);
    V onError(final WorkflowInstance workflowInstance);
}

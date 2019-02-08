package com.anindoasaha.prianza.bo;

import java.util.Map;

public interface Task<V> {

    String getId();
    void setId(String id);

    void setTaskVariables(Map<String, String> taskVariables);

    V beforeAction(final WorkflowInstance workflowInstance);
    V onAction(final WorkflowInstance workflowInstance);
    V onSuccess(final WorkflowInstance workflowInstance);
    V onError(final WorkflowInstance workflowInstance);
}

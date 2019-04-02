package com.anindoasaha.workflowengine.prianza.data;

import com.anindoasaha.workflowengine.prianza.bo.Workflow;
import com.anindoasaha.workflowengine.prianza.bo.WorkflowInstance;

import java.util.Map;

public interface WorkflowRepository {
    void addWorkflow(Workflow workflow);

    String addWorkflowInstance(WorkflowInstance workflowInstance);

    Workflow getWorkflow(String workflowId);

    Map<String, String> listWorkflows();

    Map<String, String> listWorkflowInstances();

    String updateWorkflowInstance(WorkflowInstance workflowInstance);

    WorkflowInstance getWorkflowInstance(String workflowInstanceId);

    String deleteWorkflowInstance(String workflowInstanceId);


}

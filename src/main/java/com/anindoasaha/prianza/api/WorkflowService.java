package com.anindoasaha.prianza.api;

import com.anindoasaha.prianza.bo.Workflow;
import com.anindoasaha.prianza.bo.WorkflowInstance;

import java.util.List;
import java.util.Map;

public interface WorkflowService {

    List<Workflow> listWorkflows();

    Workflow getWorkflowByWorkflowId(String workflowId);

    Workflow getWorkflowByWorkflowName(String workflowName);

    void addWorkflow(Workflow workflow);

    void updateWorkflow(Workflow workflow);

    void deleteWorkflow(String workflowId);

    WorkflowInstance createWorkflowInstance(Workflow workflow, Map<String, String> instanceVariables);

    WorkflowInstance startWorkflowInstance(WorkflowInstance workflowInstance);

    void executeWorkflowInstance(WorkflowInstance workflowInstance, Map<String, String> parameters);

    void executeWorkflowInstance(WorkflowInstance workflowInstance, String taskId, Map<String, String> parameters);

}

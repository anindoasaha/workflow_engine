package com.anindoasaha.workflowengine.prianza.api;

import com.anindoasaha.workflowengine.prianza.bo.Workflow;
import com.anindoasaha.workflowengine.prianza.bo.WorkflowInstance;

import java.util.List;
import java.util.Map;

public interface WorkflowService {

    List<Workflow> listWorkflows();

    Workflow getWorkflowByWorkflowId(String workflowId);

    Workflow getWorkflowByWorkflowName(String workflowName);

    WorkflowInstance getWorkflowInstanceByWorkflowInstanceId(String workflowInstanceId);

    void addWorkflow(Workflow workflow);

    WorkflowInstance getWorkflowInstanceByWorkflowName(String workflowName);

    void validateWorkflow(Workflow workflow);

    void updateWorkflow(Workflow workflow);

    void deleteWorkflow(String workflowId);

    String createWorkflowInstance(String workflowId, Map<String, String> instanceVariables);

    WorkflowInstance createWorkflowInstance(Workflow workflow, Map<String, String> instanceVariables);

    String startWorkflowInstance(String workflowInstanceId);

    WorkflowInstance startWorkflowInstance(WorkflowInstance workflowInstance);

    void executeWorkflowInstance(String workflowInstanceId, Map<String, String> parameters);

    void executeWorkflowInstance(WorkflowInstance workflowInstance, Map<String, String> parameters);

    void executeWorkflowInstance(String workflowInstanceId, String taskId, Map<String, String> parameters);

    void executeWorkflowInstance(WorkflowInstance workflowInstance, String taskId, Map<String, String> parameters);

}

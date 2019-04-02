package com.anindoasaha.workflowengine.prianza.data.impl;

import com.anindoasaha.workflowengine.prianza.bo.Workflow;
import com.anindoasaha.workflowengine.prianza.bo.WorkflowInstance;
import com.anindoasaha.workflowengine.prianza.data.WorkflowRepository;

import java.util.HashMap;
import java.util.Map;

public class InMemoryWorkflowRepositoryImpl implements WorkflowRepository {

    Map<String, Workflow> workflowByName = new HashMap<>();
    Map<String, Workflow> workflowById = new HashMap<>();

    Map<String, WorkflowInstance> workflowInstanceByName = new HashMap<>();
    Map<String, WorkflowInstance> workflowInstanceById = new HashMap<>();


    @Override
    public void addWorkflow(Workflow workflow) {
        workflowById.put(workflow.getId(), workflow);
        workflowByName.put(workflow.getName(), workflow);
    }

    @Override
    public String addWorkflowInstance(WorkflowInstance workflowInstance) {
        workflowInstanceById.put(workflowInstance.getId(), workflowInstance);
        workflowInstanceByName.put(workflowInstance.getName(), workflowInstance);
        return null;
    }

    @Override
    public Workflow getWorkflow(String workflowId) {
        return workflowById.get(workflowId);
    }

    @Override
    public Map<String, String> listWorkflows() {
        return new HashMap<>();
    }

    @Override
    public String updateWorkflowInstance(WorkflowInstance workflowInstance) {
        workflowInstanceById.put(workflowInstance.getId(), workflowInstance);
        workflowInstanceByName.put(workflowInstance.getName(), workflowInstance);
        return null;
    }

    @Override
    public WorkflowInstance getWorkflowInstance(String workflowInstanceId) {
        return workflowInstanceById.get(workflowInstanceId);
    }

    @Override
    public String deleteWorkflowInstance(String workflowInstanceId) {
        WorkflowInstance workflowInstance = workflowInstanceById.get(workflowInstanceId);
        workflowInstanceById.remove(workflowInstanceId);
        workflowInstanceByName.remove(workflowInstance.getName());
        return null;
    }


}

package com.anindoasaha.prianza.data.impl;

import com.anindoasaha.prianza.bo.Workflow;
import com.anindoasaha.prianza.bo.WorkflowInstance;
import com.anindoasaha.prianza.data.WorkflowRepository;

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
        workflowInstanceById.put(workflowInstance.getWorkflowInstanceId(), workflowInstance);
        workflowInstanceByName.put(workflowInstance.getWorkflowInstanceName(), workflowInstance);
        return null;
    }
}

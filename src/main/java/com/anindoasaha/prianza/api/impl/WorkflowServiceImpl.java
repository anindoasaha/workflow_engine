package com.anindoasaha.prianza.api.impl;

import com.anindoasaha.prianza.api.WorkflowService;
import com.anindoasaha.prianza.bo.Task;
import com.anindoasaha.prianza.bo.Workflow;
import com.anindoasaha.prianza.bo.WorkflowInstance;
import com.anindoasaha.prianza.data.WorkflowRepository;
import com.anindoasaha.prianza.data.impl.InMemoryWorkflowRepositoryImpl;

import java.util.*;

public class WorkflowServiceImpl implements WorkflowService {

    WorkflowRepository workflowRepository = new InMemoryWorkflowRepositoryImpl();

    @Override
    public List<Workflow> listWorkflows() {
        return null;
    }

    @Override
    public Workflow getWorkflowByWorkflowId(String workflowId) {
        return null;
    }

    @Override
    public Workflow getWorkflowByWorkflowName(String workflowName) {
        return null;
    }

    @Override
    public void addWorkflow(Workflow workflow) {
        workflowRepository.addWorkflow(workflow);
    }

    @Override
    public void updateWorkflow(Workflow workflow) {

    }

    @Override
    public void deleteWorkflow(String workflowId) {

    }

    @Override
    public WorkflowInstance createWorkflowInstance(Workflow workflow, Map<String, String> instanceVariables) {
        WorkflowInstance workflowInstance = new WorkflowInstance(
                                workflow.getName() + UUID.randomUUID().toString(),
                                                    workflow.getId(),
                                                    instanceVariables);
        workflowInstance.setTasks(workflow.getTasks());
        workflowInstance.setDirectedAcyclicGraph(workflow.getDirectedAcyclicGraph());
        workflowRepository.addWorkflowInstance(workflowInstance);
        return workflowInstance;
    }

    @Override
    public WorkflowInstance startWorkflowInstance(WorkflowInstance workflowInstance) {
        // Move to initial state(s)
        workflowInstance.setCurrentTaskIds(new ArrayList<>(workflowInstance.getTasks().keySet()));
        Set<Map.Entry<String, List<String>>> entries = workflowInstance.getDirectedAcyclicGraph().entrySet();

        for(Map.Entry<String, List<String>> entry : entries) {
            workflowInstance.removeCurrentTaskIds(entry.getValue());
        }
        return workflowInstance;
    }

    @Override
    public void executeWorkflowInstance(WorkflowInstance workflowInstance, Map<String, String> parameters) {
        executeWorkflowInstance(workflowInstance, workflowInstance.getCurrentTaskIds().get(0), parameters);
    }

    @Override
    public void executeWorkflowInstance(WorkflowInstance workflowInstance, String taskId, Map<String, String> parameters) {
        if(workflowInstance.getCurrentTaskIds().contains(taskId)) {
            Task task = workflowInstance.getTasks().get(taskId);
            task.setTaskVariables(parameters);
            task.onAction(workflowInstance);
            task.onSuccess(workflowInstance);
            workflowInstance.addExecutedTaskId(taskId);
            // Compute if any task can be added to currentTaskIds
            // TODO Remove tasks which still haven't met their dependencies
            workflowInstance.addCurrentTaskIds(workflowInstance.getDirectedAcyclicGraph().get(taskId));
            // Compute if we have exhausted all tasks
            if(workflowInstance.getExecutedTaskIds().containsAll(workflowInstance.getTasks().keySet())) {
                workflowInstance.setWorkflowInstanceStatus(WorkflowInstance.WORFLOW_INSTANCE_FINISHED);
            }
        } else {

        }
    }

}

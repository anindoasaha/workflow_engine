package com.anindoasaha.workflowengine.prianza.api.impl;

import com.anindoasaha.workflowengine.prianza.api.WorkflowService;
import com.anindoasaha.workflowengine.prianza.bo.Task;
import com.anindoasaha.workflowengine.prianza.bo.Workflow;
import com.anindoasaha.workflowengine.prianza.bo.WorkflowInstance;
import com.anindoasaha.workflowengine.prianza.data.WorkflowRepository;
import com.anindoasaha.workflowengine.prianza.data.impl.FileBasedWorkflowRepositoryImpl;
import com.anindoasaha.workflowengine.prianza.data.impl.InMemoryWorkflowRepositoryImpl;
import com.anindoasaha.workflowengine.prianza.task.StdOutTask;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class WorkflowServiceImpl implements WorkflowService {

    private WorkflowRepository workflowRepository = new FileBasedWorkflowRepositoryImpl();

    public WorkflowServiceImpl setWorkflowRepository(WorkflowRepository workflowRepository) {
        this.workflowRepository = workflowRepository;
        return this;
    }

    @Override
    public List<Workflow> listWorkflows() {
        return new ArrayList<>();
    }

    @Override
    public WorkflowInstance getWorkflowInstanceByWorkflowInstanceId(String workflowInstanceId) {
        return workflowRepository.getWorkflowInstance(workflowInstanceId);
    }

    @Override
    public WorkflowInstance getWorkflowInstanceByWorkflowName(String workflowName) {
        return null;
    }


    @Override
    public Workflow getWorkflowByWorkflowId(String workflowId) {
        return workflowRepository.getWorkflow(workflowId);
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
    public void validateWorkflow(Workflow workflow) {

    }

    @Override
    public void updateWorkflow(Workflow workflow) {

    }

    @Override
    public void deleteWorkflow(String workflowId) {

    }

    @Override
    public String createWorkflowInstance(String workflowId, Map<String, String> instanceVariables) {
        Workflow workflow = getWorkflowByWorkflowId(workflowId);
        return createWorkflowInstance(workflow, instanceVariables).getWorkflowInstanceId();
    }

    @Override
    public WorkflowInstance createWorkflowInstance(Workflow workflow, Map<String, String> instanceVariables) {
        WorkflowInstance workflowInstance = new WorkflowInstance(
                                workflow.getName() + "_" + ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME),
                                                    workflow.getId(),
                                                    instanceVariables);
        workflowInstance.setTasks(workflow.getTasks());
        workflowInstance.setDirectedAcyclicGraph(workflow.getDirectedAcyclicGraph());
        workflowInstance.setWorkflowInstanceStatus(WorkflowInstance.WORKFLOW_INSTANCE_CREATED);
        workflowRepository.addWorkflowInstance(workflowInstance);
        return workflowInstance;
    }

    @Override
    public String startWorkflowInstance(String workflowInstanceId) {
        WorkflowInstance workflowInstance = getWorkflowInstanceByWorkflowInstanceId(workflowInstanceId);
        return startWorkflowInstance(workflowInstance).getWorkflowInstanceId();
    }

    @Override
    public WorkflowInstance startWorkflowInstance(WorkflowInstance workflowInstance) {
        // Move to initial state(s)
        // Add all tasks
        workflowInstance.setCurrentTaskIds(new ArrayList<>(workflowInstance.getTasks().keySet()));
        Set<Map.Entry<String, List<String>>> entries = workflowInstance.getDirectedAcyclicGraph().entrySet();

        // Remove once which have incoming nodes
        for(Map.Entry<String, List<String>> entry : entries) {
            workflowInstance.removeCurrentTaskIds(entry.getValue());
        }
        workflowInstance.setWorkflowInstanceStatus(WorkflowInstance.WORKFLOW_INSTANCE_STARTED);
        workflowRepository.updateWorkflowInstance(workflowInstance);
        return workflowInstance;
    }

    @Override
    public void executeWorkflowInstance(String workflowInstanceId, Map<String, String> parameters) {
        WorkflowInstance workflowInstance = getWorkflowInstanceByWorkflowInstanceId(workflowInstanceId);
        executeWorkflowInstance(workflowInstance, workflowInstance.getCurrentTaskIds().get(0), parameters);
    }

    @Override
    public void executeWorkflowInstance(WorkflowInstance workflowInstance, Map<String, String> parameters) {
        executeWorkflowInstance(workflowInstance, workflowInstance.getCurrentTaskIds().get(0), parameters);
    }

    @Override
    public void executeWorkflowInstance(String workflowInstanceId, String taskId, Map<String, String> parameters) {
        WorkflowInstance workflowInstance = getWorkflowInstanceByWorkflowInstanceId(workflowInstanceId);
        executeWorkflowInstance(workflowInstance, taskId, parameters);
    }

    @Override
    public void executeWorkflowInstance(WorkflowInstance workflowInstance, String taskId, Map<String, String> parameters) {
        if(workflowInstance.getCurrentTaskIds().contains(taskId)) {
            Task task = workflowInstance.getTasks().get(taskId);
            task.updateTaskVariables(parameters);
            task.onAction(workflowInstance);
            task.onSuccess(workflowInstance);
            workflowInstance.setWorkflowInstanceStatus(WorkflowInstance.WORKFLOW_INSTANCE_IN_PROCESS);
            workflowInstance.addExecutedTaskId(taskId);
            workflowInstance.removeCurrentTaskId(taskId);
            // Compute if any task can be added to currentTaskIds
            // Add tasks which are outgoing edges to current task
            workflowInstance.addCurrentTaskIds(workflowInstance.getDirectedAcyclicGraph().get(taskId));

            // Remove tasks which still haven't met their dependencies
            Set<Map.Entry<String, List<String>>> entries = workflowInstance.getDirectedAcyclicGraph().entrySet();
            for(Map.Entry<String, List<String>> entry : entries) {
                if(!workflowInstance.getExecutedTaskIds().contains(entry.getKey())) {
                    workflowInstance.removeCurrentTaskIds(entry.getValue());
                }
            }

            // Compute if we have exhausted all tasks
            if(workflowInstance.getExecutedTaskIds().containsAll(workflowInstance.getTasks().keySet())) {
                workflowInstance.setWorkflowInstanceStatus(WorkflowInstance.WORKFLOW_INSTANCE_FINISHED);
            }
            workflowRepository.updateWorkflowInstance(workflowInstance);
        } else {
            // Error code
        }
    }

}
package com.anindoasaha.workflowengine.prianza.api.impl;

import com.anindoasaha.workflowengine.prianza.api.WorkflowInstanceEventListener;
import com.anindoasaha.workflowengine.prianza.api.WorkflowService;
import com.anindoasaha.workflowengine.prianza.bo.Task;
import com.anindoasaha.workflowengine.prianza.bo.Workflow;
import com.anindoasaha.workflowengine.prianza.bo.WorkflowInstance;
import com.anindoasaha.workflowengine.prianza.bo.exception.WorkflowException;
import com.anindoasaha.workflowengine.prianza.bo.impl.simple.ExecutionType;
import com.anindoasaha.workflowengine.prianza.data.WorkflowRepository;
import com.anindoasaha.workflowengine.prianza.data.impl.LocalFileBasedWorkflowRepositoryImpl;
import com.anindoasaha.workflowengine.prianza.bo.event.WorkflowInstanceFinishEvent;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.*;

@Singleton
public class WorkflowServiceImpl implements WorkflowService {


    private WorkflowRepository workflowRepository = new LocalFileBasedWorkflowRepositoryImpl();

    @Inject
    public WorkflowService setWorkflowRepository(WorkflowRepository workflowRepository) {
        this.workflowRepository = workflowRepository;
        return this;
    }

    @Override
    public Map<String, String> listWorkflows() {
        return workflowRepository.listWorkflows();
    }

    @Override
    public Map<String, String> listWorkflowInstances() {
        return workflowRepository.listWorkflowInstances();
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
        return createWorkflowInstance(workflow, instanceVariables).getId();
    }

    @Override
    public WorkflowInstance createWorkflowInstance(Workflow workflow, Map<String, String> instanceVariables) {
        WorkflowInstance workflowInstance = new WorkflowInstance(
                                                    workflow.getName(),
                                                    workflow.getId(),
                                                    instanceVariables);
        workflowInstance.setTasks(workflow.getTasks());
        workflowInstance.setTaskExecutionInfo(workflow.getTaskExecutionInfo());
        workflowInstance.setDirectedAcyclicGraph(workflow.getDirectedAcyclicGraph());
        workflowInstance.setWorkflowInstanceStatus(WorkflowInstance.WORKFLOW_INSTANCE_CREATED);
        if (workflowInstance.getInstanceType().equals(WorkflowInstance.InstanceType.WORKFLOW_INSTANCE_MAIN)) {
            workflowRepository.addWorkflowInstance(workflowInstance);
        } else {
            // TODO Add to index with path
            workflowRepository.addWorkflowInstance(workflowInstance);
        }
        return workflowInstance;
    }

    @Override
    public WorkflowInstance updateWorkflowInstance(WorkflowInstance workflowInstance, Map<String, String> instanceVariables) {
        workflowInstance.getInstanceVariables().putAll(instanceVariables);
        workflowRepository.updateWorkflowInstance(workflowInstance);
        return workflowInstance;
    }

    @Override
    public String startWorkflowInstance(String workflowInstanceId) {
        WorkflowInstance workflowInstance = getWorkflowInstanceByWorkflowInstanceId(workflowInstanceId);
        return startWorkflowInstance(workflowInstance).getId();
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
        try {
            executeWorkflowInstance(workflowInstance, workflowInstance.getCurrentTaskIds().get(0), parameters);
        }
        catch (Exception e) {
            System.out.println("ERROR");
        }
    }

    @Override
    public void executeWorkflowInstance(String workflowInstanceId, String taskId, Map<String, String> parameters) {
        WorkflowInstance workflowInstance = getWorkflowInstanceByWorkflowInstanceId(workflowInstanceId);
        executeWorkflowInstance(workflowInstance, taskId, parameters);
    }

    @Override
    public void executeWorkflowInstance(WorkflowInstance workflowInstance, String taskId, Map<String, String> parameters) {
        if(workflowInstance.getCurrentTaskIds().contains(taskId)) {
            workflowInstance.setWorkflowInstanceStatus(WorkflowInstance.WORKFLOW_INSTANCE_IN_PROCESS);

            Task task = workflowInstance.getTasks().get(taskId);
            task.updateTaskVariables(parameters);
            try {
                task.beforeAction(workflowInstance);
                task.onAction(workflowInstance);
                task.onSuccess(workflowInstance);
            }
            catch (Exception e) {
                task.onError(workflowInstance);
            }
            finally {
                task.afterAction(workflowInstance);
            }

            // Execute and proceed
            if(workflowInstance.getTaskExecutionInfo().get(taskId).getExecutionType() == ExecutionType.EXECUTE_AND_PROCEED) {
                workflowInstance.proceed(taskId);

                // Compute if we have exhausted all tasks
                if(workflowInstance.getExecutedTaskIds().containsAll(workflowInstance.getTasks().keySet())) {
                    workflowInstance.setWorkflowInstanceStatus(WorkflowInstance.WORKFLOW_INSTANCE_FINISHED);
                    // Call listeners
                    for (WorkflowInstanceEventListener listener : workflowInstance.getEventListeners()) {
                        listener.onInstanceFinish(new WorkflowInstanceFinishEvent(workflowInstance));
                    }
                }
            }
            workflowRepository.updateWorkflowInstance(workflowInstance);
        } else {
            throw new WorkflowException("No such task id: "+ taskId + " for instance: " + workflowInstance.getId());
        }
    }

    @Override
    public void proceedWorkflowInstance(WorkflowInstance workflowInstance, Map<String, String> parameters) {
        proceedWorkflowInstance(workflowInstance, workflowInstance.getCurrentTaskIds().get(0), parameters);
    }

    public void proceedWorkflowInstance(WorkflowInstance workflowInstance, String taskId,  Map<String, String> parameters) {
        if(workflowInstance.getCurrentTaskIds().contains(taskId)) {
            workflowInstance.setWorkflowInstanceStatus(WorkflowInstance.WORKFLOW_INSTANCE_IN_PROCESS);

            // It was waiting for a manual push
            if(workflowInstance.getTaskExecutionInfo().get(taskId).getExecutionType() == ExecutionType.EXECUTE_AND_WAIT) {
                //  Call proceed on instance
                workflowInstance.proceed(taskId);

                // Compute if we have exhausted all tasks
                if(workflowInstance.getExecutedTaskIds().containsAll(workflowInstance.getTasks().keySet())) {
                    workflowInstance.setWorkflowInstanceStatus(WorkflowInstance.WORKFLOW_INSTANCE_FINISHED);
                    // Call listeners
                    for (WorkflowInstanceEventListener listener : workflowInstance.getEventListeners()) {
                        listener.onInstanceFinish(new WorkflowInstanceFinishEvent(workflowInstance));
                    }
                }
                workflowRepository.updateWorkflowInstance(workflowInstance);
            }

        } else {
            throw new WorkflowException("No such task id: "+ taskId + " for instance: " + workflowInstance.getId());
        }
    }


    /**
     * Used to signal whether to proceed after execution.
     *
     * EXECUTE_AND_PROCEED in one step.
     * or
     * EXECUTE_AND_WAIT and PROCEED in two steps.
     */
    public enum Signal {
        EXECUTE_AND_PROCEED, // Execute the task and compute next tasks
        EXECUTE_AND_WAIT, // Execute and wait
        PROCEED, // Compute next tasks
    }
}

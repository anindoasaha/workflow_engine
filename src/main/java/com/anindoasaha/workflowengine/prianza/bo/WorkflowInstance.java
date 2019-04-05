package com.anindoasaha.workflowengine.prianza.bo;

import com.anindoasaha.workflowengine.prianza.api.WorkflowInstanceEventListener;
import com.anindoasaha.workflowengine.prianza.bo.event.WorkflowInstanceFinishEvent;
import com.anindoasaha.workflowengine.prianza.util.IdGenerator;

import java.util.*;
import java.util.function.BiFunction;

public class WorkflowInstance implements Entity {

    public static final String WORKFLOW_INSTANCE_CREATED = "WORKFLOW_INSTANCE_CREATED";
    public static final String WORKFLOW_INSTANCE_STARTED = "WORKFLOW_INSTANCE_STARTED";
    public static final String WORKFLOW_INSTANCE_IN_PROCESS = "WORKFLOW_INSTANCE_IN_PROCESS";
    public static final String WORKFLOW_INSTANCE_WAITING = "WORKFLOW_INSTANCE_WAITING";
    public static final String WORKFLOW_INSTANCE_FINISHED = "WORKFLOW_INSTANCE_FINISHED";

    public enum InstanceType { WORKFLOW_INSTANCE_MAIN,  WORKFLOW_INSTANCE_EMBEDDED }

    private InstanceType instanceType = InstanceType.WORKFLOW_INSTANCE_MAIN;

    private String id = null;
    private String name = null;
    private String workflowId = null;
    private List<String> currentTaskIds = null;
    private List<String> waitingTaskIds = null;
    private List<String> executedTaskIds = null;
    private final Map<String, String> instanceVariables = new HashMap<>();

    private String workflowInstanceStatus;

    private Map<String, Task> tasks = null;
    private Map<String, TaskExecutionInfo> taskExecutionInfo = null;
    private Map<String, List<String>> directedAcyclicGraph = null;

    private List<WorkflowInstanceEventListener> eventListeners = null;

    private static IdGenerator<String, String> identityGenerator = new IdGenerator<>(
                                n -> n + "_" + UUID.randomUUID().toString());

    public WorkflowInstance() {
    }

    public WorkflowInstance(String workflowInstanceName, String workflowId, Map<String, String> instanceVariables) {
        this.id = identityGenerator.generate(workflowInstanceName);
        this.name = workflowInstanceName;
        this.workflowId = workflowId;
        this.currentTaskIds = new ArrayList<>();
        this.waitingTaskIds = new ArrayList<>();
        this.executedTaskIds = new ArrayList<>();
        this.instanceVariables.putAll(instanceVariables);
        this.eventListeners = new ArrayList<>();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }

    public List<String> getCurrentTaskIds() {
        return currentTaskIds;
    }

    public void setCurrentTaskIds(List<String> currentTaskIds) {
        this.currentTaskIds = currentTaskIds;
    }

    public void addCurrentTaskIds(List<String> taskIds) {
        this.currentTaskIds.addAll(taskIds);
    }

    public void removeCurrentTaskId(String taskId) {
        this.currentTaskIds.remove(taskId);
    }

    public void removeCurrentTaskIds(List<String> taskIds) {
        this.currentTaskIds.removeAll(taskIds);
    }

    public List<String> getExecutedTaskIds() {
        return executedTaskIds;
    }

    public void setExecutedTaskIds(List<String> executedTaskIds) {
        this.executedTaskIds = executedTaskIds;
    }

    public void addExecutedTaskId(String taskId) {
        this.executedTaskIds.add(taskId);
    }

    public void removeExecutedTaskId(String taskId) {
        this.executedTaskIds.remove(taskId);
    }

    public List<String> getWaitingTaskIds() {
        return waitingTaskIds;
    }

    public void setWaitingTaskIds(List<String> waitingTaskIds) {
        this.waitingTaskIds = waitingTaskIds;
    }

    public void addWaitingTaskId(String taskId) {
        this.waitingTaskIds.add(taskId);
    }

    public void removeWaitingTaskId(String taskId) {
        this.waitingTaskIds.remove(taskId);
    }

    public Map<String, String> getInstanceVariables() {
        return instanceVariables;
    }

    public String getWorkflowInstanceStatus() {
        return workflowInstanceStatus;
    }

    public void setWorkflowInstanceStatus(String workflowInstanceStatus) {
        this.workflowInstanceStatus = workflowInstanceStatus;
        if (this.getWorkflowInstanceStatus().equals(WORKFLOW_INSTANCE_FINISHED)) {
            for (WorkflowInstanceEventListener workflowInstanceEventListener: eventListeners) {
                workflowInstanceEventListener.onInstanceFinish(new WorkflowInstanceFinishEvent(this));
            }
        }
    }

    public InstanceType getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(InstanceType instanceType) {
        this.instanceType = instanceType;
    }

    public Map<String, Task> getTasks() {
        return tasks;
    }

    public void setTasks(Map<String, Task> tasks) {
        this.tasks = tasks;
    }


    public Map<String, TaskExecutionInfo> getTaskExecutionInfo() {
        return taskExecutionInfo;
    }

    public void setTaskExecutionInfo(Map<String, TaskExecutionInfo> taskExecutionInfo) {
        this.taskExecutionInfo = taskExecutionInfo;
    }

    public Map<String, List<String>> getDirectedAcyclicGraph() {
        return directedAcyclicGraph;
    }

    public void setDirectedAcyclicGraph(Map<String, List<String>> directedAcyclicGraph) {
        this.directedAcyclicGraph = directedAcyclicGraph;
    }

    public List<WorkflowInstanceEventListener> getEventListeners() {
        return eventListeners;
    }

    public void setEventListeners(List<WorkflowInstanceEventListener> eventListeners) {
        this.eventListeners = eventListeners;
    }

    public void addEventListener(WorkflowInstanceEventListener workflowInstanceEventListener) {
        eventListeners.add(workflowInstanceEventListener);
    }

    /**
     *
     * @param taskId
     */
    public void wait(String taskId) {
        this.addWaitingTaskId(taskId);
    }

    /**
     *
     * @param taskId
     * @param waitCondition wait if condition is true otherwise proceed.
     */
    public void wait(String taskId, BiFunction<WorkflowInstance, String, Boolean> waitCondition) {
        // Set status of workflow as waiting
        if (waitCondition.apply(this, taskId)) {
            this.addWaitingTaskId(taskId);
        }
        else {
            this.removeWaitingTaskId(taskId);
        }
    }

    public void proceed(String taskId, BiFunction<WorkflowInstance, String, Boolean> proceedCondition) {
        // If proceedCondition returns false don't proceed
        if (!proceedCondition.apply(this, taskId)) {
            return;
        }
        this.addExecutedTaskId(taskId);
        this.removeWaitingTaskId(taskId);
        this.removeCurrentTaskId(taskId);
        // Compute if any task can be added to currentTaskIds
        // Add tasks which are outgoing edges to current task
        this.addCurrentTaskIds(this.getDirectedAcyclicGraph().get(taskId));

        // Remove tasks which still haven't met their dependencies
        Set<Map.Entry<String, List<String>>> entries = this.getDirectedAcyclicGraph().entrySet();
        for(Map.Entry<String, List<String>> entry : entries) {
            if(!this.getExecutedTaskIds().contains(entry.getKey())) {
                this.removeCurrentTaskIds(entry.getValue());
            }
        }
        // TODO Execute SYSTEM tasks
    }

    public void proceed(String taskId) {
        proceed(taskId, (workflowInstance, s) -> true);
    }
}

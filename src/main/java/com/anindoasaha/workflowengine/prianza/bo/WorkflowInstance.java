package com.anindoasaha.workflowengine.prianza.bo;

import java.util.*;

public class WorkflowInstance {

    public static final String WORKFLOW_INSTANCE_CREATED = "WORKFLOW_INSTANCE_CREATED";
    public static final String WORKFLOW_INSTANCE_STARTED = "WORKFLOW_INSTANCE_STARTED";
    public static final String WORKFLOW_INSTANCE_IN_PROCESS = "WORKFLOW_INSTANCE_IN_PROCESS";
    public static final String WORKFLOW_INSTANCE_FINISHED = "WORKFLOW_INSTANCE_FINISHED";

    private String workflowInstanceId = null;
    private String workflowInstanceName = null;
    private String workflowId = null;
    private List<String> currentTaskIds = null;
    private List<String> executedTaskIds = null;
    private final Map<String, String> instanceVariables = new HashMap<>();

    private String workflowInstanceStatus;

    private Map<String, Task> tasks = null;
    private Map<String, List<String>> directedAcyclicGraph = null;

    public WorkflowInstance() {
    }

    public WorkflowInstance(String workflowInstanceName, String workflowId, Map<String, String> instanceVariables) {
        this.workflowInstanceId = UUID.randomUUID().toString();
        this.workflowInstanceName = workflowInstanceName;
        this.workflowId = workflowId;
        this.currentTaskIds = new ArrayList<>();
        this.executedTaskIds = new ArrayList<>();
        this.instanceVariables.putAll(instanceVariables);
    }

    public String getWorkflowInstanceId() {
        return workflowInstanceId;
    }

    public void setWorkflowInstanceId(String workflowInstanceId) {
        this.workflowInstanceId = workflowInstanceId;
    }

    public String getWorkflowInstanceName() {
        return workflowInstanceName;
    }

    public void setWorkflowInstanceName(String workflowInstanceName) {
        this.workflowInstanceName = workflowInstanceName;
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

    public Map<String, String> getInstanceVariables() {
        return instanceVariables;
    }

    public String getWorkflowInstanceStatus() {
        return workflowInstanceStatus;
    }

    public void setWorkflowInstanceStatus(String workflowInstanceStatus) {
        this.workflowInstanceStatus = workflowInstanceStatus;
    }

    public Map<String, Task> getTasks() {
        return tasks;
    }

    public void setTasks(Map<String, Task> tasks) {
        this.tasks = tasks;
    }

    public Map<String, List<String>> getDirectedAcyclicGraph() {
        return directedAcyclicGraph;
    }

    public void setDirectedAcyclicGraph(Map<String, List<String>> directedAcyclicGraph) {
        this.directedAcyclicGraph = directedAcyclicGraph;
    }
}

package com.anindoasaha.workflowengine.prianza.task.control.composite;

import com.anindoasaha.workflowengine.prianza.bo.ActorType;
import com.anindoasaha.workflowengine.prianza.bo.Task;
import com.anindoasaha.workflowengine.prianza.bo.WorkflowInstance;

import java.util.List;
import java.util.Map;

public class RepeatableTask implements Task {

    public enum RepeatableType {
        UNTIL_USER_ACTION,
    }

    private Task repeatedTask = null;
    RepeatableType repeatableType = RepeatableType.UNTIL_USER_ACTION;

    public RepeatableTask(Task repeatedTask) {
        this.repeatedTask = repeatedTask;
    }

    @Deprecated
    public String getTaskType() {
        return getTaskType();
    }

    public ActorType getActorType() {
        return repeatedTask.getActorType();
    }

    public Map<String, String> getTaskVariables() {
        return repeatedTask.getTaskVariables();
    }

    public void updateTaskVariables(Map taskVariables) {
        repeatedTask.updateTaskVariables(taskVariables);
    }

    public void setTaskVariables(Map taskVariables) {
        repeatedTask.setTaskVariables(taskVariables);
    }

    public Object beforeAction(WorkflowInstance workflowInstance) {
        return repeatedTask.beforeAction(workflowInstance);
    }

    public Object onAction(WorkflowInstance workflowInstance) {
        return repeatedTask.onAction(workflowInstance);
    }

    public Object onAction(Object taskExecutionInput, WorkflowInstance workflowInstance) {
        return repeatedTask.onAction(taskExecutionInput, workflowInstance);
    }

    public Object afterAction(WorkflowInstance workflowInstance) {
        return repeatedTask.afterAction(workflowInstance);
    }

    public Object onSuccess(WorkflowInstance workflowInstance) {
        return repeatedTask.onSuccess(workflowInstance);
    }

    public Object onError(WorkflowInstance workflowInstance) {
        return repeatedTask.onError(workflowInstance);
    }

    public String getId() {
        return repeatedTask.getId();
    }

    public void setId(String id) {
        repeatedTask.setId(id);
    }

    public String getName() {
        return repeatedTask.getName();
    }

    public void setName(String name) {
        repeatedTask.setName(name);
    }

    public String getType() {
        return repeatedTask.getType();
    }




}

package com.anindoasaha.workflowengine.prianza.task;

import com.anindoasaha.workflowengine.prianza.bo.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class AbstractTask implements Task {
    private String id = null;
    private Map<String, String> taskVariables = new HashMap<>();
    private String taskType = getTaskType();

    public AbstractTask() {
        this.id = UUID.randomUUID().toString();
    }

    @Override
    public void updateTaskVariables(Map taskVariables) {
        this.taskVariables.putAll(taskVariables);
    }

    @Override
    public void setTaskVariables(Map taskVariables) {
        this.taskVariables = taskVariables;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
}

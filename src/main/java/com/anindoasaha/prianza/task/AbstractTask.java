package com.anindoasaha.prianza.task;

import com.anindoasaha.prianza.bo.Task;
import com.anindoasaha.prianza.bo.WorkflowInstance;

import java.util.Map;
import java.util.UUID;

public abstract class AbstractTask implements Task {
    private String id = null;
    protected Map<String, String> taskVariables = null;

    public AbstractTask() {
        this.id = UUID.randomUUID().toString();
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

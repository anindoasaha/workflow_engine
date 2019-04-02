package com.anindoasaha.workflowengine.prianza.bo;

import com.anindoasaha.workflowengine.prianza.util.IdGenerator;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class AbstractTask implements Task {
    private String id = null;
    private String name = null;
    private Map<String, String> taskVariables = new HashMap<>();
    private String taskType = getTaskType();

    public AbstractTask() {
        this(null);
    }

    public AbstractTask(String name) {
        this.id = identityGenerator.generate(name);
        this.name = name;
    }

    public AbstractTask name(String name) {
        this.name = name;
        return this;
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

    @Override
    public Map<String, String> getTaskVariables() {
        return taskVariables;
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
    public abstract Map<String, String> onAction(WorkflowInstance workflowInstance);

}

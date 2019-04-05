package com.anindoasaha.workflowengine.prianza.task;

import com.anindoasaha.workflowengine.prianza.bo.AbstractTask;
import com.anindoasaha.workflowengine.prianza.bo.WorkflowInstance;

import java.util.Map;

public class StdOutTask extends AbstractTask {

    private String message = null;
    private int invocationCount = 0;

    public StdOutTask() {}

    public StdOutTask(String name, String message) {
        super(name);
        this.message = message;
    }

    public StdOutTask(String message) {
        super("StdOutTask");
        this.message = message;
    }


    @Override
    public Object beforeAction(WorkflowInstance workflowInstance) {
        return null;
    }

    @Override
    public Map<String, String> onAction(WorkflowInstance workflowInstance) {
        String message = getTaskVariables().get("message");
        System.out.println(message == null ? this.message : message);
        System.out.println(++this.invocationCount);
        return null;
    }

    @Override
    public Object onSuccess(WorkflowInstance workflowInstance) {
        return null;
    }

    @Override
    public Object onError(WorkflowInstance workflowInstance) {
        return null;
    }
}

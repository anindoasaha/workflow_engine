package com.anindoasaha.workflowengine.prianza.task;

import com.anindoasaha.workflowengine.prianza.bo.AbstractTask;
import com.anindoasaha.workflowengine.prianza.bo.WorkflowInstance;

import java.util.Map;

public class StdOutTask extends AbstractTask {

    String message = null;

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
        String message = workflowInstance.getInstanceVariables().get("message");
        System.out.println(message == null ? this.message : message + "1");
        return null;

    }

    @Override
    public Object onSuccess(WorkflowInstance workflowInstance) {
        workflowInstance.getInstanceVariables().put("message",
                workflowInstance.getInstanceVariables().getOrDefault("message", "Modified message.") + "1");
        return null;
    }

    @Override
    public Object onError(WorkflowInstance workflowInstance) {
        return null;
    }
}

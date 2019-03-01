package com.anindoasaha.workflowengine.prianza.task;

import com.anindoasaha.workflowengine.prianza.bo.WorkflowInstance;

public class StdOutTask extends AbstractTask {

    String message = null;

    public StdOutTask() {}

    public StdOutTask(String message) {
        super();
        this.message = message;
    }


    @Override
    public Object beforeAction(WorkflowInstance workflowInstance) {
        return null;
    }

    @Override
    public Object onAction(WorkflowInstance workflowInstance) {
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

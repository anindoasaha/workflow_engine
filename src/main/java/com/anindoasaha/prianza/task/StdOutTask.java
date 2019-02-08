package com.anindoasaha.prianza.task;

import com.anindoasaha.prianza.bo.WorkflowInstance;

public class StdOutTask extends AbstractTask {

    String message = null;

    public StdOutTask() {

    }

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
        System.out.println(message == null ? this.message : message);
        return null;

    }

    @Override
    public Object onSuccess(WorkflowInstance workflowInstance) {
        workflowInstance.getInstanceVariables().put("message", "Modified message.");
        return null;
    }

    @Override
    public Object onError(WorkflowInstance workflowInstance) {
        return null;
    }
}

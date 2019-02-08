package com.anindoasaha.prianza.task;

import com.anindoasaha.prianza.bo.WorkflowInstance;

public class CommandLineTask extends AbstractTask {

    private String command = null;

    public CommandLineTask(String command) {
        this.command = command;
    }

    @Override
    public Object beforeAction(WorkflowInstance workflowInstance) {
        return null;
    }

    @Override
    public Object onAction(WorkflowInstance workflowInstance) {
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

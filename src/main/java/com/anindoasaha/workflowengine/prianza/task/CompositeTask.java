package com.anindoasaha.workflowengine.prianza.task;

import com.anindoasaha.workflowengine.prianza.bo.Workflow;
import com.anindoasaha.workflowengine.prianza.bo.WorkflowInstance;

public class CompositeTask extends AbstractTask {

    Workflow nestedWorkflow = null;
    WorkflowInstance nestedWorkflowInstance = null;

    public CompositeTask() {}

    public CompositeTask(Workflow nestedWorkflow) {
        this.nestedWorkflow = nestedWorkflow;
    }

    @Override
    public Object beforeAction(WorkflowInstance workflowInstance) {
        // Create nestedWorkflowInstance if it doesnt exist and start
        return null;
    }

    @Override
    public Object onAction(WorkflowInstance workflowInstance) {
        // Delegate to task of nested workflowInstance
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

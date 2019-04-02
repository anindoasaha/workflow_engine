package com.anindoasaha.workflowengine.prianza.bo.event;

import com.anindoasaha.workflowengine.prianza.api.Event;
import com.anindoasaha.workflowengine.prianza.bo.WorkflowInstance;

public class WorkflowInstanceFinishEvent implements Event {

    private WorkflowInstance workflowInstance;

    public WorkflowInstanceFinishEvent(WorkflowInstance workflowInstance) {
        this.workflowInstance = workflowInstance;
    }
}

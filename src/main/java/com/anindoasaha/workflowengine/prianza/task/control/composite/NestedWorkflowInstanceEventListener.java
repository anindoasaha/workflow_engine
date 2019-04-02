package com.anindoasaha.workflowengine.prianza.task.control.composite;

import com.anindoasaha.workflowengine.prianza.api.WorkflowInstanceEventListener;
import com.anindoasaha.workflowengine.prianza.api.WorkflowService;
import com.anindoasaha.workflowengine.prianza.api.impl.WorkflowServiceImpl;
import com.anindoasaha.workflowengine.prianza.bo.event.WorkflowInstanceFinishEvent;
import com.anindoasaha.workflowengine.prianza.bo.WorkflowInstance;

public class NestedWorkflowInstanceEventListener implements WorkflowInstanceEventListener {

    private String id;

    private String name;

    private String eventListenerType = getType();

    private String parentWorkflowInstanceId;
    private String parentWorkflowInstanceTaskId;

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String getEventListenerType() {
        return eventListenerType;
    }

    public void setEventListenerType(String eventListenerType) {
        this.eventListenerType = eventListenerType;
    }

    public NestedWorkflowInstanceEventListener(String id, String name, String parentWorkflowInstanceId, String parentWorkflowInstanceTaskId) {
        this.id = id;
        this.name = name;
        this.parentWorkflowInstanceId = parentWorkflowInstanceId;
        this.parentWorkflowInstanceTaskId = parentWorkflowInstanceTaskId;
    }

    public void onInstanceFinished(WorkflowInstanceFinishEvent event) {
        WorkflowService workflowService = new WorkflowServiceImpl();
        WorkflowInstance workflowInstance = workflowService.getWorkflowInstanceByWorkflowInstanceId(this.parentWorkflowInstanceId);
        workflowInstance.proceed(this.parentWorkflowInstanceTaskId);
    }
}

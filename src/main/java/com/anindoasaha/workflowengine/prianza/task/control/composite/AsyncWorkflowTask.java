package com.anindoasaha.workflowengine.prianza.task.control.composite;

import com.anindoasaha.workflowengine.prianza.api.WorkflowInstanceEventListener;
import com.anindoasaha.workflowengine.prianza.api.WorkflowService;
import com.anindoasaha.workflowengine.prianza.bo.AbstractTask;
import com.anindoasaha.workflowengine.prianza.bo.ActorType;
import com.anindoasaha.workflowengine.prianza.bo.Workflow;
import com.anindoasaha.workflowengine.prianza.bo.WorkflowInstance;
import com.google.inject.Inject;

import java.util.*;

/**
 * Inbuilt task which references a workflow,
 * the workflow is not exclusive to the task and can be reused or ran independently.
 * The task does not wait for the reference workflow to finish.
 */
public class AsyncWorkflowTask extends AbstractTask {

    private String workflowId = null;
    private transient Workflow nestedWorkflow = null;
    private String nestedWorkflowInstanceId = null;

    @Inject
    private transient WorkflowService workflowService = null;

    /**
     * Variables to copy over from parent workflow instance to child
     * workflow instance at instance creation.
     *
     * keyInParent => keyInChild
     *
     */
    private Map<String, String> parentInstanceVariablesMapping = null;

    /**
     * Variables to copy over from child workflow instance to parent
     * workflow instance at embedded instance completion.
     *
     * keyInChild => keyInParent
     */
    private Map<String, String> childInstanceVariablesMapping = null;

    /**
     * Default constructor for serialization/deserialization purposes
     */
    public AsyncWorkflowTask() {
    }

    public AsyncWorkflowTask(String workflowId, Map<String, String> parentInstanceVariablesMapping) {
        this.workflowId = workflowId;
        this.parentInstanceVariablesMapping = parentInstanceVariablesMapping;
    }

    @Override
    public ActorType getActorType() {
        return ActorType.SYSTEM;
    }

    @Override
    public Map<String, String> beforeAction(WorkflowInstance workflowInstance) {

        this.nestedWorkflow = workflowService.getWorkflowByWorkflowId(this.workflowId);
        Map<String, String> instanceVariables = copyOverParentVariables(workflowInstance.getInstanceVariables());
        WorkflowInstance nestedWorkflowInstance =
                workflowService.createWorkflowInstance(this.nestedWorkflow, instanceVariables);
        nestedWorkflowInstance.setInstanceType(WorkflowInstance.InstanceType.WORKFLOW_INSTANCE_EMBEDDED);

        workflowService.updateWorkflowInstance(nestedWorkflowInstance, instanceVariables);
        nestedWorkflowInstanceId = nestedWorkflowInstance.getId();
        return null;
    }

    @Override
    public Map<String, String> onAction(WorkflowInstance workflowInstance) {
        return null;
    }

    @Override
    public Object afterAction(WorkflowInstance workflowInstance) {
        return null;
    }

    @Override
    public Map<String, String> onSuccess(WorkflowInstance workflowInstance) {
        return null;
    }

    @Override
    public Map<String, String> onError(WorkflowInstance workflowInstance) {
        return null;
    }

    private Map<String, String> copyOverParentVariables(Map<String, String> instanceVariables) {
        Map<String, String> parentInstanceVariablesMapping = this.parentInstanceVariablesMapping;
        Map<String, String> childInstanceVariables = new HashMap<>();
        for (Map.Entry<String, String> entry : parentInstanceVariablesMapping.entrySet()) {
            childInstanceVariables.put(entry.getValue(), instanceVariables.get(entry.getKey()));
        }
        return childInstanceVariables;
    }
}

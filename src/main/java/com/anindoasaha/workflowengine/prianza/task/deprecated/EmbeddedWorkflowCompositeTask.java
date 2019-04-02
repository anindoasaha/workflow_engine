package com.anindoasaha.workflowengine.prianza.task.deprecated;

import com.anindoasaha.workflowengine.prianza.bo.AbstractTask;
import com.anindoasaha.workflowengine.prianza.bo.ActorType;
import com.anindoasaha.workflowengine.prianza.bo.Workflow;
import com.anindoasaha.workflowengine.prianza.bo.WorkflowInstance;
import com.anindoasaha.workflowengine.prianza.task.control.composite.NestedWorkflowInstanceEventListener;

import java.util.*;

/**
 * Task which contains its own workflow, the workflow is managed by the task
 * and cannot be used or ran independently.
 */
@Deprecated
public class EmbeddedWorkflowCompositeTask extends AbstractTask {

    private Workflow nestedWorkflow = null;
    private List<WorkflowInstance> nestedWorkflowInstances = null;
    /**
     * Variables to copy over from parent workflow instance to child
     * workflow instance at instance creation.
     */
    private Map<String, String> parentInstanceVariablesMapping = null;

    /**
     * Variables to copy over from child workflow instance to parent
     * workflow instance at embedded instance completion.
     */
    private Map<String, String> childInstanceVariablesMapping = null;

    /**
     * Default constructor for serialization/deserialization purposes
     */
    public EmbeddedWorkflowCompositeTask() {}

    public EmbeddedWorkflowCompositeTask(Workflow nestedWorkflow) {
        this.nestedWorkflow = nestedWorkflow;
        this.nestedWorkflowInstances = new ArrayList<>();
    }

    @Override
    public ActorType getActorType() {
        return ActorType.SYSTEM;
    }

    @Override
    public Map<String, String> beforeAction(WorkflowInstance workflowInstance) {
        WorkflowInstance nestedWorkflowInstance = new WorkflowInstance(
                nestedWorkflow.getName(),
                nestedWorkflow.getId(),
                new HashMap<>()); // TODO Propagate instance variables as per mapping
        nestedWorkflowInstance.setTasks(nestedWorkflow.getTasks());
        nestedWorkflowInstance.setDirectedAcyclicGraph(nestedWorkflow.getDirectedAcyclicGraph());
        nestedWorkflowInstance.setWorkflowInstanceStatus(WorkflowInstance.WORKFLOW_INSTANCE_CREATED);
        nestedWorkflowInstance.setInstanceType(WorkflowInstance.InstanceType.WORKFLOW_INSTANCE_EMBEDDED);
        String eventListenerId = UUID.randomUUID().toString();
        String eventListenerName = nestedWorkflowInstance.getName() + "_" + UUID.randomUUID().toString();
        nestedWorkflowInstance.addEventListener(
                new NestedWorkflowInstanceEventListener(
                        eventListenerId, eventListenerName,
                        workflowInstance.getId(),
                        getId()));
        nestedWorkflowInstances.add(nestedWorkflowInstance);
        return null;
    }

    @Override
    public Map<String, String> onAction(WorkflowInstance workflowInstance) {
        // Has no computation, subclasses may override this to add computation
        return null;
    }

    @Override
    public Object afterAction(WorkflowInstance workflowInstance) {
        // Wait for subprocess to complete, other tasks not dependent on this task may proceed.
        workflowInstance.wait(getId());
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
}

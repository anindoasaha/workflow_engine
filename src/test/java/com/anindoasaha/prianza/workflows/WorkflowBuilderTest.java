package com.anindoasaha.prianza.workflows;

import com.anindoasaha.prianza.api.SimpleWorkflowBuilder;
import com.anindoasaha.prianza.api.WorkflowService;
import com.anindoasaha.prianza.api.impl.WorkflowServiceImpl;
import com.anindoasaha.prianza.bo.Task;
import com.anindoasaha.prianza.bo.Workflow;
import com.anindoasaha.prianza.bo.WorkflowInstance;
import com.anindoasaha.prianza.task.StdOutTask;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class WorkflowBuilderTest {

    private SimpleWorkflowBuilder workflowBuilder = null;

    @Test
    public void testOneStepStatelessWorkflow() {
        workflowBuilder = new SimpleWorkflowBuilder("OneStepStatelessWorkflow");
        Task task = new StdOutTask("Hello, world!");

        workflowBuilder.addTask(task);
        Workflow workflow = workflowBuilder.build();

        WorkflowService workflowService = new WorkflowServiceImpl();
        workflowService.addWorkflow(workflow);

        WorkflowInstance workflowInstance = workflowService.createWorkflowInstance(workflow, new HashMap<>());
        workflowInstance = workflowService.startWorkflowInstance(workflowInstance);

        // Execute first step
        workflowService.executeWorkflowInstance(workflowInstance, new HashMap<>());
    }

    @Test
    public void testOneStepStatefulWorkflow() {
        workflowBuilder = new SimpleWorkflowBuilder("OneStepStatefulWorkflow");
        Task task = new StdOutTask("Default message");

        workflowBuilder.addTask(task);
        Workflow workflow = workflowBuilder.build();

        WorkflowService workflowService = new WorkflowServiceImpl();
        workflowService.addWorkflow(workflow);

        WorkflowInstance workflowInstance = workflowService.createWorkflowInstance(workflow, Map.of("message", "Creation Message."));
        workflowInstance = workflowService.startWorkflowInstance(workflowInstance);

        // Execute first step
        workflowService.executeWorkflowInstance(workflowInstance, new HashMap<>());
    }

    @Test
    public void testTwoStepStatefulWorkflow() {
        workflowBuilder = new SimpleWorkflowBuilder("OneStepStatefulWorkflow");
        Task task1 = new StdOutTask("Default message 1");
        Task task2 = new StdOutTask("Default message 2");

        workflowBuilder.addTask(task1)
                       .addTask(task2)
                       .addPipe(task1, task2);
        Workflow workflow = workflowBuilder.build();

        WorkflowService workflowService = new WorkflowServiceImpl();
        workflowService.addWorkflow(workflow);

        WorkflowInstance workflowInstance = workflowService.createWorkflowInstance(workflow, Map.of("message", "Creation Message."));
        workflowInstance = workflowService.startWorkflowInstance(workflowInstance);

        // Execute first step
        workflowService.executeWorkflowInstance(workflowInstance, new HashMap<>());

        // Execute second step
        workflowService.executeWorkflowInstance(workflowInstance, new HashMap<>());
    }
}

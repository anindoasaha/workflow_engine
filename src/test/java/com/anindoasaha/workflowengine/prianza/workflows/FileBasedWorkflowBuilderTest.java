package com.anindoasaha.workflowengine.prianza.workflows;

import com.anindoasaha.workflowengine.prianza.api.WorkflowService;
import com.anindoasaha.workflowengine.prianza.api.impl.WorkflowServiceImpl;
import com.anindoasaha.workflowengine.prianza.bo.Task;
import com.anindoasaha.workflowengine.prianza.bo.Workflow;
import com.anindoasaha.workflowengine.prianza.bo.WorkflowInstance;
import com.anindoasaha.workflowengine.prianza.bo.impl.SimpleWorkflowBuilder;
import com.anindoasaha.workflowengine.prianza.task.StdOutTask;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileBasedWorkflowBuilderTest {

    private SimpleWorkflowBuilder workflowBuilder = null;

    @Test
    public void testOneStepStatelessWorkflow() {
        workflowBuilder = new SimpleWorkflowBuilder("OneStepStatelessWorkflow");
        Task task = new StdOutTask("Hello, world!");

        workflowBuilder.addTask(task);
        Workflow workflow = workflowBuilder.build();

        WorkflowService workflowService = new WorkflowServiceImpl();
        workflowService.addWorkflow(workflow);

        String workflowInstanceId = workflowService.createWorkflowInstance(workflow.getId(), new HashMap<>());
        workflowService.startWorkflowInstance(workflowInstanceId);

        // Execute first step
        workflowService.executeWorkflowInstance(workflowInstanceId, new HashMap<>());
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
        workflowBuilder = new SimpleWorkflowBuilder("TwoStepStatefulWorkflow");
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

        while (!workflowInstance.getWorkflowInstanceStatus().equals(
                WorkflowInstance.WORKFLOW_INSTANCE_FINISHED)) {
            workflowService.executeWorkflowInstance(workflowInstance, new HashMap<>());
        }

    }

    @Test
    public void testThreeStepStatefulWorkflow() {
        workflowBuilder = new SimpleWorkflowBuilder("ThreeStepStatefulWorkflow");
        Task task1 = new StdOutTask("Default message 1");
        Task task2 = new StdOutTask("Default message 2");
        Task task3 = new StdOutTask("Default message 3");

        workflowBuilder.addTask(task1)
                .addTask(task2)
                .addTask(task3)
                .addPipe(task1, task3)
                .addPipe(task2, task3);
        Workflow workflow = workflowBuilder.build();

        WorkflowService workflowService = new WorkflowServiceImpl();
        workflowService.addWorkflow(workflow);

        WorkflowInstance workflowInstance = workflowService.createWorkflowInstance(workflow, Map.of("message", "Creation Message."));
        workflowInstance = workflowService.startWorkflowInstance(workflowInstance);

        while (!workflowInstance.getWorkflowInstanceStatus().equals(
                WorkflowInstance.WORKFLOW_INSTANCE_FINISHED)) {
            workflowService.executeWorkflowInstance(workflowInstance, new HashMap<>());
        }

    }

    @Test
    public void testThreeStepFanInStatefulWorkflow() {
        workflowBuilder = new SimpleWorkflowBuilder("ThreeStepStatefulWorkflow");
        Task task1 = new StdOutTask("Default message 1");
        Task task2 = new StdOutTask("Default message 2");
        Task task3 = new StdOutTask("Default message 3");

        workflowBuilder.addTasks(task1, task2, task3)
                .addFanInPipe(List.of(task1, task2), task3);
        Workflow workflow = workflowBuilder.build();

        WorkflowService workflowService = new WorkflowServiceImpl();
        workflowService.addWorkflow(workflow);

        WorkflowInstance workflowInstance = workflowService.createWorkflowInstance(workflow, Map.of("message", "Creation Message."));
        workflowInstance = workflowService.startWorkflowInstance(workflowInstance);

        while (!workflowInstance.getWorkflowInstanceStatus().equals(
                WorkflowInstance.WORKFLOW_INSTANCE_FINISHED)) {
            workflowService.executeWorkflowInstance(workflowInstance, new HashMap<>());
        }

    }

    @Test
    public void testThreeStepFanInInProcessStatefulWorkflow() {
        workflowBuilder = new SimpleWorkflowBuilder("ThreeStepStatefulWorkflow");
        Task task1 = new StdOutTask("Default message 1");
        Task task2 = new StdOutTask("Default message 2");
        Task task3 = new StdOutTask("Default message 3");

        workflowBuilder.addTasks(task1, task2, task3)
                .addFanInPipe(List.of(task1, task2), task3);
        Workflow workflow = workflowBuilder.build();

        WorkflowService workflowService = new WorkflowServiceImpl();
        workflowService.addWorkflow(workflow);

        WorkflowInstance workflowInstance = workflowService.createWorkflowInstance(workflow, Map.of("message", "Creation Message."));
        workflowInstance = workflowService.startWorkflowInstance(workflowInstance);

        workflowService.executeWorkflowInstance(workflowInstance, new HashMap<>());
    }
}

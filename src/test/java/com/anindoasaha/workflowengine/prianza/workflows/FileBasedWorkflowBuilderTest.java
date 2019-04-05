package com.anindoasaha.workflowengine.prianza.workflows;

import com.anindoasaha.workflowengine.prianza.api.WorkflowService;
import com.anindoasaha.workflowengine.prianza.api.impl.WorkflowServiceImpl;
import com.anindoasaha.workflowengine.prianza.bo.Task;
import com.anindoasaha.workflowengine.prianza.bo.TaskExecutionInfo;
import com.anindoasaha.workflowengine.prianza.bo.Workflow;
import com.anindoasaha.workflowengine.prianza.bo.WorkflowInstance;
import com.anindoasaha.workflowengine.prianza.bo.impl.simple.ExecutionArity;
import com.anindoasaha.workflowengine.prianza.bo.impl.simple.ExecutionType;
import com.anindoasaha.workflowengine.prianza.bo.impl.simple.SimpleWorkflow;
import com.anindoasaha.workflowengine.prianza.task.StdOutTask;
import com.anindoasaha.workflowengine.prianza.task.control.composite.RepeatableTask;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.anindoasaha.workflowengine.prianza.bo.TaskExecutionInfo.*;
import static java.util.Arrays.asList;

public class FileBasedWorkflowBuilderTest {

    private SimpleWorkflow.Builder workflowBuilder = null;

    @Test
    @Ignore
    public void testOneStepStatelessWorkflow() {
        workflowBuilder = new SimpleWorkflow.Builder("OneStepStatelessWorkflow");
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
    @Ignore
    public void testOneStepStatefulWorkflow() {
        workflowBuilder = new SimpleWorkflow.Builder("OneStepStatefulWorkflow");
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
    @Ignore
    public void testTwoStepStatefulWorkflow() {
        workflowBuilder = new SimpleWorkflow.Builder("TwoStepStatefulWorkflow");
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
    @Ignore
    public void testThreeStepStatefulWorkflow() {
        workflowBuilder = new SimpleWorkflow.Builder("ThreeStepStatefulWorkflow");
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
    @Ignore
    public void testThreeStepFanInStatefulWorkflow() {
        workflowBuilder = new SimpleWorkflow.Builder("ThreeStepStatefulWorkflow");
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
    @Ignore
    public void testThreeStepFanInInProcessStatefulWorkflow() {
        workflowBuilder = new SimpleWorkflow.Builder("ThreeStepStatefulWorkflow");
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


    /**
     * Simple Task => <Repeatable Task>*n => Last Task
     */
    @Test
    public void testTaskExecutionInfoWorkflow() {
        workflowBuilder = new SimpleWorkflow.Builder("TaskExecutionInfoWorkflow");
        Task task1 = new StdOutTask("Simple Task"); // Simple Task

        // This task can be executed multiple times,
        // unless its explicitly messaged to move to the next stage.
        Task task2 = new StdOutTask("Repeatable Tasks");

        Task task3 = new StdOutTask("Last Task");

        workflowBuilder.addTasks(
                asList(task1, task2, task3),
                asList(executeOnce(), executeMultiple(), executeOnce()))
                        .addPipe(task1, task2)
                        .addPipe(task2, task3);

        Workflow workflow = workflowBuilder.build();
        WorkflowService workflowService = new WorkflowServiceImpl();
        workflowService.addWorkflow(workflow);

        WorkflowInstance workflowInstance = workflowService.createWorkflowInstance(workflow, Map.of("message", "Creation Message."));
        workflowInstance = workflowService.startWorkflowInstance(workflowInstance);

        /*try {
            System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/

        // Execute Task 1 and proceed
        workflowService.executeWorkflowInstance(workflowInstance, new HashMap<>());

        // Execute Task 2 and wait
        workflowService.executeWorkflowInstance(workflowInstance, new HashMap<>());

        // Execute Task 2 and wait
        workflowService.executeWorkflowInstance(workflowInstance, new HashMap<>());

        // Execute Task 2 and wait
        workflowService.executeWorkflowInstance(workflowInstance, new HashMap<>());

        // Move from Task 2 to next tasks
        workflowService.proceedWorkflowInstance(workflowInstance, new HashMap<>());

        // Execute Task 3 and proceed
        workflowService.executeWorkflowInstance(workflowInstance, new HashMap<>());

    }
}

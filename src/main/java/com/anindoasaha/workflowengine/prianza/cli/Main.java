package com.anindoasaha.workflowengine.prianza.cli;

import com.anindoasaha.workflowengine.prianza.api.WorkflowService;
import com.anindoasaha.workflowengine.prianza.api.impl.WorkflowServiceImpl;
import com.anindoasaha.workflowengine.prianza.bo.Workflow;
import com.anindoasaha.workflowengine.prianza.bo.WorkflowInstance;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class Main {
    public static void main(String[] args) {
        defaultParser("workflow", args, null);
    }

    /**
     * This parser assumes that it will be supplied a workflow
     * by the users of this library.
     *
     * @param program
     * @param args
     */
    public static Namespace defaultParser(String program, String args[], Workflow workflow) {
        ArgumentParser argumentParser = ArgumentParsers.newFor(program).build();

        Subparsers subparsers = argumentParser.addSubparsers()
                .dest("subparser")
                .title("subcommands")
                .description("valid subcommands")
                .metavar("COMMAND");

        // Load default workflow
        Subparser subparserLoad = subparsers.addParser("load").defaultHelp(true).help("-h");

        // List all workflows
        Subparser subparserList = subparsers.addParser("list").defaultHelp(true).help("-h");

        // Create a new workflow instance
        Subparser subparserCreate = subparsers.addParser("create").defaultHelp(true).help("-h");
        subparserCreate.addArgument("-w", "--workflow-id").required(true);
        subparserCreate.addArgument("-v", "--vars").nargs("*");

        // List all tasks
        Subparser subparserTasks = subparsers.addParser("tasks").help("-h");
        subparserTasks.addArgument("-i", "--instance-id").required(true);

        // Run task for instance
        Subparser subparserRun = subparsers.addParser("run").help("-h");
        subparserRun.addArgument("-i", "--workflow-id").required(true);
        subparserRun.addArgument("-t", "--task-id").required(true);
        subparserRun.addArgument("-v", "--vars").nargs("*");

        Namespace namespace = null;
        try {
            namespace = argumentParser.parseArgs(args);
        } catch (ArgumentParserException e) {
            argumentParser.handleError(e);
        }

        if (namespace != null) {

            WorkflowService workflowService = new WorkflowServiceImpl();
            String workflowInstanceId = null;
            WorkflowInstance instance = null;
            List<String> vars = null;
            switch (namespace.getString("subparser")) {
                case "load" :
                    workflowService.addWorkflow(workflow);
                    System.out.println("Workflow loaded with ID: " + workflow.getId());
                    break;
                case "list" :
                    Map<String, String> workflows = workflowService.listWorkflows();
                    System.out.println("Workflows: ");
                    for (Map.Entry<String, String> entry : workflows.entrySet()) {
                        System.out.println(entry.getValue() + " : " + entry.getKey());
                    }
                    break;
                case "create" :
                    String workflowId = namespace.getString("workflow_id");
                    // Read instance variables
                    vars = namespace.getList("vars");
                    workflowInstanceId = workflowService.createWorkflowInstance(workflowId, convertToMap(vars));
                    workflowService.startWorkflowInstance(workflowInstanceId);
                    System.out.println("Instance created and started with ID: " + workflowInstanceId);
                    break;
                case "tasks" :
                    workflowInstanceId = namespace.getString("instance_id");
                    instance = workflowService.getWorkflowInstanceByWorkflowInstanceId(workflowInstanceId);
                    System.out.println("Tasks: ");
                    System.out.println("[Available]");
                    List<String> currentTaskIds = instance.getCurrentTaskIds();
                    for (String currentTaskId : currentTaskIds) {
                        System.out.println(currentTaskId);
                    }
                    System.out.println("[Executed]");
                    List<String> executedTaskIds = instance.getExecutedTaskIds();
                    for (String executedTaskId : executedTaskIds) {
                        System.out.println(executedTaskId);
                    }
                    break;
                case "run" :
                    workflowInstanceId = namespace.getString("instance_id");
                    String taskId = namespace.getString("task_id");
                    vars = namespace.getList("vars");
                    instance = workflowService.getWorkflowInstanceByWorkflowInstanceId(workflowInstanceId);

                    // Read task variables
                    workflowService.executeWorkflowInstance(workflowInstanceId, taskId, convertToMap(vars));
                    break;
            }
        }
        return namespace;
    }

    private static Map<String, String> convertToMap(List<String> vars) {
        if(vars == null || vars.isEmpty()) {
            return new HashMap<>();
        }
        Map<String, String> parameters = new HashMap<>();
        for (String var: vars) {
            String[] split = var.split("=");
            parameters.put(split[0], split[1]);
        }
        return parameters;
    }
}

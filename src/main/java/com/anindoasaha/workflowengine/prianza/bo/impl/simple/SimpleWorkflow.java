package com.anindoasaha.workflowengine.prianza.bo.impl.simple;

import com.anindoasaha.workflowengine.prianza.bo.Task;
import com.anindoasaha.workflowengine.prianza.bo.TaskExecutionInfo;
import com.anindoasaha.workflowengine.prianza.bo.Workflow;
import com.anindoasaha.workflowengine.prianza.util.IdGenerator;

import java.util.*;

/**
 * This implementation is a 'simplistic' model of a workflow.
 * Workflow objects that derive from this class are best suited
 * to be run from commandline application.
 *
 * Features:
 * The flow from one task to another cannot be time-based.
 *
 * The tasks can be from among the following execution types:
 * The task can be executed immediately by the engine on arriving at a node
 * and proceed from that task to another after execution.
 *
 * Or it can wait before execution for user input and
 * proceed from that task to another immediately after execution.
 *
 * Or it can wait after execution for manual push into the next state.
 * A task that can wait after execution can be configured to be run
 * once or multiple times.
 *
 */
public class SimpleWorkflow implements Workflow {
    private String id = null;
    private String name = null;
    private String description = null;

    private String workflowType = getWorkflowType();


    private Map<String, Task> tasks = null;
    private Map<String, TaskExecutionInfo> taskExecutionInfo = null;
    private Map<String, List<String>> directedAcyclicGraph = null;

    private Map<String, String> initVariables = null;

    private static IdGenerator<String, String> identityGenerator = new IdGenerator<>(
            n -> n + "_" + UUID.randomUUID().toString());


    public SimpleWorkflow() {}

    public SimpleWorkflow(String name, String description) {
        this.id = identityGenerator.generate(name);
        this.name = name;
        this.description = description;
        this.tasks = new HashMap<>();
        this.taskExecutionInfo = new HashMap<>();
        this.directedAcyclicGraph = new HashMap<>();
        this.initVariables = new HashMap<>();
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Task> getTasks() {
        return tasks;
    }

    public void setTasks(Map<String, Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public Map<String, TaskExecutionInfo> getTaskExecutionInfo() {
        return taskExecutionInfo;
    }

    @Override
    public void setTaskExecutionInfo(Map<String, TaskExecutionInfo> taskExecutionInfo) {
        this.taskExecutionInfo = taskExecutionInfo;
    }

    public void addTask(Task task) {
        this.getTasks().put(task.getId(), task);
        this.getTaskExecutionInfo().put(task.getId(), new TaskExecutionInfo());
        this.getDirectedAcyclicGraph().put(task.getId(), new ArrayList<>());
    }

    public void addTask(Task task, TaskExecutionInfo taskExecutionInfo) {
        this.getTasks().put(task.getId(), task);
        TaskExecutionInfo t = taskExecutionInfo != null ? taskExecutionInfo : new TaskExecutionInfo();
        this.getTaskExecutionInfo().put(task.getId(), t);
        this.getDirectedAcyclicGraph().put(task.getId(), new ArrayList<>());
    }

    public Map<String, List<String>> getDirectedAcyclicGraph() {
        return directedAcyclicGraph;
    }

    public void setDirectedAcyclicGraph(Map<String, List<String>> directedAcyclicGraph) {
        this.directedAcyclicGraph = directedAcyclicGraph;
    }

    public Map<String, String> getInitVariables() {
        return initVariables;
    }

    public void setInitVariables(Map<String, String> initVariables) {
        this.initVariables = initVariables;
    }

    public static class Builder {
        private SimpleWorkflow workflow = null;

        public Builder(String name) {
            this.workflow = new SimpleWorkflow(name, name);
        }

        public Workflow build() {
            return workflow;
        }

        public Builder addTask(Task task) {
            workflow.addTask(task);
            return this;
        }

        public Builder addTask(Task task, TaskExecutionInfo taskExecutionInfo) {
            workflow.addTask(task, taskExecutionInfo);
            return this;
        }

        public Builder addTasks(Task... tasks) {
            for (Task task : tasks) {
                workflow.addTask(task);
            }
            return this;
        }

        public Builder addTasks(List<Task> tasks, List<TaskExecutionInfo> taskExecutionInfos) {
            for (int i = 0; i < tasks.size(); i++) {
                workflow.addTask(tasks.get(i), taskExecutionInfos.get(i));
            }
            return this;
        }

        public Builder addPipe(Task source, Task destination) {
            workflow.getDirectedAcyclicGraph().get(source.getId()).add(destination.getId());
            return this;
        }

        public Builder addScatterGatherPipe(Task source, List<Task> intermediateTasks, Task destinationTask) {
            return null;
        }

        public Builder addFanOutPipe(Task sourceTask, List<Task> destinationTasks) {
            return null;
        }

        public Builder addZipWithPipe(List<Task> sourceTasks, List<Task> destinationTasks) {
            return null;
        }

        public Builder addFanInPipe(List<Task> sourceTasks, Task destinationTask) {
            for (Task sourceTask : sourceTasks) {
                workflow.getDirectedAcyclicGraph().get(sourceTask.getId()).add(destinationTask.getId());
            }
            return this;
        }

        public Builder initVariables(Map<String, String> initVariables) {
            workflow.setInitVariables(initVariables);
            return this;
        }
    }
}

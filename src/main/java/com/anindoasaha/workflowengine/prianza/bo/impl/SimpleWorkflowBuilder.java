package com.anindoasaha.workflowengine.prianza.bo.impl;

import com.anindoasaha.workflowengine.prianza.bo.Task;
import com.anindoasaha.workflowengine.prianza.bo.Workflow;

import java.util.List;
import java.util.Map;

public class SimpleWorkflowBuilder {
    private SimpleWorkflow workflow = null;

    public SimpleWorkflowBuilder(String name) {
        this.workflow = new SimpleWorkflow(name, name);
    }

    public Workflow build() {
        return workflow;
    }

    public SimpleWorkflowBuilder addTask(Task task) {
        workflow.addTask(task);
        return this;
    }

    public SimpleWorkflowBuilder addTasks(Task... tasks) {
        for (Task task : tasks) {
            workflow.addTask(task);
        }
        return this;
    }

    public SimpleWorkflowBuilder addPipe(Task source, Task destination) {
        workflow.getDirectedAcyclicGraph().get(source.getId()).add(destination.getId());
        return this;
    }

    public SimpleWorkflowBuilder addScatterGatherPipe(Task source, List<Task> intermediateTasks, Task destinationTask) {
        return null;
    }

    public SimpleWorkflowBuilder addFanOutPipe(Task sourceTask, List<Task> destinationTasks) {
        return null;
    }

    public SimpleWorkflowBuilder addZipWithPipe(List<Task> sourceTasks, List<Task> destinationTasks) {
        return null;
    }

    public SimpleWorkflowBuilder addFanInPipe(List<Task> sourceTasks, Task destinationTask) {
        for (Task sourceTask : sourceTasks) {
            workflow.getDirectedAcyclicGraph().get(sourceTask.getId()).add(destinationTask.getId());
        }
        return this;
    }

    public SimpleWorkflowBuilder initVariables(Map<String, String> initVariables) {
        workflow.setInitVariables(initVariables);
        return this;
    }
}

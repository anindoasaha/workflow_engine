package com.anindoasaha.prianza.api;

import com.anindoasaha.prianza.bo.Task;
import com.anindoasaha.prianza.bo.Workflow;
import com.anindoasaha.prianza.bo.impl.SimpleWorkflow;

import java.util.List;

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
        return null;
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
        return null;
    }
}

package com.anindoasaha.workflowengine.prianza.bo.impl;

import com.anindoasaha.workflowengine.prianza.bo.Task;
import com.anindoasaha.workflowengine.prianza.bo.Workflow;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SimpleWorkflow implements Workflow {
    private String id = null;
    private String name = null;
    private String description = null;

    private String workflowType = getWorkflowType();


    private Map<String, Task> tasks = null;
    private Map<String, List<String>> directedAcyclicGraph = null;

    public SimpleWorkflow() {
    }

    public SimpleWorkflow(String name, String description) {
        this.id = UUID.randomUUID().toString();
        this.name = name + "_" + ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME);
        this.description = description;
        this.tasks = new HashMap<>();
        this.directedAcyclicGraph = new HashMap<>();
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

    public void addTask(Task task) {
        this.getTasks().put(task.getId(), task);
        this.getDirectedAcyclicGraph().put(task.getId(), new ArrayList<>());
    }

    public Map<String, List<String>> getDirectedAcyclicGraph() {
        return directedAcyclicGraph;
    }

    public void setDirectedAcyclicGraph(Map<String, List<String>> directedAcyclicGraph) {
        this.directedAcyclicGraph = directedAcyclicGraph;
    }
}

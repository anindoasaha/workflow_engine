package com.anindoasaha.workflowengine.prianza.data.impl;

import com.anindoasaha.workflowengine.prianza.bo.Task;
import com.anindoasaha.workflowengine.prianza.bo.Workflow;
import com.anindoasaha.workflowengine.prianza.bo.WorkflowInstance;
import com.anindoasaha.workflowengine.prianza.bo.impl.SimpleWorkflow;
import com.anindoasaha.workflowengine.prianza.data.WorkflowRepository;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class FileBasedWorkflowRepositoryImpl implements WorkflowRepository {

    private Map<String, Workflow> workflowByName = new HashMap<>();
    private Map<String, Workflow> workflowById = new HashMap<>();

    private Map<String, WorkflowInstance> workflowInstanceByName = new HashMap<>();
    private Map<String, WorkflowInstance> workflowInstanceById = new HashMap<>();

    private static final Properties defaultProperties = new Properties();

    private Properties properties = null;

    static {
        defaultProperties.setProperty("BASE_PATH", ".");
    }

    public FileBasedWorkflowRepositoryImpl() {
        this.properties = new Properties(defaultProperties);
    }

    public FileBasedWorkflowRepositoryImpl(Properties properties) {
        this.properties = new Properties(properties);
    }


    @Override
    public void addWorkflow(Workflow workflow) {
        this.workflowById.put(workflow.getId(), workflow);
        this.workflowByName.put(workflow.getName(), workflow);
        saveWorkflowToFile(workflow);
    }

    @Override
    public Workflow getWorkflow(String workflowId) {
        Workflow workflow = this.workflowById.get(workflowId);
        if(workflow == null) {
            workflow = loadWorkflowFromFile(workflowId);
        }
        return loadWorkflowFromFile(workflowId);
    }

    private void saveWorkflowToFile(Workflow workflow) {
        try(FileWriter fileWriter = new FileWriter("workflow_" + workflow.getId() + ".json")) {
            SimpleWorkflow simpleWorkflow = (SimpleWorkflow) workflow;
            new GsonBuilder().setPrettyPrinting().serializeNulls()
                    .create()
                    .toJson(simpleWorkflow, fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveInstanceToFile(WorkflowInstance workflowInstance) {
        try(FileWriter fileWriter = new FileWriter("workflow_instance_" + workflowInstance.getWorkflowInstanceId() + ".json")) {
            new GsonBuilder().setPrettyPrinting().serializeNulls()
                    .create()
                    .toJson(workflowInstance, fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Workflow loadWorkflowFromFile(String workflowId) {
        Workflow workflow = null;
        try {
            workflow = new GsonBuilder()
                    .registerTypeAdapter(Workflow.class, new WorkflowJsonDeserializer())
                    .registerTypeAdapter(Task.class, new TaskJsonDeserializer())
                    .create()
                    .fromJson(new FileReader("workflow_" + workflowId + ".json"),
                    Workflow.class);
            // Create task object
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return workflow;
    }

    private WorkflowInstance loadInstanceFromFile(String workflowInstanceId) {
        WorkflowInstance workflowInstance = null;
        Workflow workflow = null;
        try {
            workflowInstance = new GsonBuilder()
                    .registerTypeAdapter(Task.class, new TaskJsonDeserializer())
                    .create()
                    .fromJson(new FileReader("workflow_instance_" + workflowInstanceId), WorkflowInstance.class);
            workflow = new Gson().fromJson(new FileReader("workflow_" + workflowInstance.getWorkflowId()),
                    Workflow.class);
            // Create task object
            workflowInstance.setTasks(workflow.getTasks());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return workflowInstance;
    }

    @Override
    public String addWorkflowInstance(WorkflowInstance workflowInstance) {
        this.workflowInstanceById.put(workflowInstance.getWorkflowInstanceId(), workflowInstance);
        this.workflowInstanceByName.put(workflowInstance.getWorkflowInstanceName(), workflowInstance);
        saveInstanceToFile(workflowInstance);
        return null;
    }

    @Override
    public String updateWorkflowInstance(WorkflowInstance workflowInstance) {
        this.workflowInstanceById.put(workflowInstance.getWorkflowInstanceId(), workflowInstance);
        this.workflowInstanceByName.put(workflowInstance.getWorkflowInstanceName(), workflowInstance);
        saveInstanceToFile(workflowInstance);
        return null;
    }

    @Override
    public WorkflowInstance getWorkflowInstance(String workflowInstanceId) {
        WorkflowInstance workflowInstance = workflowInstanceById.get(workflowInstanceId);
        if(workflowInstance == null) {
            workflowInstance = loadInstanceFromFile(workflowInstanceId);
        }
        return loadInstanceFromFile(workflowInstanceId);
    }

    @Override
    public String deleteWorkflowInstance(String workflowInstanceId) {
        WorkflowInstance workflowInstance = workflowInstanceById.get(workflowInstanceId);
        workflowInstanceById.remove(workflowInstanceId);
        workflowInstanceByName.remove(workflowInstance.getWorkflowInstanceName());
        return null;
    }

    private static class TaskJsonDeserializer implements JsonDeserializer<Task> {
        @Override
        public Task deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            Class clazz = null;
            try {
                clazz = Class.forName(jsonObject.get("taskType").getAsString());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return context.deserialize(json, clazz);
        }
    }

    private static class WorkflowJsonDeserializer implements JsonDeserializer<Workflow> {
        @Override
        public Workflow deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            Class clazz = null;
            try {
                clazz = Class.forName(jsonObject.get("workflowType").getAsString());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return context.deserialize(json, clazz);
        }
    }
}

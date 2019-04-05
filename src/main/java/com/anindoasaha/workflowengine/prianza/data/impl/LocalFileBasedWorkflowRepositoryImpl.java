package com.anindoasaha.workflowengine.prianza.data.impl;

import com.anindoasaha.workflowengine.prianza.bo.Task;
import com.anindoasaha.workflowengine.prianza.bo.Workflow;
import com.anindoasaha.workflowengine.prianza.bo.WorkflowInstance;
import com.anindoasaha.workflowengine.prianza.bo.impl.simple.SimpleWorkflow;
import com.anindoasaha.workflowengine.prianza.data.WorkflowRepository;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class LocalFileBasedWorkflowRepositoryImpl implements WorkflowRepository {

    private Map<String, Workflow> workflowByName = new HashMap<>();
    private Map<String, Workflow> workflowById = new HashMap<>();

    private Map<String, WorkflowInstance> workflowInstanceByName = new HashMap<>();
    private Map<String, WorkflowInstance> workflowInstanceById = new HashMap<>();

    private static final Properties defaultProperties = new Properties();

    private Properties properties = null;

    static {
        defaultProperties.setProperty("BASE_PATH", System.getProperty("user.home", ".") + File.separatorChar + ".workflow");
    }

    public LocalFileBasedWorkflowRepositoryImpl() {
        this.properties = new Properties(defaultProperties);
        initProperties();
    }

    private void initProperties() {
        boolean success = new java.io.File(properties.getProperty("BASE_PATH")).mkdirs();
    }

    public LocalFileBasedWorkflowRepositoryImpl(Properties properties) {
        this.properties = new Properties(properties);
    }


    @Override
    public void addWorkflow(Workflow workflow) {
        this.workflowById.put(workflow.getId(), workflow);
        this.workflowByName.put(workflow.getName(), workflow);
        saveWorkflowToIndex(workflow);
        saveWorkflowToFile(workflow);
    }

    @Override
    public Workflow getWorkflow(String workflowId) {
        Workflow workflow = this.workflowById.get(workflowId);
        if (workflow == null) {
            workflow = loadWorkflowFromFile(workflowId);
        }
        return loadWorkflowFromFile(workflowId);
    }

    @Override
    public Map<String, String> listWorkflows() {
        return loadWorkflowFromIndex();
    }

    @Override
    public Map<String, String> listWorkflowInstances() {
        return loadWorkflowInstanceFromIndex();
    }

    private Map<String, String> loadWorkflowInstanceFromIndex() {
        Map<String, String> workflowInstanceIndex = null;

        FileReader fileReader = null;
        try {
            File file = new File(properties.getProperty("BASE_PATH"), "workflow_instance_index.json");
            if (file.createNewFile()) {
                workflowInstanceIndex = new HashMap<>();
            } else {
                fileReader = new FileReader(file);
                Type type = new TypeToken<Map<String, String>>() {
                }.getType();
                workflowInstanceIndex = new GsonBuilder()
                        .create()
                        .fromJson(fileReader, type);
                if (workflowInstanceIndex == null) {
                    workflowInstanceIndex = new HashMap<>();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileReader != null) {
                    fileReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return workflowInstanceIndex;
    }

    private Map<String, String> loadWorkflowFromIndex() {
        Map<String, String> workflowIndex = null;

        FileReader fileReader = null;
        try {
            File file = new File(properties.getProperty("BASE_PATH"), "workflow_index.json");
            if (file.createNewFile()) {
                workflowIndex = new HashMap<>();
            } else {
                fileReader = new FileReader(file);
                Type type = new TypeToken<Map<String, String>>() {
                }.getType();
                workflowIndex = new GsonBuilder()
                        .create()
                        .fromJson(fileReader, type);
                if (workflowIndex == null) {
                    workflowIndex = new HashMap<>();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileReader != null) {
                    fileReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return workflowIndex;
    }

    private void saveWorkflowInstanceToIndex(WorkflowInstance workflowInstance) {
        Map<String, String> workflowInstanceFromIndex = loadWorkflowInstanceFromIndex();
        try (FileWriter fileWriter = new FileWriter(properties.getProperty("BASE_PATH") + File.separatorChar + "workflow_instance_index.json")) {
            workflowInstanceFromIndex.put(workflowInstance.getId(),
                                          workflowInstance.getName());
            new GsonBuilder().setPrettyPrinting().serializeNulls()
                    .create()
                    .toJson(workflowInstanceFromIndex, fileWriter);
        } catch (RuntimeException e) {
            System.out.println("workflowInstanceFromIndex: " + workflowInstanceFromIndex);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveWorkflowToIndex(Workflow workflow) {
        Map<String, String> workflowFromIndex = loadWorkflowFromIndex();
        try (FileWriter fileWriter = new FileWriter(properties.getProperty("BASE_PATH") + File.separatorChar + "workflow_index.json")) {
            workflowFromIndex.put(workflow.getId(), workflow.getName());
            new GsonBuilder().setPrettyPrinting().serializeNulls()
                    .create()
                    .toJson(workflowFromIndex, fileWriter);
        } catch (RuntimeException e) {
            System.out.println("workflow: " + workflow);
            System.out.println("workflowFromIndex: " + workflowFromIndex);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveWorkflowToFile(Workflow workflow) {
        try (FileWriter fileWriter = new FileWriter(properties.getProperty("BASE_PATH") + File.separatorChar + "workflow_" + workflow.getId() + ".json")) {
            SimpleWorkflow simpleWorkflow = (SimpleWorkflow) workflow;
            new GsonBuilder().setPrettyPrinting().serializeNulls()
                    .create()
                    .toJson(simpleWorkflow, fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveInstanceToFile(WorkflowInstance workflowInstance) {
        try (FileWriter fileWriter = new FileWriter(properties.getProperty("BASE_PATH") + File.separatorChar + "workflow_instance_" + workflowInstance.getId() + ".json")) {
            new GsonBuilder().setPrettyPrinting().serializeNulls()
                    .create()
                    .toJson(workflowInstance, fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Workflow loadWorkflowFromFile(String workflowId) {
        Workflow workflow = null;
        try (FileReader fileReader = new FileReader(properties.getProperty("BASE_PATH") + File.separatorChar + "workflow_" + workflowId + ".json")) {

            workflow = new GsonBuilder()
                    .registerTypeAdapter(Workflow.class, new WorkflowJsonDeserializer())
                    .registerTypeAdapter(Task.class, new TaskJsonDeserializer())
                    .create()
                    .fromJson(fileReader,
                            Workflow.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return workflow;
    }

    private WorkflowInstance loadInstanceFromFile(String workflowInstanceId) {
        WorkflowInstance workflowInstance = null;
        Workflow workflow = null;
        try (FileReader workflowInstanceFileReader = new FileReader(properties.getProperty("BASE_PATH") + File.separatorChar + "workflow_instance_" + workflowInstanceId + ".json")) {

            workflowInstance = new GsonBuilder()
                    .registerTypeAdapter(Task.class, new TaskJsonDeserializer())
                    .create()
                    .fromJson(workflowInstanceFileReader, WorkflowInstance.class);

            try (FileReader workflowFileReader = new FileReader(properties.getProperty("BASE_PATH") + File.separatorChar + "workflow_" + workflowInstance.getWorkflowId() + ".json")) {
                workflow = new GsonBuilder()
                        .registerTypeAdapter(Workflow.class, new WorkflowJsonDeserializer())
                        .registerTypeAdapter(Task.class, new TaskJsonDeserializer())
                        .create()
                        .fromJson(workflowFileReader,
                                Workflow.class);
            }
            // Create task object
            workflowInstance.setTasks(workflow.getTasks());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return workflowInstance;
    }

    @Override
    public String addWorkflowInstance(WorkflowInstance workflowInstance) {
        this.workflowInstanceById.put(workflowInstance.getId(), workflowInstance);
        this.workflowInstanceByName.put(workflowInstance.getName(), workflowInstance);
        saveWorkflowInstanceToIndex(workflowInstance);
        saveInstanceToFile(workflowInstance);
        return null;
    }

    @Override
    public String updateWorkflowInstance(WorkflowInstance workflowInstance) {
        this.workflowInstanceById.put(workflowInstance.getId(), workflowInstance);
        this.workflowInstanceByName.put(workflowInstance.getName(), workflowInstance);
        saveInstanceToFile(workflowInstance);
        return null;
    }

    @Override
    public WorkflowInstance getWorkflowInstance(String workflowInstanceId) {
        WorkflowInstance workflowInstance = workflowInstanceById.get(workflowInstanceId);
        if (workflowInstance == null) {
            workflowInstance = loadInstanceFromFile(workflowInstanceId);
        }
        return loadInstanceFromFile(workflowInstanceId);
    }

    @Override
    public String deleteWorkflowInstance(String workflowInstanceId) {
        WorkflowInstance workflowInstance = workflowInstanceById.get(workflowInstanceId);
        workflowInstanceById.remove(workflowInstanceId);
        workflowInstanceByName.remove(workflowInstance.getName());
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

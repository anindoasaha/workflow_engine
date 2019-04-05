package com.anindoasaha.workflowengine.prianza.bo;

import java.util.List;
import java.util.Map;

public interface Workflow extends Entity {

    @Deprecated
    default String getWorkflowType() {
        return this.getClass().getCanonicalName();
    }

    public Map<String, Task> getTasks();

    public void setTasks(Map<String, Task> tasks);

    public Map<String, TaskExecutionInfo> getTaskExecutionInfo();

    public void setTaskExecutionInfo(Map<String, TaskExecutionInfo> taskExecutionInfo);

    public Map<String, List<String>> getDirectedAcyclicGraph();

    public void setDirectedAcyclicGraph(Map<String, List<String>> directedAcyclicGraph);
}

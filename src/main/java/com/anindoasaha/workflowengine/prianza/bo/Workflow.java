package com.anindoasaha.workflowengine.prianza.bo;

import java.util.List;
import java.util.Map;

public interface Workflow {

    default String getWorkflowType() {
        return this.getClass().getCanonicalName();
    }

    String getName();

    String getId();

    public Map<String, Task> getTasks();

    public void setTasks(Map<String, Task> tasks);

    public Map<String, List<String>> getDirectedAcyclicGraph();

    public void setDirectedAcyclicGraph(Map<String, List<String>> directedAcyclicGraph);
}

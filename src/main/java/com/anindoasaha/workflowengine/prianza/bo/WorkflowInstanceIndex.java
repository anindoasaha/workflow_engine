package com.anindoasaha.workflowengine.prianza.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkflowInstanceIndex {

    /**
     * A mapping from instanceId to their location.
     *
     * For example:
     * <instanceId> => <instanceId> for parent_level workflow instances
     * <instanceId> => <instanceId/task_id/nested_instanceId> for inner workflow instances
     */
    Map<String, InstancePath> pathMap = new HashMap<>();

    public class InstancePath {
        List<PathLevel> pathLevels = new ArrayList<>();
    }

    public class PathLevel {
        int level; // For level 0, only instanceId exists
        String taskId = null;
        String instanceId = null;
    }
}

package com.anindoasaha.prianza.data;

import com.anindoasaha.prianza.bo.Workflow;
import com.anindoasaha.prianza.bo.WorkflowInstance;

public interface WorkflowRepository {
    void addWorkflow(Workflow workflow);

    String addWorkflowInstance(WorkflowInstance workflowInstance);
}

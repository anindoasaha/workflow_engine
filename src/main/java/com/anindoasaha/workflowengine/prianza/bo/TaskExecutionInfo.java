package com.anindoasaha.workflowengine.prianza.bo;

import com.anindoasaha.workflowengine.prianza.bo.impl.simple.ExecutionArity;
import com.anindoasaha.workflowengine.prianza.bo.impl.simple.ExecutionType;

import java.util.Map;

public class TaskExecutionInfo {

    private ExecutionType EXECUTION_TYPE = ExecutionType.EXECUTE_AND_PROCEED;
    private ExecutionArity EXECUTION_ARITY = ExecutionArity.EXECUTE_ONCE;

    public TaskExecutionInfo() {}

    private TaskExecutionInfo(ExecutionType executionType, ExecutionArity executionArity) {
        this.EXECUTION_TYPE = executionType;
        this.EXECUTION_ARITY = executionArity;
    }

    public static TaskExecutionInfo defaultInstance() {
        return new TaskExecutionInfo();
    }

    public static TaskExecutionInfo executeOnce() {
        return defaultInstance();
    }

    public static TaskExecutionInfo executeMultiple() {
        return new TaskExecutionInfo(ExecutionType.EXECUTE_AND_WAIT, ExecutionArity.EXECUTE_MULTIPLE);
    }

    public ExecutionType getExecutionType() {
        return EXECUTION_TYPE;
    }

    public ExecutionArity getExecutionArity() {
        return EXECUTION_ARITY;
    }
}

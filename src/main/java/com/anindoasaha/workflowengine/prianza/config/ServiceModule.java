package com.anindoasaha.workflowengine.prianza.config;

import com.anindoasaha.workflowengine.prianza.api.WorkflowService;
import com.anindoasaha.workflowengine.prianza.api.impl.WorkflowServiceImpl;
import com.google.inject.AbstractModule;

public class ServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(WorkflowService.class).to(WorkflowServiceImpl.class);
    }
}

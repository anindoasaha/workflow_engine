package com.anindoasaha.workflowengine.prianza.config;

import com.anindoasaha.workflowengine.prianza.data.WorkflowRepository;
import com.anindoasaha.workflowengine.prianza.data.impl.LocalFileBasedWorkflowRepositoryImpl;
import com.google.inject.AbstractModule;

public class RepositoryModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(WorkflowRepository.class).to(LocalFileBasedWorkflowRepositoryImpl.class);
    }
}

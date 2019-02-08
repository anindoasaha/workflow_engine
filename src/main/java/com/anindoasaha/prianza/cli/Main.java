package com.anindoasaha.prianza.cli;

import com.anindoasaha.prianza.api.WorkflowService;
import com.anindoasaha.prianza.api.impl.WorkflowServiceImpl;
import com.anindoasaha.prianza.bo.Workflow;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.*;

public class Main {
    public static void main(String[] args) {
        ArgumentParser argumentParser = ArgumentParsers.newFor("workflow").build();

        Subparsers subparsers = argumentParser.addSubparsers()
                .title("subcommands")
                .description("valid subcommands")
                .help("additional help");

        // List all workflows
        Subparser subparserList = subparsers.addParser("list");
        subparserList.addArgument("-v", "--verbose");

        // Create a new workflow
        Subparser subparserCreate = subparsers.addParser("create");
        subparserCreate.addArgument("-w", "--workflow-name");

        Subparser subparserStatus = subparsers.addParser("status");
        subparserStatus.addArgument("-w", "--workflow-name");

        Namespace namespace = null;
        try {
            namespace = argumentParser.parseArgs(args);
        } catch (ArgumentParserException e) {
            argumentParser.handleError(e);
        }
        String workflowName = namespace.getString("workflow_name");
        System.out.println(workflowName);

        WorkflowService workflowService = new WorkflowServiceImpl();
    }
}

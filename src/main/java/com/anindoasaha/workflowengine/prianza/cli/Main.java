package com.anindoasaha.workflowengine.prianza.cli;

import com.anindoasaha.workflowengine.prianza.cli.cmd.Parser;
import com.anindoasaha.workflowengine.prianza.config.Configuration;

public class Main {
    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        Parser.defaultParser("workflow", args, null);
    }
}

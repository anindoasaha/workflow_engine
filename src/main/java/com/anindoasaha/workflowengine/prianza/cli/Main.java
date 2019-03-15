package com.anindoasaha.workflowengine.prianza.cli;

import com.anindoasaha.workflowengine.prianza.cli.cmd.Parser;

public class Main {
    public static void main(String[] args) {
        Parser.defaultParser("workflow", args, null);
    }
}

package com.roscopeco.jasm.model.disasm;

public class FinallyClass {
    public String theMethod() {
        var str = "Original";

        try {
            str = "Try";
        } finally {
            str = "Finally";
        }

        return str;
    }
}

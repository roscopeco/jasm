package com.roscopeco.jasm.model.disasm;

public class ExceptionTest {
    public void test() {
        try {
            throw new InterruptedException("BANG");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

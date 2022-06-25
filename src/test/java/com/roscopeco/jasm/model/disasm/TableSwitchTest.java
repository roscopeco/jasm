package com.roscopeco.jasm.model.disasm;

public class TableSwitchTest {
    public void test(final int i) {
        switch (i) {
            case 1:
                System.out.println("One");
                break;
            case 2:
                System.out.println("Two");
                break;
            case 5:
                System.out.println("Five");
                break;
        }
    }
}

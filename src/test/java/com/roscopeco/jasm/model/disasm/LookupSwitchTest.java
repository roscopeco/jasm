package com.roscopeco.jasm.model.disasm;

public class LookupSwitchTest {
    public void test(final int i) {
        switch (i) {
            case 1:
                System.out.println("One");
                break;
            case 10000:
                System.out.println("TenThou");
                break;
            case 2000000:
                System.out.println("TwoMil");
                break;
        }
    }
}

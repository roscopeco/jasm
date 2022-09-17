package com.roscopeco.jasm.model.disasm;

public enum AnEnum {
    ONE(1),
    TWO(2);

    private final int i;

    AnEnum(int i) {
        this.i = i;
    }

    public int getI() {
        return i;
    }
}

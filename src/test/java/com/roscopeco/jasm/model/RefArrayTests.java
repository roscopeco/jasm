package com.roscopeco.jasm.model;

public interface RefArrayTests {
    String[] newSingleElementArray();
    void putInArray(final String[] array, final String data);
    String getFromArray(final String[] array);
    int getArrayLength(final String[] array);

    String[][] multiANewArrayTest();
    String[][] multiANewArrayExplicitDimsTest();
}

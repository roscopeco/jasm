package com.roscopeco.jasm.model.disasm;

import java.util.Map;
import java.util.stream.Collectors;

public class InvokeDynamicTest {
    public static String test() {
        var f = Map.of("World", 'a').entrySet().stream().map(Map.Entry::getKey).map(Object::toString);
        return "Hello " + f.collect(Collectors.joining());
    }
}

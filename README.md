## JASM Java Assembler for the modern age

### What?

JASM is an assembler for JVM bytecode. It is similar in spirit to the venerable 
[Jasmin](https://github.com/davidar/jasmin) (which I help maintain via code shelter),
though has a different syntax, and is built on modern tools and targets modern JVMs 
(currently 17 by default).

It lets you write code like this:

```java
public class com/example/MyClass
extends com/example/Superclass
implements com/example/SomeInterface {
    public <init>()V {
        aload 0
        invokespecial com/example/Superclass.<init>()V
        return
    }

    public dontAddCanary(Ljava/util/List;)V {
        goto skipAdd

        aload 1
        ldc "CANARY"
        invokeinterface java/util/List.add(Ljava/lang/Object;)Z 
        
    skipAdd:
        return
    }
}
```

or use advanced JVM instructions like `invokedynamic`:

```java
public doBasicInvokeDynamicTest()Ljava/lang/String; {
    invokedynamic get()Ljava/util/function/Supplier; {
        invokestatic java/lang/invoke/LambdaMetafactory.metafactory(
            Ljava/lang/invoke/MethodHandles$Lookup;
            Ljava/lang/String;
            Ljava/lang/invoke/MethodType;
            Ljava/lang/invoke/MethodType;
            Ljava/lang/invoke/MethodHandle;
            Ljava/lang/invoke/MethodType;
        )Ljava/lang/invoke/CallSite;
        [
            ()Ljava/lang/Object;,
            invokestatic com/roscopeco/jasm/model/TestBootstrap.lambdaGetImpl()Ljava/lang/String;,
            ()Ljava/lang/String;
        ]
    }

    invokeinterface java/util/function/Supplier.get()Ljava/lang/Object;
    checkcast java/lang/String
    areturn
}
```
The main code and most of the test support code is written in Kotlin. Tests 
themselves are written in Java however, because I felt like mixing it up
(and wanted to validate the custom AssertJ stuff interop with Java was good).

Lexing, parsing and syntax trees are handled by Antlr4 (https://www.antlr.org).
Bytecode generation is done with ASM (https://asm.ow2.io). Both of these
projects are awesome and deserve your attention.

### Why??

Well, **why not**?

I wrote this for fun, which I had both in writing it and playing with it. 

I release it without any expectation that it will be at all _useful_, but with 
the sincere hope that you too might find it fun.

If you really need some use-cases to justify the electrons squandered in
pursuit of this project, how about these (some lifted from Jasmin's README):

* Curious People - Want to understand more about the way JVM bytecode works
  or the things that are possible at the bytecode level? Always wondered what 
  `invokedynamic` is for? Curious as to how `@SneakyThrows` can possibly work?
  This might help!

* System Implementors - If you're writing a JVM or runtime this could be useful
  for generating test classes...

* Advanced Programmers - You could hand-generate critical classes or methods
  if you think Javac isn't doing the right thing (but spoiler alert: by this
  point, it almost certainly is).

* Language Implementors - You could use this as an IL if you liked, rather
  than getting involved in the nuts and bolts of the binary `.class` format.

* Security Researchers - Create hostile classes and see if you can sneak them
  past the class verifier.
* 
* Teachers - Perhaps you're teaching a compiler course, maybe you could use this
  to introduce students to JVM bytecode, or even as an IL for the compilers.

### Who? 

JASM is copyright 2022 Ross Bamford (roscopeco AT gmail DOT com). 

See LICENSE.md for the gory legal stuff (spoiler: MIT).
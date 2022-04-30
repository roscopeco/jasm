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

### Why??

Well, **why not**?

I wrote this for fun, both in writing and using it. I release it without any 
expectation that it will be especially _useful_, but with the hope that you
too might find it fun.

If you really need some use-cases to justify the electrons squandered in
pursuit of this project, how about these (mostly lifted from Jasmin's README):

* Curious People - Want to understand more about the way JVM bytecode works
  or the things that are possible at the bytecode level? This might help!

* Teachers - Perhaps you're teaching a compiler course, you could use this
  to introduce students to JVM bytecode, or even as an IL for the compilers.

* System Implementors - If you're writing a JVM or runtime this could be useful
  for generating test classes...

* Advanced Programmers - You could hand-generate critical classes or methods
  if you think Javac isn't doing the right thing (but spoiler alert: by this
  point, it almost certainly is).

* Language Implementors - You could use this as an IL if you liked, rather
  than getting involved in the nuts and bolts of the binary `.class` format.

* Security Researchers - Create hostile classes and see if you can sneak them
  past the class verifier.

### Who? 

JASM is copyright 2022 Ross Bamford (roscopeco AT gmail DOT com). See LICENSE.md
for the gory legal stuff.
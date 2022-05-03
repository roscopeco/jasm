## JASM Cookbook

### Some standard JVM assembly recipes

#### General notes

* `javac` together with `javap` are great for finding out how a given Java construct compiles -
  simply write out the construct in Java, compile it and then disassemble the `.class` file 
  with `javap`, passing in the `-c` option to see the disassembly.

#### The verifier

The JVM passes all classes through the bytecode verifier when they are linked. The verifier is 
pretty clever and won't let you get away with anything _too_ dangerous (for the most part anyway).

The rules used by the verifier change with JVM versions, but classes will be verified based on
the rules that the verifier for their particular class format version used.

Being an assembler, JASM only provides the bare minimum protection from verifier errors (after all, you might
want to generate deliberately-shady classes to test the verifier or see if you can beat it). 

Most of the time, when you see errors while loading classes assembled with JASM, those errors are
coming from the verifier. In these cases:

* Check that the stack / locals contain what you think they do (the error message will tell you)
* Check that you've used the right operand type (e.g. not using `dadd` for float arguments)
* If related to reference types, you might need to add a `checkcast` to satisfy the verifier that your type is correct
* If related to locals, and you have `double` or `long` in the mix, remember that these take up two slots!

More info on the verifier can be found here: https://docs.oracle.com/javase/specs/jvms/se17/html/jvms-4.html#jvms-4.10.2.2

#### Calling methods

When calling a method, the important thing is to ensure your stack is in the right order and that you have 
the correct number of arguments (of the correct types) to satisy the method descriptor.

Generally, you want the stack to look like:

```
..., receiver, arg1, arg2
```

For example, if you have a method `com/example/MyClass.someMethod(java/lang/String str1, I anInt)java/lang/String` 
you will want the stack to look like:

```
..., <MyClass instance>, <String>, <int>
```

Assuming this is a virtual method, you would then do 
`invokevirtual com/example/MyClass.someMethod(java/lang/String str1, I anInt)java/lang/String`
which will call the method with the args and receiver popped from the stack. The instruction will push the
return value from the method (a String in this case according to the descriptor) back onto the stack, leaving it
looking like this:

```
..., <Returned String>
```

Another way to put the above (which the JVM spec uses quite a bit so is worth getting used to reading) is:

```
..., receiver, arg1, arg2 -> result
```

Some things to bear in mind:

* In the case of `void` methods no return value will be pushed to the stack
* Arguments are loaded with `aload`. Argument 0 is `this`, except in static methods (when 0 is the first argument)
* Non-private instance methods should usually be invoked with `invokevirtual`
* For static methods (invoked with `invokedstatic`) there will be no receiver
* Private methods are _non-virtual_, and should be invoked with `invokespecial`
  * Constructors are also _non-virtual_, so should be invoked with `invokespecial` regardless of their access modifiers
* At the risk of stating the obvious, interface methods should be invoked with `invokeinterface`
  * There is some nuance here, but generally if the receiver type on the stack is of an interface type
    rather than a concrete one, use `invokeinterface`
* `invokedynamic` is quite different, and will have its own section later in this document


#### Object instantiation

The general form for instantiation and initialization of a new object is:

```java
new com/example/SomeClass
dup    
invokespecial com/example/SomeClass.<init>()V
```

At the end of this sequence you will have an initialized instance at the top of the stack. The default (i.e. no-arg)
constructor will have been called (indicated by the `()V` method descriptor).

If your constructor requires arguments, load them after the `dup` to keep the stack in the right order - 
the order is as for any other method call. For example, if your constructor takes a String argument:

```java
new com/example/SomeClass
dup
ldc "A String"        
invokespecial com/example/SomeClass.<init>(java/lang/String)V
```

Things to note:

* Constructors are _always_ named `<init>`
* They _always_ have a `void` return type
* They are _always_ invokved with `invokespecial` (they are non-virtual instance methods)
* You don't _have_ to call a constructor - the JVM will let you proceed with an `uninitialized` reference.
* You need the `dup` because, as with any other method call, the receiver is popped from the stack

#### Writing constructors

When you write constructors, you **must** call through to a super constructor, and ultimately to
`java/lang/Object.<init>()V`. If you have a constructor that doesn't do this, you won't get it past the
verifier.

Additionally, there is no "default" constructor at the JVM level. If your class doesn't explicitly declare
a constructor, then you won't be able to instantiate it from Java (even with reflection).

The general form for declaring a constructor is:

```java
public <init>()V{
    aload 0
    invokespecial java/lang/Object.<init>()V
    return    
}
```

Notes:

* `aload 0` loads `this` onto the stack (i.e. argument 0 is always `this`)
* If your immediate superclass is not `java/lang/Object` then you should call through a constructor on that class instead
* If the superclass doesn't have a no-arg constructor, you'll need to supply appropriate arguments (and a suitable descriptor)

#### Generics

Generics are _mostly_ syntactic sugar and checks provided (and enforced) by `javac` - the JVM only has very limited
support for them - they are largely informational in the `.class` file.

JASM currently doesn't have any support for the attributes and annotations that store generics information - 
however it would probably be useful to have (even if non-critical to the actual operation) so I'll probably
add it at some point.

#### Autoboxing

Autoboxing is syntactic sugar provided by `javac`. If you want boxing, you must do it yourself - the runtime provides
nice easy methods to it:

```java
// box, assuming we have a primitive int at the top of the stack
invokestatic java/lang/Integer.valueOf(I)java/lang/Integer

// unbox, assuming we have an Integer instance at the top of the stack
invokevirtual java/lang/Integer.intValue()I
```

The other primitive type wrappers in `java/lang` all have similar methods (and can be used to 
cast between primitives too if you don't want to use the cast instructions for whatever reason).

#### Exceptions

The JVM doesn't really know or particularly care about checked exceptions. You can `athrow` any `java/lang/Throwable`
whenever you like at the bytecode level.

The `.class` format _does_ allow methods to be annotated with what exceptions they throw, but since this is mostly
pointless JASM doesn't expose it.

#### Static initializers

In Java, you have static initializers. For example, given the Java code:

```java
public class MyClass {
  private static final Something SOMETHING;
  private static final AnotherThing ANOTHERTHING = new AnotherThing("Whatever");
    
    static {
        SOMETHING = new Something();
    }
}
```

`javac` will gather together the initialization of `SOMETHING` and `ANOTHERTHING` and put them into a synthetic
method named `<clinit>`. This method then gets called automatically when the class is first used (exactly when
depends on how you load the class, but it's always before any code that uses the class is called).

In JASM, there is no such syntactic sugar. If you need a static initializer, you write `<clinit>` just like
any other static, no-arg, void method:

```java
public class MyClass {
    static <clinit>()V {
        // ... set up fields etc
  }
}
```

As a side note: Specifically for static fields, there are a bunch of types that can be stored directly in the 
constant pool, so _technically_ these wouldnt have to be initialized in a `<clinit>`. These include all the 
types you are able to use with the `ldc` instruction (and do include dynamic consts).

However, JASM currently doesn't provide an easy way to pass a default value for a field, so you're stuck with
`<clinit>`. I may well add this in the future though (or maybe I already did, and forgot to update this document :D ).

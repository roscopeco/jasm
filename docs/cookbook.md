## JASM Cookbook

### Some standard JVM assembly recipes

#### General notes

* `javac` together with `javap` are great for finding out how a given Java construct compiles -
  simply write out the construct in Java, compile it and then disassemble the `.class` file 
  with `javap`, passing in the `-c` option to see the disassembly.

* The key to successful JVM assembly programming is knowing the state of your stack before and after
  every instruction. Bear in mind what effect each instruction has on the stack, and take care to manage  
  the stack properly. Remember you can always massage the stack with `POP` and `SWAP` if you need to, but 
  this can usually be avoided with a bit of careful planning. If all else fails the verifier (discussed below) 
  will pretty-much _always_ catch any stack-related errors before it passes your code.

* If you've done any JNI programming, you already have a lot of transferable knowledge that will work here!

#### The verifier

The JVM passes all classes through the bytecode verifier when they are linked. The verifier is 
quite a slick piece of tech in its own right and won't let you get away with anything _too_ dangerous 
(for the most part anyway).

The rules used by the verifier change with JVM versions, but classes will be verified based on
the rules that the verifier for their particular class format version used (which is why you can still use 
`JSR` and `RET` on modern JVMs if the classes are targeted to version < 50, for example).

Being an assembler, JASM only provides the bare minimum protection from verifier errors (after all, you might
want to generate deliberately-shady classes to test the verifier or see if you can beat it). 

Most of the time, when you see errors while loading classes assembled with JASM, those errors are
coming from the verifier. In these cases:

* Check that the stack / locals contain what you think they do (the error message will tell you)
* Check that you've used the right operand type (e.g. not using `dadd` for float arguments)
* If related to reference types, you might need to add a `checkcast` to satisfy the verifier that your type is correct
* If related to locals (and arguments), and you have `double` or `long` in the mix, remember that these take up 
  two slots!
  * A good hint that you've hit this is if the error mentions `double_2nd` or `long_2nd`.

One thing the verifier is quite keen on is that the stack map frames (for class format versions since Java 1.6) or 
`MAXLOCALS` and `MAXSTACK` (for older versions) are correct - JASM computes these for you based on the
code so you shouldn't need to worry about it too much. If you _do_ find that they're being generated
incorrectly it's quite possible you have found a bug - please report it!

More info on the verifier can be found here: https://docs.oracle.com/javase/specs/jvms/se17/html/jvms-4.html#jvms-4.10.2.2

#### Calling methods

When calling a method, the important thing is to ensure your stack is in the right order and that you have 
the correct number of arguments (of the correct types) to satisy the method descriptor.

Generally, you want the stack to look like:

```
..., receiver, arg1, arg2
```

For example, if you have a method `com/example/MyClass.someMethod(java/lang/String, I)java/lang/String` 
you will want the stack to look like:

```
..., <MyClass instance>, <String>, <int>
```

Assuming this is a virtual method, you would then do 
`invokevirtual com/example/MyClass.someMethod(java/lang/String, I)java/lang/String`
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
* Arguments are passed in local slots, and are loaded with the `Xload` family of instructions (`aload`, `iload`, etc 
  depending on type). Argument 0 is `this`, except in static methods (when 0 is the first argument)
* Non-private instance methods should usually be invoked with `invokevirtual`
* For static methods (invoked with `invokedstatic`) there will be no receiver
* Private methods are _non-virtual_, and should be invoked with `invokespecial`
  * Constructors are also _non-virtual_, so should be invoked with `invokespecial` regardless of their access modifiers
  * Another way to think of `invokespecial` is that it's direct invocation, rather than going through the virtual table...
    * Bonus trivia - the register-based Dalvik VM that was once used on Android devices actually called this 
      `invokedirect` for that reason :)
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
* You don't _have_ to call a constructor - the JVM will let you proceed with an `uninitialized_this` reference.
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

* `aload 0` loads `this` onto the stack (i.e. argument 0 is always `this` in a non-static method or constructor)
* If your immediate superclass is not `java/lang/Object` then you should call through a constructor on that class instead
* If the superclass doesn't have a no-arg constructor, you'll need to supply appropriate arguments (and a suitable descriptor)

#### Generics

Generics are _mostly_ syntactic sugar and checks provided (and enforced) by `javac` - the JVM only has very limited
support for them - they are largely informational in the `.class` file.

The subject is broad and deep, but the essence of it is that generics in Java are implemented by _type erasure_. 
What this means is, if you're implementing a generic method in JASM, you will generally use `java/lang/Object`
as the type. 

JASM currently doesn't have any support for the attributes and annotations that store generics information - 
however it would probably be useful to have (even if non-critical to the actual operation) so I'll probably
add it at some point.

#### Autoboxing

Autoboxing is syntactic sugar provided by `javac`. If you want boxing, you must do it yourself - the runtime provides
nice easy methods to handle it in both directions (which are also used by code compiled with `javac`):

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

_Catching_ exceptions is a different matter, and is done with a bunch of annotations (not the Java language ones, these
are the OG attributes in the class file) on a method that specify instruction offsets and jumps to take on exceptions.

Currently, the latter is not implemented in JASM (but is pretty close to the top of my list of things to add!)

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

As a side note: Specifically for static fields, there are a few types that can be stored directly in the 
constant pool, so _technically_ these don't have to be initialized in a `<clinit>`. These types are:

* String
* int
* float
* double
* long

Currently, JASM supports the first three, with syntax like:

```java
private static final CONST_STR java/lang/String = "Constant String"
private static final CONST_INT I = 10
private static final CONST_FLOAT F = 42.0
```

Note that this is **only** for static fields, a `SyntaxErrorException` will be thrown if you
try to use this for non-statics.
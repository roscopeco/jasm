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

_Catching_ exceptions is a different matter - JASM fully supports this. You can either do it manually, using
labels for your `try` and `catch` blocks, e.g.

```java
    public manualExceptionHandler()V {
        // The handler is wired to the labels and the caught type with the exception statement
        // This can be anywhere in the method, and you can have as many as you need...
        //
        exception tryBegin, tryEnd, catchBegin, java/lang/Exception
        
      tryBegin:
        
        // Stuff that might throw
        
      tryEnd:

        // Stuff to do if it didn't throw

      catchBegin:
        
        // Stuff to do if it did throw. Usually you'll want the previous bit to 
        // return or otherwise skip this, or it'll get executed as part of normal flow
        
    }
```

or you can use the convenient syntactic-sugar JASM provides that looks (and works) a lot like Java:

```java
    public tryCatchSyntax()V {
        try {
            
            // regular JASM instructions that might throw
        
        } catch (java/lang/Exception) {
            
            // Stuff to do if it threw
        
        }
    }
```

`try/catch` as shown in the latter example is fully supported by the syntax, and so can (for example) be nested as 
deep as you need.

It's worth noting that the (Java) feature in which a single catch can handle multiple exception types
(with the `|` operator) is _not_ supported. If you need to catch multiple types, you'll have to have
multiple catches, e.g:

```java
        try {
    
            // code...
        
        } catch (java/lang/RuntimeException) {
    
            // code...
        
        } catch (java/lang/Exception) {
    
            // code...
        }
```

However, if the handlers are common you can easily use `goto` in the catch blocks to redirect execution to a
common handler (or just use the more manual syntax as detailed above).

> **Note**: Where you have multiple catch blocks for a single try, order **does** matter. You always want more
> specific types first, followed by more general ones below, e.g.:
>
> ```java
>        try {
>            // ...
>        } catch (java/io/FileNotFoundException) {
>            // ...
>        } catch (java/io/IOException) {
>            // ...
>        } catch (java/lang/Exception) {
>            // ...
>        }   
>```
> 
> If you don't do this, then the more generic handlers will always be used by the JVM.


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

#### invokedynamic & constdynamic

`invokedynamic` (and its somewhat-related cousin CONSTDYNAMIC) is quite possibly the best JVM instruction you never 
heard of. If you're primarily a Java programmer, you almost certainly use it every day without knowing it.

Briefly, `invokedynamic` is a mechanism by which the totally static, single-dispatch JVM supports less-static multiple
dispatch in a type-safe and hotspot-friendly fashion. Added to ease support for dynamic languages in Java 1.7 (at the
time mostly focused on JRuby and Groovy) it became a fundamental part of every Java developer's toolkit from Java 8
onward, without ever being surfaced into the Java language. This was thanks in no small part to the `LambdaMetafactory`
introduced in J8 to support the new Lambda (and especially method reference) features in the language.

If you're so inclined, you can read more about `invokedynamic` under [JSR-292](https://jcp.org/en/jsr/detail?id=292)
from back the late 2000s/early 2010s when it was finally released.

Put simply, `invokedynamic` allows one to implement a method call which is linked at runtime. One does this by specifying
a so-called "bootstrap method" which will be called directly by the JVM when it encounters an `invokedynamic` instruction.
This method receives a few arguments stacked by the JVM itself, together with an arbitrary number of arguments that are
specified in the instruction itself (and which are constrained to those types that can be stored in the constant pool, 
though thanks to CONSTDYNAMIC this is quite flexible). This bootstrap method must then return a 
`java.lang.invoke.Callsite` which links the dynamic invocation to a regular call.

`invokedynamic` is fully supported by JASM, along with all the variants and argument types. A typical use of the
instruction looks like this:

```java
public doBasicInvokeDynamicTest()Ljava/lang/String {
    invokedynamic get()Ljava/util/function/Supplier {
        invokestatic java/lang/invoke/LambdaMetafactory.metafactory(
            Ljava/lang/invoke/MethodHandles$Lookup,
            Ljava/lang/String,
            Ljava/lang/invoke/MethodType,
            Ljava/lang/invoke/MethodType,
            Ljava/lang/invoke/MethodHandle,
            Ljava/lang/invoke/MethodType
        )Ljava/lang/invoke/CallSite
        [
            ()Ljava/lang/Object,
            invokestatic com/roscopeco/jasm/model/TestBootstrap.lambdaGetImpl()Ljava/lang/String,
            ()Ljava/lang/String
        ]
    }

    invokeinterface java/util/function/Supplier.get()Ljava/lang/Object
    checkcast java/lang/String
    areturn
}
```

while a full-featured one (**note** this is a contrived example intented to demonstrate the syntax):

```java
        invokedynamic get()java/lang/Object {
            invokestatic com/roscopeco/jasm/model/TestBootstrap.testBootstrap(
                java/lang/invoke/MethodHandles$Lookup,
                java/lang/String,
                java/lang/invoke/MethodType,
                I,
                F,
                java/lang/String,
                java/lang/Class,
                java/lang/invoke/MethodHandle,
                java/lang/invoke/MethodType,
                java/lang/String,
            )java/lang/invoke/CallSite
            [
                42,                                                                                             // Static int
                10.0,                                                                                           // Static float
                "Bootstrap test",                                                                               // Static string
                java/util/List,                                                                                 // Static class
                invokestatic com/roscopeco/jasm/model/TestBootstrap.staticForHandleTest()java/lang/String,      // Static MethodHandle
                (java/lang/String)I,                                                                            // Static MethodType
                constdynamic DYNAMIC_CONST_FOR_TEST java/lang/String {                                          // Dynamic const
                    invokestatic java/lang/invoke/ConstantBootstraps.getStaticFinal(
                        java/lang/invoke/MethodHandles$Lookup,
                        java/lang/String,
                        java/lang/Class,
                        java/lang/Class
                    )java/lang/Object
                    [com/roscopeco/jasm/model/TestBootstrap]
                }
            ]
        }
```

See [here](https://github.com/roscopeco/jasm/blob/main/src/test/java/com/roscopeco/jasm/model/TestBootstrap.java) for
the bootstrap method and other support used in that example.
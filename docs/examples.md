## JASM Examples

This is a collection of small examples in JASM. If there's an example you'd like to see
here feel free to [raise an issue](https://github.com/roscopeco/jasm/issues) or
(even better!) [submit a PR](https://github.com/roscopeco/jasm/pulls).

Some of these omit the class boilerplate for clarity - if you want to compile them 
you'll need to put them in a JASM class.

### Readable types

There's a smidge of syntactic sugar around types in case you just can't
get used to the JVM internal names (for primitives), so you can do e.g. :

```java
public myGreatMethod(int, long, java/util/List) java/util/List {
    
    // cool assembly stuff...
        
}
```

### General Syntax

```java
public class com/example/MyClass
extends com/example/Superclass
implements com/example/SomeInterface {
    public <init>()V {
        aload 0
        invokespecial com/example/Superclass.<init>()V
        return
    }

    public dontAddCanary(java/util/List)V {
        goto skipAdd

        aload 1
        ldc "CANARY"
        invokeinterface java/util/List.add(java/lang/Object)Z 
        
    skipAdd:
        return
    }
}
```

### Loading Constants (ldc)

```java
public ldcExample()V {
    ldc true                    // Syntactic sugar; Translates to integer 1
    ldc false                   // Syntactic sugar; Translates to integer 0
    ldc 10                      // int
    ldc 1000L                   // long
    ldc 5.0                     // float
    ldc 3.142d                  // double
    ldc "Test string"           // String
    ldc java/util/List          // Class
    ldc (java/util/String)I     // MethodType
    
    // Method handle
    ldc invokevirtual com/roscopeco/jasm/Tests.example()V
    
    // Dynamic constant
    ldc constdynamic DYNAMIC_CONST java/lang/Object {
        invokeinterface com/example/Bootstrap.constsite(java/lang/String)java/lang/invoke/CallSite
        ["Something else"]
    }
}
```

### Exception Handling

#### With try/catch

```java
public tryCatchExample(java/lang/Exception)java/lang/String {
    ldc "Fail"
    astore 2

    try {
        aload 1
        athrow
    } catch (java/io/IOException) {
        ldc "IOE"
        astore 2
    } catch (java/lang/NullPointerException) {
        ldc "NPE"
        astore 2
    } catch (java/lang/Exception) {
        ldc "EXCEPTION"
        astore 2
    }

    aload 2
    areturn
}
```

#### Manual exception handling

```java
public manualExceptionHandlerTest()java/lang/Exception {
    exception tryBegin, tryEnd, catchBegin, java/lang/Exception  // Can be anywhere in the method...

  tryBegin:
    new java/lang/Exception
    dup
    ldc "Pass"
    invokespecial java/lang/Exception.<init>(java/lang/String)V
    athrow
  tryEnd:

    new java/lang/Exception
    dup
    ldc "Fail"
    invokespecial java/lang/Exception.<init>(java/lang/String)V
    areturn

  catchBegin:
    checkcast java/lang/Exception
    areturn
}
```
### Invokedynamic

```java
public doBasicInvokeDynamicTest()java/lang/String {
    invokedynamic get()java/util/function/Supplier {
        invokestatic java/lang/invoke/LambdaMetafactory.metafactory(
            java/lang/invoke/MethodHandles$Lookup,
            java/lang/String,
            java/lang/invoke/MethodType,
            java/lang/invoke/MethodType,
            java/lang/invoke/MethodHandle,
            java/lang/invoke/MethodType,
        )java/lang/invoke/CallSite
        [
            ()java/lang/Object,
            invokestatic com/roscopeco/jasm/model/TestBootstrap.lambdaGetImpl()java/lang/String,
            ()java/lang/String
        ]
    }
    
    invokeinterface java/util/function/Supplier.get()java/lang/Object
    checkcast java/lang/String
    areturn
}
```

### Dynamic constants (constdynamic)

```java
public testLdcDynamicConst()java/lang/String {
    ldc constdynamic DYNAMIC_CONST_FOR_TEST java/lang/String {
        invokestatic java/lang/invoke/ConstantBootstraps.getStaticFinal(
            java/lang/invoke/MethodHandles$Lookup,
            java/lang/String,
            java/lang/Class,
            java/lang/Class,
        )java/lang/Object
        [com/roscopeco/jasm/model/TestBootstrap]
    }

    areturn
}
```
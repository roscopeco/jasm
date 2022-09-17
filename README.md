## JASM - A JVM Assembler for the modern age

### What?

JASM is an assembler/disassembler for JVM bytecode. It provides a nice syntax
for writing JVM classes in a bytecode-focused assembly language, and can also
disassemble any Java `.class` file to JASM source code.

JASM has a [Gradle plugin](https://github.com/roscopeco/jasm-gradle-plugin) and 
a (WIP) [Plugin for IntelliJ](https://github.com/roscopeco/jasm-intellij-plugin).

See the [Example Gradle project](https://github.com/roscopeco/jasm-example) for
an example of how JASM might fit in to your project.

Let's just get this out of the way, shall we?

```java
public class com/example/HelloWorld {
    public static main([java/lang/String)V {
        getstatic java/lang/System.out java/io/PrintStream
        ldc "Hello, World"
        invokevirtual java/io/PrintStream.println(java/lang/String)V
        return
    }
}
```

See the [Examples](docs/examples.md) for more examples of JASM code.

### How?

#### Requirements

* Java 11 or higher is required to run the tool and/or gradle plugin
  * (However, JASM can assemble classes targeted at any JVM version!)

#### Using the Gradle plugin

If you just want to use some JASM code in your own Gradle project, the easiest way to get started is
via the [Gradle plugin](https://plugins.gradle.org/plugin/com.roscopeco.jasm).

For more information and some documentation about the plugin, see the 
[Github repo](https://github.com/roscopeco/jasm-gradle-plugin)

#### Using the command-line tool

If you [downloaded a binary distribution](https://github.com/roscopeco/jasm/releases) then
you should be all set. Inside the archive you'll find a `bin/jasm' script that will take
care of running the command-line tool for you.

To see usage details:

`bin/jasm --help`

To simply assemble a file `src/com/example/MyClass.jasm` to the `classes` directory:

`bin/jasm -i src -o classes com/example/MyClass.jasm`

Or to disassemble a `.class` file `classes/com/example/MyClass.class` to the `src` directory:

`bin/jasm -d -i classes -o src com/example/MyClass.class`

Notice that you set the source and destination directories, and just pass the relative
path to the files within them - this is how the assembler creates the output files in the
appropriate place (in a `com/example` directory under the destination directory in
the example above).

When disassembling, you can optionally specify the `-l` flag, which will cause JASM to 
output comments in the disassembly with the original line number (if this information is
present in the `.class` file).

#### Building the tool with Gradle

If you grabbed the source from [Github](https://github.com/roscopeco/jasm) you can 
easily build a binary distribution with `./gradlew clean assemble` (pun not intended).

#### Using JASM as a library 

If you want to use this as a library (with Maven or Gradle), you'll want to 
pull in the dependency from Maven central.

E.g. (for Gradle):

```kotlin
dependencies {
  implementation("com.roscopeco.jasm:jasm:0.7.0")
}
```

### Why??

Well, **why not**?

I wrote this for fun, which I had both in writing it and playing with it. 

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

* Teachers - Perhaps you're teaching a compiler course, maybe you could use this
  to introduce students to JVM bytecode, or even as an IL for the compilers.

#### Why not just use Jasmin?

The venerable [Jasmin](https://github.com/davidar/jasmin) project has been around for years,
and has the advantage of being mature, stable, and well supported everywhere (for example,
Github does syntax highlighting for it). So why not just use that?

Of course it's totally personal choice which you use, but there are a few reasons to choose
JASM over Jasmin:

* JASM supports all the modern features of the latest JVMs, such as
  * `invokedynamic` and dynamic constants
  * `record` classes etc
* JASM has some nice "quality of life" features, such as automatically computing stack map frames / maxlocals for you
* JASM is built on modern tooling, whereas Jasmin's code is showing its age a bit
  * This makes it easy, for example, to build [Gradle](https://github.com/roscopeco/jasm-gradle-plugin) and [IntelliJ](https://github.com/roscopeco/jasm-intellij-plugin) integration for JASM
* JASM comes with a full-featured built-in disassembler
* JASM has (IMHO) a cleaner syntax than Jasmin


### Who?

JASM is copyright 2022 Ross Bamford (roscopeco AT gmail DOT com). 

See LICENSE.md for the gory legal stuff (spoiler: MIT).

import io.freefair.gradle.plugins.lombok.tasks.LombokTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    java
    id("io.freefair.lombok") version "6.3.0"
    antlr
    `maven-publish`
    application
}

group = "com.roscopeco.jasm"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile>().all {
    kotlinOptions {
        jvmTarget = "17"
    }

    dependsOn(tasks.getByName<AntlrTask>("generateGrammarSource"))
}

repositories {
    mavenCentral()
}

dependencies {
    antlr("org.antlr:antlr4:4.10.1")

    implementation("org.ow2.asm:asm:9.3")
    implementation("com.beust:jcommander:1.75")

    testImplementation("org.ow2.asm:asm-util:9.3")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.2")
    testImplementation("org.assertj:assertj-core:3.22.0")
    testImplementation("org.mockito:mockito-junit-jupiter:4.5.1")
}

application {
    mainModule.set("com.roscopeco.jasm") // name defined in module-info.java
    mainClass.set("com.roscopeco.jasm.tool.Jasm")
}

distributions {
    main {
        contents {
            from("README.md", "LICENSE.md")
        }
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<AntlrTask> {
    arguments = arguments + listOf("-visitor")
}

tasks.withType<Checkstyle> {
    exclude("**/antlr/*.java")
}

tasks.withType<LombokTask>().all {
    dependsOn(tasks.getByName<AntlrTask>("generateGrammarSource"))
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            groupId = "com.roscopeco.jasm"
            artifactId = "jasm"
            version = "0.1-SNAPSHOT"
            pom {
                name.set("JASM")
                description.set("A JVM Assembler for the modern age")
                url.set("https://github.com/roscopeco/jasm")
                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://github.com/roscopeco/jasm/blob/main/LICENSE.md")
                    }
                }
                developers {
                    developer {
                        id.set("roscopeco")
                        name.set("Ross Bamford")
                        email.set("roscopeco AT gmail DOT com")
                    }
                }
                scm {
                    connection.set("https://github.com/roscopeco/jasm/blob/main/LICENSE.md")
                    developerConnection.set("git@github.com:roscopeco/jasm.git")
                    url.set("https://github.com/roscopeco/jasm")
                }
            }
        }
    }
}

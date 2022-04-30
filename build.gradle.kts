import io.freefair.gradle.plugins.lombok.tasks.LombokTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    java
    id("io.freefair.lombok") version "6.3.0"
    antlr
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
    implementation("org.ow2.asm:asm-tree:9.3")

    testImplementation("org.ow2.asm:asm-util:9.3")

    implementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.2")
    testImplementation("com.tngtech.jgiven:jgiven-junit5:1.2.0")
    testImplementation("org.assertj:assertj-core:3.22.0")
    testImplementation("org.mockito:mockito-junit-jupiter:4.5.1")
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
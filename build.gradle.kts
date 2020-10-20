/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Kotlin application project to get you started.
 */

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin.
    //id("org.jetbrains.kotlin.jvm") version "1.3.72"
    kotlin("jvm") version ("1.4.10")

    id("org.openjfx.javafxplugin") version "0.0.9"

    // Apply the application plugin to add support for building a CLI application.
    application
}

javafx {
    modules("javafx.controls", "javafx.graphics") 
    version = "11.0.2"
}



repositories {
    // Use jcenter for resolving dependencies.
    // You can declare any Maven/Ivy/file repository here.
    jcenter()
    mavenCentral()
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("org.junit.jupiter:junit-jupiter:5.4.2")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

    implementation("no.tornado:tornadofx:1.7.20")

    implementation("io.github.java-native:jssc:2.9.2")

    //implementation("org.scream3r:jssc:2.8.0")
}

val compileKotlin: KotlinCompile by tasks

compileKotlin.kotlinOptions.jvmTarget = "11"

application {
    // Define the main class for the application.
    mainClassName = "kotlinGUI.AppKt"
}

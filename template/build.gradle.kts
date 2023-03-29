plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.7.10"

    id("com.gradle.plugin-publish") version "1.1.0"
}

group = "com.github.rkotkiewicz"
version = "1.0.0"

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use the Kotlin JUnit 5 integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
}

// Add a source set for the functional test suite
val functionalTestSourceSet = sourceSets.create("functionalTest") {
}

configurations["functionalTestImplementation"].extendsFrom(configurations["testImplementation"])

// Add a task to run the functional tests
val functionalTest by tasks.registering(Test::class) {
    testClassesDirs = functionalTestSourceSet.output.classesDirs
    classpath = functionalTestSourceSet.runtimeClasspath
    useJUnitPlatform()
}

gradlePlugin.testSourceSets(functionalTestSourceSet)

tasks.named<Task>("check") {
    // Run the functional tests as part of `check`
    dependsOn(functionalTest)
}

tasks.named<Test>("test") {
    // Use JUnit Jupiter for unit tests.
    useJUnitPlatform()
}


pluginBundle {
    website = "https://github.com/rkotkiewicz/gradle-template"
    vcsUrl = "https://github.com/rkotkiewicz/gradle-template"
    tags = listOf("template", "template-engine")
}


gradlePlugin {
    plugins {
        create("templatePlugin") {
            id = "com.github.rkotkiewicz.template"
            implementationClass = "com.github.rkotkiewicz.TemplatePlugin"
            displayName = "template filler plugin"
            description = "This Gradle plugin creates tasks that fill template files."
        }
    }
}
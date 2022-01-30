plugins {
    //`jvm-test-suite`
    kotlin("jvm") version "1.6.10"
}

group = "org.gradle.testsuite"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    //testImplementation(kotlin("test"))
    //testImplementation(kotlin("test-junit5")) // Add kotlin.test with JUnit 5 bundled
    //testImplementation(kotlin("test-junit")) // Add kotlin.test with JUnit 4 bundled
    testImplementation(gradleTestKit())
}

@Suppress("UnstableApiUsage", "RemoveRedundantQualifierName")
extensions.configure<org.gradle.testing.base.TestingExtension> {
    // Configure the built-in test suite
    suites.withType<org.gradle.api.plugins.jvm.JvmTestSuite>().configureEach {
    //suites.withType<org.gradle.api.plugins.jvm.internal.DefaultJvmTestSuite>().configureEach {
        // Use 'Kotlin Test' test framework
        useKotlinTest("1.6.10")
    }
}

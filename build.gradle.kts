plugins {
    base
}

group = "ru.pihanya.reproduce.gradle"

fun includedBuildTask(buildName: String, taskPath: String): Provider<TaskReference> = provider {
    gradle.includedBuilds
        .single { includedBuild -> includedBuild.name == buildName }
        .task(taskPath)
}

fun allIncludedBuildsTask(taskPath: String): List<Provider<TaskReference>> =
    gradle.includedBuilds.map { includedBuild -> provider { includedBuild.task(taskPath) } }

tasks {
    /**
     * Issue: _[DefaultJvmTestSuite uses JUnit 4 instead of JUnit 5 when applying useKotlinTest](https://github.com/gradle/gradle/issues/19761)_
     */
    val checkDefaultJvmTestSuiteUsesJunit5 by registering {
        group = "verification"
        description = "Runs all checks related to issue of Gradle DefaultJvmTestSuite that uses JUnit 4 instead of JUnit 5 when applying userKotlinTest."

        dependsOn(
            includedBuildTask(
                buildName = "reproduce-default-jvm-test-suite-uses-junit4-instead-of-junit5",
                taskPath = ":build"
            )
        )
    }
    check {
        dependsOn(checkDefaultJvmTestSuiteUsesJunit5)
    }
    clean {
        dependsOn(allIncludedBuildsTask(taskPath = ":clean"))
    }
}

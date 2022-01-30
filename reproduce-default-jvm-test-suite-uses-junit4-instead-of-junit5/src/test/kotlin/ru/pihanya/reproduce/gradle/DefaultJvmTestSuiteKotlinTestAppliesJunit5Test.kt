package ru.pihanya.reproduce.gradle

class DefaultJvmTestSuiteKotlinTestAppliesJunit5Test {

    @kotlin.test.Test
    fun `check JUnit5 is present in the classpath`() = runCatching {
        Class.forName("org.junit.jupiter.api.Test")
    }.fold(
        onSuccess = { },
        onFailure = { kotlin.test.fail(message = "Junit5 was expected to be present in the classpath", cause = it) }
    )
}

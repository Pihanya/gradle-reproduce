package ru.pihanya.reproduce.gradle

class DefaultJvmTestSuiteKotlinTestNotAppliesJunit4Test {

    @kotlin.test.Test
    fun `check JUnit4 is not present in the classpath`() {
        kotlin.test.assertFailsWith<ClassNotFoundException>(message = "Junit4 was not expected to present in the classpath") {
            Class.forName("org.junit.Test")
        }
    }
}

package ru.pihanya.reproduce.gradle.testkit

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.writeText
import kotlin.test.Test
import org.gradle.testkit.runner.GradleRunner

/**
 * Functional test case that finds out whether [org.gradle.api.plugins.jvm.JvmTestSuite]
 * can be adapted to use `org.jetbrains.kotlin:kotlin-test-junit5`.
 * Test case uses [GradleRunner] tu execute test
 *
 * [Testing Build Logic with TestKit](https://docs.gradle.org/current/userguide/test_kit.html)
 * 
 * [API Documentation of GradleRunner](https://docs.gradle.org/current/javadoc/org/gradle/testkit/runner/GradleRunner.html)
 */
class FunctionalDefaultJvmTestSuiteCanApplyJunit5Test {

    private val gradleVersion: String = "7.3.3"

    @Test
    fun `check DefaultJvmTestSuite can apply JUnit5 using dependency`() = runCatching {
        withTempDirectory(prefix = "check-DefaultJvmTestSuite-can-apply-JUnit5-using-dependency") {
            with(TestCaseSources.SettingsScript) { resolve(fileName).writeText(content) }
            with(TestCaseSources.BuildScript) { resolve(fileName).writeText(content) }
            with(TestCaseSources.KotlinTestFile) {
                val sourcePackageDir = resolve(path).createDirectories()
                sourcePackageDir.resolve(fileName).writeText(content)
            }
            GradleRunner.create()
                .withGradleVersion(gradleVersion)
                .withProjectDir(this.toFile())
                .withArguments("test")
                .build()
        }
    }.fold(
        onSuccess = { },
        onFailure = { kotlin.test.fail(message = "Junit5 was expected to be present in the classpath", cause = it) }
    )

    private object TestCaseSources {

        object SettingsScript {

            val fileName: Path = Path("settings.gradle.kts")

            val content: String = """
                rootProject.name = "reproduce-default-jvm-test-suite-with-dependencies-applies-junit5"
            """.trimIndent()
        }

        object BuildScript {

            val fileName: Path = Path("build.gradle.kts")

            val content: String = """
                plugins { kotlin("jvm") version "1.6.10" }
                group = "ru.pihanya.reproduce.gradle"
                repositories { mavenCentral() }
                dependencies { implementation(kotlin("stdlib")) }
                extensions.configure<org.gradle.testing.base.TestingExtension> {
                    suites.withType<org.gradle.api.plugins.jvm.internal.DefaultJvmTestSuite>().configureEach {
                        dependencies {
                            implementation("org.jetbrains.kotlin:kotlin-test-junit5:1.6.10")
                        }
                    }
                }
            """.trimIndent()
        }

        object KotlinTestFile {

            val path: Path = Path("src/test/kotlin/ru/pihanya/reproduce/gradle")

            val fileName: Path = Path("DefaultJvmTestSuiteWithDependenciesAppliesJunit5Test.kt")

            val content: String = """
                package ru.pihanya.reproduce.gradle
                
                class DefaultJvmTestSuiteWithDependenciesAppliesJunit5Test {
                
                    @kotlin.test.Test
                    fun `check JUnit5 is present in the classpath`() = runCatching {
                        Class.forName("org.junit.jupiter.api.Test")
                    }.fold(
                        onSuccess = { },
                        onFailure = { kotlin.test.fail(message = "Junit5 was expected to be present in the classpath", cause = it) }
                    )
                }

            """.trimIndent()
        }
    }
}

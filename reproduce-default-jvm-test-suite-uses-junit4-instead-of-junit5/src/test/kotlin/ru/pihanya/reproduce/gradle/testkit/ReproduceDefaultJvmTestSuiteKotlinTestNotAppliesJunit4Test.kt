package ru.pihanya.reproduce.gradle.testkit

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.writeText
import kotlin.test.Test
import org.gradle.testkit.runner.GradleRunner

class ReproduceDefaultJvmTestSuiteKotlinTestNotAppliesJunit4Test {

    private val gradleVersion: String = "7.3.3"

    @Test
    fun `check DefaultJvmTestSuite userKotlinTest does not apply JUnit4`() = runCatching {
        withTempDirectory(prefix = "check-DefaultJvmTestSuite-userKotlinTest-does not-apply-JUnit4") {
            with(TestCaseSources.SettingsScript) { resolve(fileName).writeText(content) }
            with(TestCaseSources.BuildScript) { resolve(fileName).writeText(content) }
            with(TestCaseSources.KotlinTestFile) {
                val sourcePackageDir = resolve(path).createDirectories()
                sourcePackageDir.resolve(fileName).writeText(content)
            }
            GradleRunner.create()
                .withGradleVersion(gradleVersion)
                .withProjectDir(this.toFile())
                .withArguments("build")
                .build()
        }
    }.fold(
        onSuccess = { },
        onFailure = { kotlin.test.fail(message = "Junit4 was not expected to present in the classpath", cause = it) }
    )

    private object TestCaseSources {

        object SettingsScript {

            val fileName: Path = Path("settings.gradle.kts")

            val content: String = """
                rootProject.name = "reproduce-default-jvm-test-suite-kotlin-test-applies-junit5"
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
                    suites.withType<org.gradle.api.plugins.jvm.internal.DefaultJvmTestSuite>()
                        .configureEach { useKotlinTest("1.6.10") }
                }
            """.trimIndent()
        }

        object KotlinTestFile {

            val path: Path = Path("src/test/kotlin/ru/pihanya/reproduce/gradle")

            val fileName: Path = Path("DefaultJvmTestSuiteKotlinTestNotAppliesJunit4Test.kt")

            val content: String = """
                package ru.pihanya.reproduce.gradle
                
                class DefaultJvmTestSuiteKotlinTestNotAppliesJunit4Test {
                
                    @kotlin.test.Test
                    fun `check JUnit4 is not present in the classpath`() {
                        kotlin.test.assertFailsWith<ClassNotFoundException>(message = "Junit4 was not expected to present in the classpath") {
                            Class.forName("org.junit.Test")
                        }
                    }
                }

            """.trimIndent()
        }
    }
}

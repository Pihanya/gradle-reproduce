import java.util.concurrent.Executors

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "gradle-reproduce"

plugins {
    id("com.gradle.enterprise") version ("3.8.1")
}

val termsOfServiceUrlValue = "https://gradle.com/terms-of-service"
val termsOfServiceAgreeProp = "termsOfServiceAgree"
if (extra.has(termsOfServiceAgreeProp)) {

    val termsOfServiceAgreeValue = extra[termsOfServiceAgreeProp].toString()
    if (termsOfServiceAgreeValue == "yes") {
        logger.warn(
            """
                ==========
                You are going to agree with 'Gradle Terms of Use' ($termsOfServiceUrlValue).
                You have 30 seconds to abort Gradle build before you agree with 'Gradle Terms of Use'.
                This message is shown because you have assigned property '$termsOfServiceAgreeProp' to value 'yes'.
                ==========
            """.trimIndent()
        )
        Executors.newSingleThreadScheduledExecutor()
            .schedule(
                {
                    gradleEnterprise {
                        buildScan {
                            termsOfServiceUrl = termsOfServiceUrlValue
                            termsOfServiceAgree = "yes"
                        }
                    }
                    logger.warn("You have agreed with 'Gradle Terms of Use'")
                },
                40, TimeUnit.SECONDS
            )
            .get()
    }
}

includeBuild("reproduce-default-jvm-test-suite-uses-junit4-instead-of-junit5")

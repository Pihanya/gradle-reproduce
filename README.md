# Gradle Reproduce

Project to reproduce Gradle issues.

#### DefaultJvmTestSuite uses JUnit 4 instead of JUnit 5 when applying useKotlinTest
Link to issue: [click](https://github.com/gradle/gradle/issues/19761)<br>
Link to subproject: [click](reproduce-default-jvm-test-suite-uses-junit4-instead-of-junit5)

Reproduce issue that causes JUnit 4 implementation of `kotlin.test` library
to be added by calling `DefaultJvmJvmTestSuite.userKotlinTest()`.

Expected behaviour is that JUnit 5 implementation of `kotlin.test` library is added in such a case.

Run using Gradle Wrapper:
```shell
./gradlew checkDefaultJvmTestSuiteUsesJunit5
```

Run in Docker using [Build Scan](https://scans.gradle.com/):
```shell
# By executing command below you agree with https://gradle.com/terms-of-service
docker run \
    --rm --user gradle \
    --name reproduce-default-jvm-test-suite-uses-junit4-instead-of-junit5 \
    --volume "$PWD":/home/gradle/project \
    --workdir /home/gradle/project \
    gradle:7.3.3-jdk11 \
    gradle checkDefaultJvmTestSuiteUsesJunit5 --no-daemon --scan -PtermsOfServiceAgree=yes
```

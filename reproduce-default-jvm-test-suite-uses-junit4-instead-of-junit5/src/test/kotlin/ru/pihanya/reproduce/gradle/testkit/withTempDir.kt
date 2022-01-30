package ru.pihanya.reproduce.gradle.testkit

import java.nio.file.Path
import java.nio.file.attribute.FileAttribute
import kotlin.io.path.createTempDirectory

internal inline fun <T> withTempDirectory(
    prefix: String? = null,
    attributes: Array<FileAttribute<*>> = emptyArray(),
    crossinline closure: Path.() -> T
) {
    val tempDir = createTempDirectory(prefix = prefix, attributes = attributes)
    try {
        tempDir.closure()
    } finally {
        tempDir.toFile().deleteRecursively()
    }
}

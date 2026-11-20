/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package gobley.gradle.cargo.tasks

import io.kotest.matchers.shouldBe
import java.nio.file.Files
import kotlin.test.Test

class CargoBuildTaskTest {
    @Test
    fun resolveBuildScriptOutputReadsLegacyOutputFile() {
        val buildScriptDir = Files.createTempDirectory("cargo-legacy").toFile()
        try {
            val outDir = buildScriptDir.resolve("out").apply { mkdirs() }
            buildScriptDir.resolve("output").writeText("cargo:rustc-link-search=native=/tmp/legacy\n")

            CargoBuildTask.resolveBuildScriptOutput(outDir) shouldBe listOf(
                "cargo:rustc-link-search=native=/tmp/legacy"
            )
        } finally {
            buildScriptDir.deleteRecursively()
        }
    }

    @Test
    fun resolveBuildScriptOutputReadsCargo1100RunStdout() {
        val buildScriptDir = Files.createTempDirectory("cargo-1100").toFile()
        try {
            val outDir = buildScriptDir.resolve("out").apply { mkdirs() }
            buildScriptDir.resolve("run").apply { mkdirs() }
                .resolve("stdout")
                .writeText("cargo:rustc-link-search=native=/tmp/cargo1100\n")

            CargoBuildTask.resolveBuildScriptOutput(outDir) shouldBe listOf(
                "cargo:rustc-link-search=native=/tmp/cargo1100"
            )
        } finally {
            buildScriptDir.deleteRecursively()
        }
    }

    @Test
    fun resolveBuildScriptOutputReturnsNullWhenMissing() {
        val buildScriptDir = Files.createTempDirectory("cargo-empty").toFile()
        try {
            val outDir = buildScriptDir.resolve("out").apply { mkdirs() }

            CargoBuildTask.resolveBuildScriptOutput(outDir) shouldBe null
        } finally {
            buildScriptDir.deleteRecursively()
        }
    }
}

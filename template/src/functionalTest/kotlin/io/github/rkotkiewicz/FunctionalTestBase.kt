package io.github.rkotkiewicz

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.io.TempDir
import java.io.File

open class FunctionalTestBase {

    @field:TempDir
    lateinit var projectDir: File

    protected val buildFile by lazy { projectDir.resolve("build.gradle") }
    private val settingsFile by lazy { projectDir.resolve("settings.gradle") }

    private val srcFolder by lazy { projectDir.resolve("src") }
    private val srcSubFolder by lazy { srcFolder.resolve("subFolder") }

    protected val templateFile0 by lazy { srcFolder.resolve("test0.template.txt") }
    protected val templateFile1 by lazy { srcSubFolder.resolve("test1.template.txt") }

    protected val buildDir by lazy { projectDir.resolve("build") }

    @BeforeEach
    fun initProject() {
        // Set up the test build
        settingsFile.writeText("")
        buildFile.writeText(
            """
            plugins {
                id('io.github.rkotkiewicz.template')
            }
            
            """.trimIndent()
        )
        srcSubFolder.mkdirs()
    }
    protected fun gradlew(vararg args: String): BuildResult {
        val runner = GradleRunner.create()
        runner.forwardOutput()
        runner.withPluginClasspath()
        runner.withArguments(args.toList())
        runner.withProjectDir(projectDir)
        return runner.build()
    }
}
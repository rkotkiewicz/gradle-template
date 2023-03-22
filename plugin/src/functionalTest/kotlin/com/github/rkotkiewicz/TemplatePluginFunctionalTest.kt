package com.github.rkotkiewicz

import java.io.File
import kotlin.test.assertTrue
import kotlin.test.Test
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.io.TempDir

class TemplatePluginFunctionalTest {

    @field:TempDir
    lateinit var projectDir: File

    private val buildFile by lazy { projectDir.resolve("build.gradle") }
    private val settingsFile by lazy { projectDir.resolve("settings.gradle") }
    private val templateFile by lazy { projectDir.resolve("src").resolve("test.template.txt") }
    private val templateFile1 by lazy { projectDir.resolve("src").resolve("test1.template.txt") }
    private val filledTemplateFile by lazy { projectDir.resolve("build/template/test/test.template.txt") }
    private val filledTemplateFile1 by lazy { projectDir.resolve("build/template/test/test1.template.txt") }

    @Test fun `generated task 'testFillTemplate' produces output file for input file`() {
        // Set up the test build
        settingsFile.writeText("")
        buildFile.writeText("""
            plugins {
                id('com.github.rkotkiewicz.template')
            }
            
            template {
                create('test') {
                    from.set(file("${'$'}projectDir/src/${templateFile.name}"))
                }
            }
            
            
        """.trimIndent())
        templateFile.parentFile.mkdirs()
        templateFile.writeText("test")

        // Run the build
        val runner = GradleRunner.create()
        runner.forwardOutput()
        runner.withPluginClasspath()
        runner.withArguments(":testFillTemplate")
        runner.withProjectDir(projectDir)
        runner.build()

        // Verify the result
        assertTrue(filledTemplateFile.exists())
    }

    @Test fun `generated task 'testFillTemplate' produces output files for input directory`() {
        // Set up the test build
        settingsFile.writeText("")
        buildFile.writeText("""
            plugins {
                id('com.github.rkotkiewicz.template')
            }
            
            template {
                create('test') {
                    from.set(file("${'$'}projectDir/src"))
                }
            }
            
            
        """.trimIndent())
        templateFile.parentFile.mkdirs()
        templateFile.writeText("test")
        templateFile1.writeText("test1")

        // Run the build
        val runner = GradleRunner.create()
        runner.forwardOutput()
        runner.withPluginClasspath()
        runner.withArguments(":testFillTemplate")
        runner.withProjectDir(projectDir)
        runner.build()

        // Verify the result
        assertTrue(filledTemplateFile.exists())
        assertTrue(filledTemplateFile1.exists())
    }
}

package com.github.rkotkiewicz

import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class TemplatePluginFunctionalTest {

    @field:TempDir
    lateinit var projectDir: File

    private val buildFile by lazy { projectDir.resolve("build.gradle") }
    private val settingsFile by lazy { projectDir.resolve("settings.gradle") }

    private val srcFolder by lazy { projectDir.resolve("src") }
    private val srcSubFolder by lazy { srcFolder.resolve("subFolder") }

    private val templateFile0 by lazy { srcFolder.resolve("test0.template.txt") }
    private val templateFile1 by lazy { srcSubFolder.resolve("test1.template.txt") }

    private val buildDir by lazy { projectDir.resolve("build") }

    @BeforeEach
    fun initProject() {
        // Set up the test build
        settingsFile.writeText("")
        buildFile.writeText(
            """
            plugins {
                id('com.github.rkotkiewicz.template')
            }
            
            """.trimIndent()
        )
        srcSubFolder.mkdirs()
    }


    @Test
    fun `generated task 'testFileFillTemplate' produces output file for input file`() {

        buildFile.appendText(
            """                
            template {
                create('testFile') {
                    from.set(file("${'$'}projectDir/src/${templateFile0.name}"))
                }
            }           
            
        """.trimIndent()
        )
        templateFile0.writeText("whatever")

        // Run the build
        runTask(":testFileFillTemplate")

        // Verify the result
        assertEquals("whatever", buildDir.resolve("template/testFile/test0.template.txt").readText())
    }


    @Test
    fun `generated task 'testDirFillTemplate' produces output files for input directory`() {
        buildFile.appendText(
            """            
            template {
                create('testDir') {
                    from.set(file("${'$'}projectDir/src"))
                }
            }
        """.trimIndent()
        )
        templateFile0.writeText("first file")
        templateFile1.writeText("second file")

        // Run the build
        runTask(":testDirFillTemplate")

        // Verify the result
        val destinationDir = buildDir.resolve("template/testDir")
        assertEquals("first file", destinationDir.resolve("test0.template.txt").readText())
        assertEquals("second file", destinationDir.resolve("subFolder/test1.template.txt").readText())
    }

    private fun runTask(taskName: String) {
        val runner = GradleRunner.create()
        runner.forwardOutput()
        runner.withPluginClasspath()
        runner.withArguments(taskName)
        runner.withProjectDir(projectDir)
        runner.build()

    }
}

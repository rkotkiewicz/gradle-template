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


    @Test
    fun `generated task 'testFillTemplate' fill template into output file`() {

        buildFile.appendText(
            """                
            template {
                create('test') {
                    from.set(file("${'$'}projectDir/src/${templateFile0.name}"))
                    binding(stringValue : "sssstring",
                        intValue : 42 ,
                        stringList : ["no0", "no1", "no10"]
                    )
                }
            }           
            
        """.trimIndent()
        )
        templateFile0.writeText(
            """
            String value: ${'$'}stringValue
            Int value: ${'$'}intValue
            Strings list:<% for (str in stringList) { %>
              String: <%= str %><% } %>
        """.trimIndent()
        )

        // Run the build
        runTask(":testFillTemplate")

        // Verify the result
        assertEquals(
            """
            String value: sssstring
            Int value: 42
            Strings list:
              String: no0
              String: no1
              String: no10
        """.trimIndent(), buildDir.resolve("template/test/test0.template.txt").readText()
        )
    }

    @Test
    fun `generated task 'testTwoFillTemplate' fill templates into output files`() {

        buildFile.appendText(
            """                
            template {
                create('testTwo') {
                    from.set(file("${'$'}projectDir/src/"))
                    binding(question : "What is the reason?",
                        answer : 101010
                    )
                }
            }           
            
        """.trimIndent()
        )
        templateFile0.writeText(
            """
            The question: "${'$'}question"
        """.trimIndent()
        )
        templateFile1.writeText(
            """
            The answer: "${'$'}answer"
        """.trimIndent()
        )

        // Run the build
        runTask(":testFillTemplate")

        // Verify the result
        assertEquals(
            """
            The question: "What is the reason?"
        """.trimIndent(), buildDir.resolve("template/testTwo/test0.template.txt").readText()
        )
        assertEquals(
            """
            The answer: "101010"
        """.trimIndent(), buildDir.resolve("template/testTwo/subFolder/test1.template.txt").readText()
        )
    }

    private fun runTask(taskName: String) {
        val runner = GradleRunner.create()
        runner.forwardOutput()
        runner.withPluginClasspath()
        runner.withArguments(taskName)
        runner.withProjectDir(projectDir)
        runner.withDebug(true)
        runner.build()

    }
}

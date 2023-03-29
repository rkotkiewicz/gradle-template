package io.github.rkotkiewicz

import kotlin.test.Test
import kotlin.test.assertEquals

class TemplatePluginFunctionalTest: FunctionalTestBase()  {

    @Test
    fun `a generated task 'testFileFillTemplate' produces an output file for an input file`() {

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
        gradlew(":fillTestFileTemplate")

        // Verify the result
        assertEquals("whatever", buildDir.resolve("template/testFile/test0.template.txt").readText())
    }



    @Test
    fun `a generated task 'testDirFillTemplate' produces output files for an input directory`() {
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
        gradlew(":fillTestDirTemplate")

        // Verify the result
        val destinationDir = buildDir.resolve("template/testDir")
        assertEquals("first file", destinationDir.resolve("test0.template.txt").readText())
        assertEquals("second file", destinationDir.resolve("subFolder/test1.template.txt").readText())
    }

}

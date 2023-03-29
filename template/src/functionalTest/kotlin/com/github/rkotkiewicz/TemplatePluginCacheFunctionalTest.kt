package com.github.rkotkiewicz

import org.junit.jupiter.api.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class TemplatePluginCacheFunctionalTest: FunctionalTestBase()  {


    @Test
    fun `generated task uses cache for the second run`() {

        buildFile.appendText(
            """                
            template {
                create('testFile') {
                    from.set(file("${'$'}projectDir/src/${templateFile0.name}"))
                }
            }           
            
        """.trimIndent()
        )
        templateFile0.writeText("Ala ma kota")

        // Run the build
        gradlew(":fillTestFileTemplate")
        val res = gradlew(":fillTestFileTemplate")

        // Verify the result
        assertContains(res.output,":fillTestFileTemplate UP-TO-DATE")
    }

    @Test
    fun `a result file is regenerated after an ext property binding is changed`() {

        buildFile.appendText(
            """                
            ext {
                str = findProperty('strVal')
            }
                
            template {
                create('test') {
                    from.set(file("${'$'}projectDir/src/${templateFile0.name}"))
                    binding(stringValue : str)
                }
            }
            
        """.trimIndent()
        )
        templateFile0.writeText("value: \$stringValue")

        // Run the build
        gradlew(":fillTestTemplate", "-PstrVal=val0")
        gradlew(":fillTestTemplate", "-PstrVal=val1")

        // Verify the result
        assertEquals("value: val1",
            buildDir.resolve("template/test/test0.template.txt").readText()
        )
    }

    @Test
    fun `a result file is regenerated after a template change`() {

        buildFile.appendText(
            """                
            ext {
                str = findProperty('strVal')
            }
                
            template {
                create('test') {
                    from.set(file("${'$'}projectDir/src/${templateFile0.name}"))
                }
            }
            
        """.trimIndent()
        )
        templateFile0.writeText("test123")
        gradlew(":fillTestTemplate")



        templateFile0.writeText("test456")
        gradlew(":fillTestTemplate")

        // Verify the result
        assertEquals("test456",
            buildDir.resolve("template/test/test0.template.txt").readText()
        )
    }

    @Test
    fun `a result file is regenerated after a template file in a directory is changed`() {

        buildFile.appendText(
            """                
            ext {
                str = findProperty('strVal')
            }
                
            template {
                create('test') {
                    from.set(file("${'$'}projectDir/src"))
                }
            }
            
        """.trimIndent()
        )
        templateFile0.writeText("test123")
        gradlew(":fillTestTemplate")



        templateFile0.writeText("test456")
        gradlew(":fillTestTemplate")

        // Verify the result
        assertEquals("test456",
            buildDir.resolve("template/test/test0.template.txt").readText()
        )
    }


    @Test
    fun `a result file given by a directory is regenerated after an ext properties binding change`() {

        buildFile.appendText(
            """                
            ext {
                str = findProperty('strVal')
            }
                
            template {
                create('test') {
                    from.set(file("${'$'}projectDir/src/"))
                    binding(stringValue : str)
                }
            }
            
        """.trimIndent()
        )
        templateFile0.writeText("value: \$stringValue")

        // Run the build
        gradlew(":fillTestTemplate", "-PstrVal=val10")
        gradlew(":fillTestTemplate", "-PstrVal=val11")

        // Verify the result
        assertEquals("value: val11",
            buildDir.resolve("template/test/test0.template.txt").readText()
        )
    }

}
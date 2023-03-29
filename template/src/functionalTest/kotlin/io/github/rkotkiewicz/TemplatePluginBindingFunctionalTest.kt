package io.github.rkotkiewicz

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TemplatePluginBindingFunctionalTest: FunctionalTestBase() {


    @Test
    fun `a template given by a file uses binding`() {

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
        gradlew(":fillTestTemplate")

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
    fun `a template supports ext properties binding`() {

        buildFile.appendText(
            """                
            ext {
                str = "it is a string"
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
        gradlew(":fillTestTemplate")

        // Verify the result
        assertEquals("value: it is a string",
            buildDir.resolve("template/test/test0.template.txt").readText()
        )
    }


    @Test
    fun `templates given by a directory use binding`() {

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
        gradlew(":fillTestTemplate")

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
}
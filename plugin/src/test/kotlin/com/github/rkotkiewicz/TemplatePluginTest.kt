package com.github.rkotkiewicz

import org.gradle.testfixtures.ProjectBuilder
import kotlin.test.Test
import kotlin.test.assertTrue

class TemplatePluginTest {
    @Test fun `plugin registers configuration`() {
        // Create a test project and apply the plugin
        val project = ProjectBuilder.builder().build()
        project.plugins.apply("com.github.rkotkiewicz.template")

        // Verify the result
        assertTrue(project.extensions.findByName("template") is TemplatePluginExtension)
    }

    @Test fun `plugin can configure template`() {
        // Create a test project and apply the plugin
        val project = ProjectBuilder.builder().build()
        project.plugins.apply("com.github.rkotkiewicz.template")
        val config = project.extensions.findByName("template") as TemplatePluginExtension

        // config should compile and does not throw an exception
        config.create("foo") {
            it.from.set(project.file("bar"))
            it.into.set(project.file("baz"))
            it.parameters("p1" to "v1", "p2" to "2", "p3" to listOf("v3","v4","v5"))
        }
    }

}

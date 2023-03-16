package com.github.rkotkiewicz

import org.gradle.testfixtures.ProjectBuilder
import kotlin.test.Test
import kotlin.test.assertNotNull

class TemplatePluginTest {
    @Test fun `plugin registers configuration`() {
        // Create a test project and apply the plugin
        val project = ProjectBuilder.builder().build()
        project.plugins.apply("com.github.rkotkiewicz.template")

        // Verify the result
        assertNotNull(project.extensions.findByName("template"))
    }
}

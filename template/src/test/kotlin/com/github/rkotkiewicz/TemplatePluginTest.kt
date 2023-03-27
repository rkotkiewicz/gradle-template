package com.github.rkotkiewicz

import com.github.rkotkiewicz.internal.TemplateIdException
import com.github.rkotkiewicz.internal.TemplateSourceException
import org.gradle.api.provider.Provider
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.io.File
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class TemplatePluginTest {
    private val pluginId = "com.github.rkotkiewicz.template"

    @Test
    fun `plugin registers configuration`() {
        // Create a test project and apply the plugin
        val project = ProjectBuilder.builder().build()
        project.plugins.apply(pluginId)

        // Verify the result
        assertTrue(project.extensions.findByName("template") is TemplatePluginExtension)
    }

    @Test
    fun `plugin can configure template`() {
        // Create a test project and apply the plugin
        val project = ProjectBuilder.builder().build()
        project.plugins.apply(pluginId)
        val config = project.extensions.findByName("template") as TemplatePluginExtension

        // config should compile and does not throw an exception
        config.create("foo") {
            it.from.set(project.file("bar"))
            it.into.set(project.file("baz"))
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["abc", "foo42", "camelCase"])
    fun `create(name) configuration generates '${name}FillTemplate' task`(taskPrefix: String) {
        // Create a test project and apply the plugin
        val project = ProjectBuilder.builder().build()
        project.plugins.apply(pluginId)
        val config = project.extensions.findByName("template") as TemplatePluginExtension

        // config should compile and does not throw an exception
        config.create(taskPrefix) {
            it.from.set(project.file("bar"))
        }

        assertNotNull(project.tasks.findByName("${taskPrefix}FillTemplate"))
    }

    @Test
    fun `create('bar') and create('baz') generate 'barFillTemplate' and 'bazFillTemplate' tasks`() {
        // Create a test project and apply the plugin
        val project = ProjectBuilder.builder().build()
        project.plugins.apply(pluginId)
        val config = project.extensions.findByName("template") as TemplatePluginExtension

        config.create("bar") {
            it.from.set(project.file("f1"))
        }
        config.create("baz") {
            it.from.set(project.file("f2"))
        }

        assertNotNull(project.tasks.findByName("barFillTemplate"))
        assertNotNull(project.tasks.findByName("bazFillTemplate"))
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "\n", "\t", " foo", "bar ", "b az", "1", "23noooo", "snek_ssss", "k-e-b-a-b"])
    fun `invalid taskPrefix should throw exception`(taskPrefix: String) {
        // Create a test project and apply the plugin
        val project = ProjectBuilder.builder().build()
        project.plugins.apply(pluginId)
        val config = project.extensions.findByName("template") as TemplatePluginExtension

        assertThrows<TemplateIdException> {
            config.create(taskPrefix) {
                it.from.set(project.file("bar"))
            }
        }
    }


    @Test
    fun `'from' is mandatory in template configuration`() {
        // Create a test project and apply the plugin
        val project = ProjectBuilder.builder().build()
        project.plugins.apply(pluginId)
        val config = project.extensions.findByName("template") as TemplatePluginExtension


        // config should throw an exception
        assertThrows<TemplateSourceException> {
            config.create("foo") {
                it.into.set(project.file("baz"))
            }
        }
    }

    @Test
    fun `'into' has default value in template configuration`() {
        // Create a test project and apply the plugin
        val project = ProjectBuilder.builder().build()
        project.plugins.apply(pluginId)
        val config = project.extensions.findByName("template") as TemplatePluginExtension

        lateinit var into: Provider<File>
        config.create("bar") {
            it.from.set(project.file("f1.txt"))
            into = it.into
        }

        assertTrue (into.isPresent)
    }


    @Test
    fun `we can bind values in template configuration`() {
        // Create a test project and apply the plugin
        val project = ProjectBuilder.builder().build()
        project.plugins.apply(pluginId)
        val config = project.extensions.findByName("template") as TemplatePluginExtension

        config.create("bar") {
            it.from.set(project.file("f1.txt"))
            it.binding("p1" to "v1", "p2" to listOf("v2", 1))
        }
    }

    @Test
    fun `we can bind values by passing a map in template configuration`() {
        // Create a test project and apply the plugin
        val project = ProjectBuilder.builder().build()
        project.plugins.apply(pluginId)
        val config = project.extensions.findByName("template") as TemplatePluginExtension

        config.create("bar") {
            it.from.set(project.file("f1.txt"))
            it.binding(mapOf("p0" to 11))
        }
    }
}

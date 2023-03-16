package com.github.rkotkiewicz

import org.gradle.api.Plugin
import org.gradle.api.Project


class TemplatePlugin: Plugin<Project> {
    override fun apply(project: Project) {
        val templates = project.container(TemplateConfiguration::class.java)
        project.extensions.create("template", TemplatePluginExtension::class.java, templates)

        templates.all {
            val t = project.tasks.register("${it.name}FillTemplate")
            t.configure {

            }
        }

    }
}

package com.github.rkotkiewicz

import com.github.rkotkiewicz.internal.FillingTemplateTask
import org.gradle.api.Plugin
import org.gradle.api.Project


class TemplatePlugin: Plugin<Project> {
    override fun apply(project: Project) {
        val templates = project.container(TemplateConfiguration::class.java)
        project.extensions.create("template", TemplatePluginExtension::class.java, templates, project.buildDir)

        templates.all {
            project.tasks.register("${it.name}FillTemplate", FillingTemplateTask::class.java, it)
        }

    }
}

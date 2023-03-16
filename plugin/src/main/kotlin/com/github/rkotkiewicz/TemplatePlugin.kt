package com.github.rkotkiewicz

import org.gradle.api.Project
import org.gradle.api.Plugin


class TemplatePlugin: Plugin<Project> {
    override fun apply(project: Project) {
        val config = project.extensions.create("template", TemplatePluginExtension::class.java)
    }
}

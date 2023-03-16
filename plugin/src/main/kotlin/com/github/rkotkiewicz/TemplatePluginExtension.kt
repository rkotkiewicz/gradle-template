package com.github.rkotkiewicz

import org.gradle.api.Action

abstract class TemplatePluginExtension {
    fun create(taskPrefix: String, templateConfig: Action<TemplateConfiguration>) {

    }
}
package com.github.rkotkiewicz

import com.github.rkotkiewicz.internal.TemplateIdException
import com.github.rkotkiewicz.internal.TemplateSourceException
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer

abstract class TemplatePluginExtension(
    private val templateConfigurations: NamedDomainObjectContainer<TemplateConfiguration>) {
    fun create(taskPrefix: String, templateConfig: Action<TemplateConfiguration>) {
        assertName(taskPrefix)
        val tc = templateConfigurations.create(taskPrefix, templateConfig)
        assertFrom(tc)
    }

    private fun assertName(name: String) {
        if(name.isEmpty()) {
            throw TemplateIdException("taskPrefix cannot be empty")
        }
        if(!name.matches("^\\S+$".toRegex())) {
            throw TemplateIdException("taskPrefix cannot have whitespace")
        }
        if(name.contains("[-_]".toRegex())) {
            throw TemplateIdException("taskPrefix cannot have '-' or '_'")
        }
        if(name.first().isDigit()) {
            throw TemplateIdException("taskPrefix cannot starts with a number")
        }
    }
    private fun assertFrom(tc: TemplateConfiguration) {
        if(!tc.from.isPresent) {
            throw TemplateSourceException("missing 'from' for ${tc.name} template configuration")
        }
    }
}
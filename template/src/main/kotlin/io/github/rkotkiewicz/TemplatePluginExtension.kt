package io.github.rkotkiewicz

import io.github.rkotkiewicz.internal.TemplateIdException
import io.github.rkotkiewicz.internal.TemplateSourceException
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import java.io.File

abstract class TemplatePluginExtension(
    private val templateConfigurations: NamedDomainObjectContainer<TemplateConfiguration>,
    private val buildDir: File
) {
    fun create(templateName: String, templateConfig: Action<TemplateConfiguration>) {
        assertName(templateName)
        val tc = templateConfigurations.create(templateName, templateConfig)
        tc.into.convention(getDefaultOutput(templateName))
        assertFrom(tc)
    }

    private fun getDefaultOutput(subfolder: String): File {
        return File("$buildDir/template/$subfolder")
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
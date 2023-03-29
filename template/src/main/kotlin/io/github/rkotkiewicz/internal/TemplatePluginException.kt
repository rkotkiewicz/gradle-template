package io.github.rkotkiewicz.internal

import org.gradle.api.GradleException

abstract class TemplatePluginException(message: String) : GradleException(message) {
}
package com.github.rkotkiewicz

import org.gradle.api.provider.Property
import java.io.File

interface TemplateConfiguration {
    val from: Property<File>
    val into: Property<File>
    fun parameters(vararg parameters: Pair<String, *>)
}
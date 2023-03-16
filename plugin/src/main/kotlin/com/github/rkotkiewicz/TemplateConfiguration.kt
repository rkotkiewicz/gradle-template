package com.github.rkotkiewicz

import org.gradle.api.Named
import org.gradle.api.provider.Property
import java.io.File
abstract class TemplateConfiguration(private val name: String): Named {

    abstract val from: Property<File>
    abstract val into: Property<File>
//    abstract fun parameters(vararg parameters: Pair<String, *>)
    override fun getName(): String {
        return name
    }
}
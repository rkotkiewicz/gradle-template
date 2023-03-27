package com.github.rkotkiewicz

import org.gradle.api.Named
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import java.io.File

abstract class TemplateConfiguration(private val name: String) : Named {

    abstract val from: Property<File>
    abstract val into: Property<File>
    abstract val binding: MapProperty<String, Any>
    override fun getName(): String {
        return name
    }

    fun binding(vararg mapping: Pair<String, Any>) {
        mapping.forEach { binding.put(it.first, it.second) }
    }

    fun binding(mapping: Map<String, Any>) {
        mapping.forEach { binding.put(it.key, it.value) }
    }
}
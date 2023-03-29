package io.github.rkotkiewicz.internal

import io.github.rkotkiewicz.TemplateConfiguration
import groovy.text.SimpleTemplateEngine
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*
import java.io.File
import javax.inject.Inject


abstract class FillingTemplateTask
@Inject constructor(
    private val configuration: TemplateConfiguration
) : DefaultTask() {


    private val templateEngine = SimpleTemplateEngine()

    @get:InputDirectory
    @get:Optional
    protected val inputDir: File? by lazy {
        val maybeDirectory = configuration.from.get()
        if (maybeDirectory.isDirectory) {
            return@lazy maybeDirectory
        }
        return@lazy null
    }

    @get:InputFile
    @get:Optional
    protected val inputFile: File? by lazy {
        val maybeFile = configuration.from.get()
        if (maybeFile.isFile) {
            return@lazy maybeFile
        }
        return@lazy null
    }

    private val binding get() = configuration.binding.get()

    @get:OutputDirectory
    protected val outputDir: File by lazy {
        return@lazy configuration.into.get()
    }

    init {
        group = "template"
        inputs.properties(binding)
    }

    @TaskAction
    fun fillTemplate() {
        logger.debug("input: $inputFile")
        logger.debug("output: $outputDir")

        getTemplates().forEach { fillTemplate(it.first, it.second) }
    }

    private fun fillTemplate(inputTemplate: File, output: File) {
        logger.info("input template: $inputTemplate")
        logger.info("output file: $output")

        output.parentFile.mkdirs()

        val template = templateEngine.createTemplate(inputTemplate).make(binding.toMutableMap())
        output.writeText(template.toString())
    }

    private fun getTemplates() = sequence {
        val iFile = inputFile
        if (iFile != null) {
            yield(iFile to File(outputDir, iFile.name))
        }

        val iDir = inputDir
        if (iDir != null) {
            iDir.walk().filter { it.isFile }.forEach {
                yield(it to File(outputDir, it.relativeTo(iDir).path))
            }
        }

    }
}

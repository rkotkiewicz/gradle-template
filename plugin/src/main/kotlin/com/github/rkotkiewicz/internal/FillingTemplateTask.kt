package com.github.rkotkiewicz.internal

import com.github.rkotkiewicz.TemplateConfiguration
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File
import javax.inject.Inject


abstract class FillingTemplateTask
@Inject constructor(
    private val configuration: TemplateConfiguration)  : DefaultTask() {

    @get:InputDirectory
    @get:Optional
    protected val inputDir: File? by lazy {
        val maybeDirectory = configuration.from.get()
        if(maybeDirectory.isDirectory) {
            return@lazy maybeDirectory
        }
        return@lazy null
    }

    @get:InputFile
    @get:Optional
    protected val inputFile: File? by lazy {
        val maybeFile = configuration.from.get()
        if(maybeFile.isFile) {
            return@lazy maybeFile
        }
        return@lazy null
    }

    @get:OutputDirectory
    protected val outputDir: File by lazy {
        return@lazy configuration.into.get()
    }
    init {
        group = "template"
    }

    @TaskAction
    fun fillTemplate() {
        logger.debug("from: $inputFile")
        logger.debug("into: $outputDir")
        try {
            val iFile = inputFile
            iFile?.copyTo(File(outputDir, iFile.name), true)

            inputDir?.copyRecursively(outputDir, true)

        } catch (ex: Exception) {
            logger.error("cannot copy file ${ex.message}")
            throw ex
        }
    }
}

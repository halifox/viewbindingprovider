package com.google.dagger

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import java.io.File
import kotlin.concurrent.thread
import kotlin.random.Random

class MySymbolProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return MySymbolProcessor(environment)
    }
}

class MySymbolProcessor(private val environment: SymbolProcessorEnvironment) : SymbolProcessor {
    override fun finish() {

    }

    override fun onError() {
    }


    override fun process(resolver: Resolver): List<KSAnnotated> {

        thread(true) {

            val viewBindingBuildDirectory = environment.options["viewBindingBuildDirectory"] ?: throw IllegalStateException()

            val directory = File(viewBindingBuildDirectory)
            val fileList = mutableListOf<File>()
            listFilesRecursively(directory, fileList)
            fileList.forEach {
                environment.logger.warn(it.name)
            }

            val outputStream = environment.codeGenerator.createNewFile(Dependencies.ALL_FILES, "com.google.dagger.di", "ViewBindingModule")
            outputStream.write(
                """
            package com.google.dagger.di

            import android.content.Context
            import android.view.LayoutInflater
            import com.google.dagger.databinding.ActivityMainBinding
            import dagger.Module
            import dagger.Provides
            import dagger.hilt.InstallIn
            import dagger.hilt.android.components.ActivityComponent
            import dagger.hilt.android.qualifiers.ActivityContext


            @Module
            @InstallIn(ActivityComponent::class)
            class ViewBindingModel {

          
                @Provides
                fun provideActivityMainBinding(layoutInflater: LayoutInflater): ActivityMainBinding {
                    return ActivityMainBinding.inflate(layoutInflater)
                }

            }
        """.trimIndent().encodeToByteArray()
            )
            outputStream.flush()
            outputStream.close()

        }


        return emptyList()
    }

    private fun listFilesRecursively(directory: File, fileList: MutableList<File>) {
        val files = directory.listFiles()
        if (files != null) {
            for (file in files) {
                if (file.isFile) {
                    fileList.add(file)
                } else if (file.isDirectory) {
                    listFilesRecursively(file, fileList)
                }
            }
        }
    }
}
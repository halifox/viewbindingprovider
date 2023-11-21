package com.google.dagger

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.writeTo
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
            val fileSpec = generateViewBindingModule()
            fileSpec.writeTo(environment.codeGenerator, Dependencies(false))
        }

        return emptyList()
    }


    fun generateViewBindingModule(): FileSpec {
        val packageName = "com.google.dagger.di"
        val className = "ViewBindingModel"

        // Create file builder
        val fileBuilder = FileSpec.builder(packageName, className)

        // Create module annotation
        val moduleAnnotation = AnnotationSpec.builder(ClassName("dagger", "Module")).build()

        // Create installIn annotation
        val installInAnnotation = AnnotationSpec.builder(ClassName("dagger.hilt", "InstallIn"))
            .addMember("%T::class", ClassName("dagger.hilt.android.components", "ActivityComponent"))
            .build()

        val providesAnnotation = AnnotationSpec.builder(ClassName("dagger", "Provides"))
            .build()


        // Create class builder
        val classBuilder = TypeSpec.classBuilder(className)
            .addAnnotation(moduleAnnotation)
            .addAnnotation(installInAnnotation)
//            .addModifiers(KModifier.PUBLIC)

        // Create provide method
        val provideMethod = FunSpec.builder("provideActivityMainBinding")
            .addAnnotation(providesAnnotation)
//            .addModifiers(KModifier.PUBLIC)
            .addParameter("layoutInflater", ClassName("android.view", "LayoutInflater"))
            .returns(ClassName("com.google.dagger.databinding", "ActivityMainBinding"))
            .addStatement("return %T.inflate(layoutInflater)", ClassName("com.google.dagger.databinding", "ActivityMainBinding"))

        // Add provide method to class builder
        classBuilder.addFunction(provideMethod.build())

        // Add class builder to file builder
        fileBuilder.addType(classBuilder.build())

        // Write the file
        return fileBuilder.build()


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
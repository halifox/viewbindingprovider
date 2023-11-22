package com.google.dagger

import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.writeTo
import java.io.File
import kotlin.concurrent.thread

class ViewBindingSymbolProcessor(private val environment: SymbolProcessorEnvironment) : SymbolProcessor {
    companion object {
        // 常量，表示生成的 ViewBinding 类文件的目录
        private const val VIEW_BINDING_BUILD_DIRECTORY = "viewBindingBuildDirectory"

        // 常量，表示生成的 ViewBinding 类文件的后缀名
        private const val VIEW_BINDING_SUFFIX = "Binding.java"
    }

    private val classModule = ClassName("dagger", "Module")
    private val classInstallIn = ClassName("dagger.hilt", "InstallIn")
    private val classProvides = ClassName("dagger", "Provides")
    private val classActivityComponent = ClassName("dagger.hilt.android.components", "ActivityComponent")
    private val classLayoutInflater = ClassName("android.view", "LayoutInflater")

    // 创建一个 Module 注解对象
    // @Module
    private val moduleAnnotation = AnnotationSpec.builder(classModule).build()

    // 创建一个 InstallIn 注解对象，注解值为 ActivityComponent::class
    // @InstallIn(ActivityComponent::class)
    private val installInAnnotation = AnnotationSpec.builder(classInstallIn)
        .addMember("%T::class", classActivityComponent)
        .build()

    // 创建一个 Provides 注解对象
    // @Provides
    private val providesAnnotation = AnnotationSpec.builder(classProvides)
        .build()

    // 实现 SymbolProcessor 接口中的 process 方法
    override fun process(resolver: Resolver): List<KSAnnotated> {
        // 获取配置中的 viewBindingBuildDirectory 参数，如果不存在则抛出异常
        val viewBindingDirectoryPath = environment.options[VIEW_BINDING_BUILD_DIRECTORY] ?: throw IllegalStateException("The \"${VIEW_BINDING_BUILD_DIRECTORY}\" parameter was not found in KSP.")
        val viewBindingDirectory = File(viewBindingDirectoryPath)
        val viewBindingFileSet = mutableSetOf<File>()
        val viewBindingClassNameSet = mutableSetOf<ClassName>()
        // 递归地将 viewBindingDirectory 目录下的文件添加到 viewBindingFileSet 列表中
        listFilesRecursively(viewBindingDirectory, viewBindingFileSet)
        // 遍历文件列表
        viewBindingFileSet.forEach {
            // 打印日志，显示当前处理的 viewBinding 文件的路径
            environment.logger.info("The path to the generated files for view binding is:${it.absolutePath} ")
            // 判断文件是否以 表示生成的 ViewBinding 类文件的 后缀名结尾
            if (it.name.endsWith(VIEW_BINDING_SUFFIX)) {
                try {
                    // 解析 Java 文件并创建 CompilationUnit 对象
                    val unit = StaticJavaParser.parse(it)
                    // 获取包名
                    val packageName = unit.packageDeclaration.get().nameAsString
                    // 获取类名
                    val simpleNames = unit.findFirst(ClassOrInterfaceDeclaration::class.java).get().nameAsString
                    // 创建 ClassName 对象，并将其添加到 viewBindingClassNameSet 集合中
                    viewBindingClassNameSet.add(ClassName(packageName, simpleNames))
                    // 打印日志，显示解析成功的 viewBinding 类名
                    environment.logger.info("Parsing of the view binding file is complete. The parsing result is:${ClassName(packageName, simpleNames)} ")
                } catch (e: Exception) {
                    // 打印日志，显示解析异常信息
                    environment.logger.warn("An error occurred while parsing the view binding file. The error exception is:${e} ")
                    environment.logger.warn("${e.message} ")
                }
            }
        }
        // 在新线程中生成 ViewBindingModule 类文件，并写入磁盘
        thread {
            val fileSpec = generateViewBindingModule(viewBindingClassNameSet)
            fileSpec.writeTo(environment.codeGenerator, Dependencies.ALL_FILES)
        }

        return emptyList()
    }

    // 实现 SymbolProcessor 接口中的 finish 方法
    override fun finish() {

    }

    // 实现 SymbolProcessor 接口中的 onError 方法
    override fun onError() {
    }

    // 生成 ViewBindingModule 类文件
    private fun generateViewBindingModule(viewBindingClassNameSet: Set<ClassName>): FileSpec {
        // 指定包名和文件名
        val fileBuilder = FileSpec.builder("com.google.dagger.di", "ViewBindingModel")
        // 指定类名和注解
        val classBuilder = TypeSpec.classBuilder("ViewBindingModel")
            .addAnnotation(moduleAnnotation)
            .addAnnotation(installInAnnotation)
        // 遍历 viewBindingClassNameSet 集合
        viewBindingClassNameSet.forEach { className ->
            // 创建 provideViewBinding 方法
            val provideMethod = FunSpec.builder("provide${className.simpleName}")
                .addAnnotation(providesAnnotation)
                .addParameter("layoutInflater", classLayoutInflater)
                .returns(className)
                .addStatement("return %T.inflate(layoutInflater)", className)
            // 将 provide 方法添加到 classBuilder 中
            classBuilder.addFunction(provideMethod.build())
        }
        // 将 classBuilder 构建的类添加到 fileBuilder 中
        fileBuilder.addType(classBuilder.build())
        // 构建 FileSpec 对象并返回
        return fileBuilder.build()
    }

    /**
     * 递归地列出目录下的所有文件，并将其添加到给定的文件列表中。
     *
     * @param directory 目录
     * @param fileSet 文件列表
     */
    private fun listFilesRecursively(directory: File, fileSet: MutableSet<File>) {
        // 获取目录下的所有文件和目录
        val files = directory.listFiles()
        if (files != null) {
            // 遍历文件和目录
            for (file in files) {
                if (file.isFile) {
                    // 如果是文件，则将其添加到 fileList 中
                    fileSet.add(file)
                } else if (file.isDirectory) {
                    // 如果是目录，则递归调用 listFilesRecursively 方法
                    listFilesRecursively(file, fileSet)
                }
            }
        }
    }
}

> 非正式工具 请勿在工作项目中使用

[![](https://jitpack.io/v/HuairenWu/HiltAndroidExt.svg)](https://jitpack.io/#HuairenWu/HiltAndroidExt)

# 自动 Provides Android 中的系统服务

```groovy
dependencies {
    implementation("com.github.HuairenWu.AndroidExt:systemserviceprovider:1.1.5")
}
```

# 自动 Provides 项目内所有 ViewBinding 生成类

```groovy
dependencies {
    ksp("com.github.HuairenWu.AndroidExt:viewbindingprovider:1.1.5")
}

//指定ViewBinding生成类目录 如果没有对其修改则使用以下示例
ksp {
    arg("viewBindingBuildDirectory", "${layout.buildDirectory.dir("generated/data_binding_base_class_source_out").get().asFile}")
}
```


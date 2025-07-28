# 通过 Hilt 自动 Provides 项目内所有 ViewBinding 生成类

```groovy
dependencies {
    ksp("com.github.halifox:viewbindingprovider:tag")
}

//指定ViewBinding生成类目录 如果没有对其修改则使用以下示例
ksp {
    arg("viewBindingBuildDirectory", "${layout.buildDirectory.dir("generated/data_binding_base_class_source_out").get().asFile}")
}
```


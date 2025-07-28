# 通过 Hilt 自动提供项目内所有 ViewBinding 生成类

在 Android 项目中，每个 XML 布局文件对应一个 `ViewBinding` 类。通常我们需要在 `Activity` 或 `Fragment`
中手动初始化这些类，例如：

```kotlin
val binding = ActivityMainBinding.inflate(layoutInflater)
```

当项目中有大量布局时，这种重复代码会增加样板、降低可维护性。

该工具结合 Hilt + KSP，**自动扫描项目中的所有 `ViewBinding` 类，并将其注册为 Hilt 的依赖项**，从而实现：

```kotlin
@Inject
lateinit var binding: ActivityMainBinding
```

即：

* 无需手动调用 `inflate`；
* Hilt 自动注入绑定类；
* 避免重复代码，保持结构一致；
* 提升初始化 View 的可维护性和模块化程度。

本质作用是：
**利用 KSP 编译期生成 Hilt Module，提供所有 `ViewBinding` 的 `@Provides` 方法，实现统一自动注入。**

## 使用方法

步骤1. 在构建文件中添加 JitPack 仓库

在 settings.gradle.kts 的 repositories 末尾添加：

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
````

步骤2. 添加依赖

[![](https://jitpack.io/v/halifox/viewbindingprovider.svg)](https://jitpack.io/#halifox/viewbindingprovider)

```kotlin
dependencies {
    ksp("com.github.halifox:viewbindingprovider:Tag")
}
```

步骤3. 指定 ViewBinding 生成目录

```groovy
// 指定 ViewBinding 生成类目录，如未修改，参考如下示例
ksp {
    arg("viewBindingBuildDirectory", "${layout.buildDirectory.dir("generated/data_binding_base_class_source_out").get().asFile}")
}
```

## 使用实例

```kotlin
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 直接使用 binding 访问布局控件
        binding.textView.text = "Hello Hilt + ViewBinding"
    }
}
```
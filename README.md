# 通过 Hilt 自动提供项目内所有 ViewBinding 生成类

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
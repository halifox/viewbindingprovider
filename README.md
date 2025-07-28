# 通过 Hilt 自动 Provides 项目内所有 ViewBinding 生成类

## How to

Step 1. Add the JitPack repository to your build file

Add it in your settings.gradle.kts at the end of repositories:

```kotlin
	dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url = uri("https://jitpack.io") }
		}
	}
```

Step 2. Add the dependency

[![](https://jitpack.io/v/halifox/viewbindingprovider.svg)](https://jitpack.io/#halifox/viewbindingprovider)

```kotlin
	dependencies {
	        implementation("com.github.halifox:viewbindingprovider:Tag")
	}
```

Step 3. Specify ViewBinding generation directory


```groovy
//Specify ViewBinding Generate Class Directory If it is not modified, use the following example
ksp {
    arg("viewBindingBuildDirectory", "${layout.buildDirectory.dir("generated/data_binding_base_class_source_out").get().asFile}")
}
```


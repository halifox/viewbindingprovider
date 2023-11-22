plugins {
    alias(libs.plugins.application)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kapt)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.github.huairenwu"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.github.huairenwu"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }


    signingConfigs {
        create("android") {
            storeFile = file("../platform.jks")
            storePassword = "android"
            keyAlias = "android"
            keyPassword = "android"
        }
    }
    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("android")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("android")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildTypes {
        viewBinding {
            enable = true
        }
    }
}

ksp {
    arg("viewBindingBuildDirectory", "${layout.buildDirectory.dir("generated/data_binding_base_class_source_out").get().asFile}")
}

dependencies {
//    implementation("com.github.HuairenWu:HiltAndroidExt:1.0.4")
//    ksp("com.github.HuairenWu:ViewBinding:1.0.4")
//    implementation("com.github.HuairenWu.HiltAndroidExt:HiltAndroidExt:1.0.3")
//    ksp("com.github.HuairenWu.HiltAndroidExt:HiltAndroidExt-ViewBinding:1.0.3")
    implementation(project(":systemservice"))
    ksp(project(":viewbinding"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.junit.ext)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
}
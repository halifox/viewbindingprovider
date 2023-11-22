plugins {
    kotlin("jvm")
    id("maven-publish")
}

dependencies {
    //https://github.com/google/ksp
    implementation(libs.symbol.processing.api)
    //https://github.com/javaparser/javaparser
    implementation(libs.javaparser.symbol.solver.core)
    implementation(libs.javaparser.core)
    implementation(libs.javaparser.core.serialization)
    //https://github.com/square/kotlinpoet
    implementation(libs.kotlinpoet.ksp)
}
afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                from(components["java"])
                groupId = "com.github.HuairenWu.HiltAndroidExt"
                artifactId = "viewbinding"
                version = "1.1.1"
            }
        }
    }
}
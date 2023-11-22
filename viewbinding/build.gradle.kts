plugins {
    kotlin("jvm")
    id("maven-publish")
}

dependencies {
    //https://github.com/google/ksp
    implementation("com.google.devtools.ksp:symbol-processing-api:1.9.20-1.0.14")
    //https://github.com/javaparser/javaparser
    implementation("com.github.javaparser:javaparser-symbol-solver-core:3.25.6")
    implementation("com.github.javaparser:javaparser-core:3.25.6")
    implementation("com.github.javaparser:javaparser-core-serialization:3.25.6")
    //https://github.com/square/kotlinpoet
    implementation("com.squareup:kotlinpoet-ksp:1.15.1")
}
afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                from(components["java"])
                groupId = "com.github.huairenwu"
                artifactId = "HiltAndroidExt-ViewBinding"
                version = "1.0.3"
            }
        }
    }
}
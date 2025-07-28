plugins {
    kotlin("jvm") version "2.2.0"
}

dependencies {
    implementation("com.google.devtools.ksp:symbol-processing-api:2.2.0-2.0.2")
    implementation("com.github.javaparser:javaparser-symbol-solver-core:3.27.0")
    implementation("com.github.javaparser:javaparser-core:3.27.0")
    implementation("com.github.javaparser:javaparser-core-serialization:3.27.0")
    implementation("com.squareup:kotlinpoet-ksp:2.2.0")
}
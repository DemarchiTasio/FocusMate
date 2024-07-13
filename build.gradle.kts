// build.gradle.kts (Project Level)
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false
}

buildscript {
    val kotlin_version by extra("1.9.0") // Actualiza esta línea

    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://www.jitpack.io" ) }
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.1.0") // Asegúrate de tener la versión correcta de Gradle
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.48")
    }
}

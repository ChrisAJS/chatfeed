import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.21"
}

repositories {
    google()
    mavenCentral()
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    tasks.withType<KotlinCompile>() {
        kotlinOptions.jvmTarget = "11"
    }

    group = "uk.sawcz"
    version = "1.0"
}

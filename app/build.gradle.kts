import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose") version "1.0.0-alpha3"
}

dependencies {
    implementation(compose.desktop.currentOs)

    implementation(project(":ircclient"))

    testImplementation("junit:junit:4.13.2")
}

compose.desktop {
    application {
        mainClass = "uk.sawcz.chatfeed.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "chatfeed"
            packageVersion = "1.0.0"
        }
    }
}
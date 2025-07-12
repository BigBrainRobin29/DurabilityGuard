pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven { url = uri("https://maven.fabricmc.net/") }
        maven { url = uri("https://maven.modmuss50.me/") }
        maven("https://maven.kikugie.dev/snapshots") { name = "KikuGie Snapshots" }
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.7-beta.7"
}

stonecutter {
    create(rootProject) {
        versions("1.20.1", "1.21.1", "1.21.3", "1.21.4", "1.21.5", "1.21.7")
        vcsVersion = "1.21.7"
    }
}

rootProject.name = "DurabilityGuard"
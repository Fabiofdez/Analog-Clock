pluginManagement {
    repositories {
        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net/")
        }
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.kikugie.dev/snapshots") { name = "KikuGie Snapshots" }
    }

    plugins {
        id("net.fabricmc.fabric-loom-remap") version "1.15-SNAPSHOT"
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.9.2"
}

stonecutter {
    create(rootProject) {
        versions("1.21.5", "1.21.11")
        vcsVersion = "1.21.5"
    }
}

// Should match your modid
rootProject.name = "analog-clock"

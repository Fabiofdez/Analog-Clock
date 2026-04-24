plugins {
    id("net.fabricmc.fabric-loom-remap")
}

version = "${property("mod_version")}"
group = "${property("maven_group")}"

var minecraftVersion = "${property("minecraft_version")}"
var fabricApiVersion = "${property("fabric_api_version")}"
var fabricLoaderVersion = "${property("loader_version")}"

var modId = "${property("mod_id")}"
var displayName = "${property("display_name")}"
var jarName = "${modId}-${project.version}+mc${minecraftVersion}"

repositories {
}

loom {
    splitEnvironmentSourceSets()

    mods {
        register(modId) {
            sourceSet(sourceSets.main.get())
            sourceSet(sourceSets.getByName("client"))
        }
    }
}

fabricApi {
    configureDataGeneration {
        client = true
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${minecraftVersion}")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:${fabricLoaderVersion}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${fabricApiVersion}")
}

tasks.processResources {
    var props = mapOf(
        "id" to modId,
        "version" to project.version,
        "name" to displayName,
        "loader_version" to fabricLoaderVersion,
        "minecraft" to minecraftVersion
    )

    filesMatching("fabric.mod.json") { expand(props) }
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 21
}

java {
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.jar {
    inputs.property("projectName", project.name)

    from("LICENSE") {
        rename { "${it}_${project.name}" }
    }
}

tasks.remapJar {
    archiveFileName = "${jarName}.jar"
}

tasks.remapSourcesJar {
    archiveFileName = "${jarName}-sources.jar"
}

// configure the maven publication
//publishing {
//    publications {
//        register<MavenPublication>("mavenJava") {
//            from(components["java"])
//        }
//    }
//
//    // See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
//    repositories {
//        // Add repositories to publish to here.
//        // Notice: This block does NOT have the same function as the block in the top level.
//        // The repositories here will be used for publishing your artifact, not for
//        // retrieving dependencies.
//    }
//}

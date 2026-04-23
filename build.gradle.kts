plugins {
    id("net.fabricmc.fabric-loom-remap")
}

version = property("mod_version") as String
group = property("maven_group") as String

var modId = property("mod_id") as String
var displayName = property("display_name") as String
var minecraftVersion = property("minecraft_version") as String
var loaderVersion = property("loader_version") as String
var jarName = "${modId}-${project.version}+mc${minecraftVersion}"

repositories {
    // Add repositories to retrieve artifacts from in here.
    // You should only use this when depending on other mods because
    // Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
    // See https://docs.gradle.org/current/userguide/declaring_repositories.html
    // for more information about repositories.
}

loom {
    splitEnvironmentSourceSets()

    mods {
        register("analog-clock") {
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
    modImplementation("net.fabricmc:fabric-loader:${loaderVersion}")

    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_api_version")}")
}

tasks.processResources {
    inputs.property("version", project.version)
    inputs.property("minecraft", minecraftVersion)

    var props = mapOf(
        "id" to modId,
        "version" to project.version,
        "name" to displayName,
        "loader_version" to loaderVersion,
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

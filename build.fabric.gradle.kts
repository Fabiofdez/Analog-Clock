plugins {
	id("mod-platform")
	id("fabric-loom")
}

platform {
	loader = "fabric"
	dependencies {
		required("minecraft") {
			versionRange = prop("deps.minecraft")
		}
		required("fabric-api") {
			slug("fabric-api")
			versionRange = ">=${prop("deps.fabric-api")}"
		}
		required("fabricloader") {
			versionRange = ">=${prop("deps.fabric-loader")}"
		}
//		optional("modmenu") {}
	}
}

loom {
	accessWidenerPath = rootProject.file("src/main/resources/aw/${stonecutter.current.version}.accesswidener")
	runs.named("client") {
		client()
		ideConfigGenerated(true)
		runDir = "run/"
		environment = "client"
		programArgs("--username=Dev")
		configName = "Fabric Client"
	}
	runs.named("server") {
		server()
		ideConfigGenerated(true)
		runDir = "run/"
		environment = "server"
		configName = "Fabric Server"
	}
}

fabricApi {
	configureDataGeneration {
		outputDirectory = file("${rootDir}/versions/datagen/${stonecutter.current.version.split("-")[0]}/src/main/generated")
		client = true
	}
}

repositories {
	mavenCentral()
	strictMaven("https://maven.terraformersmc.com/", "com.terraformersmc") { name = "TerraformersMC" }
	strictMaven("https://api.modrinth.com/maven", "maven.modrinth") { name = "Modrinth" }
}

dependencies {
	minecraft("com.mojang:minecraft:${prop("deps.minecraft")}")
	mappings(
		loom.layered {
			officialMojangMappings()
			if (hasProperty("deps.parchment")) parchment("org.parchmentmc.data:parchment-${prop("deps.parchment")}@zip")
		})
	modImplementation("net.fabricmc:fabric-loader:${prop("deps.fabric-loader")}")
	implementation(libs.moulberry.mixinconstraints)
	include(libs.moulberry.mixinconstraints)
	modImplementation("net.fabricmc.fabric-api:fabric-api:${prop("deps.fabric-api")}")
//	modLocalRuntime("com.terraformersmc:modmenu:${prop("deps.modmenu")}")
}

tasks.register("genDevModJson") {
	group = "fabric"
	description = "Generates fabric.mod.json from template"

	doLast {
		val templateFile = file("${rootDir}/src/main/resources/fabric.mod.template.json")
		val outputFile = file("${rootDir}/src/main/resources/fabric.mod.json")

		if (!templateFile.exists()) {
			println("Template not found, skipping")
			return@doLast
		}

		val content = templateFile.readText()
			.replace($$"${id}", prop("mod.id"))
			.replace($$"${group}", prop("mod.group"))

		if (!outputFile.exists() || outputFile.readText() != content) {
			outputFile.writeText(content)
			println("fabric.mod.json generated")
		} else {
			println("fabric.mod.json already up to date")
		}
	}
}


plugins {
	alias(libs.plugins.stonecutter)
	alias(libs.plugins.dotenv)
	alias(libs.plugins.fabric.loom).apply(false)
	alias(libs.plugins.neoforged.moddev).apply(false)
	alias(libs.plugins.jsonlang.postprocess).apply(false)
	alias(libs.plugins.mod.publish.plugin).apply(false)
	alias(libs.plugins.kotlin.jvm).apply(false)
	alias(libs.plugins.devtools.ksp).apply(false)
	alias(libs.plugins.fletching.table).apply(false)
	alias(libs.plugins.legacyforge.moddev).apply(false)
}

stonecutter active file(".sc_active_version")

for (version in stonecutter.versions.map { it.version }.distinct()) tasks.register("publish$version") {
	group = "publishing"
	dependsOn(stonecutter.tasks.named("publishMods") { metadata.version == version })
}

stonecutter tasks {
	val ordering = versionComparator.thenComparingInt { task ->
		if (task.metadata.project.endsWith("fabric")) 1 else 0
	}

	listOf("publishModrinth", "publishCurseforge").forEach { taskName ->
		gradle.allprojects {
			if (project.tasks.findByName(taskName) != null) {
				order(taskName, ordering)
			}
		}
	}
}

stonecutter parameters {
	var loader = current.project.substringAfterLast("-")

	constants.match(loader, "fabric", "neoforge", "forge")
	filters.include("**/*.fsh", "**/*.vsh")
	swaps["mod_version"] = "\"" + property("mod.version") + "\";"
	swaps["mod_id"] = "\"" + property("mod.id") + "\";"
	swaps["mod_name"] = "\"" + property("mod.name") + "\";"
	swaps["mod_group"] = "\"" + property("mod.group") + "\";"
	swaps["minecraft"] = "\"" + node.metadata.version + "\";"
	constants["release"] = property("mod.id") != "modtemplate"

	replacements {
		string(current.parsed >= "1.21.11") {
			replace("net.minecraft.Util", "net.minecraft.util.Util")
			replace("world.level.GameRules", "world.level.gamerules.GameRules")
			replace("blockrenderlayer.v1.BlockRenderLayerMap", "client.rendering.v1.BlockRenderLayerMap")
			replace("BlockRenderLayerMap.INSTANCE.putBlock", "BlockRenderLayerMap.putBlock")
			replace("renderer.RenderType", "renderer.rendertype.RenderType")
			replace("ARGB.lerp", "ARGB.srgbLerp")
			replace("ResourceLocation", "Identifier")
		}

		string(current.parsed >= "1.21.9") {
			replace("FMLEnvironment.dist", "FMLEnvironment.getDist()")
		}

		string(current.parsed >= "1.21.4") {
			replace("${property("mod.group")}.${property("mod.id")}.util.ARGB", "net.minecraft.util.ARGB")
		}

		string(current.parsed > "1.21.1") {
			replace("RecipeProvider.has", "provider.has")
		}

		string("has_interaction_result", current.parsed eq "1.21.1") {
			replace("InteractionResult", "ItemInteractionResult")
			replace("protected InteractionResult useItemOn", "protected ItemInteractionResult useItemOn")
			replace("InteractionResult.PASS", "ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION")
		}

		string(current.parsed >= "1.21") {
			replace("public VoxelShape getShape", "protected VoxelShape getShape")
			replace("public RenderShape getRenderShape", "protected RenderShape getRenderShape")
			replace("public BlockState updateShape", "protected BlockState updateShape")
			replace("public boolean isCollisionShapeFullBlock", "protected boolean isCollisionShapeFullBlock")
			replace("public boolean canSurvive", "protected boolean canSurvive")
			replace("public List<ItemStack> getDrops", "protected List<ItemStack> getDrops")
			replace("public InteractionResult use", "protected InteractionResult useItemOn")
		}

		string(loader == "neoforge") {
			replace("BlockSupplier", "DeferredBlock<Block>")
		}

		string(loader == "forge") {
			replace("net.neoforged.neoforge.registries.DeferredBlock", "net.minecraftforge.registries.RegistryObject")
			replace("BlockSupplier", "RegistryObject<Block>")
		}
	}
}

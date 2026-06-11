@file:OptIn(dev.kikugie.stonecutter.StonecutterExperimentalAPI::class)

plugins {
	alias(libs.plugins.stonecutter)
	alias(libs.plugins.dotenv)
	alias(libs.plugins.fabric.loom).apply(false)
	alias(libs.plugins.fabric.loom.remap).apply(false)
	alias(libs.plugins.neoforged.moddev).apply(false)
	alias(libs.plugins.jsonlang.postprocess).apply(false)
	alias(libs.plugins.mod.publish.plugin).apply(false)
	alias(libs.plugins.kotlin.jvm).apply(false)
	alias(libs.plugins.devtools.ksp).apply(false)
	alias(libs.plugins.fletching.table).apply(false)
	alias(libs.plugins.legacyforge.moddev).apply(false)
}

stonecutter active file(".sc_active_version")

tasks.register("runActiveClient") {
	group = "stonecutter"
	description = "Run client of the active Stonecutter version"
	dependsOn(stonecutter.current!!.project + ":runClient")
}

tasks.register("runActiveServer") {
	group = "stonecutter"
	description = "Run server of the active Stonecutter version"
	dependsOn(stonecutter.current!!.project + ":runServer")
}

stonecutter parameters {
	var loader = current.project.substringAfterLast("-")

	constants.match(loader, "fabric", "neoforge", "forge")
	swaps["mod_version"] = "\"${properties.get<String>("mod.version")}\";"
	swaps["mod_id"] = "\"${properties.get<String>("mod.id")}\";"
	swaps["mod_name"] = "\"${properties.get<String>("mod.name")}\";"
	swaps["mod_group"] = "\"${properties.get<String>("mod.group")}\";"
	swaps["minecraft"] = "\"${current.version}\";"
	constants["release"] = properties.get<String>("mod.id") != "modtemplate"

	replacements {
		string(current.parsed >= "1.21") {
			replace("public VoxelShape getShape", "protected VoxelShape getShape")
			replace("public RenderShape getRenderShape", "protected RenderShape getRenderShape")
			replace("public BlockState updateShape", "protected BlockState updateShape")
			replace("public boolean isCollisionShapeFullBlock", "protected boolean isCollisionShapeFullBlock")
			replace("public boolean canSurvive", "protected boolean canSurvive")
			replace("public List<ItemStack> getDrops", "protected List<ItemStack> getDrops")
			replace("public InteractionResult use(", "protected ItemInteractionResult useItemOn(")
		}

		string(current.parsed eq "1.21.1", "has_interaction_result") {
			replace("InteractionResult", "ItemInteractionResult")
			replace("InteractionResult.PASS", "ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION")
		}

		string(current.parsed > "1.21.1") {
			replace("RecipeProvider.has", "provider.has")
			replace("protected ItemInteractionResult useItemOn(", "protected InteractionResult useItemOn(")
		}

		string(current.parsed >= "1.21.4") {
			replace("${property("mod.group")}.${property("mod.id")}.util.ARGB", "net.minecraft.util.ARGB")
		}

		string(current.parsed >= "1.21.9") {
			replace("FMLEnvironment.dist", "FMLEnvironment.getDist()")
		}

		string(current.parsed >= "1.21.11") {
			replace("net.minecraft.Util", "net.minecraft.util.Util")
			replace("world.level.GameRules", "world.level.gamerules.GameRules")
			replace("blockrenderlayer.v1.BlockRenderLayerMap", "client.rendering.v1.BlockRenderLayerMap")
			replace("BlockRenderLayerMap.INSTANCE.putBlock", "BlockRenderLayerMap.putBlock")
			replace("renderer.RenderType", "renderer.rendertype.RenderType")
			replace("ARGB.lerp", "ARGB.srgbLerp")
			replace("ResourceLocation", "Identifier")
		}

		string(current.parsed >= "26.1") {
			replace("net.minecraft.client.renderer.state.CameraRenderState", "net.minecraft.client.renderer.state.level.CameraRenderState")
			replace("net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry", "net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry")
			replace("net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents", "net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents")
			replace("ColorProviderRegistry.BLOCK.register", "BlockColorRegistry.register")
			replace("ItemGroupEvents.ModifyEntries", "CreativeModeTabEvents.ModifyOutput")
			replace("ItemGroupEvents.modifyEntriesEvent", "CreativeModeTabEvents.modifyOutputEvent")
			replace("SoundTypeBuilder.EntryBuilder", "SoundTypeBuilder.RegistrationBuilder")
			replace("SoundTypeBuilder.of().category", "SoundTypeBuilder.of().source")
			replace("FabricBlockLootTableProvider", "FabricBlockLootSubProvider")
			replace("FabricDataOutput", "FabricPackOutput")
			replace("entityCutoutNoCull", "entityCutout")
			replace("level.getDayTime()", "level.getDefaultClockTime()")
			replace("PayloadTypeRegistry.playC2S", "PayloadTypeRegistry.serverboundPlay")
		}

		string(loader == "neoforge") {
			replace("BlockSupplier", "DeferredBlock<Block>")
			replace("ItemSupplier", "DeferredItem<Item>")
		}

		string(loader == "forge") {
			replace("BlockSupplier", "RegistryObject<Block>")
			replace("ItemSupplier", "RegistryObject<Item>")
		}
	}
}

for (version in stonecutter.versions.map { it.version }.distinct()) tasks.register("publish$version") {
	group = "publishing"
	dependsOn(stonecutter.tasks.named("publishMods") { metadata.version == version })
}

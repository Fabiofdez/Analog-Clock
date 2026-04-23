plugins {
    id("dev.kikugie.stonecutter")
}
stonecutter active "1.21.5"

stonecutter parameters {
    replacements {
        string(current.parsed >= "1.21.11") {
            replace("ResourceLocation", "Identifier")
            replace("world.level.GameRules", "world.level.gamerules.GameRules")
            replace("renderer.RenderType", "renderer.rendertype.RenderType")
        }
    }
}

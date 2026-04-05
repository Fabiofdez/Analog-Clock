package fabiofdez.analogclock.client.datagen;

import fabiofdez.analogclock.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
  public ModRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
    super(output, registriesFuture);
  }

  @Override
  protected @NotNull RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
    return new RecipeProvider(provider, recipeOutput) {
      @Override
      public void buildRecipes() {
        shaped(RecipeCategory.MISC, ModBlocks.ANALOG_CLOCK)
            .pattern("cic")
            .pattern("iri")
            .pattern("cqc")

            .define('c', Items.COPPER_INGOT)
            .define('i', Items.IRON_INGOT)
            .define('r', Items.REDSTONE)
            .define('q', Items.QUARTZ)

            .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))

            .save(output);
      }
    };
  }

  @Override
  public @NotNull String getName() {
    return "ModRecipeProvider";
  }
}

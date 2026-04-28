package fabiofdez.analogclock.platform.fabric.datagen;

//? fabric {

import fabiofdez.analogclock.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
//? <= 1.21.1
//import java.util.function.Supplier;
//? > 1.21.1
import java.util.function.Function;

public class ModRecipeProvider extends FabricRecipeProvider {

  //? <= 1.21.1
  //Supplier<RecipeBuilder> ANALOG_CLOCK_RECIPE = () -> ShapedRecipeBuilder
  //? > 1.21.1
  Function<RecipeProvider, RecipeBuilder> ANALOG_CLOCK_RECIPE = (provider) -> provider
      .shaped(RecipeCategory.MISC, ModBlocks.ANALOG_CLOCK.get())
      .pattern("cic")
      .pattern("iri")
      .pattern("cqc")

      .define('c', Items.COPPER_INGOT)
      .define('i', Items.IRON_INGOT)
      .define('r', Items.REDSTONE)
      .define('q', Items.QUARTZ)

      .unlockedBy(RecipeProvider.getHasName(Items.IRON_INGOT), provider.has(Items.IRON_INGOT));

  //? <= 1.21.1
  //Supplier<RecipeBuilder> AMETHYST_PENDULUM_RECIPE = () -> ShapedRecipeBuilder
  //? > 1.21.1
  Function<RecipeProvider, RecipeBuilder> AMETHYST_PENDULUM_RECIPE = (provider) -> provider
      .shaped(RecipeCategory.MISC, ModBlocks.AMETHYST_PENDULUM.get())
      .pattern("c")
      .pattern("i")
      .pattern("a")

      .define('c', Items.COPPER_INGOT)
      .define('i', Items.IRON_INGOT)
      .define('a', Items.AMETHYST_SHARD)

      .unlockedBy(RecipeProvider.getHasName(Items.AMETHYST_SHARD), provider.has(Items.AMETHYST_SHARD));

  public ModRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
    super(output, registriesFuture);
  }

  //? <= 1.21.1 {
  /*@Override
  public void buildRecipes(RecipeOutput exporter) {
    ANALOG_CLOCK_RECIPE.get().save(exporter);
    AMETHYST_PENDULUM_RECIPE.get().save(exporter);
  }
  *///? }

  //? > 1.21.1 {
  @Override
  protected @NotNull RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
    return new RecipeProvider(provider, recipeOutput) {
      @Override
      public void buildRecipes() {
        ANALOG_CLOCK_RECIPE.apply(this).save(output);
        AMETHYST_PENDULUM_RECIPE.apply(this).save(output);
      }
    };
  }
  //? }

  @Override
  public @NotNull String getName() {
    return "ModRecipeProvider";
  }
}
//?}

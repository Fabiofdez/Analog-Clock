package fabiofdez.analogclock.platform.fabric.datagen;

//? fabric {

import fabiofdez.analogclock.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
//? < 1.21
//import net.minecraft.data.recipes.FinishedRecipe;
//? <= 1.21.1 {
/*import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.level.ItemLike;
*///? }
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
//? >= 1.21 {
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;

import java.util.concurrent.CompletableFuture;
//? }
//? < 1.21
//import java.util.function.Consumer;
//? <= 1.21.1
//import java.util.function.Supplier;
import java.util.function.Function;

public class ModRecipeProvider extends FabricRecipeProvider {

  RecipeDef ANALOG_CLOCK_RECIPE = buildRecipe((provider) -> provider
      .shaped(RecipeCategory.MISC, ModBlocks.ANALOG_CLOCK.get())
      .unlockedBy(RecipeProvider.getHasName(Items.IRON_INGOT), provider.has(Items.IRON_INGOT))
      .define('c', Items.COPPER_INGOT)
      .define('i', Items.IRON_INGOT)
      .define('r', Items.REDSTONE)
      .define('q', Items.QUARTZ)
      .pattern("cic")
      .pattern("iri")
      .pattern("cqc"));

  RecipeDef AMETHYST_PENDULUM_RECIPE = buildRecipe((provider) -> provider
      .shaped(RecipeCategory.MISC, ModBlocks.AMETHYST_PENDULUM.get())
      .unlockedBy(RecipeProvider.getHasName(Items.AMETHYST_SHARD), provider.has(Items.AMETHYST_SHARD))
      .define('c', Items.COPPER_INGOT)
      .define('i', Items.IRON_INGOT)
      .define('a', Items.AMETHYST_SHARD)
      .pattern("c")
      .pattern("i")
      .pattern("a"));

  public ModRecipeProvider(FabricDataOutput output /*? if >= 1.21 >> ') {'*/, CompletableFuture<HolderLookup.Provider> registriesFuture) {
    super(output /*? if >= 1.21 >> ');'*/, registriesFuture);
  }

  @Override
      //? if < 1.21 {
  /*public void buildRecipes(Consumer<FinishedRecipe> exporter) {
    ANALOG_CLOCK_RECIPE.get().save(exporter);
    AMETHYST_PENDULUM_RECIPE.get().save(exporter);
  }
  *///? } else if <= 1.21.1 {
  /*public void buildRecipes(RecipeOutput exporter) {
    ANALOG_CLOCK_RECIPE.get().save(exporter);
    AMETHYST_PENDULUM_RECIPE.get().save(exporter);
  }
  *///? } else if  > 1.21.1 {
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

  private static RecipeDef buildRecipe(RecipePredicate predicate) {
    //? <= 1.21.1
    //return () -> predicate.apply(new MyRecipeProvider());
    //? > 1.21.1
    return predicate::apply;
  }

  //? <= 1.21.1 {
  /*private static class MyRecipeProvider {
    public ShapedRecipeBuilder shaped(RecipeCategory recipeCategory, ItemLike itemLike) {
      return ShapedRecipeBuilder.shaped(recipeCategory, itemLike);
    }
  }
  *///? }

  //? <= 1.21.1
  //private interface RecipeDef extends Supplier<RecipeBuilder> {
  //? > 1.21.1
  private interface RecipeDef extends Function<RecipeProvider, RecipeBuilder> {
  }

  @FunctionalInterface
  //? <= 1.21.1
  //private interface RecipePredicate extends Function<MyRecipeProvider, RecipeBuilder> {
  //? > 1.21.1
  private interface RecipePredicate extends Function<RecipeProvider, RecipeBuilder> {
  }
}
//?}

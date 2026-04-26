package fabiofdez.analogclock.platform.fabric.datagen;

//? fabric {

import fabiofdez.analogclock.ModBlocks;
import fabiofdez.analogclock.ModSounds;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class ModLangProvider extends FabricLanguageProvider {
  public ModLangProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
    super(dataOutput, registryLookup);
  }

  @Override
  public void generateTranslations(HolderLookup.Provider provider, TranslationBuilder translationBuilder) {
    translationBuilder.add(ModBlocks.ANALOG_CLOCK.get(), "Analog Clock");
    translationBuilder.add(ModBlocks.AMETHYST_PENDULUM.get(), "Amethyst Pendulum");

    translationBuilder.add(ModSounds.CLOCK_WIND.get(), "Clock winding up");
    translationBuilder.add(ModSounds.CLOCK_TICK.get(), "Pendulum ticking");
  }
}
//?}

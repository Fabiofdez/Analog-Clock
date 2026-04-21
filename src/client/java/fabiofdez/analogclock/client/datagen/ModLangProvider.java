package fabiofdez.analogclock.client.datagen;

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
    translationBuilder.add(ModBlocks.ANALOG_CLOCK, "Analog Clock");
    translationBuilder.add(ModBlocks.AMETHYST_PENDULUM, "Amethyst Pendulum");

    translationBuilder.add(ModSounds.CLOCK_WIND, "Clock winding up");
    translationBuilder.add(ModSounds.CLOCK_TICK, "Pendulum ticking");
  }
}

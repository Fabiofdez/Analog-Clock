package fabiofdez.analogclock.platform.fabric.datagen;

//? fabric {

import fabiofdez.analogclock.ModBlocks;
import fabiofdez.analogclock.ModSounds;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ModLangProvider extends FabricLanguageProvider {
  public ModLangProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
    super(dataOutput, registryLookup);
  }

  @Override
  public void generateTranslations(HolderLookup.Provider provider, TranslationBuilder translationBuilder) {
    translationBuilder.add(ModBlocks.ANALOG_CLOCK.get(), "Analog Clock");
    translationBuilder.add(ModBlocks.AMETHYST_PENDULUM.get(), "Amethyst Pendulum");

    translationBuilder.add(subtitleFor(ModSounds.CLOCK_WIND), "Clock winding up");
    translationBuilder.add(subtitleFor(ModSounds.CLOCK_TICK), "Pendulum ticking");
    translationBuilder.add(subtitleFor(ModSounds.CLOCK_CHIME), "Clock chime ringing");
    translationBuilder.add(subtitleFor(ModSounds.CHIME_RESONATE), "Clock chime resonating");
  }

  private String subtitleFor(Supplier<SoundEvent> sound) {
    //? > 1.21.1
    ResourceLocation id = sound.get().location();
    //? <= 1.21.1
    //ResourceLocation id = sound.get().getLocation();

    return Util.makeDescriptionId("subtitles", id);
  }
}
//?}

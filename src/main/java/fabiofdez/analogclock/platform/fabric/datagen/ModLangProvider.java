package fabiofdez.analogclock.platform.fabric.datagen;

//? fabric {

import fabiofdez.analogclock.ModBlocks;
import fabiofdez.analogclock.ModSounds;
import fabiofdez.analogclock.item.AnalogClockItem;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.Util;
//? > 1.21
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

//? > 1.21
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ModLangProvider extends FabricLanguageProvider {
  public ModLangProvider(FabricDataOutput dataOutput/*? if > 1.21 >> ') {' */, CompletableFuture<HolderLookup.Provider> registryLookup) {
    super(dataOutput/*? if > 1.21 >> ');' */, registryLookup);
  }

  @Override
  public void generateTranslations(/*? if > 1.21 >> 'TranslationBuilder' */HolderLookup.Provider provider, TranslationBuilder builder) {
    builder.add(ModBlocks.ANALOG_CLOCK.get().asItem(), "Analog Clock");
    builder.add(ModBlocks.AMETHYST_PENDULUM.get().asItem(), "Amethyst Pendulum");

    builder.add(subtitleFor(ModSounds.CLOCK_WIND), "Clock winding up");
    builder.add(subtitleFor(ModSounds.CLOCK_TICK), "Pendulum ticking");
    builder.add(subtitleFor(ModSounds.CLOCK_CHIME), "Clock chime ringing");
    builder.add(subtitleFor(ModSounds.CHIME_RESONATE), "Clock chime resonating");
    builder.add(subtitleFor(ModSounds.CLOCK_PLATING_ADD), "Plating clock hands");
    builder.add(subtitleFor(ModSounds.CLOCK_PLATING_SCRAPE), "Pickaxe scraping");

    builder.add(AnalogClockItem.Tooltip.DYE.getTranslationKey(), "%1$s Dial");
    builder.add(AnalogClockItem.Tooltip.PLATING.getTranslationKey(), "%1$s-Plated Hands");
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

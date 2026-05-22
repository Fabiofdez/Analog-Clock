package fabiofdez.analogclock.platform.fabric.datagen;

//? fabric && > 1.21.1 {

import fabiofdez.analogclock.AnalogClock;
import fabiofdez.analogclock.ModSounds;
import net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricSoundsProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModSoundsProvider extends FabricSoundsProvider {
  public ModSoundsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
    super(output, registriesFuture);
  }

  @Override
  protected void configure(HolderLookup.Provider provider, SoundExporter exporter) {
    SoundPacker
        .outputTo(exporter)
        .add(ModSounds.CLOCK_TICK, ofFile("pendulum_tick").attenuationDistance(8), 5)
        .add(ModSounds.CLOCK_WIND, ofEvent(SoundEvents.SPYGLASS_USE))
        .add(ModSounds.CLOCK_CHIME, ofEvent(SoundEvents.BELL_BLOCK).volume(0.04F).attenuationDistance(48))
        .add(ModSounds.CHIME_RESONATE, ofEvent(SoundEvents.NOTE_BLOCK_CHIME).pitch(0.5F).attenuationDistance(48))
        .add(ModSounds.CLOCK_PLATING_ADD, ofEvent(SoundEvents.COPPER_STEP).volume(0.6F).pitch(1.5F))
        .add(ModSounds.CLOCK_PLATING_SCRAPE, ofEvent(SoundEvents.AXE_SCRAPE).volume(0.6F).pitch(1.5F));
  }

  private static SoundTypeBuilder blockSound(SoundEvent sound) {
    return SoundTypeBuilder.of().category(SoundSource.BLOCKS).subtitle(sound.location().toLanguageKey("subtitles"));
  }

  private static SoundTypeBuilder.EntryBuilder ofFile(String path) {
    return SoundTypeBuilder.EntryBuilder.ofFile(AnalogClock.id(path));
  }

  private static SoundTypeBuilder.EntryBuilder ofEvent(SoundEvent event) {
    return SoundTypeBuilder.EntryBuilder.ofEvent(event);
  }

  private static SoundTypeBuilder.EntryBuilder ofEvent(Holder<SoundEvent> event) {
    return SoundTypeBuilder.EntryBuilder.ofEvent(event);
  }

  @Override
  public @NotNull String getName() {
    return "ModSoundsProvider";
  }

  private record SoundPacker(SoundExporter exporter) {

    static SoundPacker outputTo(SoundExporter exporter) {
      return new SoundPacker(exporter);
    }

    private SoundPacker buildSoundEvent(Supplier<SoundEvent> sound, Function<SoundTypeBuilder, SoundTypeBuilder> predicate) {
      SoundTypeBuilder builder = blockSound(sound.get());
      this.exporter.add(sound.get(), predicate.apply(builder));

      return this;
    }

    SoundPacker add(Supplier<SoundEvent> target, SoundTypeBuilder.EntryBuilder soundEntry) {
      return buildSoundEvent(target, (builder) -> builder.sound(soundEntry));
    }

    SoundPacker add(Supplier<SoundEvent> target, SoundTypeBuilder.EntryBuilder soundEntry, int numVariants) {
      return buildSoundEvent(target, (builder) -> builder.sound(soundEntry, numVariants));
    }
  }
}
//?}

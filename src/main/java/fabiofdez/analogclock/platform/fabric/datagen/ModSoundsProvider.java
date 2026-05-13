package fabiofdez.analogclock.platform.fabric.datagen;

//? fabric && > 1.21.1 {

import fabiofdez.analogclock.AnalogClock;
import fabiofdez.analogclock.ModSounds;
import net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricSoundsProvider;
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
  protected void configure(HolderLookup.Provider provider, SoundExporter soundExporter) {
    buildSoundEvent(
        soundExporter,
        ModSounds.CLOCK_TICK,
        (builder) -> builder
            .sound(ofFile("pendulum_tick1").attenuationDistance(8))
            .sound(ofFile("pendulum_tick2").attenuationDistance(8))
            .sound(ofFile("pendulum_tick3").attenuationDistance(8))
            .sound(ofFile("pendulum_tick4").attenuationDistance(8))
            .sound(ofFile("pendulum_tick5").attenuationDistance(8))
    );

    buildSoundEvent(soundExporter, ModSounds.CLOCK_WIND, (builder) -> builder.sound(ofEvent(SoundEvents.SPYGLASS_USE)));

    buildSoundEvent(
        soundExporter,
        ModSounds.CLOCK_CHIME,
        (builder) -> builder.sound(ofEvent(SoundEvents.BELL_BLOCK).volume(0.04F).attenuationDistance(48))
    );

    buildSoundEvent(
        soundExporter,
        ModSounds.CHIME_RESONATE,
        (builder) -> builder.sound(ofEvent(SoundEvents.NOTE_BLOCK_CHIME.value()).pitch(0.5F).attenuationDistance(48))
    );
  }

  private static void buildSoundEvent(SoundExporter exporter, Supplier<SoundEvent> sound, Function<SoundTypeBuilder, SoundTypeBuilder> predicate) {
    SoundTypeBuilder builder = blockSound(sound.get());
    exporter.add(sound.get(), predicate.apply(builder));
  }

  private static SoundTypeBuilder blockSound(SoundEvent sound) {
    return SoundTypeBuilder.of().category(SoundSource.BLOCKS).subtitle(sound.location().toLanguageKey("subtitles"));
  }

  private static SoundTypeBuilder.EntryBuilder ofFile(String path) {
    return SoundTypeBuilder.EntryBuilder.ofFile(AnalogClock.id(path));
  }

  private static SoundTypeBuilder.EntryBuilder ofEvent(SoundEvent sound) {
    return SoundTypeBuilder.EntryBuilder.ofEvent(sound);
  }

  @Override
  public @NotNull String getName() {
    return "ModSoundsProvider";
  }
}
//?}

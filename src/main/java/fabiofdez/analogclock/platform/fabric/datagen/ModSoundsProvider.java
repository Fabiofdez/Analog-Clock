package fabiofdez.analogclock.platform.fabric.datagen;

//? fabric && > 1.21.1 {

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

public class ModSoundsProvider extends FabricSoundsProvider {
  public ModSoundsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
    super(output, registriesFuture);
  }

  @Override
  protected void configure(HolderLookup.Provider provider, SoundExporter soundExporter) {
    soundExporter.add(
        ModSounds.CLOCK_WIND.get(),
        blockSound(ModSounds.CLOCK_WIND.get()).sound(ofEvent(SoundEvents.SPYGLASS_USE))
    );

    soundExporter.add(
        ModSounds.CLOCK_TICK.get(),
        blockSound(ModSounds.CLOCK_TICK.get()).sound(ofEvent(SoundEvents.SHIELD_BLOCK).volume(0.04F))
    );
  }

  private static SoundTypeBuilder blockSound(SoundEvent sound) {
    return SoundTypeBuilder.of().category(SoundSource.BLOCKS).subtitle(sound.location().toLanguageKey("subtitles"));
  }

  private static SoundTypeBuilder.EntryBuilder ofEvent(Holder<SoundEvent> soundHolder) {
    return SoundTypeBuilder.EntryBuilder.ofEvent(soundHolder);
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

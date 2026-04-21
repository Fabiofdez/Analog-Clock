package fabiofdez.analogclock;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class ModSounds {
  public static SoundEvent CLOCK_WIND = register("clock_wind");
  public static SoundEvent CLOCK_TICK = register("clock_tick");

  private static SoundEvent register(String id) {
    ResourceLocation soundId = AnalogClock.id(id);
    return Registry.register(BuiltInRegistries.SOUND_EVENT, soundId, SoundEvent.createVariableRangeEvent(soundId));
  }

  public static void initialize() {
  }
}

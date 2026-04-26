package fabiofdez.analogclock;

//? fabric

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
//? neoforge {
/*import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
*///? }
import java.util.function.Supplier;

public class ModSounds {
  //? neoforge {
  /*public static DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(
      BuiltInRegistries.SOUND_EVENT,
      AnalogClock.MOD_ID
  );
  *///? }

  public static Supplier<SoundEvent> CLOCK_WIND = register("clock_wind");
  public static Supplier<SoundEvent> CLOCK_TICK = register("clock_tick");

  //? fabric {
  private static Supplier<SoundEvent> register(String id) {
    ResourceLocation soundId = AnalogClock.id(id);

    SoundEvent toRegister = SoundEvent.createVariableRangeEvent(soundId);
    SoundEvent registeredSound = Registry.register(BuiltInRegistries.SOUND_EVENT, soundId, toRegister);

    return () -> registeredSound;
  }

  public static void initialize() {
  }
  //? }

  //? neoforge {
  /*private static Supplier<SoundEvent> register(String name) {
    ResourceLocation soundId = AnalogClock.id(name);
    return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(soundId));
  }

  public static void register(IEventBus eventBus) {
    SOUND_EVENTS.register(eventBus);
  }
  *///? }
}

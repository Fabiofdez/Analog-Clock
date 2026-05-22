package fabiofdez.analogclock;

//? fabric
import net.minecraft.core.Registry;
//? !forge
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

//? neoforge {
/*import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
*///? }
//? forge {
/*import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
*///? }
import java.util.function.Supplier;

public class ModSounds {
  //? !fabric {
  /*public static DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(
      //? neoforge
      //BuiltInRegistries.SOUND_EVENT,
      //? forge
      //ForgeRegistries.SOUND_EVENTS,
      AnalogClock.MOD_ID
  );
  *///? }

  public static Supplier<SoundEvent> CLOCK_WIND = register("clock_wind");
  public static Supplier<SoundEvent> CLOCK_TICK = register("clock_tick");
  public static Supplier<SoundEvent> CLOCK_CHIME = register("clock_chime");
  public static Supplier<SoundEvent> CHIME_RESONATE = register("chime_resonate");
  public static Supplier<SoundEvent> CLOCK_PLATING_ADD = register("clock_plating_add");
  public static Supplier<SoundEvent> CLOCK_PLATING_SCRAPE = register("clock_plating_scrape");

  //? if fabric {
  private static Supplier<SoundEvent> register(String id) {
    ResourceLocation soundId = AnalogClock.id(id);

    SoundEvent toRegister = SoundEvent.createVariableRangeEvent(soundId);
    SoundEvent registeredSound = Registry.register(BuiltInRegistries.SOUND_EVENT, soundId, toRegister);

    return () -> registeredSound;
  }

  public static void initialize() {
  }
  //? } else {
  /*private static Supplier<SoundEvent> register(String name) {
    Identifier soundId = AnalogClock.id(name);
    return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(soundId));
  }

  public static void register(IEventBus eventBus) {
    SOUND_EVENTS.register(eventBus);
  }
  *///? }
}

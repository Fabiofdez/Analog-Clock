package fabiofdez.analogclock.platform.fabric;

//? fabric {

import fabiofdez.analogclock.ModBlocks;
import fabiofdez.analogclock.ModItems;
import fabiofdez.analogclock.ModSounds;
import fabiofdez.analogclock.AnalogClock;
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import net.fabricmc.api.ModInitializer;

@Entrypoint("main")
public class FabricEntrypoint implements ModInitializer {

  @Override
  public void onInitialize() {
    AnalogClock.onInitialize();

    ModSounds.initialize();
    ModBlocks.initialize();
    ModItems.initialize();

    FabricEventSubscriber.registerEvents();
  }
}
//?}

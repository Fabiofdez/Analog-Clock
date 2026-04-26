package fabiofdez.analogclock.platform.neoforge;

//? neoforge {

/*import fabiofdez.analogclock.AnalogClock;
import fabiofdez.analogclock.ModBlockEntities;
import fabiofdez.analogclock.ModBlocks;
import fabiofdez.analogclock.ModSounds;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(AnalogClock.MOD_ID)
public class NeoforgeEntrypoint {
  public NeoforgeEntrypoint(IEventBus modEventBus, ModContainer ignored) {
    AnalogClock.onInitialize();

    NeoForge.EVENT_BUS.register(this);

    ModSounds.register(modEventBus);
    ModBlocks.register(modEventBus);
    ModBlockEntities.register(modEventBus);

    modEventBus.addListener(ModBlocks::addCreative);

    if (FMLEnvironment.dist == Dist.CLIENT) {
      modEventBus.addListener(NeoforgeClientEventSubscriber::onClientSetup);
    }
  }

  @SubscribeEvent
  public void onServerStarting(ServerStartingEvent event) {
  }
}
*///?}

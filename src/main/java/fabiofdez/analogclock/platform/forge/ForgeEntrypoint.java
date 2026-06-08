package fabiofdez.analogclock.platform.forge;

//? forge {

/*import fabiofdez.analogclock.ModBlockEntities;
import fabiofdez.analogclock.ModBlocks;
import fabiofdez.analogclock.ModItems;
import fabiofdez.analogclock.ModSounds;
import fabiofdez.analogclock.AnalogClock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(AnalogClock.MOD_ID)
public class ForgeEntrypoint {

  public ForgeEntrypoint() {
    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

    AnalogClock.onInitialize();

    MinecraftForge.EVENT_BUS.register(this);
    ModSounds.register(modEventBus);
    ModBlocks.register(modEventBus);
    ModItems.register(modEventBus);
    ModBlockEntities.register(modEventBus);
  }

  @SubscribeEvent
  public void onServerStarting(ServerStartingEvent event) {
  }
}
*///?}

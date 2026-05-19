package fabiofdez.analogclock.platform.forge;

//? forge {

/*import fabiofdez.analogclock.ModBlockEntities;
import fabiofdez.analogclock.ModBlocks;
import fabiofdez.analogclock.ModSounds;
import fabiofdez.analogclock.AnalogClock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(AnalogClock.MOD_ID)
public class ForgeEntrypoint {

	public ForgeEntrypoint() {
    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

    AnalogClock.onInitialize();

    MinecraftForge.EVENT_BUS.register(this);

    ModSounds.register(modEventBus);
    ModBlocks.register(modEventBus);
    ModBlockEntities.register(modEventBus);

    modEventBus.addListener(ModBlocks::addCreative);

    if (FMLEnvironment.dist == Dist.CLIENT) {
      modEventBus.addListener(ForgeClientEventSubscriber::onClientSetup);
    }
  }

  @SubscribeEvent
  public void onServerStarting(ServerStartingEvent event) {
  }
}
*///?}

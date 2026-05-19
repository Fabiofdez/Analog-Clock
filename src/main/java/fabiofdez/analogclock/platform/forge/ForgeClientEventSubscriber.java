package fabiofdez.analogclock.platform.forge;

//? forge {

/*import fabiofdez.analogclock.AnalogClock;
import fabiofdez.analogclock.ModBlockEntities;
import fabiofdez.analogclock.ModBlocks;
import fabiofdez.analogclock.client.renderer.AnalogClockFaceRenderer;
import fabiofdez.analogclock.client.renderer.PendulumRenderer;
import fabiofdez.analogclock.color.ClockFaceStyle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = AnalogClock.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ForgeClientEventSubscriber {

  @SubscribeEvent
  public static void onClientSetup(final FMLClientSetupEvent event) {
    AnalogClock.onInitializeClient();
  }

  @SubscribeEvent
  public static void registerBlockEntityRenderer(EntityRenderersEvent.RegisterRenderers event) {
    event.registerBlockEntityRenderer(ModBlockEntities.CLOCK_FACE_ENTITY.get(), AnalogClockFaceRenderer::new);
    event.registerBlockEntityRenderer(ModBlockEntities.PENDULUM_ENTITY.get(), PendulumRenderer::new);
  }

  @SubscribeEvent
  public static void registerBlockColorHandlers(RegisterColorHandlersEvent.Block event) {
    event.register(ClockFaceStyle::getColor, ModBlocks.ANALOG_CLOCK.get(), ModBlocks.INTERNAL_CLOCK_FACE.get());
  }
}
*///?}

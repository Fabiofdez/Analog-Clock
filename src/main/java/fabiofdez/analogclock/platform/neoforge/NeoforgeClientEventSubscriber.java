package fabiofdez.analogclock.platform.neoforge;

//? neoforge {

/*import fabiofdez.analogclock.AnalogClock;
import fabiofdez.analogclock.ModBlocks;
import fabiofdez.analogclock.ModBlockEntities;
import fabiofdez.analogclock.color.ClockFaceStyle;
import fabiofdez.analogclock.client.renderer.AnalogClockFaceRenderer;
import fabiofdez.analogclock.client.renderer.PendulumRenderer;
import fabiofdez.analogclock.client.renderer.LongPendulumRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

@EventBusSubscriber(modid = AnalogClock.MOD_ID, /^? if < 1.21.11 >> 'value' ^/ bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class NeoforgeClientEventSubscriber {

  @SubscribeEvent
  public static void onClientSetup(final FMLClientSetupEvent event) {
    AnalogClock.onInitializeClient();

    event.enqueueWork(() -> {
      BlockEntityRenderers.register(ModBlockEntities.CLOCK_FACE_ENTITY.get(), AnalogClockFaceRenderer::new);
      BlockEntityRenderers.register(ModBlockEntities.PENDULUM_ENTITY.get(), PendulumRenderer::new);
      BlockEntityRenderers.register(ModBlockEntities.LONG_PENDULUM_ENTITY.get(), LongPendulumRenderer::new);
    });
  }

  @SubscribeEvent
  public static void registerBlockColorHandlers(RegisterColorHandlersEvent.Block event) {
    Block[] tintedBlocks = new Block[]{ModBlocks.ANALOG_CLOCK.get(), ModBlocks.INTERNAL_CLOCK_FACE.get()};
    //? < 26.1
    event.register(ClockFaceStyle::getColor, tintedBlocks);
    //? >= 26.1
    //event.register(ClockFaceStyle.BLOCK_TINTS, tintedBlocks);
  }
}
*///?}

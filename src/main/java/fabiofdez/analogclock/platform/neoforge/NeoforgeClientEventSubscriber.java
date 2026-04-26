package fabiofdez.analogclock.platform.neoforge;

//? neoforge {

/*import fabiofdez.analogclock.AnalogClock;
import fabiofdez.analogclock.ModBlockEntities;
import fabiofdez.analogclock.client.renderer.AnalogClockFaceRenderer;
import fabiofdez.analogclock.client.renderer.PendulumRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

public class NeoforgeClientEventSubscriber {
  public static void onClientSetup(final FMLClientSetupEvent event) {
    AnalogClock.onInitializeClient();

    event.enqueueWork(() -> {
      BlockEntityRenderers.register(ModBlockEntities.CLOCK_FACE_ENTITY.get(), AnalogClockFaceRenderer::new);
      BlockEntityRenderers.register(ModBlockEntities.PENDULUM_ENTITY.get(), PendulumRenderer::new);
    });
  }
}
*///?}

package fabiofdez.analogclock.client;

import fabiofdez.analogclock.ModBlockEntities;
import fabiofdez.analogclock.client.renderer.AnalogClockFaceRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class AnalogClockClient implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    BlockEntityRenderers.register(ModBlockEntities.CLOCK_FACE_ENTITY, AnalogClockFaceRenderer::new);
  }
}
package fabiofdez.analogclock.platform.fabric;

//? fabric {

import fabiofdez.analogclock.ModBlockEntities;
import fabiofdez.analogclock.AnalogClock;
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import fabiofdez.analogclock.ModBlocks;
import fabiofdez.analogclock.client.renderer.AnalogClockFaceRenderer;
import fabiofdez.analogclock.client.renderer.PendulumRenderer;
import fabiofdez.analogclock.color.ClockFaceStyle;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
//? <= 1.21.5
import net.minecraft.client.renderer.RenderType;
//? >= 1.21.11
//import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

@Entrypoint("client")
public class FabricClientEntrypoint implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    AnalogClock.onInitializeClient();

    BlockEntityRenderers.register(ModBlockEntities.CLOCK_FACE_ENTITY.get(), AnalogClockFaceRenderer::new);
    BlockEntityRenderers.register(ModBlockEntities.PENDULUM_ENTITY.get(), PendulumRenderer::new);

    BlockRenderLayerMap.INSTANCE.putBlocks(
        //? <= 1.21.5
        RenderType.cutout(),
        //? >= 1.21.11
        //ChunkSectionLayer.CUTOUT,
        ModBlocks.ANALOG_CLOCK.get(), ModBlocks.INTERNAL_CLOCK_FACE.get()
    );

    ColorProviderRegistry.BLOCK.register(
        ClockFaceStyle::getColor,
        ModBlocks.ANALOG_CLOCK.get(),
        ModBlocks.INTERNAL_CLOCK_FACE.get()
    );
  }
}
//?}

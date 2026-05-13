package fabiofdez.analogclock.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
//? if <= 1.21.5
import net.minecraft.client.renderer.MultiBufferSource;
//? if >= 1.21.11
//import net.minecraft.client.renderer.SubmitNodeCollector;

//? if <= 1.21.5
public record RenderContext(PoseStack matrices, MultiBufferSource vertexConsumers, int light) {
  //? if >= 1.21.11
  //public record RenderContext(PoseStack matrices, SubmitNodeCollector queue, int light) {
}

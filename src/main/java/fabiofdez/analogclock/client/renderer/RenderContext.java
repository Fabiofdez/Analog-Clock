package fabiofdez.analogclock.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
//? if <= 1.21.5
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.Vec3;
//? if >= 1.21.11
//import net.minecraft.client.renderer.SubmitNodeCollector;

//? if <= 1.21.5
public record RenderContext(PoseStack matrices, MultiBufferSource vertexConsumers, int light) {
  //? if >= 1.21.11
  //public record RenderContext(PoseStack matrices, SubmitNodeCollector queue, int light) {

  public RenderContext offset(Vec3 vector) {
    matrices.translate(vector.x, vector.y, vector.z);
    return this;
  }

  public RenderContext offset(double x, double y, double z) {
    matrices.translate(x, y, z);
    return this;
  }

  public RenderContext xOffset(double offset) {
    matrices.translate(offset, 0, 0);
    return this;
  }

  public RenderContext yOffset(double offset) {
    matrices.translate(0, offset, 0);
    return this;
  }

  public RenderContext zOffset(double offset) {
    matrices.translate(0, 0, offset);
    return this;
  }

  public RenderContext rotateDegrees(Axis axis, float degrees) {
    matrices.rotateAround(axis.rotationDegrees(degrees), 0, 0, 0);
    return this;
  }
}

package fabiofdez.analogclock.client.renderer;

import com.mojang.blaze3d.vertex.VertexConsumer;
import fabiofdez.analogclock.AnalogClock;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.joml.Matrix4f;

public abstract class AnimatedEntityRenderer<T extends BlockEntity> implements BlockEntityRenderer<T> {
  public static final int NO_TINT = 0xFFFFFFFF;

  protected static ResourceLocation getTexture(String texture) {
    return AnalogClock.id(String.format("textures/block/%s.png", texture));
  }

  protected static float getModelRotation(Direction facingDirection) {
    return switch (facingDirection) {
      case NORTH -> 180F;
      case SOUTH -> 0F;
      case WEST -> 270F;
      case EAST -> 90F;
      default -> 0f;
    };
  }

  protected static void drawQuad(VertexConsumer buffer, int colorARGB, int frameOffset, int numFrames, Matrix4f matrix, int light, int overlay) {
    float vMin = (float) (frameOffset + 1) / numFrames;
    float vMax = (float) (frameOffset) / numFrames;

    buffer
        .addVertex(matrix, -0.5f, -0.5f, 0f)
        .setColor(colorARGB)
        .setUv(0f, vMin)
        .setOverlay(overlay)
        .setLight(light)
        .setNormal(0, 0, 1);

    buffer
        .addVertex(matrix, 0.5f, -0.5f, 0f)
        .setColor(colorARGB)
        .setUv(1f, vMin)
        .setOverlay(overlay)
        .setLight(light)
        .setNormal(0, 0, 1);

    buffer
        .addVertex(matrix, 0.5f, 0.5f, 0f)
        .setColor(colorARGB)
        .setUv(1f, vMax)
        .setOverlay(overlay)
        .setLight(light)
        .setNormal(0, 0, 1);

    buffer
        .addVertex(matrix, -0.5f, 0.5f, 0f)
        .setColor(colorARGB)
        .setUv(0f, vMax)
        .setOverlay(overlay)
        .setLight(light)
        .setNormal(0, 0, 1);
  }
}

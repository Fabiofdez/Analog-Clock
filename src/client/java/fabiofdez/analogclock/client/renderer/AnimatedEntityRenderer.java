package fabiofdez.analogclock.client.renderer;

import com.mojang.blaze3d.vertex.VertexConsumer;
import fabiofdez.analogclock.AnalogClock;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
//? if >= 1.21.11
//import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.joml.Matrix4f;

//? if <= 1.21.5
public abstract class AnimatedEntityRenderer<T extends BlockEntity> implements BlockEntityRenderer<T> {
  //? if >= 1.21.11
//public abstract class AnimatedEntityRenderer<T extends BlockEntity, S extends BlockEntityRenderState> implements BlockEntityRenderer<T, S> {
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

  protected static void drawQuad(VertexConsumer buffer, int colorARGB, int frameOffset, int numFrames, Matrix4f matrix, int light) {
    float vMin = (float) (frameOffset + 1) / numFrames;
    float vMax = (float) (frameOffset) / numFrames;

    buffer
        .addVertex(matrix, -0.5f, -0.5f, 0f)
        .setColor(colorARGB)
        .setUv(0f, vMin)
        .setOverlay(OverlayTexture.NO_OVERLAY)
        .setLight(light)
        .setNormal(0, 0, 1);

    buffer
        .addVertex(matrix, 0.5f, -0.5f, 0f)
        .setColor(colorARGB)
        .setUv(1f, vMin)
        .setOverlay(OverlayTexture.NO_OVERLAY)
        .setLight(light)
        .setNormal(0, 0, 1);

    buffer
        .addVertex(matrix, 0.5f, 0.5f, 0f)
        .setColor(colorARGB)
        .setUv(1f, vMax)
        .setOverlay(OverlayTexture.NO_OVERLAY)
        .setLight(light)
        .setNormal(0, 0, 1);

    buffer
        .addVertex(matrix, -0.5f, 0.5f, 0f)
        .setColor(colorARGB)
        .setUv(0f, vMax)
        .setOverlay(OverlayTexture.NO_OVERLAY)
        .setLight(light)
        .setNormal(0, 0, 1);
  }
}

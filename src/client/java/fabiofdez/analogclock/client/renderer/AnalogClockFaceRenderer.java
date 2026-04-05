package fabiofdez.analogclock.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import fabiofdez.analogclock.AnalogClock;
import fabiofdez.analogclock.block.AnalogClockBlock;
import fabiofdez.analogclock.block.state.properties.Alignment;
import fabiofdez.analogclock.entity.AnalogClockFace;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class AnalogClockFaceRenderer implements BlockEntityRenderer<AnalogClockFace> {
  public AnalogClockFaceRenderer(BlockEntityRendererProvider.Context ctx) {
  }

  @Override
  public void render(AnalogClockFace entity, float tickProgress, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay, Vec3 cameraPos) {
    int hourFrame = entity.getHourFrame();
    int minuteFrame = entity.getMinuteFrame();

    ResourceLocation HOUR_TEXTURE = AnalogClock.id(String.format("textures/block/hour_hand_%d.png", hourFrame));
    ResourceLocation MINUTE_TEXTURE = AnalogClock.id(String.format("textures/block/minute_hand_%d.png", minuteFrame));

    BlockState state = entity.getBlockState();
    Direction facing = state.getValue(AnalogClockBlock.FACING);
    boolean isFront = state.getValue(AnalogClockBlock.ALIGNMENT) == Alignment.FRONT;

    Vec3 clockFaceCenter = new Vec3(0.5F, 0.5F, 0.5F);
    if (isFront) {
      Vec3 shiftForward = facing.getUnitVec3();
      clockFaceCenter = clockFaceCenter.add(shiftForward.scale(0.5F + 0.02F));
    } else {
      facing = facing.getOpposite();
      Vec3 shiftBack = facing.getUnitVec3();
      clockFaceCenter = clockFaceCenter.add(shiftBack.scale(0.375F - 0.02F));
    }

    float rotation = switch (facing) {
      case NORTH -> isFront ? 180F : 0F;
      case SOUTH -> isFront ? 0F : 180F;
      case WEST -> isFront ? 270F : 90F;
      case EAST -> isFront ? 90F : 270F;
      default -> 0f;
    };

    matrices.pushPose();

    matrices.translate(clockFaceCenter);
    matrices.rotateAround(Axis.YP.rotationDegrees(rotation), 0F, 0F, 0F);

    VertexConsumer hourBuffer = vertexConsumers.getBuffer(RenderType.entityCutoutNoCull(HOUR_TEXTURE));
    VertexConsumer minuteBuffer = vertexConsumers.getBuffer(RenderType.entityCutoutNoCull(MINUTE_TEXTURE));

    Matrix4f matrix = matrices
        .last()
        .pose();

    drawQuad(hourBuffer, matrix, light, overlay);
    drawQuad(minuteBuffer, matrix, light, overlay);

    matrices.popPose();
  }

  private void drawQuad(VertexConsumer buffer, Matrix4f matrix, int light, int overlay) {

    buffer
        .addVertex(matrix, -0.5f, -0.5f, 0f)
        .setColor(255, 255, 255, 255)
        .setUv(0f, 1f)
        .setOverlay(overlay)
        .setLight(light)
        .setNormal(0, 0, 1);

    buffer
        .addVertex(matrix, 0.5f, -0.5f, 0f)
        .setColor(255, 255, 255, 255)
        .setUv(1f, 1f)
        .setOverlay(overlay)
        .setLight(light)
        .setNormal(0, 0, 1);

    buffer
        .addVertex(matrix, 0.5f, 0.5f, 0f)
        .setColor(255, 255, 255, 255)
        .setUv(1f, 0f)
        .setOverlay(overlay)
        .setLight(light)
        .setNormal(0, 0, 1);

    buffer
        .addVertex(matrix, -0.5f, 0.5f, 0f)
        .setColor(255, 255, 255, 255)
        .setUv(0f, 0f)
        .setOverlay(overlay)
        .setLight(light)
        .setNormal(0, 0, 1);
  }
}

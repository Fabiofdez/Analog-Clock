package fabiofdez.analogclock.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import fabiofdez.analogclock.block.AnalogClockBlock;
import fabiofdez.analogclock.block.state.properties.Alignment;
import fabiofdez.analogclock.entity.AnalogClockFace;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class AnalogClockFaceRenderer extends AnimatedEntityRenderer<AnalogClockFace> {
  private static final ResourceLocation HOUR_TEXTURE = getTexture("hour_hand");
  private static final ResourceLocation MINUTE_TEXTURE = getTexture("minute_hand");

  private static final double CLOCK_HAND_OFFSET = 0.1 / 16;

  public AnalogClockFaceRenderer(BlockEntityRendererProvider.Context ignoredCtx) {
  }

  @Override
  public void render(AnalogClockFace clockFace, float tickProgress, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay, Vec3 cameraPos) {
    matrices.pushPose();

    orientClockFace(matrices, clockFace.getBlockState());
    drawClockHand(HOUR_TEXTURE, clockFace.getHourFrame(), matrices, vertexConsumers, light, overlay);
    drawClockHand(MINUTE_TEXTURE, clockFace.getMinuteFrame(), matrices, vertexConsumers, light, overlay);

    matrices.popPose();
  }

  private static void orientClockFace(PoseStack matrices, BlockState state) {
    boolean isFront = state.getValue(AnalogClockBlock.ALIGNMENT) == Alignment.FRONT;
    Direction facingDirection = state.getValue(AnalogClockBlock.FACING);
    Direction shiftDirection = isFront ? facingDirection : facingDirection.getOpposite();
    float rotation = getModelRotation(facingDirection);

    Vec3 clockFaceCenter = new Vec3(0.5, 0.5, 0.5)
        .relative(facingDirection, 0.5)
        .relative(shiftDirection, isFront ? 0 : (double) 14 / 16);

    matrices.translate(clockFaceCenter);
    matrices.rotateAround(Axis.YP.rotationDegrees(rotation), 0, 0, 0);
  }

  private static void drawClockHand(ResourceLocation texture, int frameOffset, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
    matrices.translate(0, 0, CLOCK_HAND_OFFSET);
    PoseStack.Pose lastPose = matrices.last();

    VertexConsumer buf = vertexConsumers.getBuffer(RenderType.entityCutoutNoCull(texture));
    drawQuad(buf, NO_TINT, frameOffset, AnalogClockFace.CLOCK_HAND_FRAMES, lastPose.pose(), light, overlay);
  }
}

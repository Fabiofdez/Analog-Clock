package fabiofdez.analogclock.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import fabiofdez.analogclock.block.AnalogClockBlock;
import fabiofdez.analogclock.block.state.properties.Alignment;
import fabiofdez.analogclock.color.GemstoneColor;
import fabiofdez.analogclock.entity.PendulumEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class PendulumRenderer extends AnimatedEntityRenderer<PendulumEntity> {
  private static final ResourceLocation PENDULUM_TEXTURE = getTexture("pendulum_frames");
  private static final ResourceLocation GEMSTONE_TEXTURE = getTexture("pendulum_stone_frames");

  private static final double UNIT_PIXEL_SCALE = 0.0625;
  private static final double GEMSTONE_OFFSET = 0.1 / 16;

  public PendulumRenderer(BlockEntityRendererProvider.Context ignoredCtx) {
  }

  @Override
  public void render(PendulumEntity pendulum, float tickProgress, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay, Vec3 cameraPos) {
    matrices.pushPose();

    int phaseTint = GemstoneColor.getTint(pendulum);
    orientPendulum(matrices, pendulum.getBlockState());
    drawPiece(PENDULUM_TEXTURE, NO_TINT, pendulum.getSwingFrame(), matrices, vertexConsumers, light, overlay);
    drawPiece(GEMSTONE_TEXTURE, phaseTint, pendulum.getSwingFrame(), matrices, vertexConsumers, light, overlay);

    matrices.popPose();
  }

  private static void orientPendulum(PoseStack matrices, BlockState state) {
    boolean isFront = state.getValue(AnalogClockBlock.ALIGNMENT) == Alignment.FRONT;
    Direction facingDirection = state.getValue(AnalogClockBlock.FACING);
    Direction shiftDirection = isFront ? facingDirection : facingDirection.getOpposite();
    float rotation = getModelRotation(facingDirection);

    Vec3 pendulumCenter = new Vec3(0.5, 0.5, 0.5);
    if (isFront) {
      pendulumCenter = pendulumCenter.relative(facingDirection, 0.5 - UNIT_PIXEL_SCALE);
    } else {
      pendulumCenter = pendulumCenter.relative(shiftDirection, 0.5 - UNIT_PIXEL_SCALE);
    }

    matrices.translate(pendulumCenter);
    matrices.rotateAround(Axis.YP.rotationDegrees(rotation), 0, 0, 0);

    matrices.translate(0, 0, -GEMSTONE_OFFSET);
  }

  private static void drawPiece(ResourceLocation texture, int tint, int frameOffset, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
    matrices.translate(0, 0, GEMSTONE_OFFSET);
    PoseStack.Pose lastPose = matrices.last();

    VertexConsumer buf = vertexConsumers.getBuffer(RenderType.entityCutoutNoCull(texture)); // TODO: cast shadow?
    drawQuad(buf, tint, frameOffset, PendulumEntity.NUM_PENDULUM_FRAMES, lastPose.pose(), light, overlay);
  }
}

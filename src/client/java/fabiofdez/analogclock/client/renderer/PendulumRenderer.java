package fabiofdez.analogclock.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fabiofdez.analogclock.block.AnalogClockBlock;
import fabiofdez.analogclock.block.state.properties.Alignment;
import fabiofdez.analogclock.entity.PendulumEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
//? if <= 1.21.5 {
import fabiofdez.analogclock.color.GemstoneColor;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
//? }
//? if >= 1.21.11 {
/*import net.minecraft.client.renderer.SubmitNodeCollector;
import fabiofdez.analogclock.client.renderer.state.PendulumRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.RenderTypes;
*///? }
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

//? if <= 1.21.5
public class PendulumRenderer extends AnimatedEntityRenderer<PendulumEntity> {
//? if >= 1.21.11
//public class PendulumRenderer extends AnimatedEntityRenderer<PendulumEntity, PendulumRenderState> {
  private static final ResourceLocation PENDULUM_TEXTURE = getTexture("pendulum_frames");
  private static final ResourceLocation GEMSTONE_TEXTURE = getTexture("pendulum_stone_frames");

  private static final double UNIT_PIXEL_SCALE = 0.0625;
  private static final double GEMSTONE_OFFSET = 0.1 / 16;

  public PendulumRenderer(BlockEntityRendererProvider.Context ignoredCtx) {
  }

  //? if >= 1.21.11 {
  /*@Override
  public PendulumRenderState createRenderState() {
    return new PendulumRenderState();
  }

  @Override
  public void extractRenderState(PendulumEntity pendulum, PendulumRenderState renderState, float tickProgress, Vec3 cameraPos, ModelFeatureRenderer CrumblingOverlay crumblingOverlay) {
    super.extractRenderState(pendulum, renderState, tickProgress, cameraPos, crumblingOverlay);
    renderState.extractRenderState(pendulum);
  }

  @Override
  public void submit(PendulumRenderState renderState, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState) {
    matrices.pushPose();

    int swingFrame = renderState.getSwingFrame();
    orientPendulum(matrices, renderState.getBlockState());
    drawPiece(PENDULUM_TEXTURE, NO_TINT, swingFrame, matrices, queue, renderState.lightCoords);
    drawPiece(GEMSTONE_TEXTURE, renderState.getTint(), swingFrame, matrices, queue, renderState.lightCoords);

    matrices.popPose();
  }
  *///? }

  //? if <= 1.21.5 {
  @Override
  public void render(PendulumEntity pendulum, float tickProgress, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay, Vec3 cameraPos) {
    matrices.pushPose();

    int phaseTint = GemstoneColor.getTint(pendulum);
    int swingFrame = pendulum.getSwingFrame();

    orientPendulum(matrices, pendulum.getBlockState());
    drawPiece(PENDULUM_TEXTURE, NO_TINT, swingFrame, matrices, vertexConsumers, light);
    drawPiece(GEMSTONE_TEXTURE, phaseTint, swingFrame, matrices, vertexConsumers, light);

    matrices.popPose();
  }
  //? }


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

  //? if <= 1.21.5
  private static void drawPiece(ResourceLocation texture, int tint, int frameOffset, PoseStack matrices, MultiBufferSource vertexConsumers, int light) {
  //? if >= 1.21.11
  //private static void drawPiece(ResourceLocation texture, int tint, int frameOffset, PoseStack matrices, SubmitNodeCollector queue, int light) {
    matrices.translate(0, 0, GEMSTONE_OFFSET);
    //? if <= 1.21.5
    RenderType renderType = RenderType.entityCutoutNoCull(texture);
    //? if >= 1.21.11
    //RenderType renderType = RenderTypes.entityCutoutNoCull(texture);

    //? if <= 1.21.5 {
    PoseStack.Pose lastPose = matrices.last();
    VertexConsumer buf = vertexConsumers.getBuffer(renderType);
    drawQuad(buf, tint, frameOffset, PendulumEntity.NUM_PENDULUM_FRAMES, lastPose.pose(), light);
    //? }

    //? if >= 1.21.11 {
    /*queue.submitCustomGeometry(
        matrices,
        renderType,
        (lastPose, buf) -> drawQuad(buf, tint, frameOffset, PendulumEntity.NUM_PENDULUM_FRAMES, lastPose.pose(), light)
    );
    *///? }
  }
}

package fabiofdez.analogclock.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fabiofdez.analogclock.block.AnalogClockBlock;
import fabiofdez.analogclock.block.state.properties.Alignment;
import fabiofdez.analogclock.entity.AnalogClockFace;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
//? if <= 1.21.5 {
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
//? }
//? if >= 1.21.11 {
/*import net.minecraft.client.renderer.SubmitNodeCollector;
import fabiofdez.analogclock.client.renderer.state.ClockFaceRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
*///? }
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

//? if <= 1.21.5
public class AnalogClockFaceRenderer extends AnimatedEntityRenderer<AnalogClockFace> {
//? if >= 1.21.11
//public class AnalogClockFaceRenderer extends AnimatedEntityRenderer<AnalogClockFace, ClockFaceRenderState> {
  private static final ResourceLocation HOUR_TEXTURE = getTexture("hour_hand");
  private static final ResourceLocation MINUTE_TEXTURE = getTexture("minute_hand");

  private static final double CLOCK_HAND_OFFSET = 0.1 / 16;

  public AnalogClockFaceRenderer(BlockEntityRendererProvider.Context ignoredCtx) {
  }

  //? if >= 1.21.11 {
  /*@Override
  public ClockFaceRenderState createRenderState() {
    return new ClockFaceRenderState();
  }

  @Override
  public void extractRenderState(AnalogClockFace clockFace, ClockFaceRenderState renderState, float tickProgress, Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
    super.extractRenderState(clockFace, renderState, tickProgress, cameraPos, crumblingOverlay);
    renderState.extractStateFrom(clockFace);
  }

  @Override
  public void submit(ClockFaceRenderState renderState, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState) {
    matrices.pushPose();

    orientClockFace(matrices, renderState.getBlockState());
    drawClockHand(MINUTE_TEXTURE, renderState.getMinuteFrame(), matrices, queue, renderState.lightCoords);
    drawClockHand(HOUR_TEXTURE, renderState.getHourFrame(), matrices, queue, renderState.lightCoords);

    matrices.popPose();
  }
  *///? }

  //? if <= 1.21.5 {
  @Override
  public void render(AnalogClockFace clockFace, float tickProgress, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay /*? if > 1.21.1 >> ') {' */ , Vec3 cameraPos) {
    matrices.pushPose();

    orientClockFace(matrices, clockFace.getBlockState());
    drawClockHand(MINUTE_TEXTURE, clockFace.getMinuteFrame(), matrices, vertexConsumers, light);
    drawClockHand(HOUR_TEXTURE, clockFace.getHourFrame(), matrices, vertexConsumers, light);

    matrices.popPose();
  }
  //? }

  private static void orientClockFace(PoseStack matrices, BlockState state) {
    boolean isFront = state.getValue(AnalogClockBlock.ALIGNMENT) == Alignment.FRONT;
    Direction facingDirection = state.getValue(AnalogClockBlock.FACING);
    Direction shiftDirection = isFront ? facingDirection : facingDirection.getOpposite();
    float rotation = getModelRotation(facingDirection);

    Vec3 clockFaceCenter = new Vec3(0.5, 0.5, 0.5)
        .relative(facingDirection, 0.5)
        .relative(shiftDirection, isFront ? 0 : (double) 14 / 16);

    //? if > 1.21.1
    matrices.translate(clockFaceCenter);
    //? if <= 1.21.1
    //matrices.translate(clockFaceCenter.x, clockFaceCenter.y, clockFaceCenter.z);
    matrices.rotateAround(Axis.YP.rotationDegrees(rotation), 0, 0, 0);
  }

  //? if <= 1.21.5
  private static void drawClockHand(ResourceLocation texture, int frameOffset, PoseStack matrices, MultiBufferSource vertexConsumers, int light) {
  //? if >= 1.21.11
  //private static void drawClockHand(Identifier texture, int frameOffset, PoseStack matrices, SubmitNodeCollector queue, int light) {
    matrices.translate(0, 0, CLOCK_HAND_OFFSET);
    //? if <= 1.21.5
    RenderType renderType = RenderType.entityCutoutNoCull(texture);
    //? if >= 1.21.11
    //RenderType renderType = RenderTypes.entityCutoutNoCull(texture);

    //? if <= 1.21.5 {
    PoseStack.Pose lastPose = matrices.last();
    VertexConsumer buf = vertexConsumers.getBuffer(renderType);
    drawQuad(buf, NO_TINT, frameOffset, AnalogClockFace.CLOCK_HAND_FRAMES, lastPose.pose(), light);
    //? }

    //? if >= 1.21.11 {
    /*queue.submitCustomGeometry(
        matrices,
        renderType,
        (lastPose, buf) -> drawQuad(
            buf,
            NO_TINT,
            frameOffset,
            AnalogClockFace.CLOCK_HAND_FRAMES,
            lastPose.pose(),
            light
        )
    );
    *///? }
  }
}

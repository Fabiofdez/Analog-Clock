package fabiofdez.analogclock.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import fabiofdez.analogclock.AnalogClock;
import fabiofdez.analogclock.block.DirectionalAlignedBlock;
import fabiofdez.analogclock.block.state.properties.Alignment;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

//? if <= 1.21.5 {
import net.minecraft.client.renderer.MultiBufferSource;
//? } else if >= 1.21.11 {
/*import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import org.jspecify.annotations.NonNull;
*///? }

import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class AnimatedEntityRenderer<T extends BlockEntity/*? if >= 1.21.11 >> '>' *//*, S extends BlockEntityRenderState*/> implements BlockEntityRenderer<T/*? if >= 1.21.11 >> '> {' *//*, S*/> {

  //? <= 1.21.5 {
  protected static final RenderTypePredicate CUTOUT = RenderType::entityCutoutNoCull;
  protected static final RenderTypePredicate EMISSIVE = RenderType::entityTranslucentEmissive;
  //? }
  //? >= 1.21.11 {
  /*protected static final RenderTypePredicate CUTOUT = RenderTypes::entityCutoutNoCull;
  protected static final RenderTypePredicate EMISSIVE = RenderTypes::entityTranslucentEmissive;
  *///? }

  protected static final int NO_TINT = ARGB.opaque(0xFFFFFF);

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

  protected static void orientWithAlignment(PoseStack matrices, BlockState state, Function<Boolean, Double> offsetCenter) {
    boolean isFront = state.getValue(DirectionalAlignedBlock.ALIGNMENT) == Alignment.FRONT;
    Direction facingDirection = state.getValue(DirectionalAlignedBlock.FACING);
    Direction shiftDirection = isFront ? facingDirection : facingDirection.getOpposite();

    Vec3 center = new Vec3(0.5, 0.5, 0.5).relative(shiftDirection, offsetCenter.apply(isFront));
    float rotation = getModelRotation(facingDirection);

    //? > 1.21.1
    matrices.translate(center);
    //? <= 1.21.1
    //matrices.translate(center.x, center.y, center.z);

    matrices.rotateAround(Axis.YP.rotationDegrees(rotation), 0, 0, 0);
  }

  protected static void drawStaticAsset(RenderType renderType, int tint, RenderContext ctx) {
    renderTexture(renderType, ctx, RenderHandler.STATIC(tint, ctx));
  }

  protected static void drawAnimatedAsset(RenderType renderType, int tint, RenderFrame frame, RenderContext ctx) {
    renderTexture(renderType, ctx, RenderHandler.ANIMATED(tint, frame, ctx));
  }

  protected static void drawBlockModel(BlockState state, BlockRenderDispatcher blockRenderer, RenderContext ctx) {
    //? if <= 1.21.5 {
    blockRenderer.renderSingleBlock(
        state,
        ctx.matrices(),
        ctx.vertexConsumers(),
        ctx.light(),
        OverlayTexture.NO_OVERLAY
    );
    //? } else {
    /*ctx.queue().submitBlock(ctx.matrices(), state, ctx.light(), OverlayTexture.NO_OVERLAY, 0);
     *///? }
  }

  protected static void renderTexture(RenderType renderType, RenderContext ctx, RenderHandler handler) {

    //? <= 1.21.5
    handler.accept(ctx.matrices().last(), ctx.vertexConsumers().getBuffer(renderType));
    //? >= 1.21.11
    //ctx.queue().submitCustomGeometry(ctx.matrices(), renderType, handler::accept);
  }

  protected static void drawQuad(VertexConsumer buffer, int tint, PoseStack.Pose pose, int light) {
    drawQuad(buffer, tint, 0, 1, pose, light);
  }

  protected static void drawQuad(VertexConsumer buffer, int tint, int frameOffset, int totalFrames, PoseStack.Pose pose, int light) {
    float vMin = (float) (frameOffset + 1) / totalFrames;
    float vMax = (float) (frameOffset) / totalFrames;

    addVertex(buffer, pose, -0.5f, -0.5f, tint, 0f, vMin, light);
    addVertex(buffer, pose, 0.5f, -0.5f, tint, 1f, vMin, light);
    addVertex(buffer, pose, 0.5f, 0.5f, tint, 1f, vMax, light);
    addVertex(buffer, pose, -0.5f, 0.5f, tint, 0f, vMax, light);
  }

  private static void addVertex(VertexConsumer buffer, PoseStack.Pose pose, float x, float y, int tint, float u, float v, int light) {
    //? if < 1.21 {
    /*buffer
        .vertex(pose.pose(), x, y, 0f)
        .color(tint)
        .uv(u, v)
        .overlayCoords(OverlayTexture.NO_OVERLAY)
        .uv2(light)
        .normal(pose.normal(), 0, 0, 1)
        .endVertex();
    *///? } else {
    buffer
        .addVertex(pose.pose(), x, y, 0f)
        .setColor(tint)
        .setUv(u, v)
        .setOverlay(OverlayTexture.NO_OVERLAY)
        .setLight(light)
        .setNormal(0, 0, 1);
    //? }
  }

  //? >= 1.21.11 {
  /*@Override
  public void submit(S renderState, @NonNull PoseStack matrices, @NonNull SubmitNodeCollector queue, @NonNull CameraRenderState cameraState) {
    submitRender(renderState, new RenderContext(matrices, queue, renderState.lightCoords));
  }
  *///? }

  //? <= 1.21.5 {
  protected abstract Object parseRenderState(T blockEntity);

  @Override
  final public void render(T blockEntity, float tickProgress, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay /*? if > 1.21.1 >> ') {' */, Vec3 cameraPos) {
    submitRender(parseRenderState(blockEntity), new RenderContext(matrices, vertexConsumers, light));
  }
  //? }

  protected abstract void submitRender(Object renderState, RenderContext ctx);

  @FunctionalInterface
  protected interface RenderHandler extends BiConsumer<PoseStack.Pose, VertexConsumer> {
    static RenderHandler STATIC(int tint, RenderContext ctx) {
      return (lastPose, buf) -> drawQuad(buf, tint, lastPose, ctx.light());
    }

    static RenderHandler ANIMATED(int tint, RenderFrame frame, RenderContext ctx) {
      return (lastPose, buf) -> drawQuad(buf, tint, frame.offset(), frame.total(), lastPose, ctx.light());
    }
  }

  @FunctionalInterface
  protected interface RenderTypePredicate extends Function<ResourceLocation, RenderType> {
  }
}

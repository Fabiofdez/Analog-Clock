package fabiofdez.analogclock.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import fabiofdez.analogclock.block.AnalogClockBlock;
import fabiofdez.analogclock.client.renderer.state.ClockFaceRenderState;
import fabiofdez.analogclock.color.ClockFaceStyle;
import fabiofdez.analogclock.entity.AnalogClockFace;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
//? if >= 1.21.11 {
/*import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
*///? }
import net.minecraft.resources.ResourceLocation;

//? if <= 1.21.5
public class AnalogClockFaceRenderer extends AnimatedEntityRenderer<AnalogClockFace> {
  //? if >= 1.21.11
//public class AnalogClockFaceRenderer extends AnimatedEntityRenderer<AnalogClockFace, ClockFaceRenderState> {
  private static final ResourceLocation DIAL_MARKS_TEXTURE = getTexture("dial_marks");
  private static final ResourceLocation DIAL_MARKS_PLATED_TEXTURE = getTexture("dial_marks_plated");

  private static final ResourceLocation HOUR_TEXTURE = getTexture("hour_hand");
  private static final ResourceLocation HOUR_PLATED_TEXTURE = getTexture("hour_hand_plated");

  private static final ResourceLocation MINUTE_TEXTURE = getTexture("minute_hand");
  private static final ResourceLocation MINUTE_PLATED_TEXTURE = getTexture("minute_hand_plated");

  private static final double CLOCK_MODEL_THICKNESS = 2.0 / 16;
  private static final double CLOCK_HAND_OFFSET = 0.01 / 16;

  public AnalogClockFaceRenderer(BlockEntityRendererProvider.Context ignoredCtx) {
  }

  //? if >= 1.21.11 {
  /*@Override
  public ClockFaceRenderState createRenderState() {
    return new ClockFaceRenderState();
  }

  @Override
  public void extractRenderState(AnalogClockFace clockFace, ClockFaceRenderState renderState, float tickProgress, @NonNull Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
    super.extractRenderState(clockFace, renderState, tickProgress, cameraPos, crumblingOverlay);
    renderState.extractStateFrom(clockFace);
  }
  *///? }

  //? if <= 1.21.5 {
  @Override
  protected Object parseRenderState(AnalogClockFace clockFace) {
    ClockFaceRenderState renderState = new ClockFaceRenderState();
    renderState.extractStateFrom(clockFace);
    return renderState;
  }
  //? }

  @Override
  protected void submitRender(Object renderState, RenderContext ctx) {
    if (!(renderState instanceof ClockFaceRenderState clockFace)) return;

    int numFrames = AnalogClockFace.CLOCK_HAND_FRAMES;
    PoseStack matrices = ctx.matrices();
    matrices.pushPose();

    ClockFaceStyle.Plating plating = clockFace.getBlockState().getValue(AnalogClockBlock.HANDS_PLATING);
    boolean hasPlating = plating != ClockFaceStyle.HANDS_NO_PLATING;
    int tint = plating.getColor();

    ResourceLocation dialMarksTexture = hasPlating ? DIAL_MARKS_PLATED_TEXTURE : DIAL_MARKS_TEXTURE;
    ResourceLocation minuteHandTexture = hasPlating ? MINUTE_PLATED_TEXTURE : MINUTE_TEXTURE;
    ResourceLocation hourHandTexture = hasPlating ? HOUR_PLATED_TEXTURE : HOUR_TEXTURE;

    orientWithAlignment(matrices, clockFace.getBlockState(), (isFront) -> 0.5 + (isFront ? 0 : -CLOCK_MODEL_THICKNESS));
    drawStaticAsset(dialMarksTexture, tint, ctx);

    matrices.translate(0, 0, CLOCK_HAND_OFFSET);
    drawAnimatedAsset(minuteHandTexture, tint, clockFace.getMinuteFrame(), numFrames, ctx);

    matrices.translate(0, 0, CLOCK_HAND_OFFSET);
    drawAnimatedAsset(hourHandTexture, tint, clockFace.getHourFrame(), numFrames, ctx);

    matrices.popPose();
  }
}

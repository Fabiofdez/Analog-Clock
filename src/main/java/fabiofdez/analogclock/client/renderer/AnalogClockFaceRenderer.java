package fabiofdez.analogclock.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fabiofdez.analogclock.ModBlocks;
import fabiofdez.analogclock.block.AnalogClockBlock;
import fabiofdez.analogclock.block.PlacedClockKey;
import fabiofdez.analogclock.client.renderer.state.ClockFaceRenderState;
import fabiofdez.analogclock.color.ClockFaceStyle;
import fabiofdez.analogclock.block.entity.AnalogClockFace;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
//? >= 1.21.11 {
/*import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
*///? }
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class AnalogClockFaceRenderer extends AnimatedEntityRenderer<AnalogClockFace/*? if >= 1.21.11 >> '>' *//*, ClockFaceRenderState*/> {
  private static final ResourceLocation KEY_HANDLE = getTexture("key_handle");

  private static final ResourceLocation DIAL_MARKS_TEXTURE = getTexture("dial_marks");
  private static final ResourceLocation DIAL_MARKS_PLATED_TEXTURE = getTexture("dial_marks_plated");

  private static final ResourceLocation HOUR_TEXTURE = getTexture("hour_hand");
  private static final ResourceLocation HOUR_PLATED_TEXTURE = getTexture("hour_hand_plated");

  private static final ResourceLocation MINUTE_TEXTURE = getTexture("minute_hand");
  private static final ResourceLocation MINUTE_PLATED_TEXTURE = getTexture("minute_hand_plated");

  private static final double CLOCK_MODEL_THICKNESS = 2.0 / 16;
  private static final double CLOCK_HAND_OFFSET = 0.01 / 16;

  private static final BlockState PLACED_KEY = placedKeyState();
  private static final Vec3 KEY_OFFSET = new Vec3(0.5, 0, -CLOCK_MODEL_THICKNESS / 2);

  private static final int HOURS_PER_KEY_TURN = 2;
  private static final int FRAMES_PER_KEY_TURN = AnalogClockFace.CLOCK_HAND_FRAMES * HOURS_PER_KEY_TURN;
  private static final float KEY_DEGREES_PER_FRAME = 360F / FRAMES_PER_KEY_TURN;

  private final BlockRenderDispatcher blockRenderer;

  public AnalogClockFaceRenderer(BlockEntityRendererProvider.Context ctx) {
    //? <= 1.21.5
    this.blockRenderer = ctx.getBlockRenderDispatcher();
    //? >= 1.21.11
    //this.blockRenderer = ctx.blockRenderDispatcher();
  }

  //? >= 1.21.11 {
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

  //? <= 1.21.5 {
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
    RenderFrame minuteFrame = new RenderFrame(clockFace.getMinuteFrame(), numFrames);
    RenderFrame hourFrame = new RenderFrame(clockFace.getHourFrame(), numFrames);
    PoseStack matrices = ctx.matrices();
    matrices.pushPose();

    ClockFaceStyle.Plating plating = clockFace.getBlockState().getValue(AnalogClockBlock.HANDS_PLATING);
    boolean hasPlating = plating != ClockFaceStyle.HANDS_NO_PLATING;
    int metalColor = plating.getColor();
    int markingsTint = metalColor;

    if (!hasPlating) {
      ClockFaceStyle.DyeColor faceDye = clockFace.getBlockState().getValue(AnalogClockBlock.FACE_TINT);
      if (faceDye != ClockFaceStyle.FACE_NO_DYE) markingsTint = ARGB.lerp(0.75F, markingsTint, faceDye.getColor());
    }

    ResourceLocation dialMarks = hasPlating ? DIAL_MARKS_PLATED_TEXTURE : DIAL_MARKS_TEXTURE;
    ResourceLocation minuteHand = hasPlating ? MINUTE_PLATED_TEXTURE : MINUTE_TEXTURE;
    ResourceLocation hourHand = hasPlating ? HOUR_PLATED_TEXTURE : HOUR_TEXTURE;

    orientWithAlignment(matrices, clockFace.getBlockState(), (isFront) -> 0.5 + (isFront ? 0 : -CLOCK_MODEL_THICKNESS));
    if (clockFace.isWinding()) drawClockKey(this.blockRenderer, clockFace.getClockFrame(), ctx);

    drawStaticAsset(CUTOUT.apply(dialMarks), markingsTint, ctx);
    drawAnimatedAsset(CUTOUT.apply(minuteHand), metalColor, minuteFrame, ctx.zOffset(CLOCK_HAND_OFFSET));
    drawAnimatedAsset(CUTOUT.apply(hourHand), metalColor, hourFrame, ctx.zOffset(CLOCK_HAND_OFFSET));

    matrices.popPose();
  }

  private static void drawClockKey(BlockRenderDispatcher blockRenderer, int clockFrame, RenderContext ctx) {
    float keyRotation = keyAngleDegrees(clockFrame);

    drawBlockModel(PLACED_KEY, blockRenderer, ctx.offset(KEY_OFFSET));
    drawStaticAsset(CUTOUT.apply(KEY_HANDLE), NO_TINT, ctx.rotateDegrees(Axis.XP, keyRotation));

    ctx.rotateDegrees(Axis.XP, -keyRotation).offset(KEY_OFFSET.reverse());
  }

  private static BlockState placedKeyState() {
    return ModBlocks.PLACED_CLOCK_KEY.get().defaultBlockState().setValue(PlacedClockKey.ATTACHED, true);
  }

  private static float keyAngleDegrees(int clockFrame) {
    return (clockFrame % FRAMES_PER_KEY_TURN) * KEY_DEGREES_PER_FRAME;
  }
}

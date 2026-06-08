package fabiofdez.analogclock.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import fabiofdez.analogclock.client.renderer.state.PendulumRenderState;
import fabiofdez.analogclock.block.entity.PendulumEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
//? >= 1.21.11 {
/*import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
*///? }
import net.minecraft.resources.ResourceLocation;

public class PendulumRenderer extends AnimatedEntityRenderer<PendulumEntity/*? if >= 1.21.11 >> '>' *//*, PendulumRenderState*/> {
  protected static final ResourceLocation GEMSTONE_TEXTURE = getTexture("pendulum_stone_frames");
  private static final ResourceLocation PENDULUM_TEXTURE = getTexture("pendulum_frames");

  protected static final double GEMSTONE_OFFSET = 0.01 / 16;
  private static final double PENDULUM_OFFSET = 1.0 / 16;

  public PendulumRenderer(BlockEntityRendererProvider.Context ignoredCtx) {
  }

  //? >= 1.21.11 {
  /*@Override
  public PendulumRenderState createRenderState() {
    return new PendulumRenderState();
  }

  @Override
  public void extractRenderState(PendulumEntity pendulum, PendulumRenderState renderState, float tickProgress, @NonNull Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
    super.extractRenderState(pendulum, renderState, tickProgress, cameraPos, crumblingOverlay);
    renderState.extractStateFrom(pendulum);
  }
  *///? }

  //? <= 1.21.5 {
  @Override
  protected Object parseRenderState(PendulumEntity pendulum) {
    PendulumRenderState renderState = new PendulumRenderState();
    renderState.extractStateFrom(pendulum);
    return renderState;
  }
  //? }

  @Override
  protected void submitRender(Object renderState, RenderContext ctx) {
    if (!(renderState instanceof PendulumRenderState pendulum)) return;

    RenderFrame frame = new RenderFrame(pendulum.getSwingFrame(), PendulumEntity.NUM_PENDULUM_FRAMES);
    PoseStack matrices = ctx.matrices();
    matrices.pushPose();

    orientWithAlignment(matrices, pendulum.getBlockState(), (ignored) -> 0.5 - PENDULUM_OFFSET);
    drawPendulum(pendulum, frame, ctx);

    matrices.popPose();
  }

  protected void drawPendulum(PendulumRenderState pendulum, RenderFrame frame, RenderContext ctx) {
    drawAnimatedAsset(CUTOUT.apply(PENDULUM_TEXTURE), NO_TINT, frame, ctx);
    drawAnimatedAsset(CUTOUT.apply(GEMSTONE_TEXTURE), pendulum.getTint(), frame, ctx.zOffset(GEMSTONE_OFFSET));
  }
}

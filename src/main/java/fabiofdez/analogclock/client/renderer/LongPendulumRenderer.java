package fabiofdez.analogclock.client.renderer;

import fabiofdez.analogclock.client.renderer.state.PendulumRenderState;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class LongPendulumRenderer extends PendulumRenderer {
  private static final ResourceLocation PENDULUM_ROD_TEXTURE = getTexture("long_pendulum_rod_frames");
  private static final ResourceLocation PENDULUM_TEXTURE = getTexture("long_pendulum_frames");

  private static final double GEMSTONE_Y_OFFSET = 3.0 / 16;

  public LongPendulumRenderer(BlockEntityRendererProvider.Context ignoredCtx) {
    super(ignoredCtx);
  }

  @Override
  protected void drawPendulum(PendulumRenderState pendulum, RenderFrame frame, RenderContext ctx) {
    drawAnimatedAsset(CUTOUT.apply(PENDULUM_ROD_TEXTURE), NO_TINT, frame, ctx);
    drawAnimatedAsset(CUTOUT.apply(PENDULUM_TEXTURE), NO_TINT, frame, ctx.yOffset(-1));

    ctx.offset(0, GEMSTONE_Y_OFFSET, GEMSTONE_OFFSET);
    drawAnimatedAsset(CUTOUT.apply(GEMSTONE_TEXTURE), pendulum.getTint(), frame, ctx);
  }
}

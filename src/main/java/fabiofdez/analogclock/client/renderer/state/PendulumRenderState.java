package fabiofdez.analogclock.client.renderer.state;

import fabiofdez.analogclock.color.GemstoneColor;
import fabiofdez.analogclock.block.entity.PendulumEntity;
//? >= 1.21.11
//import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.world.level.block.state.BlockState;

public class PendulumRenderState /*? if >= 1.21.11 >> '{' */ /*extends BlockEntityRenderState  */{
  private BlockState blockState;
  private int tint;
  private int swingFrame;

  public void extractStateFrom(PendulumEntity pendulum) {
    this.blockState = pendulum.getBlockState();
    this.tint = GemstoneColor.getTint(pendulum);
    this.swingFrame = pendulum.getSwingFrame();
  }

  public BlockState getBlockState() {
    return blockState;
  }

  public int getTint() {
    return tint;
  }

  public int getSwingFrame() {
    return swingFrame;
  }
}

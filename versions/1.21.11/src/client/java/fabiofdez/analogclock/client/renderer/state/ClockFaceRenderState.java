package fabiofdez.analogclock.client.renderer.state;

import fabiofdez.analogclock.entity.AnalogClockFace;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.world.level.block.state.BlockState;

public class ClockFaceRenderState extends BlockEntityRenderState {
  private BlockState blockState;
  private int hourFrame;
  private int minuteFrame;

  public void extractStateFrom(AnalogClockFace clockFace) {
    this.blockState = clockFace.getBlockState();
    this.hourFrame = clockFace.getHourFrame();
    this.minuteFrame = clockFace.getMinuteFrame();
  }

  public BlockState getBlockState() {
    return blockState;
  }

  public int getHourFrame() {
    return hourFrame;
  }

  public int getMinuteFrame() {
    return minuteFrame;
  }
}

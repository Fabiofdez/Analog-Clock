package fabiofdez.analogclock.client.renderer.state;

import fabiofdez.analogclock.block.entity.AnalogClockFace;
//? >= 1.21.11
//import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
//? >= 26.1
//import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.world.level.block.state.BlockState;

public class ClockFaceRenderState /*? if >= 1.21.11 >> '{' */ /*extends BlockEntityRenderState  */{
  private BlockState blockState;
  private int clockFrame;
  private int hourFrame;
  private int minuteFrame;
  private boolean winding;
  //? >= 26.1
  //private final BlockModelRenderState keyShaftState = new BlockModelRenderState();

  public void extractStateFrom(AnalogClockFace clockFace) {
    this.blockState = clockFace.getBlockState();
    this.clockFrame = clockFace.getClockFrame();
    this.hourFrame = clockFace.getHourFrame();
    this.minuteFrame = clockFace.getMinuteFrame();
    this.winding = clockFace.isManuallyWinding();
  }

  public BlockState getBlockState() {
    return blockState;
  }

  public int getClockFrame() {
    return clockFrame;
  }

  public int getHourFrame() {
    return hourFrame;
  }

  public int getMinuteFrame() {
    return minuteFrame;
  }

  public boolean isWinding() {
    return winding;
  }

  //? >= 26.1 {
  /*public BlockModelRenderState getKeyShaftState() {
    return keyShaftState;
  }
  *///? }
}

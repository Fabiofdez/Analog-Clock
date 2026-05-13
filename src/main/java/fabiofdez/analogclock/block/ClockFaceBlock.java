package fabiofdez.analogclock.block;

import fabiofdez.analogclock.color.ClockFaceStyle;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.PushReaction;

public class ClockFaceBlock extends Block {
  public static final EnumProperty<ClockFaceStyle.DyeColor> FACE_TINT;

  public ClockFaceBlock(BlockBehaviour.Properties properties) {
    super(properties.noOcclusion().pushReaction(PushReaction.DESTROY));
    this.registerDefaultState(this.defaultBlockState().setValue(FACE_TINT, ClockFaceStyle.FACE_NO_DYE));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(FACE_TINT);
  }

  static {
    FACE_TINT = AnalogClockBlock.FACE_TINT;
  }
}

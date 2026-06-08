package fabiofdez.analogclock.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class PlacedClockKey extends InvisibleBlock {
  public static final BooleanProperty ATTACHED;

  public PlacedClockKey(Properties properties) {
    super(properties);
    this.registerDefaultState(this.defaultBlockState().setValue(ATTACHED, false));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(ATTACHED);
  }

  @Override
  protected RenderShape getRenderShape(BlockState state) {
    if (state.getValue(ATTACHED)) return RenderShape.MODEL;

    return super.getRenderShape(state);
  }

  static {
    ATTACHED = BooleanProperty.create("attached");
  }
}

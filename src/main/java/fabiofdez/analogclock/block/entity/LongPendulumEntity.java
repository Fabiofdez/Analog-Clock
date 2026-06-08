package fabiofdez.analogclock.block.entity;

import fabiofdez.analogclock.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class LongPendulumEntity extends PendulumEntity {

  public LongPendulumEntity(BlockPos pos, BlockState state) {
    super(ModBlockEntities.LONG_PENDULUM_ENTITY.get(), pos, state);
  }
}

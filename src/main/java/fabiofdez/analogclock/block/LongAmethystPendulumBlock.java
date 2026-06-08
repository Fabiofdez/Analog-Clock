package fabiofdez.analogclock.block;

//? >= 1.21
import com.mojang.serialization.MapCodec;
import fabiofdez.analogclock.ModBlockEntities;
import fabiofdez.analogclock.block.entity.LongPendulumEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LongAmethystPendulumBlock extends AmethystPendulumBlock {

  public LongAmethystPendulumBlock(Properties properties) {
    super(properties);
  }

  //? >= 1.21 {
  @Override
  protected @NotNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
    return simpleCodec(LongAmethystPendulumBlock::new);
  }
  //? }

  @Override
  public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new LongPendulumEntity(pos, state);
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
    if (level.isClientSide() || blockEntityType != ModBlockEntities.LONG_PENDULUM_ENTITY.get()) return null;

    return LongPendulumEntity::tick;
  }
}

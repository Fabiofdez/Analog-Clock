package fabiofdez.analogclock.block;

import com.mojang.serialization.MapCodec;
import fabiofdez.analogclock.ModBlockEntities;
import fabiofdez.analogclock.entity.AnalogClockFace;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AnalogClockBlock extends DirectionalAlignedBlock implements EntityBlock {
  public AnalogClockBlock(Properties properties) {
    super(properties);
  }

  @Override
  protected @NotNull VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
    return getShapeWithThickness(state, 2);
  }

  @Override
  protected @NotNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
    return simpleCodec(AnalogClockBlock::new);
  }

  @Override
  public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new AnalogClockFace(pos, state);
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
    if (level.isClientSide() || blockEntityType != ModBlockEntities.CLOCK_FACE_ENTITY) return null;

    return AnalogClockFace::tick;
  }
}

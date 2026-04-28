package fabiofdez.analogclock.block;

import com.mojang.serialization.MapCodec;
import fabiofdez.analogclock.ModBlockEntities;
import fabiofdez.analogclock.ModBlocks;
import fabiofdez.analogclock.entity.PendulumEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
//? <= 1.21.1
//import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
//? > 1.21.1 {
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.util.RandomSource;
//? }
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AmethystPendulumBlock extends DirectionalAlignedBlock implements EntityBlock {
  public static final IntegerProperty DAY_PHASE;

  public AmethystPendulumBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(this.defaultBlockState().setValue(DAY_PHASE, 0));
  }

  protected boolean mayPlaceUnder(BlockState state, BlockGetter blockGetter, BlockPos pos) {
    if (!state.is(ModBlocks.ANALOG_CLOCK.get())) {
      return state.isFaceSturdy(blockGetter, pos, Direction.DOWN);
    }
    return true;
  }

  @Override
  protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
    BlockState blockAbove = level.getBlockState(pos.above());
    if (mayPlaceUnder(blockAbove, level, pos.above())) {
      return super.canSurvive(state, level, pos);
    }
    return false;
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(DAY_PHASE);
  }

  @Override
  public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
    Level level = ctx.getLevel();
    BlockPos clickedPos = ctx.getClickedPos();
    BlockState blockAbove = level.getBlockState(clickedPos.above());
    if (!mayPlaceUnder(blockAbove, level, clickedPos.above())) return defaultBlockState();

    BlockState newState = super.getStateForPlacement(ctx);
    if (newState == null) return null;

    if (blockAbove.is(ModBlocks.ANALOG_CLOCK.get())) {
      return newState.setValue(FACING, blockAbove.getValue(FACING)).setValue(ALIGNMENT, blockAbove.getValue(ALIGNMENT));
    }

    return newState;
  }

  @Override
  protected @NotNull VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext ctx) {
    BlockState blockAbove = blockGetter.getBlockState(pos.above());
    if (!mayPlaceUnder(blockAbove, blockGetter, pos.above())) return Shapes.empty();

    return getShapeWithThickness(state, 2);
  }

  @Override
  //? > 1.21.1
  protected @NotNull BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess tickAccess, BlockPos pos, Direction direction, BlockPos pos2, BlockState state2, RandomSource rand) {
  //? <= 1.21.1
  //protected @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState state2, LevelAccessor level, BlockPos pos, BlockPos pos2) {
    if (!state.canSurvive(level, pos)) return Blocks.AIR.defaultBlockState();

    //? > 1.21.1
    return super.updateShape(state, level, tickAccess, pos, direction, pos2, state2, rand);
    //? <= 1.21.1
    //return super.updateShape(state, direction, state2, level, pos, pos2);
  }

  @Override
  protected @NotNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
    return simpleCodec(AmethystPendulumBlock::new);
  }

  @Override
  public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new PendulumEntity(pos, state);
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
    if (level.isClientSide() || blockEntityType != ModBlockEntities.PENDULUM_ENTITY.get()) return null;

    return PendulumEntity::tick;
  }

  static {
    DAY_PHASE = IntegerProperty.create("day_phase", 0, 23);
  }
}

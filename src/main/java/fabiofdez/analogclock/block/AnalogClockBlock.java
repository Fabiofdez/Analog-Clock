package fabiofdez.analogclock.block;

import com.mojang.serialization.MapCodec;
import fabiofdez.analogclock.ModBlockEntities;
import fabiofdez.analogclock.block.state.properties.Alignment;
import fabiofdez.analogclock.entity.AnalogClockFace;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AnalogClockBlock extends HorizontalDirectionalBlock implements EntityBlock {
  public static final MapCodec<AnalogClockBlock> CODEC = simpleCodec(AnalogClockBlock::new);
  public static final EnumProperty<Alignment> ALIGNMENT;

  public AnalogClockBlock(Properties properties) {
    super(properties);
    BlockState initialState = this
        .getStateDefinition()
        .any();

    this.registerDefaultState(initialState
        .setValue(FACING, Direction.NORTH)
        .setValue(ALIGNMENT, Alignment.BACK));
  }

  @Override
  public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
    Direction lookingDirection = ctx.getHorizontalDirection();

    Vec3 clickLocation = ctx.getClickLocation();
    BlockPos clickPos = ctx.getClickedPos();
    Direction.Axis lookingAxis = lookingDirection.getAxis();

    boolean lookingAtBack;
    if (lookingDirection.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
      lookingAtBack = clickLocation.get(lookingAxis) - (double) clickPos.get(lookingAxis) > (double) 0.5F;
    } else {
      lookingAtBack = clickLocation.get(lookingAxis) - (double) clickPos.get(lookingAxis) < (double) 0.5F;
    }

    return this
        .defaultBlockState()
        .setValue(FACING, lookingDirection.getOpposite())
        .setValue(ALIGNMENT, lookingAtBack ? Alignment.BACK : Alignment.FRONT);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING, ALIGNMENT);
  }

  @Override
  protected @NotNull VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
    boolean isFront = state.getValue(ALIGNMENT) == Alignment.FRONT;

    VoxelShape shape;
    switch (state.getValue(FACING)) {
      case NORTH -> shape = Block.box(0, 0, isFront ? 0 : 14, 16, 16, isFront ? 2 : 16);
      case SOUTH -> shape = Block.box(0, 0, isFront ? 14 : 0, 16, 16, isFront ? 16 : 2);
      case WEST -> shape = Block.box(isFront ? 0 : 14, 0, 0, isFront ? 2 : 16, 16, 16);
      case EAST -> shape = Block.box(isFront ? 14 : 0, 0, 0, isFront ? 16 : 2, 16, 16);
      default -> shape = Shapes.block();
    }

    return shape;
  }

  @Override
  protected boolean isCollisionShapeFullBlock(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
    return false;
  }

  @Override
  protected @NotNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
    return CODEC;
  }

  @Override
  public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new AnalogClockFace(pos, state);
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
    if (level.isClientSide || blockEntityType != ModBlockEntities.CLOCK_FACE_ENTITY) return null;

    return AnalogClockFace::tick;
  }

  static {
    ALIGNMENT = EnumProperty.create("alignment", Alignment.class);
  }
}

package fabiofdez.analogclock.block;

import fabiofdez.analogclock.block.state.properties.Alignment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public abstract class DirectionalAlignedBlock extends HorizontalDirectionalBlock {
  public static final EnumProperty<Alignment> ALIGNMENT;

  protected DirectionalAlignedBlock(Properties properties) {
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
  protected boolean isCollisionShapeFullBlock(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
    return false;
  }

  protected VoxelShape getShapeWithThickness(BlockState state, int thickness) {
    boolean isFront = state.getValue(ALIGNMENT) == Alignment.FRONT;
    Direction facingDirection = state.getValue(FACING);
    Direction shiftDirection = isFront ? facingDirection : facingDirection.getOpposite();
    Vec3 shift = shiftDirection
        .getUnitVec3()
        .scale(1 - (thickness / 16F));

    VoxelShape FACING_X = Block.box(0, 0, 0, thickness, 16, 16);
    VoxelShape FACING_Z = Block.box(0, 0, 0, 16, 16, thickness);

    return switch (facingDirection) {
      case NORTH -> isFront ? FACING_Z : FACING_Z.move(shift);
      case SOUTH -> isFront ? FACING_Z.move(shift) : FACING_Z;
      case WEST -> isFront ? FACING_X : FACING_X.move(shift);
      case EAST -> isFront ? FACING_X.move(shift) : FACING_X;
      default -> Shapes.empty();
    };
  }

  static {
    ALIGNMENT = EnumProperty.create("alignment", Alignment.class);
  }
}

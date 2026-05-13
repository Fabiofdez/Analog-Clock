package fabiofdez.analogclock.block;

import com.mojang.serialization.MapCodec;
import fabiofdez.analogclock.ModBlockEntities;
import fabiofdez.analogclock.color.ClockFaceStyle;
import fabiofdez.analogclock.entity.AnalogClockFace;
import fabiofdez.analogclock.item.AnalogClockItem;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AnalogClockBlock extends DirectionalAlignedBlock implements EntityBlock {
  public static final EnumProperty<ClockFaceStyle.DyeColor> FACE_TINT;
  public static final EnumProperty<ClockFaceStyle.Plating> HANDS_PLATING;

  public AnalogClockBlock(Properties properties) {
    super(properties);

    this.registerDefaultState(this
        .defaultBlockState()
        .setValue(FACE_TINT, ClockFaceStyle.FACE_NO_DYE)
        .setValue(HANDS_PLATING, ClockFaceStyle.HANDS_NO_PLATING));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(FACE_TINT, HANDS_PLATING);
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
    if (level.isClientSide() || blockEntityType != ModBlockEntities.CLOCK_FACE_ENTITY.get()) return null;

    return AnalogClockFace::tick;
  }

  @NotNull
  @Override
  protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
    Item item = stack.getItem();

    if (stack.is(Items.BRUSH)) {
      boolean hasDye = state.getValue(FACE_TINT) != ClockFaceStyle.FACE_NO_DYE;

      if (!level.isClientSide() && hasDye && level.getBlockEntity(pos) instanceof AnalogClockFace clockFace) {
        clockFace.startBrushing(player);
      }

      return InteractionResult.PASS;
    }

    if (stack.is(ItemTags.PICKAXES)) {
      ClockFaceStyle.Plating plating = state.getValue(HANDS_PLATING);
      if (plating == ClockFaceStyle.HANDS_NO_PLATING) return InteractionResult.PASS;

      if (!level.isClientSide()) {
        level.setBlockAndUpdate(pos, state.setValue(HANDS_PLATING, ClockFaceStyle.HANDS_NO_PLATING));
        level.playSound(null, pos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 0.6F, 1.5F);
        popResourceFromFace(level, pos, state.getValue(FACING), new ItemStack(plating.item()));
      }

      return InteractionResult.SUCCESS;
    }

    if (ClockFaceStyle.DyeColor.has(item)) {
      if (state.getValue(FACE_TINT) == ClockFaceStyle.DyeColor.getColorOf(item)) return InteractionResult.PASS;

      if (level.isClientSide()) return InteractionResult.SUCCESS;

      level.setBlockAndUpdate(pos, state.setValue(FACE_TINT, ClockFaceStyle.DyeColor.getColorOf(item)));
      level.playSound(null, pos, SoundEvents.DYE_USE, SoundSource.BLOCKS);
      stack.consume(1, player);
      return InteractionResult.CONSUME;
    }

    if (ClockFaceStyle.Plating.has(item)) {
      if (state.getValue(HANDS_PLATING) == ClockFaceStyle.Plating.getMetalOf(item)) return InteractionResult.PASS;

      if (level.isClientSide()) return InteractionResult.SUCCESS;

      level.setBlockAndUpdate(pos, state.setValue(HANDS_PLATING, ClockFaceStyle.Plating.getMetalOf(item)));
      level.playSound(null, pos, SoundEvents.COPPER_STEP, SoundSource.BLOCKS, 0.6F, 1.5F);
      stack.consume(1, player);
      return InteractionResult.CONSUME;
    }

    return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
  }

  @NotNull
  @Override
  protected List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
    List<ItemStack> stacks = super.getDrops(state, builder);
    if (stacks.isEmpty()) return stacks;

    ItemStack stack = stacks.getFirst();
    if (!stack.is(this.asItem())) return stacks;

    stack.set(AnalogClockItem.FACE_TINT, state.getValue(FACE_TINT).dyeId());
    stack.set(AnalogClockItem.HANDS_PLATING, state.getValue(HANDS_PLATING).metalId());

    return List.of(stack);
  }

  // TODO: output comparator signal?

  static {
    FACE_TINT = EnumProperty.create("face_tint", ClockFaceStyle.DyeColor.class);
    HANDS_PLATING = EnumProperty.create("hands_plating", ClockFaceStyle.Plating.class);
  }
}

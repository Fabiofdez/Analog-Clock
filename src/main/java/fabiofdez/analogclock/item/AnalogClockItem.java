package fabiofdez.analogclock.item;

import fabiofdez.analogclock.AnalogClock;
import fabiofdez.analogclock.ModBlocks;
import fabiofdez.analogclock.block.AnalogClockBlock;
import fabiofdez.analogclock.block.entity.AnalogClockFace;
import fabiofdez.analogclock.color.ClockFaceStyle;
import fabiofdez.analogclock.item.data.ItemAttribute;
import fabiofdez.analogclock.item.data.ItemAttributes;
import fabiofdez.analogclock.util.ClockTime;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
//? > 1.21.1
import net.minecraft.world.item.component.TooltipDisplay;

//? <= 1.21.1
//import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

public class AnalogClockItem extends BlockItem {
  public static final ItemAttribute<String> FACE_TINT;
  public static final ItemAttribute<String> HANDS_PLATING;
  public static final ItemAttribute<String> TIME_ZONE;

  public AnalogClockItem(Block block, Properties properties) {
    super(block, Attributes.addTo(properties));
  }

  //? <= 1.21.1 {
  /*@Override
  public String getDescriptionId() {
    return this.getOrCreateDescriptionId();
  }
  *///? }

  @Nullable
  @Override
  protected BlockState getPlacementState(BlockPlaceContext ctx) {
    BlockState initialState = super.getPlacementState(ctx);
    if (initialState == null) return null;

    ItemStack stack = ctx.getItemInHand();
    if (!stack.is(this)) return initialState;

    ItemAttributes attributes = Attributes.parseFrom(stack);
    String dyeId = attributes.get(FACE_TINT);
    String metalId = attributes.get(HANDS_PLATING);

    return initialState
        .setValue(AnalogClockBlock.FACE_TINT, ClockFaceStyle.DyeColor.getColorOf(dyeId))
        .setValue(AnalogClockBlock.HANDS_PLATING, ClockFaceStyle.Plating.getMetalOf(metalId));
  }

  @Override
  protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, @Nullable Player player, ItemStack stack, BlockState state) {
    if (level.isClientSide()) return false;

    BlockEntity blockEntity = level.getBlockEntity(pos);
    if (!(blockEntity instanceof AnalogClockFace clockFace)) return false;

    String zoneId = Attributes.parseFrom(stack).get(TIME_ZONE);
    if (!zoneId.equals(AnalogClockFace.IN_GAME_ZONE_ID)) clockFace.setTimeZone(zoneId);

    return true;
  }

  @Override
      //? if < 1.21 {
  /*public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
    super.appendHoverText(stack, level, components, flag);
    addClockInfo(stack, components::add);
  }

  *///? } else if <= 1.21.1 {
  /*public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> components, TooltipFlag flag) {
    super.appendHoverText(stack, ctx, components, flag);
    addClockInfo(stack, components::add);
  }
  *///? } else {
  public void appendHoverText(ItemStack stack, TooltipContext ctx, TooltipDisplay display, Consumer<Component> components, TooltipFlag flag) {
    super.appendHoverText(stack, ctx, display, components, flag);
    addClockInfo(stack, components);
  }
  //? }

  private void addClockInfo(ItemStack stack, Consumer<Component> components) {
    ItemAttributes attributes = Attributes.parseFrom(stack);
    String dyeId = attributes.get(FACE_TINT);
    String metalId = attributes.get(HANDS_PLATING);
    String zoneId = attributes.get(TIME_ZONE);

    if (!zoneId.equals(TIME_ZONE.defaultValue())) {
      String zone = ClockTime.getOffset(zoneId);
      components.accept(Component
          .translatable(Tooltip.REAL_WORLD.getTranslationKey(), zone)
          .withStyle(ChatFormatting.BLUE));
    }

    if (!dyeId.equals(FACE_TINT.defaultValue())) {
      String faceDye = ClockFaceStyle.DyeColor.getColorOf(dyeId).readable();
      components.accept(Component
          .translatable(Tooltip.DYE.getTranslationKey(), faceDye)
          .withStyle(ChatFormatting.GRAY));
    }

    if (!metalId.equals(HANDS_PLATING.defaultValue())) {
      String metalPlating = ClockFaceStyle.Plating.getMetalOf(metalId).readable();
      components.accept(Component
          .translatable(Tooltip.PLATING.getTranslationKey(), metalPlating)
          .withStyle(ChatFormatting.GRAY));
    }
  }

  static class Attributes extends ItemAttributes {

    static ItemAttributes parseFrom(ItemStack stack) {
      return new Attributes().parse(stack);
    }

    @Override
    protected Item targetItem() {
      return ModBlocks.ANALOG_CLOCK.get().asItem();
    }

    @Override
    protected void onRemap(CompoundTag customData) {
      //? >= 1.21 {
      FACE_TINT.checkIn(customData).ifPresent((dyeId) -> stack.set(FACE_TINT.component(), dyeId));
      HANDS_PLATING.checkIn(customData).ifPresent((metalId) -> stack.set(HANDS_PLATING.component(), metalId));
      TIME_ZONE.checkIn(customData).ifPresent((zoneId) -> stack.set(TIME_ZONE.component(), zoneId));
      //? }
    }

    protected static Properties addTo(Properties properties) {
      //? >= 1.21 {
      FACE_TINT.addTo(properties);
      HANDS_PLATING.addTo(properties);
      TIME_ZONE.addTo(properties);
      //? }

      return properties;
    }
  }

  public enum Tooltip {
    DYE("dye"),
    PLATING("plating"),
    REAL_WORLD("real_world");

    private final Function<Item, String> predicate;

    Tooltip(String tooltipName) {
      this.predicate = (item) -> AnalogClock.tooltipTranslatable(item, tooltipName);
    }

    public String getTranslationKey() {
      return this.predicate.apply(ModBlocks.ANALOG_CLOCK.get().asItem());
    }
  }

  static {
    FACE_TINT = ItemAttributes.STRING.create("clock_face_color", ClockFaceStyle.FACE_NO_DYE.dyeId());
    HANDS_PLATING = ItemAttributes.STRING.create("clock_hands_plating", ClockFaceStyle.HANDS_NO_PLATING.metalId());
    TIME_ZONE = ItemAttributes.STRING.create("time_zone", AnalogClockFace.IN_GAME_ZONE_ID);
  }
}

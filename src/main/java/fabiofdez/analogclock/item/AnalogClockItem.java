package fabiofdez.analogclock.item;

import fabiofdez.analogclock.AnalogClock;
import fabiofdez.analogclock.ModBlocks;
import fabiofdez.analogclock.block.AnalogClockBlock;
import fabiofdez.analogclock.color.ClockFaceStyle;
import fabiofdez.analogclock.item.data.ItemAttributes;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
//? < 1.21 {
/*import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
*///? } else {
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
//? }
//? > 1.21.1
import net.minecraft.world.item.component.TooltipDisplay;

//? <= 1.21.1
//import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class AnalogClockItem extends BlockItem {
  //? if < 1.21 {
  /*public static final String FACE_TINT = "clock_face_color";
  public static final String HANDS_PLATING = "clock_hands_plating";
  *///? } else {
  public static final DataComponentType<String> FACE_TINT = registerComponent("clock_face_color", Codec.STRING);
  public static final DataComponentType<String> HANDS_PLATING = registerComponent("clock_hands_plating", Codec.STRING);
  //? }

  public AnalogClockItem(Block block, Properties properties) {
    //? if < 1.21 {
    /*super(block, properties);
     *///? } else {
    super(
        block,
        properties
            .component(FACE_TINT, ClockFaceStyle.FACE_NO_DYE.dyeId())
            .component(HANDS_PLATING, ClockFaceStyle.HANDS_NO_PLATING.metalId())
    );
    //? }
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

    ItemAttributes itemData = ItemAttributes.parseFrom(stack);
    String dyeId = itemData.getStringOr(FACE_TINT, ClockFaceStyle.FACE_NO_DYE.dyeId());
    String metalId = itemData.getStringOr(HANDS_PLATING, ClockFaceStyle.HANDS_NO_PLATING.metalId());

    return initialState
        .setValue(AnalogClockBlock.FACE_TINT, ClockFaceStyle.DyeColor.getColorOf(dyeId))
        .setValue(AnalogClockBlock.HANDS_PLATING, ClockFaceStyle.Plating.getMetalOf(metalId));
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
    ItemAttributes itemData = ItemAttributes.parseFrom(stack);
    String dyeId = itemData.getStringOr(FACE_TINT, ClockFaceStyle.FACE_NO_DYE.dyeId());
    String metalId = itemData.getStringOr(HANDS_PLATING, ClockFaceStyle.HANDS_NO_PLATING.metalId());

    ClockFaceStyle.DyeColor color = ClockFaceStyle.DyeColor.getColorOf(dyeId);
    ClockFaceStyle.Plating metal = ClockFaceStyle.Plating.getMetalOf(metalId);

    if (color != ClockFaceStyle.FACE_NO_DYE) {
      components.accept(Component
          .translatable(Tooltip.DYE.getTranslationKey(), color.readable())
          .withStyle(ChatFormatting.GRAY));
    }

    if (metal != ClockFaceStyle.HANDS_NO_PLATING) {
      components.accept(Component
          .translatable(Tooltip.PLATING.getTranslationKey(), metal.readable())
          .withStyle(ChatFormatting.GRAY));
    }
  }

  public enum Tooltip {
    DYE("dye"),
    PLATING("plating");

    private final Function<Item, String> predicate;

    Tooltip(String tooltipName) {
      this.predicate = (item) -> AnalogClock.tooltipTranslatable(item, tooltipName);
    }

    public String getTranslationKey() {
      return this.predicate.apply(ModBlocks.ANALOG_CLOCK.get().asItem());
    }
  }

  //? >= 1.21 {
  private static <T> DataComponentType<T> registerComponent(String name, Codec<T> codec) {
    DataComponentType<T> component = DataComponentType.<T>builder().persistent(codec).build();
    return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, AnalogClock.id(name), component);
  }
  //? }
}

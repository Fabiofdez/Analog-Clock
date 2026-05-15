package fabiofdez.analogclock.item;

import com.mojang.serialization.Codec;
import fabiofdez.analogclock.AnalogClock;
import fabiofdez.analogclock.ModBlocks;
import fabiofdez.analogclock.block.AnalogClockBlock;
import fabiofdez.analogclock.color.ClockFaceStyle;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;

public class AnalogClockItem extends BlockItem {
  public static final DataComponentType<String> FACE_TINT = Registry.register(
      BuiltInRegistries.DATA_COMPONENT_TYPE,
      AnalogClock.id("clock_face_color"),
      DataComponentType.<String>builder().persistent(Codec.STRING).build()
  );
  public static final DataComponentType<String> HANDS_PLATING = Registry.register(
      BuiltInRegistries.DATA_COMPONENT_TYPE,
      AnalogClock.id("clock_hands_plating"),
      DataComponentType.<String>builder().persistent(Codec.STRING).build()
  );

  public AnalogClockItem(Block block, Properties properties) {
    super(
        block,
        properties
            .component(FACE_TINT, ClockFaceStyle.FACE_NO_DYE.dyeId())
            .component(HANDS_PLATING, ClockFaceStyle.HANDS_NO_PLATING.metalId())
    );
  }

  @Nullable
  @Override
  protected BlockState getPlacementState(BlockPlaceContext ctx) {
    BlockState initialState = super.getPlacementState(ctx);
    if (initialState == null) return null;

    ItemStack stack = ctx.getItemInHand();
    if (!stack.is(this)) return initialState;

    DataComponentMap itemData = stack.getComponents();
    String dyeId = itemData.getOrDefault(FACE_TINT, ClockFaceStyle.FACE_NO_DYE.dyeId());
    String metalId = itemData.getOrDefault(HANDS_PLATING, ClockFaceStyle.HANDS_NO_PLATING.metalId());

    return initialState
        .setValue(AnalogClockBlock.FACE_TINT, ClockFaceStyle.DyeColor.getColorOf(dyeId))
        .setValue(AnalogClockBlock.HANDS_PLATING, ClockFaceStyle.Plating.getMetalOf(metalId));
  }

  @SuppressWarnings("deprecation")
  @Override
  public void appendHoverText(ItemStack stack, TooltipContext ctx, TooltipDisplay display, Consumer<Component> components, TooltipFlag flag) {
    String dyeId = stack.getOrDefault(FACE_TINT, ClockFaceStyle.FACE_NO_DYE.dyeId());
    String metalId = stack.getOrDefault(HANDS_PLATING, ClockFaceStyle.HANDS_NO_PLATING.metalId());

    ClockFaceStyle.DyeColor color = (ClockFaceStyle.DyeColor.getColorOf(dyeId));
    ClockFaceStyle.Plating metal = (ClockFaceStyle.Plating.getMetalOf(metalId));

    if (color != ClockFaceStyle.FACE_NO_DYE) {
      components.accept(Component
          .translatable(Tooltip.DYE.getTranslationKey(), ClockFaceStyle.readable(color))
          .withStyle(ChatFormatting.GRAY));
    }

    if (metal != ClockFaceStyle.HANDS_NO_PLATING) {
      components.accept(Component
          .translatable(Tooltip.PLATING.getTranslationKey(), ClockFaceStyle.readable(metal))
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
}

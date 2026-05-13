package fabiofdez.analogclock.item;

import com.mojang.serialization.Codec;
import fabiofdez.analogclock.AnalogClock;
import fabiofdez.analogclock.block.AnalogClockBlock;
import fabiofdez.analogclock.color.ClockFaceStyle;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

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
}

package fabiofdez.analogclock.item.data;

//? >= 1.21 {

import fabiofdez.analogclock.ModBlocks;
import fabiofdez.analogclock.color.ClockFaceStyle;
import fabiofdez.analogclock.item.AnalogClockItem;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.component.CustomData;

import java.util.Optional;
//? }
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemAttributes {
  private final ItemStack stack;

  private ItemAttributes(ItemStack stack) {
    this.stack = stack;
  }

  public static ItemAttributes parseFrom(@NotNull ItemStack stack) {
    //? if >= 1.21 {
    CustomData extra = stack.get(DataComponents.CUSTOM_DATA);

    if (stack.is(ModBlocks.ANALOG_CLOCK.get().asItem()) && extra != null) {
      CompoundTag oldTag = extra.copyTag();

      stringInTag(oldTag, componentPath(AnalogClockItem.FACE_TINT)).ifPresent((dyeId) -> {
        if (dyeId.equals(ClockFaceStyle.FACE_NO_DYE.dyeId())) return;
        stack.set(AnalogClockItem.FACE_TINT, dyeId);
      });

      stringInTag(oldTag, componentPath(AnalogClockItem.HANDS_PLATING)).ifPresent((metalId) -> {
        if (metalId.equals(ClockFaceStyle.HANDS_NO_PLATING.metalId())) return;
        stack.set(AnalogClockItem.HANDS_PLATING, metalId);
      });

      stack.remove(DataComponents.CUSTOM_DATA);
    }
    //? }

    return new ItemAttributes(stack);
  }

  //? < 1.21
  //public String getStringOr(String tagName, String defaultValue) {
  //? >= 1.21
  public String getStringOr(DataComponentType<String> component, String defaultValue) {
    //? if < 1.21 {
    /*CompoundTag itemTag = stack.getOrCreateTag();
    String value = itemTag.getString(tagName);
    if (value.isEmpty()) return defaultValue;
    return value;
    *///? } else {
    return stack.getOrDefault(component, defaultValue);
    //? }
  }

  //? >= 1.21 {
  private static <T> String componentPath(DataComponentType<T> component) {
    ResourceLocation id = BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(component);
    assert id != null;

    return id.getPath();
  }

  private static Optional<String> stringInTag(CompoundTag tag, String key) {
    //? if 1.21.1 {
    /*String value = tag.getString(key);
    if (value == null || value.isEmpty()) return Optional.empty();
    return Optional.of(value);
    *///? } else {
    return tag.getString(key);
    //? }
  }
  //? }
}

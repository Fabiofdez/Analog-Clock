package fabiofdez.analogclock.item.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
//? >= 1.21 {
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
//? }

import java.util.function.BiFunction;
import org.jetbrains.annotations.NotNull;


public abstract class ItemAttributes {
  public static final AttributeBuilder<Integer> INT = new AttributeBuilder<>(ItemAttribute::ofInt);
  public static final AttributeBuilder<Boolean> BOOLEAN = new AttributeBuilder<>(ItemAttribute::ofBoolean);
  public static final AttributeBuilder<String> STRING = new AttributeBuilder<>(ItemAttribute::ofString);

  protected ItemStack stack;

  protected abstract Item targetItem();

  protected abstract void onRemap(CompoundTag customData);

  protected ItemAttributes parse(@NotNull ItemStack stack) {
    this.stack = stack;

    //? >= 1.21 {
    CustomData customData = stack.get(DataComponents.CUSTOM_DATA);

    if (stack.is(targetItem()) && customData != null) {
      onRemap(customData.copyTag());
    }
    //? }

    return this;
  }

  public <T> T get(ItemAttribute<T> attribute) {
    //? if < 1.21 {
    /*CompoundTag itemTag = stack.getOrCreateTag();
    return attribute.checkIn(itemTag).orElse(attribute.defaultValue());
    *///? } else {
    return stack.getOrDefault(attribute.component(), attribute.defaultValue());
    //? }
  }

  public static class AttributeBuilder<T> {
    private final BiFunction<String, T, ItemAttribute<T>> builder;

    public AttributeBuilder(BiFunction<String, T, ItemAttribute<T>> builder) {
      this.builder = builder;
    }

    public ItemAttribute<T> create(String name, T defaultValue) {
      return this.builder.apply(name, defaultValue);
    }
  }
}

package fabiofdez.analogclock.item.data;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
//? >= 1.21 {
import fabiofdez.analogclock.AnalogClock;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
//? }

import java.util.Optional;
import java.util.function.Function;
//? < 1.21 {
/*import java.util.function.BiConsumer;
*///? }

public class ItemAttribute<T> {
  private final String name;
  private final T defaultValue;
  private final Codec<T> codec;

  //? >= 1.21
  private final DataComponentType<T> componentType;

  ItemAttribute(String name, T defaultValue, Codec<T> codec) {
    this.name = name;
    this.defaultValue = defaultValue;
    this.codec = codec;

    //? >= 1.21
    this.componentType = registerComponent(name, codec);
  }

  static ItemAttribute<Integer> ofInt(String name, int value) {
    return new ItemAttribute<>(name, value, Codec.INT);
  }

  static ItemAttribute<Boolean> ofBoolean(String name, boolean value) {
    return new ItemAttribute<>(name, value, Codec.BOOL);
  }

  static ItemAttribute<String> ofString(String name, String value) {
    return new ItemAttribute<>(name, value, Codec.STRING);
  }

  public void applyTo(ItemStack stack, T value) {
    //? if < 1.21 {
    /*CompoundTag itemTag = stack.getOrCreateTag();

    if (value == null || value.equals(defaultValue)) {
      itemTag.remove(name);
    } else {
      resolveTagSetter(codec, itemTag).accept(name, value);
    }
    *///? } else {
    stack.set(component(), value);
    //? }
  }

  public T defaultValue() {
    return defaultValue;
  }

  //? if >= 1.21 {
  public DataComponentType<T> component() {
    return componentType;
  }

  public void addTo(Item.Properties properties) {
    properties.component(component(), defaultValue());
  }

  private static <T> DataComponentType<T> registerComponent(String name, Codec<T> codec) {
    DataComponentType<T> component = DataComponentType.<T>builder().persistent(codec).build();
    return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, AnalogClock.id(name), component);
  }
  //? } else {
  /*protected static <U> BiConsumer<String, U> resolveTagSetter(Codec<U> codec, CompoundTag tag) {
    if (codec == Codec.INT) return (k, v) -> tag.putInt(k, (Integer) v);
    if (codec == Codec.BOOL) return (k, v) -> tag.putBoolean(k, (Boolean) v);
    if (codec == Codec.STRING) return (k, v) -> tag.putString(k, (String) v);

    return (k, v) -> {};
  }
  *///? }

  public Optional<T> checkIn(CompoundTag tag) {
    if (!tag.contains(name)) return Optional.empty();

    Optional<T> value = resolveTagGetter(codec, tag).apply(name);
    if (value.get().equals(defaultValue())) return Optional.empty();

    return value;
  }

  protected static <U> Function<String, Optional<U>> resolveTagGetter(Codec<U> codec, CompoundTag tag) {
    if (codec == Codec.INT) return (k) -> asOptional(tag.getInt(k));
    if (codec == Codec.BOOL) return (k) -> asOptional(tag.getBoolean(k));
    if (codec == Codec.STRING) return (k) -> asOptional(tag.getString(k));

    return (k) -> Optional.empty();
  }

  @SuppressWarnings("unchecked")
  private static <U> Optional<U> asOptional(Object value) {
    if (value instanceof Optional<?>) return (Optional<U>) value;
    return Optional.of((U) value);
  }
}

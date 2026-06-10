package fabiofdez.analogclock.block.entity.properties;

import com.mojang.serialization.Codec;
//? if < 1.21.11 {
import net.minecraft.nbt.CompoundTag;
//? } else {
/*import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
*///? }

//? > 1.21.1
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class BlockEntityProp<T> {
  private final PropGetterDef<T> getter;
  private final PropSetterDef<T> setter;
  private final String name;
  @SuppressWarnings({"FieldCanBeLocal", "unused"})
  private final Codec<T> CODEC;

  private T value;

  private BlockEntityProp(String name, Codec<T> codec, PropGetterDef<T> getter, PropSetterDef<T> setter) {
    this.name = name;
    this.CODEC = codec;
    this.getter = getter;
    this.setter = setter;
  }

  static BlockEntityProp<Integer> ofInt(String name) {
    return new BlockEntityProp<Integer>(name, Codec.INT, (input) -> input::getInt, (input) -> input::putInt);
  }

  static BlockEntityProp<Boolean> ofBoolean(String name) {
    //? < 1.21.11
    PropGetterDef<Boolean> booleanFromInput = (input) -> input::getBoolean;
    //? >= 1.21.11
    //PropGetterDef<Boolean> booleanFromInput = (input) -> (str) -> Optional.of(input.getBooleanOr(str, false));

    return new BlockEntityProp<Boolean>(name, Codec.BOOL, booleanFromInput, (input) -> input::putBoolean);
  }

  @SuppressWarnings("SameParameterValue")
  static BlockEntityProp<String> ofString(String name) {
    return new BlockEntityProp<String>(name, Codec.STRING, (input) -> input::getString, (input) -> input::putString);
  }

  public BlockEntityProp<T> setDefault(T initialValue) {
    this.value = initialValue;
    return this;
  }

  //? < 1.21.11
  public void loadFrom(CompoundTag input) {
    //? >= 1.21.11
    //public void loadFrom(ValueInput input) {

    //? if neoforge && >= 1.21.11 {
    /*if (input.read(this.name, this.CODEC).isEmpty()) return;
     *///? } else {
    if (!input.contains(this.name)) return;
    //? }

    //noinspection OptionalGetWithoutIsPresent
    T value = this.getter.apply(input).apply(this.name)/*? if >= 1.21.5 >> ';'*/.get();
    this.set(value);
  }

  //? < 1.21.11
  public void saveTo(CompoundTag output) {
    //? >= 1.21.11
    //public void saveTo(ValueOutput output) {
    this.setter.apply(output).accept(this.name, this.get());
  }


  public T get() {
    return value;
  }

  public void set(T value) {
    this.value = value;
  }

  @FunctionalInterface
      //? < 1.21.11
  protected interface PropGetterDef<T> extends Function<CompoundTag, PropGetter<T>> {
    //? >= 1.21.11
    //protected interface PropGetterDef<T> extends Function<ValueInput, PropGetter<T>> {
  }

  @FunctionalInterface
      //? < 1.21.11
  protected interface PropSetterDef<T> extends Function<CompoundTag, PropSetter<T>> {
    //? >= 1.21.11
    //protected interface PropSetterDef<T> extends Function<ValueOutput, PropSetter<T>> {
  }

  @FunctionalInterface
      //? <= 1.21.1
  //protected interface PropGetter<T> extends Function<String, T> {
      //? > 1.21.1
  protected interface PropGetter<T> extends Function<String, Optional<T>> {
  }

  @FunctionalInterface
  protected interface PropSetter<T> extends BiConsumer<String, T> {
  }
}

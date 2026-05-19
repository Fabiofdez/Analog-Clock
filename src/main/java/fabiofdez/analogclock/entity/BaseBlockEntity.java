package fabiofdez.analogclock.entity;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
//? > 1.20.1
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
//? >= 1.21.11 {
/*import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
*///? }
import org.jetbrains.annotations.NotNull;

//? > 1.21.1
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class BaseBlockEntity extends BlockEntity {

  public BaseBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
    super(type, pos, state);
  }

  protected void saveData(ExtraData output) {
  }

  protected void loadData(ExtraData input) {
  }

  @Override
      //? if <= 1.21.5
  protected void saveAdditional(@NotNull CompoundTag output /*? if <= 1.21.5 && > 1.20.1 >> ') {' */, HolderLookup.Provider registryLookup) {
      //? if >= 1.21.11
  //protected void saveAdditional(ValueOutput output) {

    saveData(ExtraData.from(output));

    super.saveAdditional(output /*? if <= 1.21.5 && > 1.20.1 >> ');' */, registryLookup);
  }

  @Override
      //? if <= 1.20.1 {
  /*public void load(@NotNull CompoundTag input) {
   *///? } else if <= 1.21.5 {
  protected void loadAdditional(CompoundTag input, HolderLookup.Provider registryLookup) {
   //? } else if >= 1.21.11 {
  /*protected void loadAdditional(ValueInput input) {
    *///? }

    //? <= 1.20.1
    //super.load(input);
    //? > 1.20.1
    super.loadAdditional(input /*? if <= 1.21.5 >> ');' */, registryLookup);

    loadData(ExtraData.from(input));
  }

  @Override
  public @NotNull CompoundTag getUpdateTag(/*? if > 1.20.1 >> ') {' */HolderLookup.Provider provider) {
    return saveWithoutMetadata(/*? if > 1.20.1 >> ');' */provider);
  }

  protected static class ExtraData {
    //? < 1.21.11
    private CompoundTag tag;
    //? >= 1.21.11 {
    /*private ValueInput tag;
    private ValueOutput output;
    *///? }

    //? < 1.21.11
    private static ExtraData from(CompoundTag tag) {
    //? >= 1.21.11
    //private static ExtraData from(ValueInput tag) {
      ExtraData data = new ExtraData();
      data.tag = tag;

      return data;
    }

    //? >= 1.21.11 {
    /*private static ExtraData from(ValueOutput output) {
      ExtraData data = new ExtraData();
      data.output = output;

      return data;
    }
    *///? }

    public <T> void load(ExtraDatum<T> datum) {
      datum.loadFrom(tag);
    }

    public <T> void save(ExtraDatum<T> datum) {
      //? < 1.21.11
      datum.saveTo(tag);
      //? >= 1.21.11
      //datum.saveTo(output);
    }
  }

  protected static class ExtraDatum<T> {
    private final DatumGetterSupplier<T> getter;
    private final DatumSetterSupplier<T> setter;
    private final String name;
    private final Codec<T> CODEC;

    private T value;

    private ExtraDatum(String name, Codec<T> codec, DatumGetterSupplier<T> getter, DatumSetterSupplier<T> setter) {
      this.name = name;
      this.CODEC = codec;
      this.getter = getter;
      this.setter = setter;
    }

    protected static ExtraDatum<Integer> ofInt(String name) {
      return new ExtraDatum<Integer>(name, Codec.INT, (input) -> input::getInt, (input) -> input::putInt);
    }

    protected static ExtraDatum<Boolean> ofBoolean(String name) {
      //? < 1.21.11
      DatumGetterSupplier<Boolean> booleanFromInput = (input) -> input::getBoolean;
      //? >= 1.21.11
      //DatumGetterSupplier<Boolean> booleanFromInput = (input) -> (str) -> Optional.of(input.getBooleanOr(str, false));

      return new ExtraDatum<Boolean>(name, Codec.BOOL, booleanFromInput, (input) -> input::putBoolean);
    }

    protected static ExtraDatum<String> ofString(String name) {
      return new ExtraDatum<String>(name, Codec.STRING, (input) -> input::getString, (input) -> input::putString);
    }

    public ExtraDatum<T> setDefault(T initialValue) {
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
  }

  @FunctionalInterface
      //? < 1.21.11
  protected interface DatumGetterSupplier<T> extends Function<CompoundTag, DatumGetter<T>> {
      //? >= 1.21.11
  //protected interface DatumGetterSupplier<T> extends Function<ValueInput, DatumGetter<T>> {
  }

  @FunctionalInterface
      //? < 1.21.11
  protected interface DatumSetterSupplier<T> extends Function<CompoundTag, DatumSetter<T>> {
      //? >= 1.21.11
  //protected interface DatumSetterSupplier<T> extends Function<ValueOutput, DatumSetter<T>> {
  }

  @FunctionalInterface
      //? <= 1.21.1
  //protected interface DatumGetter<T> extends Function<String, T> {
      //? > 1.21.1
  protected interface DatumGetter<T> extends Function<String, Optional<T>> {
  }

  @FunctionalInterface
  protected interface DatumSetter<T> extends BiConsumer<String, T> {
  }
}

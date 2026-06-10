package fabiofdez.analogclock.block.entity.properties;

import java.util.function.Function;

public class BlockEntityProps {
  public static final PropBuilder<Integer> INT = new PropBuilder<>(BlockEntityProp::ofInt);
  public static final PropBuilder<Boolean> BOOLEAN = new PropBuilder<>(BlockEntityProp::ofBoolean);
  public static final PropBuilder<String> STRING = new PropBuilder<>(BlockEntityProp::ofString);

  public static class PropBuilder<T> {
    final Function<String, BlockEntityProp<T>> builder;

    private PropBuilder(Function<String, BlockEntityProp<T>> builder) {
      this.builder = builder;
    }

    public BlockEntityProp<T> create(String name) {
      return this.builder.apply(name);
    }

    public BlockEntityProp<T> create(String name, T defaultValue) {
      return create(name).setDefault(defaultValue);
    }
  }
}

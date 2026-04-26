package fabiofdez.analogclock;

import fabiofdez.analogclock.entity.AnalogClockFace;
import fabiofdez.analogclock.entity.PendulumEntity;
//? fabric {
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
//? }
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
//? neoforge {
/*import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
*///? }
import java.util.function.Supplier;

public class ModBlockEntities {
  //? if neoforge {
  /*public static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(
      BuiltInRegistries.BLOCK_ENTITY_TYPE,
      AnalogClock.MOD_ID
  );
  *///? }

  public static final Supplier<BlockEntityType<AnalogClockFace>> CLOCK_FACE_ENTITY = register(
      "clock_face",
      AnalogClockFace::new,
      ModBlocks.ANALOG_CLOCK
  );
  public static final Supplier<BlockEntityType<PendulumEntity>> PENDULUM_ENTITY = register(
      "gemstone",
      PendulumEntity::new,
      ModBlocks.AMETHYST_PENDULUM
  );

  //? fabric {
  private static <T extends BlockEntity> Supplier<BlockEntityType<T>> register(String name, FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory, Supplier<Block> block) {
    var blockEntity = Registry.register(
        BuiltInRegistries.BLOCK_ENTITY_TYPE,
        AnalogClock.id(name),
        FabricBlockEntityTypeBuilder.<T>create(entityFactory, block.get()).build()
    );

    return () -> blockEntity;
  }
  //?}

  //? neoforge {
  /*private static <T extends BlockEntity> Supplier<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> entityFactory, DeferredBlock<Block> block) {
    return BLOCK_ENTITY_TYPES.register(name, () -> new BlockEntityType<>(entityFactory, block.get()));
  }

  public static void register(IEventBus eventBus) {
    BLOCK_ENTITY_TYPES.register(eventBus);
  }
  *///?}
}

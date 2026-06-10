package fabiofdez.analogclock;

import fabiofdez.analogclock.block.entity.AnalogClockFace;
import fabiofdez.analogclock.block.entity.LongPendulumEntity;
import fabiofdez.analogclock.block.entity.PendulumEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

//? fabric {
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
//? }

//? neoforge {
/*import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
*///? }

//? if forge {
/*import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
*///? } else {
import net.minecraft.core.registries.BuiltInRegistries;
//? }

public class ModBlockEntities {
  //? !fabric {
  /*public static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(
      //? neoforge
      //BuiltInRegistries.BLOCK_ENTITY_TYPE,
      //? forge
      //ForgeRegistries.BLOCK_ENTITY_TYPES,
      AnalogClock.MOD_ID
  );
  *///? }

  public static final Supplier<BlockEntityType<AnalogClockFace>> CLOCK_FACE_ENTITY = register(
      "clock_face",
      AnalogClockFace::new,
      ModBlocks.ANALOG_CLOCK
  );
  public static final Supplier<BlockEntityType<PendulumEntity>> PENDULUM_ENTITY = register(
      "pendulum",
      PendulumEntity::new,
      ModBlocks.AMETHYST_PENDULUM
  );
  public static final Supplier<BlockEntityType<LongPendulumEntity>> LONG_PENDULUM_ENTITY = register(
      "long_pendulum",
      LongPendulumEntity::new,
      ModBlocks.LONG_AMETHYST_PENDULUM
  );

  //? if fabric {
  private static <T extends BlockEntity> Supplier<BlockEntityType<T>> register(String name, FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory, Supplier<Block> block) {
    BlockEntityType<T> blockEntity = Registry.register(
        BuiltInRegistries.BLOCK_ENTITY_TYPE,
        AnalogClock.id(name),
        FabricBlockEntityTypeBuilder.<T>create(entityFactory, block.get()).build()
    );

    return () -> blockEntity;
  }
  //?} else {
  /*private static <T extends BlockEntity> Supplier<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> entityFactory, BlockSupplier block) {
    //? > 1.21.1
    //Supplier<BlockEntityType<T>> blockEntitySupplier = () -> new BlockEntityType<>(entityFactory, block.get());
    //? <= 1.21.1 {
    Supplier<BlockEntityType<T>> blockEntitySupplier = () -> BlockEntityType.Builder
        .of(entityFactory, block.get())
        .build(null);
    //? }

    return BLOCK_ENTITY_TYPES.register(name, blockEntitySupplier);
  }

  public static void register(IEventBus eventBus) {
    BLOCK_ENTITY_TYPES.register(eventBus);
  }
  *///?}
}

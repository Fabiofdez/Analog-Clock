package fabiofdez.analogclock;

import fabiofdez.analogclock.block.entity.AnalogClockFace;
import fabiofdez.analogclock.block.entity.PendulumEntity;
//? !forge
import net.minecraft.core.registries.BuiltInRegistries;
//? if fabric {
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
//? } else {
/*import net.neoforged.neoforge.registries.DeferredBlock;
*///? }
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
//? neoforge {
/*import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
*///? }
//? forge {
/*import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
*///? }
//? <= 1.21.1
//import java.util.Set;

import java.util.function.Supplier;

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
      "gemstone",
      PendulumEntity::new,
      ModBlocks.AMETHYST_PENDULUM
  );

  //? if fabric {
  private static <T extends BlockEntity> Supplier<BlockEntityType<T>> register(String name, FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory, Supplier<Block> block) {
    var blockEntity = Registry.register(
        BuiltInRegistries.BLOCK_ENTITY_TYPE,
        AnalogClock.id(name),
        FabricBlockEntityTypeBuilder.<T>create(entityFactory, block.get()).build()
    );

    return () -> blockEntity;
  }
  //?} else {
  /*private static <T extends BlockEntity> Supplier<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> entityFactory, DeferredBlock<Block> block) {
    //? > 1.21.1
    Supplier<BlockEntityType<T>> blockEntitySupplier = () -> new BlockEntityType<>(entityFactory, block.get());
    //? <= 1.21.1 {
    /^Supplier<BlockEntityType<T>> blockEntitySupplier = () -> BlockEntityType.Builder
        .of(entityFactory, block.get())
        .build(null);
    ^///? }

    return BLOCK_ENTITY_TYPES.register(name, blockEntitySupplier);
  }

  public static void register(IEventBus eventBus) {
    BLOCK_ENTITY_TYPES.register(eventBus);
  }
  *///?}
}

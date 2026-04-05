package fabiofdez.analogclock;

import fabiofdez.analogclock.entity.AnalogClockFace;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {
  public static final BlockEntityType<AnalogClockFace> CLOCK_FACE_ENTITY = register(
      "clock_face",
      AnalogClockFace::new,
      ModBlocks.ANALOG_CLOCK
  );

  private static <T extends BlockEntity> BlockEntityType<T> register(String name, FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory, Block... blocks) {
    return Registry.register(
        BuiltInRegistries.BLOCK_ENTITY_TYPE,
        AnalogClock.id(name),
        FabricBlockEntityTypeBuilder
            .<T>create(entityFactory, blocks)
            .build()
    );
  }
}

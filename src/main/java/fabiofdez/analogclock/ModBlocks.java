package fabiofdez.analogclock;

import fabiofdez.analogclock.block.AnalogClockBlock;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

public class ModBlocks {
  public static final Block ANALOG_CLOCK = register(
      "analog_clock",
      AnalogClockBlock::new,
      BlockBehaviour.Properties
          .of()
          .sound(SoundType.COPPER)
          .instabreak()
  );

  private static Block register(String name, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties settings) {
    ResourceKey<Block> blockKey = AnalogClock.blockKey(name);
    Block block = blockFactory.apply(settings
        .setId(blockKey)
        .noOcclusion());

    ResourceKey<Item> itemKey = AnalogClock.itemKey(name);
    BlockItem blockItem = new BlockItem(
        block,
        new Item.Properties()
            .setId(itemKey)
            .useBlockDescriptionPrefix()
    );
    Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);

    return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
  }

  public static void initialize() {
    ItemGroupEvents
        .modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
        .register(itemGroup -> itemGroup.accept(ANALOG_CLOCK.asItem()));
  }
}

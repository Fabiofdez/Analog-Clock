package fabiofdez.analogclock;

import fabiofdez.analogclock.block.AnalogClockBlock;
import fabiofdez.analogclock.block.AmethystPendulumBlock;
import fabiofdez.analogclock.block.ClockFaceBlock;
import fabiofdez.analogclock.item.AnalogClockItem;
import fabiofdez.analogclock.item.PendulumItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.BiFunction;
import java.util.function.Function;

//? if fabric {
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.function.Supplier;
//? } else {
/*import net.neoforged.neoforge.registries.DeferredBlock;
 *///? }

//? if neoforge {
/*import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
*///? } else {
import net.minecraft.core.registries.BuiltInRegistries;
//? }

//? forge {
/*import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
*///? }

public class ModBlocks {
  //? if neoforge {
  /*public static DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(AnalogClock.MOD_ID);
  public static DeferredRegister.Items ITEMS = DeferredRegister.createItems(AnalogClock.MOD_ID);
  *///? } else if forge {
  /*public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, AnalogClock.MOD_ID);
  public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AnalogClock.MOD_ID);
  *///? }

  public static final BlockSupplier ANALOG_CLOCK = register(ModBlockBuilder.ANALOG_CLOCK);
  public static final BlockSupplier AMETHYST_PENDULUM = register(ModBlockBuilder.AMETHYST_PENDULUM);
  public static final BlockSupplier INTERNAL_CLOCK_FACE = register(ModBlockBuilder.CLOCK_FACE);

  //? if fabric {
  private static BlockSupplier register(ModBlockBuilder block) {
    ResourceKey<Block> blockKey = AnalogClock.blockKey(block.name);
    Block toRegister = block.blockBuilder.apply(BlockBehaviour.Properties.of() /*? if > 1.21.1 >> ');' */.setId(blockKey));

    ResourceKey<Item> itemKey = AnalogClock.itemKey(block.name);
    Item.Properties itemProps = new Item.Properties() /*? if > 1.21.1 >> ';' */.setId(itemKey);
    BlockItem blockItem = block.itemBuilder.apply(toRegister, itemProps);

    Block registeredBlock = Registry.register(BuiltInRegistries.BLOCK, blockKey, toRegister);
    Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);

    return () -> registeredBlock;
  }
  //? } else {
  /*private static DeferredBlock<Block> register(ModBlockBuilder block) {
    DeferredBlock<Block> registeredBlock =
        //? neoforge
        //BLOCKS.registerBlock(block.name, block.blockBuilder);
        //? forge
        //BLOCKS.register(block.name, () -> block.blockBuilder.apply(BlockBehaviour.Properties.of()));

    //? > 1.21.1
    ITEMS.registerItem(block.name, (props) -> block.itemBuilder.apply(registeredBlock.get(), props));
    //? <= 1.21.1
    //ITEMS.register(block.name, () -> block.itemBuilder.apply(registeredBlock.get(), new Item.Properties()));

    return registeredBlock;
  }

  public static void addCreative(BuildCreativeModeTabContentsEvent event) {
    if (event.getTabKey() != CreativeModeTabs.TOOLS_AND_UTILITIES) return;

    event.accept(ANALOG_CLOCK);
    event.accept(AMETHYST_PENDULUM);
  }
  *///? }

  //? if fabric {
  public static void initialize() {
    ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register((itemGroup) -> {
      itemGroup.accept(ANALOG_CLOCK.get().asItem());
      itemGroup.accept(AMETHYST_PENDULUM.get().asItem());
    });
  }
  //?} else {
  /*public static void register(IEventBus eventBus) {
    BLOCKS.register(eventBus);
    ITEMS.register(eventBus);
  }
  *///? }

  enum ModBlockBuilder {
    ANALOG_CLOCK("analog_clock", AnalogClockBlock::new, AnalogClockItem::new),
    AMETHYST_PENDULUM("amethyst_pendulum", AmethystPendulumBlock::new, PendulumItem::new),
    CLOCK_FACE("clock_face", ClockFaceBlock::new);

    private final String name;
    private final Function<BlockBehaviour.Properties, Block> blockBuilder;
    private final BiFunction<Block, Item.Properties, BlockItem> itemBuilder;

    ModBlockBuilder(String name, Function<BlockBehaviour.Properties, Block> blockBuilder) {
      this(name, blockBuilder, BlockItem::new);
    }

    ModBlockBuilder(String name, Function<BlockBehaviour.Properties, Block> blockBuilder, BiFunction<Block, Item.Properties, BlockItem> itemBuilder) {
      this.name = name;
      this.blockBuilder = blockBuilder;
      this.itemBuilder = itemBuilder;
    }
  }

  //? fabric {
  public interface BlockSupplier extends Supplier<Block> {
  }
  //? }
}

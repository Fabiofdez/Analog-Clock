package fabiofdez.analogclock;

import fabiofdez.analogclock.block.AnalogClockBlock;
import fabiofdez.analogclock.block.AmethystPendulumBlock;
import fabiofdez.analogclock.block.ClockFaceBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

//? fabric {
import fabiofdez.analogclock.item.AnalogClockItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;

import java.util.function.BiFunction;
import java.util.function.Supplier;
//? }

//? neoforge {
/*import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
*///? }

public class ModBlocks {
  //? fabric {
  public static final Supplier<Block> ANALOG_CLOCK = register(ModBlockBuilder.ANALOG_CLOCK);
  public static final Supplier<Block> AMETHYST_PENDULUM = register(ModBlockBuilder.AMETHYST_PENDULUM);
  public static final Supplier<Block> INTERNAL_CLOCK_FACE = register(ModBlockBuilder.CLOCK_FACE);
  //? }

  //? neoforge {
  /*public static DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(AnalogClock.MOD_ID);
  public static DeferredRegister.Items ITEMS = DeferredRegister.createItems(AnalogClock.MOD_ID);

  public static final DeferredBlock<Block> ANALOG_CLOCK = register(ModBlockBuilder.ANALOG_CLOCK);
  public static final DeferredBlock<Block> AMETHYST_PENDULUM = register(ModBlockBuilder.AMETHYST_PENDULUM);
  public static final DeferredBlock<Block> INTERNAL_CLOCK_FACE = register(ModBlockBuilder.CLOCK_FACE);
  *///? }

  //? fabric {
  private static Supplier<Block> register(ModBlockBuilder block) {
    ResourceKey<Block> blockKey = AnalogClock.blockKey(block.name);
    Block toRegister = block.blockBuilder.apply(BlockBehaviour.Properties.of() /*? if > 1.21.1 >> ');' */.setId(blockKey));

    ResourceKey<Item> itemKey = AnalogClock.itemKey(block.name);
    Item.Properties itemProps = new Item.Properties() /*? if > 1.21.1 >> */.setId(itemKey).useBlockDescriptionPrefix();
    BlockItem blockItem = block.itemBuilder.apply(toRegister, itemProps);

    Block registeredBlock = Registry.register(BuiltInRegistries.BLOCK, blockKey, toRegister);
    Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);

    return () -> registeredBlock;
  }
  //? }

  //? neoforge {
  /*private static DeferredBlock<Block> register(ModBlockBuilder block) {
    DeferredBlock<Block> registeredBlock = BLOCKS.registerBlock(block.name, block.blockBuilder);

    //? > 1.21.1
    ITEMS.registerItem(block.name, (props) -> block.itemBuilder.apply(registeredBlock.get(), props.useBlockDescriptionPrefix()));
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

  //? fabric {
  public static void initialize() {
    ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register((itemGroup) -> {
      itemGroup.accept(ANALOG_CLOCK.get().asItem());
      itemGroup.accept(AMETHYST_PENDULUM.get().asItem());
    });
  }
  //?}

  //? neoforge {
  /*public static void register(IEventBus eventBus) {
    BLOCKS.register(eventBus);
    ITEMS.register(eventBus);
  }
  *///? }

  enum ModBlockBuilder {
    ANALOG_CLOCK("analog_clock", AnalogClockBlock::new, AnalogClockItem::new),
    AMETHYST_PENDULUM("amethyst_pendulum", AmethystPendulumBlock::new),
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
}

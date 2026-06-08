package fabiofdez.analogclock;

import fabiofdez.analogclock.block.AnalogClockBlock;
import fabiofdez.analogclock.block.AmethystPendulumBlock;
import fabiofdez.analogclock.block.ClockFaceBlock;
import fabiofdez.analogclock.block.LongAmethystPendulumBlock;
import fabiofdez.analogclock.block.PlacedClockKey;
import fabiofdez.analogclock.item.AnalogClockItem;
import fabiofdez.analogclock.item.PendulumItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.BiFunction;
import java.util.function.Function;

//? fabric {
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.function.Supplier;
//? }

//? neoforge {
/*import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredBlock;
*///? }

//? forge {
/*import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
*///? }

public class ModBlocks {
  //? neoforge
  //public static DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(AnalogClock.MOD_ID);
  //? forge
  //public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, AnalogClock.MOD_ID);

  public static final BlockSupplier ANALOG_CLOCK = register(ModBlockBuilder.ANALOG_CLOCK);
  public static final BlockSupplier AMETHYST_PENDULUM = register(ModBlockBuilder.AMETHYST_PENDULUM);
  public static final BlockSupplier LONG_AMETHYST_PENDULUM = register(ModBlockBuilder.LONG_AMETHYST_PENDULUM);
  public static final BlockSupplier INTERNAL_CLOCK_FACE = register(ModBlockBuilder.CLOCK_FACE);
  public static final BlockSupplier PLACED_CLOCK_KEY = registerBlockOnly(ModBlockBuilder.CLOCK_KEY);

  //? if fabric {
  @SuppressWarnings("SameParameterValue")
  private static BlockSupplier registerBlockOnly(ModBlockBuilder builder) {
    Block registeredBlock = BlockDef.create(builder).register();
    return () -> registeredBlock;
  }

  private static BlockSupplier register(ModBlockBuilder builder) {
    BlockDef toRegister = BlockDef.create(builder);

    ResourceKey<Item> itemKey = AnalogClock.itemKey(builder.name);
    Item.Properties itemProps = new Item.Properties() /*? if > 1.21.1 >> ';' */.setId(itemKey);
    BlockItem blockItem = builder.itemClass.apply(toRegister.block(), itemProps);

    Block registeredBlock = toRegister.register();
    Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);

    return () -> registeredBlock;
  }
  //? } else {
  /*private static DeferredBlock<Block> registerBlockOnly(ModBlockBuilder builder) {
    //? neoforge
    //return BLOCKS.registerBlock(builder.name, builder.blockClass);
    //? forge
    //return BLOCKS.register(builder.name, () -> builder.blockClass.apply(BlockBehaviour.Properties.of()));
  }

  private static DeferredBlock<Block> register(ModBlockBuilder builder) {
    DeferredBlock<Block> registeredBlock = registerBlockOnly(builder);

    //? > 1.21.1
    ModItems.ITEMS.registerItem(builder.name, (props) -> builder.itemClass.apply(registeredBlock.get(), props));
    //? <= 1.21.1
    //ModItems.ITEMS.register(builder.name, () -> builder.itemClass.apply(registeredBlock.get(), new Item.Properties()));

    return registeredBlock;
  }

  public static void register(IEventBus eventBus) {
    BLOCKS.register(eventBus);
  }
  *///? }

  public static void addCreative(AnalogClock.CreativeTabsModifier modifier) {
    modifier.forTab(CreativeModeTabs.TOOLS_AND_UTILITIES).addBlocks((entries) -> {
      entries.accept(ANALOG_CLOCK);
      entries.accept(AMETHYST_PENDULUM);
      entries.accept(LONG_AMETHYST_PENDULUM);
    });
  }

  enum ModBlockBuilder {
    ANALOG_CLOCK("analog_clock", AnalogClockBlock::new, AnalogClockItem::new),
    AMETHYST_PENDULUM("amethyst_pendulum", AmethystPendulumBlock::new, PendulumItem::new),
    LONG_AMETHYST_PENDULUM("long_amethyst_pendulum", LongAmethystPendulumBlock::new, PendulumItem::new),
    CLOCK_FACE("clock_face", ClockFaceBlock::new),
    CLOCK_KEY("placed_clock_key", PlacedClockKey::new);

    private final String name;
    private final Function<BlockBehaviour.Properties, Block> blockClass;
    private final BiFunction<Block, Item.Properties, BlockItem> itemClass;

    ModBlockBuilder(String name, Function<BlockBehaviour.Properties, Block> blockClass) {
      this(name, blockClass, BlockItem::new);
    }

    ModBlockBuilder(String name, Function<BlockBehaviour.Properties, Block> blockClass, BiFunction<Block, Item.Properties, BlockItem> itemClass) {
      this.name = name;
      this.blockClass = blockClass;
      this.itemClass = itemClass;
    }
  }

  //? fabric {
  public interface BlockSupplier extends Supplier<Block> {
  }

  public static class BlockDef {
    private final ResourceKey<Block> key;
    private final Block block;

    private BlockDef(ResourceKey<Block> key, Block block) {
      this.key = key;
      this.block = block;
    }

    static BlockDef create(ModBlockBuilder builder) {
      ResourceKey<Block> blockKey = AnalogClock.blockKey(builder.name);
      Block toRegister = builder.blockClass.apply(BlockBehaviour.Properties.of() /*? if > 1.21.1 >> ');' */.setId(blockKey));

      return new BlockDef(blockKey, toRegister);
    }

    public Block block() {
      return block;
    }

    public Block register() {
      return Registry.register(BuiltInRegistries.BLOCK, key, block);
    }
  }
  //? }

  public static void initialize() {
  }
}

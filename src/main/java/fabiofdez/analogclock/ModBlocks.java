package fabiofdez.analogclock;

import fabiofdez.analogclock.block.AnalogClockBlock;
import fabiofdez.analogclock.block.AmethystPendulumBlock;
//? fabric {
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import java.util.function.Supplier;
//? }
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
//? neoforge {
/*import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
*///? }

import java.util.function.Function;

public class ModBlocks {
  //? fabric {
  public static final Supplier<Block> ANALOG_CLOCK = register(ModBlockBuilder.ANALOG_CLOCK);
  public static final Supplier<Block> AMETHYST_PENDULUM = register(ModBlockBuilder.AMETHYST_PENDULUM);
  //? }

  //? neoforge {
  /*public static DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(AnalogClock.MOD_ID);
  public static DeferredRegister.Items ITEMS = DeferredRegister.createItems(AnalogClock.MOD_ID);

  public static final DeferredBlock<Block> ANALOG_CLOCK = register(ModBlockBuilder.ANALOG_CLOCK);
  public static final DeferredBlock<Block> AMETHYST_PENDULUM = register(ModBlockBuilder.AMETHYST_PENDULUM);
  *///? }

  //? fabric {
  private static Supplier<Block> register(ModBlockBuilder block) {
    ResourceKey<Block> blockKey = AnalogClock.blockKey(block.name);
    Block toRegister = block.builder.apply(BlockBehaviour.Properties.of() /*? if > 1.21.1 >> ');' */ .setId(blockKey));

    ResourceKey<Item> itemKey = AnalogClock.itemKey(block.name);
    BlockItem blockItem = new BlockItem(toRegister, new Item.Properties() /*? if > 1.21.1 >> ');' */ .setId(itemKey).useBlockDescriptionPrefix());

    Block registeredBlock = Registry.register(BuiltInRegistries.BLOCK, blockKey, toRegister);
    Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);

    return () -> registeredBlock;
  }
  //? }

  //? neoforge {
  /*private static DeferredBlock<Block> register(ModBlockBuilder block) {
    DeferredBlock<Block> registeredBlock = BLOCKS.registerBlock(block.name, block.builder);

    //? > 1.21.1
    ITEMS.registerItem(block.name, (props) -> new BlockItem(registeredBlock.get(), props.useBlockDescriptionPrefix()));
    //? <= 1.21.1
    //ITEMS.register(block.name, () -> new BlockItem(registeredBlock.get(), new Item.Properties()));

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
    ANALOG_CLOCK(
        "analog_clock",
        (props) -> new AnalogClockBlock(props
            .sound(SoundType.COPPER)
            .instabreak()
            .pushReaction(PushReaction.DESTROY)
            .noOcclusion())
    ),

    AMETHYST_PENDULUM(
        "amethyst_pendulum",
        (props) -> new AmethystPendulumBlock(props
            .sound(SoundType.COPPER)
            .instabreak()
            .pushReaction(PushReaction.DESTROY)
            .noOcclusion())
    );

    private final String name;
    private final Function<BlockBehaviour.Properties, Block> builder;

    ModBlockBuilder(String name, Function<BlockBehaviour.Properties, Block> builder) {
      this.name = name;
      this.builder = builder;
    }
  }
}

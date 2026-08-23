package fabiofdez.analogclock;

import fabiofdez.analogclock.platform.Platform;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

//? !fabric
//import java.util.function.Supplier;

//? fabric {
import fabiofdez.analogclock.platform.fabric.FabricPlatform;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
//?} neoforge {
/*import fabiofdez.analogclock.platform.neoforge.NeoforgePlatform;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
    *///?} forge {
/*import fabiofdez.analogclock.platform.forge.ForgePlatform;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
 *///? }

@SuppressWarnings("LoggingSimilarMessage")
public class AnalogClock {

  public static final String MOD_ID = /*$ mod_id*/ "analogclock";
  public static final String MOD_VERSION = /*$ mod_version*/ "1.5.1";
  public static final String MOD_FRIENDLY_NAME = /*$ mod_name*/ "Analog";
  public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

  private static final Platform PLATFORM = createPlatformInstance();

  public static void onInitialize() {
    LOGGER.info("Initializing {} on {}", MOD_ID, AnalogClock.platform().loader());
    LOGGER.debug("{}: { version: {}; friendly_name: {} }", MOD_ID, MOD_VERSION, MOD_FRIENDLY_NAME);
  }

  public static void onInitializeClient() {
    LOGGER.info("Initializing {} Client on {}", MOD_ID, AnalogClock.platform().loader());
    LOGGER.debug("{}: { version: {}; friendly_name: {} }", MOD_ID, MOD_VERSION, MOD_FRIENDLY_NAME);
  }

  static Platform platform() {
    return PLATFORM;
  }

  private static Platform createPlatformInstance() {
    //? fabric {
    return new FabricPlatform();
     //?} neoforge {
    /*return new NeoforgePlatform();
    *///?} forge {
    /*return new ForgePlatform();
     *///? }
  }

  public static ResourceLocation id(String path) {
    //? >= 1.21
    return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    //? < 1.21
    //return new ResourceLocation(MOD_ID, path);
  }

  public static String tooltipTranslatable(Item item, String tooltipName) {
    return String.format("%s.tooltip.%s", item.getDescriptionId(), tooltipName);
  }

  public static String itemEventTranslatable(Item item, String eventName) {
    return String.format("%s.events.%s", item.getDescriptionId(), eventName);
  }

  //? fabric {
  public static ResourceKey<Block> blockKey(String path) {
    return ResourceKey.create(Registries.BLOCK, id(path));
  }

  public static ResourceKey<Item> itemKey(String path) {
    return ResourceKey.create(Registries.ITEM, id(path));
  }
  //?}

  public static void modifyCreativeTabs(/*? if !fabric >> 'Consumer' */ /*BuildCreativeModeTabContentsEvent event, */Consumer<CreativeTabsModifier> runnable) {
    //? fabric
    runnable.accept(new CreativeTabsModifier());
    //? !fabric
    //runnable.accept(new CreativeTabsModifier(event));
  }

  public static class CreativeTabsModifier {
    private ResourceKey<CreativeModeTab> currentTab;

    public CreativeTabsModifier forTab(ResourceKey<CreativeModeTab> tab) {
      this.currentTab = tab;
      return this;
    }

    public CreativeTabsModifier addItems(ItemEntryModifier entryModifier) {
      //? fabric
      return addEntries((entries) -> entryModifier.accept((item) -> entries.accept(item.get())));
      //? !fabric
      //return addEntries(entryModifier);
    }

    public CreativeTabsModifier addBlocks(BlockEntryModifier entryModifier) {
      //? fabric
      return addEntries((entries) -> entryModifier.accept((item) -> entries.accept(item.get())));
      //? !fabric
      //return addEntries(entryModifier);
    }

    //? if fabric {
    private CreativeTabsModifier addEntries(ItemGroupEvents.ModifyEntries entryModifier) {
      if (currentTab == null) return this;
      ItemGroupEvents.modifyEntriesEvent(currentTab).register(entryModifier);
      return this;
    }

    public interface ItemEntryModifier extends Consumer<Consumer<ModItems.ItemSupplier>> {
    }

    public interface BlockEntryModifier extends Consumer<Consumer<ModBlocks.BlockSupplier>> {
    }
    //? } else {
    /*private BuildCreativeModeTabContentsEvent event;

    public CreativeTabsModifier(BuildCreativeModeTabContentsEvent event) {
      this.event = event;
    }

    private CreativeTabsModifier addEntries(Consumer<BuildCreativeModeTabContentsEvent> entryModifier) {
      if (this.event == null || this.currentTab == null) return this;
      if (this.event.getTabKey() != this.currentTab) return this;

      entryModifier.accept(this.event);
      return this;
    }

    public interface ItemEntryModifier extends Consumer<BuildCreativeModeTabContentsEvent> {
    }

    public interface BlockEntryModifier extends Consumer<BuildCreativeModeTabContentsEvent> {
    }
    *///? }
  }
}

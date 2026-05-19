package fabiofdez.analogclock;

import fabiofdez.analogclock.platform.Platform;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//? fabric {
import net.minecraft.world.level.block.Block;
import fabiofdez.analogclock.platform.fabric.FabricPlatform;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
//?} neoforge {
/*import fabiofdez.analogclock.platform.neoforge.NeoforgePlatform;
 *///?} forge {
/*import fabiofdez.analogclock.platform.forge.ForgePlatform;
 *///? }

@SuppressWarnings("LoggingSimilarMessage")
public class AnalogClock {

  public static final String MOD_ID = /*$ mod_id*/ "analogclock";
  public static final String MOD_VERSION = /*$ mod_version*/ "1.4.0";
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

  //? fabric {
  public static ResourceKey<Block> blockKey(String path) {
    return ResourceKey.create(Registries.BLOCK, id(path));
  }

  public static ResourceKey<Item> itemKey(String path) {
    return ResourceKey.create(Registries.ITEM, id(path));
  }
  //?}
}

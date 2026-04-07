package fabiofdez.analogclock;

import net.fabricmc.api.ModInitializer;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnalogClock implements ModInitializer {
  public static final String MOD_ID = "analog-clock";

  public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

  @Override
  public void onInitialize() {
    ModBlocks.initialize();
    LOGGER.info("[{}] Analog Initialized!", MOD_ID);
  }

  public static ResourceLocation id(String path) {
    return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
  }

  public static ResourceKey<Block> blockKey(String path) {
    return ResourceKey.create(Registries.BLOCK, id(path));
  }

  public static ResourceKey<Item> itemKey(String path) {
    return ResourceKey.create(Registries.ITEM, id(path));
  }
}
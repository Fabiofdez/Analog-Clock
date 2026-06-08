package fabiofdez.analogclock;

import fabiofdez.analogclock.item.ClockKeyItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

import java.util.function.Function;

//? fabric {
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.function.Supplier;
//? }

//? if neoforge {
/*import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredItem;
*///? } else {
import net.minecraft.core.registries.BuiltInRegistries;
//? }

//? forge {
/*import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
*///? }

public class ModItems {
  //? neoforge
  //public static DeferredRegister.Items ITEMS = DeferredRegister.createItems(AnalogClock.MOD_ID);
  //? forge
  //public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AnalogClock.MOD_ID);

  public static final ItemSupplier CLOCK_KEY = register("clock_key", ClockKeyItem::new);

  //? if fabric {
  private static ItemSupplier register(String name, Function<Item.Properties, Item> itemBuilder) {
    ResourceKey<Item> itemKey = AnalogClock.itemKey(name);
    Item.Properties itemProps = new Item.Properties() /*? if > 1.21.1 >> ';' */.setId(itemKey);

    Item item = itemBuilder.apply(itemProps);
    Registry.register(BuiltInRegistries.ITEM, itemKey, item);

    return () -> item;
  }
  //? } else {
  /*private static DeferredItem<Item> register(String name, Function<Item.Properties, Item> itemBuilder) {
    //? neoforge
     //return ITEMS.registerItem(name, itemBuilder);
    //? forge
    //return ITEMS.register(name, () -> itemBuilder.apply(new Item.Properties()));
  }

  public static void register(IEventBus eventBus) {
    ITEMS.register(eventBus);
  }
  *///? }

  public static void addCreative(AnalogClock.CreativeTabsModifier modifier) {
    modifier.forTab(CreativeModeTabs.TOOLS_AND_UTILITIES).addItems((entries) -> {
      entries.accept(CLOCK_KEY);
    });
  }

  //? fabric {
  public interface ItemSupplier extends Supplier<Item> {
  }
  //? }

  public static void initialize() {
  }
}

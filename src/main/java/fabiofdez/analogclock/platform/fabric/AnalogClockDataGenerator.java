package fabiofdez.analogclock.platform.fabric;

//? fabric {

import fabiofdez.analogclock.platform.fabric.datagen.ModLangProvider;
import fabiofdez.analogclock.platform.fabric.datagen.ModLootTableProvider;
import fabiofdez.analogclock.platform.fabric.datagen.ModRecipeProvider;
//? > 1.21.1
import fabiofdez.analogclock.platform.fabric.datagen.ModSoundsProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class AnalogClockDataGenerator implements DataGeneratorEntrypoint {
  @Override
  public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
    FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

    pack.addProvider(ModLangProvider::new);
    pack.addProvider(ModRecipeProvider::new);
    pack.addProvider(ModLootTableProvider::new);
    //? > 1.21.1
    pack.addProvider(ModSoundsProvider::new);
  }
}
//?}

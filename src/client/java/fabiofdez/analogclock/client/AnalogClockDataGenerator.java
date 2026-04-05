package fabiofdez.analogclock.client;

import fabiofdez.analogclock.client.datagen.ModLangProvider;
import fabiofdez.analogclock.client.datagen.ModRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class AnalogClockDataGenerator implements DataGeneratorEntrypoint {
  @Override
  public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
    FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

    pack.addProvider(ModLangProvider::new);
    pack.addProvider(ModRecipeProvider::new);
  }
}

package fabiofdez.analogclock.platform.fabric.datagen;

//? fabric {

import fabiofdez.analogclock.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
  public ModLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
    super(dataOutput, registryLookup);
  }

  @Override
  public void generate() {
    dropSelf(ModBlocks.ANALOG_CLOCK.get());
    dropSelf(ModBlocks.AMETHYST_PENDULUM.get());
  }
}
//?}

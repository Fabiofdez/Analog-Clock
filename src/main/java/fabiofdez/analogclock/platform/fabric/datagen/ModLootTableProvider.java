package fabiofdez.analogclock.platform.fabric.datagen;

//? fabric {

import fabiofdez.analogclock.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
//? > 1.21
import net.minecraft.core.HolderLookup;

//? > 1.21
import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
  public ModLootTableProvider(FabricDataOutput dataOutput/*? if > 1.21 >> ') {'*/, CompletableFuture<HolderLookup.Provider> registryLookup) {
    super(dataOutput/*? if > 1.21 >> ');'*/, registryLookup);
  }

  @Override
  public void generate() {
    dropSelf(ModBlocks.ANALOG_CLOCK.get());
    dropSelf(ModBlocks.AMETHYST_PENDULUM.get());
  }
}
//?}

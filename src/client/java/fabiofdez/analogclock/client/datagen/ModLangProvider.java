package fabiofdez.analogclock.client.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class ModLangProvider extends FabricLanguageProvider {
  public ModLangProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
    super(dataOutput, registryLookup);
  }

  @Override
  public void generateTranslations(HolderLookup.Provider provider, TranslationBuilder translationBuilder) {
    translationBuilder.add("block.analog-clock.analog_clock", "Analog Clock");
  }
}

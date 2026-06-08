package fabiofdez.analogclock.mixin;

import fabiofdez.analogclock.util.IdPatches;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ResourceLocation.class)
public class IdRemapMixin {

  @Inject(method = "tryParse", at = @At("RETURN"), cancellable = true)
  private static void AnalogClock$remapId(String string, CallbackInfoReturnable<ResourceLocation> cir) {
    ResourceLocation id = cir.getReturnValue();

    if (id == null) return;
    if (IdPatches.outsideMod(id) || IdPatches.upToDate(id)) return;

    cir.setReturnValue(IdPatches.update(id));
  }
}

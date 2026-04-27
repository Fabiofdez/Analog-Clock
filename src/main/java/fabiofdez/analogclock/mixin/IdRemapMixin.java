package fabiofdez.analogclock.mixin;

import fabiofdez.analogclock.AnalogClock;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ResourceLocation.class)
public class IdRemapMixin {

  @Inject(method = "tryParse", at = @At("RETURN"), cancellable = true)
  private static void remap(String string, CallbackInfoReturnable<ResourceLocation> cir) {
    ResourceLocation original = cir.getReturnValue();
    if (original == null || !original.getNamespace().equals("analog-clock")) return;

    cir.setReturnValue(AnalogClock.id(original.getPath()));
  }
}

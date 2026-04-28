package fabiofdez.analogclock.mixin;

import fabiofdez.analogclock.AnalogClock;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

// This mixin is a class that works as a data-fixer.
// Upon loading a world it will check for missing objects and replace them
// to prevent disappearing blocks, items, etc. when upgrading from older worlds
@Mixin(MappedRegistry.class)
public abstract class RegistryRemapMixin<T> {

  @Shadow
  //? > 1.21.1
  public abstract Optional<Holder.Reference<T>> get(ResourceLocation id);
  //? <= 1.21.1
  //public abstract Optional<Holder.Reference<T>> getHolder(ResourceLocation id);

  //? > 1.21.1
  @Inject(method = "get(Lnet/minecraft/resources/ResourceLocation;)Ljava/util/Optional;", at = @At("HEAD"), cancellable = true)
  //? <= 1.21.1
  //@Inject(method = "getHolder(Lnet/minecraft/resources/ResourceLocation;)Ljava/util/Optional;", at = @At("HEAD"), cancellable = true)
  private void fixMissingFromRegistry(ResourceLocation id, CallbackInfoReturnable<Optional<Holder.Reference<T>>> cir) {
    if (id == null || !id.getNamespace().equals("analog-clock")) return;
    //? > 1.21.1
    cir.setReturnValue(this.get(AnalogClock.id(id.getPath())));
    //? <= 1.21.1
    //cir.setReturnValue(this.getHolder(AnalogClock.id(id.getPath())));
  }
}

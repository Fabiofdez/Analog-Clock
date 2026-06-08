package fabiofdez.analogclock.mixin;

import fabiofdez.analogclock.util.IdPatches;
import net.minecraft.core.MappedRegistry;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//? > 1.21 {
import net.minecraft.core.Holder;

import java.util.Optional;
//? }

// This mixin is a class that works as a data-fixer.
// Upon loading a world it will check for missing objects and replace them
// to prevent disappearing blocks, items, etc. when upgrading from older worlds
@Mixin(MappedRegistry.class)
public abstract class RegistryRemapMixin<T> {

  @Shadow
      //? if < 1.21 {
  /*public abstract T get(ResourceLocation id);
   *///? } else if <= 1.21.1 {
  /*public abstract Optional<Holder.Reference<T>> getHolder(ResourceLocation id);
   *///? } else if > 1.21.1 {
  public abstract Optional<Holder.Reference<T>> get(ResourceLocation id);
  //? }

  //? if < 1.21 {
  /*@Inject(method = "get(Lnet/minecraft/resources/ResourceLocation;)Ljava/lang/Object;", at = @At("HEAD"), cancellable = true)
  private void fixMissingFromRegistry(ResourceLocation id, CallbackInfoReturnable<T> cir) {
    *///? } else if <= 1.21.1 {
  /*@Inject(method = "getHolder(Lnet/minecraft/resources/ResourceLocation;)Ljava/util/Optional;", at = @At("HEAD"), cancellable = true)
  private void fixMissingFromRegistry(ResourceLocation id, CallbackInfoReturnable<Optional<Holder.Reference<T>>> cir) {
   *///? } else if > 1.21.1 {
  @Inject(method = "get(Lnet/minecraft/resources/ResourceLocation;)Ljava/util/Optional;", at = @At("HEAD"), cancellable = true)
  private void fixMissingFromRegistry(ResourceLocation id, CallbackInfoReturnable<Optional<Holder.Reference<T>>> cir) {
    //? }

    if (id == null) return;
    if (IdPatches.outsideMod(id) || IdPatches.upToDate(id)) return;

    //? if < 1.21 || > 1.21.1 {
    cir.setReturnValue(this.get(IdPatches.update(id)));
    //? } else {
    /*cir.setReturnValue(this.getHolder(IdPatches.update(id)));
     *///? }
  }
}

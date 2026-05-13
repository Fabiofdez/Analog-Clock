package fabiofdez.analogclock.mixin;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BellBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BellBlockEntity.class)
public class BellEntityMixin {

  @Shadow
  public int ticks;

  @Shadow
  public boolean shaking;

  @Shadow
  public Direction clickDirection;

  @Shadow
  private List<LivingEntity> nearbyEntities;

  @Inject(method = "triggerEvent", at = @At("HEAD"), cancellable = true)
  private void analogClock$triggerEvent(int i, int j, CallbackInfoReturnable<Boolean> cir) {
    if (i != 2) return;

    this.nearbyEntities = List.of();
    this.clickDirection = Direction.from3DDataValue(j);
    this.ticks = 0;
    this.shaking = true;

    cir.setReturnValue(true);
  }
}

package fabiofdez.analogclock.mixin;

import fabiofdez.analogclock.ModBlocks;
import fabiofdez.analogclock.block.AnalogClockBlock;
import fabiofdez.analogclock.color.ClockFaceStyle;
import net.minecraft.world.item.BrushItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(BrushItem.class)
public class BrushItemMixin {
  @ModifyArg(method = """
      onUseTick(\
        Lnet/minecraft/world/level/Level;\
        Lnet/minecraft/world/entity/LivingEntity;\
        Lnet/minecraft/world/item/ItemStack;\
        I\
      )V""",

      at = @At(value = "INVOKE", target = """
          Lnet/minecraft/world/item/BrushItem;spawnDustParticles(\
            Lnet/minecraft/world/level/Level;\
            Lnet/minecraft/world/phys/BlockHitResult;\
            Lnet/minecraft/world/level/block/state/BlockState;\
            Lnet/minecraft/world/phys/Vec3;\
            Lnet/minecraft/world/entity/HumanoidArm;\
          )V"""), index = 2)
  private BlockState replaceAnalogClockState(BlockState state) {
    if (!state.is(ModBlocks.ANALOG_CLOCK.get())) return state;

    ClockFaceStyle.DyeColor tint = state.getValue(AnalogClockBlock.FACE_TINT);
    if (tint == ClockFaceStyle.FACE_NO_DYE) return Blocks.AIR.defaultBlockState();

    BlockState newState = ModBlocks.INTERNAL_CLOCK_FACE.get().defaultBlockState();
    return newState.setValue(AnalogClockBlock.FACE_TINT, tint);
  }
}

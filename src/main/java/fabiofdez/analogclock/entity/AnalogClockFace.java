package fabiofdez.analogclock.entity;

import fabiofdez.analogclock.ModBlockEntities;
import fabiofdez.analogclock.util.ClockHandsInterpolator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AnalogClockFace extends BlockEntity {
  public static final long DAY_LENGTH_TICKS = 24000;
  public static final long HALF_DAY_LENGTH_TICKS = DAY_LENGTH_TICKS / 2;
  public static final long HOUR_LENGTH_TICKS = DAY_LENGTH_TICKS / 24;
  public static final long SUNRISE_TICK_OFFSET = 6000; // 6AM = dayTime 0

  public static final int CLOCK_HAND_FRAMES = 24;
  public static final int UNIT_HOUR_FRAMES = 2;
  public static final int HOUR_FRAMES_RADIX = 12;
  public static final int NUM_CLOCK_FRAMES = CLOCK_HAND_FRAMES * HOUR_FRAMES_RADIX;

  private static final int INITIAL_CLOCK_FRAME = 3 * UNIT_HOUR_FRAMES * HOUR_FRAMES_RADIX; // 3:00

  private final ClockHandsInterpolator ANIMATOR;
  private int currentFrame;

  public AnalogClockFace(BlockPos blockPos, BlockState blockState) {
    super(ModBlockEntities.CLOCK_FACE_ENTITY, blockPos, blockState);

    currentFrame = INITIAL_CLOCK_FRAME;
    ANIMATOR = new ClockHandsInterpolator();
  }

  public static void tick(Level level, BlockPos pos, BlockState state, BlockEntity entity) {
    if (level.isClientSide || !(entity instanceof AnalogClockFace clockFace)) return;

    if (level.dimension() != Level.OVERWORLD) return; // TODO: chaotic wobble?

    int nextFrame = getNextFrame(level, clockFace);
    if (nextFrame == clockFace.currentFrame) return;

    if (clockFace.ANIMATOR.inProgress()) playWindUpTick(clockFace, level, pos);
    clockFace.currentFrame = nextFrame;
    setChanged(level, pos, state);

    ((ServerLevel) level)
        .getChunkSource()
        .blockChanged(pos);
  }

  public int getHourFrame() {
    return (currentFrame / HOUR_FRAMES_RADIX) % NUM_CLOCK_FRAMES;
  }

  public int getMinuteFrame() {
    int hourPart = (currentFrame / HOUR_FRAMES_RADIX) % UNIT_HOUR_FRAMES;
    return (currentFrame % HOUR_FRAMES_RADIX) + (hourPart * HOUR_FRAMES_RADIX);
  }

  private static int getNextFrame(Level level, AnalogClockFace clockFace) {
    ClockHandsInterpolator animator = clockFace.ANIMATOR;
    long dayTime = level.getDayTime();
    int nextFrame;

    if (animator.inProgress()) {
      if (animator.progress() >= 0.75F) {
        animator.interp(clockFace.currentFrame, toClockFrame(dayTime));
      }
      return animator.step();
    }

    nextFrame = toClockFrame(dayTime);

    if (!animator.isInitialized()) {
      animator.interp(clockFace.currentFrame, nextFrame);
      return animator.step();
    }

    return nextFrame;
  }

  private static int toClockFrame(long dayTime) {
    long clockTime = (dayTime + SUNRISE_TICK_OFFSET) % HALF_DAY_LENGTH_TICKS;
    int frameOffset = Math.toIntExact((clockTime * CLOCK_HAND_FRAMES) / HOUR_LENGTH_TICKS);

    return frameOffset % NUM_CLOCK_FRAMES;
  }

  private static void playWindUpTick(AnalogClockFace clockFace, Level level, BlockPos pos) {
    if (level == null || clockFace == null) return;

    float chance = level
        .getRandom()
        .nextFloat();

    if (chance / clockFace.ANIMATOR.speed() > 0.5F) return;

    float pitch = chance >= 0.75F ? 1.2F : 1.4F;
    level.playSound(null, pos, SoundEvents.SPYGLASS_USE, SoundSource.BLOCKS, 0.5F, pitch);
  }

  @Override
  protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
    if (ANIMATOR.isInitialized()) {
      nbt.putInt("clockFrame", currentFrame);
    }

    super.saveAdditional(nbt, registryLookup);
  }

  @Override
  protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
    super.loadAdditional(nbt, registryLookup);

    nbt
        .getInt("clockFrame")
        .ifPresent((num) -> currentFrame = num);
  }

  @Override
  public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider provider) {
    return saveWithoutMetadata(provider);
  }

  @Override
  public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }
}


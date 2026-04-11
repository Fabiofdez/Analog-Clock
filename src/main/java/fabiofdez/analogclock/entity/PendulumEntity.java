package fabiofdez.analogclock.entity;

import fabiofdez.analogclock.ModBlockEntities;
import fabiofdez.analogclock.block.AmethystPendulumBlock;
import fabiofdez.analogclock.color.GemstoneColor;
import fabiofdez.analogclock.util.FrameInterpolator;
import fabiofdez.analogclock.util.GravityInterpolator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PendulumEntity extends BlockEntity {
  public static final int NUM_DAY_SUBPHASES = 24;
  public static final long TICKS_PER_DAY_SUBPHASE = AnalogClockFace.DAY_LENGTH_TICKS / NUM_DAY_SUBPHASES;

  public static final int NUM_PENDULUM_FRAMES = 12;
  public static final int PENDULUM_FRAME_TICKS = 3;
  public static final int TICKS_PER_PENDULUM_PERIOD = NUM_PENDULUM_FRAMES * PENDULUM_FRAME_TICKS;
  public static final int PENDULUM_HALF_PHASE = NUM_PENDULUM_FRAMES / 2;

  private final GravityInterpolator SWING_INTERPOLATOR;
  private final PhaseTintInterpolator COLOR_PHASE_ANIMATOR;
  private int swingFrameOffset = -1;
  private int currentSwingFrame = 0;
  private int currentColorPhase = 0;

  private int alternateTint = GemstoneColor.NO_COLOR;
  private boolean inOverworld = true;
  private boolean swinging = true;

  public PendulumEntity(BlockPos pos, BlockState state) {
    super(ModBlockEntities.PENDULUM_ENTITY, pos, state);

    SWING_INTERPOLATOR = new GravityInterpolator();
    COLOR_PHASE_ANIMATOR = new PhaseTintInterpolator();
  }

  public static void tick(Level level, BlockPos pos, BlockState state, BlockEntity entity) {
    if (level.isClientSide || !(entity instanceof PendulumEntity pendulum)) return;
    if (!(state.getBlock() instanceof AmethystPendulumBlock)) return;

    long dayTime = level.getDayTime();
    if (pendulum.swingFrameOffset < 0) initSwingOffset(pendulum, dayTime);

    int nextColorPhase;
    if (level.dimension() == Level.OVERWORLD) {
      nextColorPhase = calculateNextColorPhase(dayTime, pendulum);
      pendulum.inOverworld = true;
    } else {
      nextColorPhase = getRandomColorPhase(level, pendulum);
      pendulum.inOverworld = false;
    }

    int nextSwingFrame = calculateNextSwingFrame(level, dayTime, pendulum);
    if (!pendulum.differentFrom(nextSwingFrame, nextColorPhase)) return;

    if (pendulum.currentSwingFrame != nextSwingFrame && phaseExtreme(nextSwingFrame)) {
      playTickTock(pendulum, level, pos);
    }

    pendulum.currentSwingFrame = nextSwingFrame;
    pendulum.currentColorPhase = nextColorPhase;
    setChanged(level, pos, state);

    ((ServerLevel) level).getChunkSource().blockChanged(pos);
  }

  public int getSwingFrame() {
    return currentSwingFrame;
  }

  public int getColorPhase() {
    return currentColorPhase;
  }

  public boolean inOverworld() {
    return inOverworld;
  }

  public int getAlternateTint() {
    return alternateTint;
  }

  private boolean settled() {
    return settled(currentSwingFrame);
  }

  private static int calculateNextSwingFrame(Level level, long dayTime, PendulumEntity pendulum) {
    GameRules rules = ((ServerLevel) level).getGameRules();
    boolean doDaylightCycle = rules.getRule(GameRules.RULE_DAYLIGHT).get();

    if (doDaylightCycle) {
      pendulum.swinging = true;
      return toSwingFrame(dayTime - pendulum.swingFrameOffset);
    }

    if (pendulum.swinging || !pendulum.settled()) {
      pendulum.swingFrameOffset = -1;
      return stopSwinging(pendulum);
    }

    return pendulum.currentSwingFrame;
  }

  private static int stopSwinging(PendulumEntity pendulum) {
    GravityInterpolator animator = pendulum.SWING_INTERPOLATOR;

    if (!animator.isInitialized() || !animator.inProgress()) {
      animator.interp(pendulum.currentSwingFrame);
    }

    int nextFrame = animator.step();
    if (!animator.inProgress()) pendulum.swinging = false;

    return nextFrame;
  }

  private static int calculateNextColorPhase(long dayTime, PendulumEntity pendulum) {
    PhaseTintInterpolator animator = pendulum.COLOR_PHASE_ANIMATOR;
    if (animator.inProgress()) return animator.step();

    int nextPhase = toColorPhase(dayTime);
    if (!animator.isInitialized()) {
      animator.interp(pendulum.currentColorPhase, nextPhase);
      return animator.step();
    }

    return nextPhase;
  }

  private static int getRandomColorPhase(Level level, PendulumEntity pendulum) {
    PhaseTintInterpolator animator = pendulum.COLOR_PHASE_ANIMATOR;
    if (animator.inProgress()) return animator.step();

    RandomSource rand = level.getRandom();
    int nextPhase = rand.nextInt(0, NUM_DAY_SUBPHASES) % NUM_DAY_SUBPHASES;
    animator.interp(pendulum.currentColorPhase, nextPhase);
    nextPhase = animator.step();

    if (level.dimension() == Level.NETHER) {
      pendulum.alternateTint = GemstoneColor.getNetherColor(nextPhase);
    } // TODO: tint for End dimension?

    return nextPhase;
  }

  private static void initSwingOffset(PendulumEntity pendulum, long dayTime) {
    pendulum.swingFrameOffset = Math.toIntExact(dayTime % TICKS_PER_PENDULUM_PERIOD);
  }

  private static boolean settled(int swingFrame) {
    return swingFrame == (PENDULUM_HALF_PHASE / 2);
  }

  private static boolean phaseExtreme(int swingFrame) {
    return swingFrame == 0 || swingFrame == PENDULUM_HALF_PHASE;
  }

  private static int toSwingFrame(long offsetDayTime) {
    long frameTime = offsetDayTime / PENDULUM_FRAME_TICKS;
    return Math.floorMod(frameTime, NUM_PENDULUM_FRAMES);
  }

  private static int toColorPhase(long dayTime) {
    return (int) (dayTime / TICKS_PER_DAY_SUBPHASE) % NUM_DAY_SUBPHASES;
  }

  private static void playTickTock(PendulumEntity pendulum, Level level, BlockPos pos) {
    float volume = 0.04F; // very quiet
    float pitch = pendulum.currentSwingFrame == 0 ? 50F : 25F;
    level.playSound(null, pos, SoundEvents.SHIELD_BLOCK.value(), SoundSource.BLOCKS, volume, pitch);
  }

  private boolean differentFrom(int swingFrame, int colorPhase) {
    return swingFrame != currentSwingFrame || colorPhase != currentColorPhase;
  }

  @Override
  protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
    nbt.putBoolean("swinging", swinging);
    nbt.putBoolean("inOverworld", inOverworld);
    nbt.putInt("alternateTint", alternateTint);
    nbt.putInt("swingOffset", swingFrameOffset);
    nbt.putInt("swingFrame", currentSwingFrame);

    if (COLOR_PHASE_ANIMATOR.isInitialized()) {
      nbt.putInt("colorPhase", currentColorPhase);
    }

    super.saveAdditional(nbt, registryLookup);
  }

  @Override
  protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
    super.loadAdditional(nbt, registryLookup);

    nbt.getBoolean("swinging").ifPresent((val) -> swinging = val);
    nbt.getBoolean("inOverworld").ifPresent((val) -> inOverworld = val);
    nbt.getInt("alternateTint").ifPresent((val) -> alternateTint = val);
    nbt.getInt("swingOffset").ifPresent((num) -> swingFrameOffset = num);
    nbt.getInt("swingFrame").ifPresent((num) -> currentSwingFrame = num);
    nbt.getInt("colorPhase").ifPresent((num) -> currentColorPhase = num);
  }

  @Override
  public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider provider) {
    return saveWithoutMetadata(provider);
  }

  @Override
  public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

  static class PhaseTintInterpolator extends FrameInterpolator {
    public PhaseTintInterpolator() {
      super(new Config(0.5F, 2F, 60, NUM_DAY_SUBPHASES));
    }
  }
}

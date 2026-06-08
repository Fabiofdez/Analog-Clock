package fabiofdez.analogclock.block.entity;

import fabiofdez.analogclock.ModBlockEntities;
import fabiofdez.analogclock.ModSounds;
import fabiofdez.analogclock.block.AmethystPendulumBlock;
import fabiofdez.analogclock.color.GemstoneColor;
import fabiofdez.analogclock.util.FrameInterpolator;
import fabiofdez.analogclock.util.GravityInterpolator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class PendulumEntity extends BaseBlockEntity {
  public static final int NUM_DAY_SUBPHASES = 24;
  public static final long DAY_SUBPHASE_TICKS = AnalogClockFace.DAY_LENGTH_TICKS / NUM_DAY_SUBPHASES;
  public static final long CHIME_PHASE_TICKS = AnalogClockFace.HOUR_LENGTH_TICKS * 6;

  public static final int NUM_PENDULUM_FRAMES = 12;
  public static final int PENDULUM_FRAME_TICKS = 3;
  public static final int PENDULUM_PERIOD_TICKS = NUM_PENDULUM_FRAMES * PENDULUM_FRAME_TICKS;
  public static final int PENDULUM_HALF_PHASE = NUM_PENDULUM_FRAMES / 2;
  public static final int PENDULUM_LOWEST_FRAME = PENDULUM_HALF_PHASE / 2;

  public static final int CHIME_BEAT_TICKS = PENDULUM_FRAME_TICKS * 4;
  public static final int CHIME_DURATION_TICKS = 4 * CHIME_BEAT_TICKS;

  // Chime Notes
  // (pitch multiplier relative to G)
  private static final float G = 1F;
  private static final float C = 1.33F;
  private static final float D = 1.5F;
  private static final float E = 1.68F;

  private final ExtraDatum<Integer> SWING_FRAME_OFFSET = ExtraDatum.ofInt("swingOffset").setDefault(-1);
  private final ExtraDatum<Integer> CURRENT_SWING_FRAME = ExtraDatum.ofInt("swingFrame").setDefault(0);
  private final ExtraDatum<Boolean> SWINGING = ExtraDatum.ofBoolean("swinging").setDefault(true);

  private final ExtraDatum<Integer> CURRENT_COLOR_PHASE = ExtraDatum.ofInt("colorPhase").setDefault(0);
  private final ExtraDatum<Boolean> IN_OVERWORLD = ExtraDatum.ofBoolean("inOverworld").setDefault(true);
  private final ExtraDatum<Integer> ALTERNATE_TINT = ExtraDatum.ofInt("alternateTint");

  private final GravityInterpolator SWING_ANIMATOR;
  private final PhaseTintInterpolator COLOR_PHASE_ANIMATOR;

  private boolean isChimeRinging = false;
  private int chimeTicks = 0;

  enum ChimeJingle {
    SUNRISE(E, C, D, G),
    NOON(C, D, E, C),
    SUNSET(E, D, C, G),
    MIDNIGHT(G, D, E, C);

    final List<Float> notes;

    ChimeJingle(Float... notes) {
      this.notes = List.of(notes);
    }

    static ChimeJingle get(int index) {
      ChimeJingle[] jingles = values();
      if (index >= jingles.length) return jingles[0];

      return jingles[index];
    }
  }

  public PendulumEntity(BlockPos pos, BlockState state) {
    this(ModBlockEntities.PENDULUM_ENTITY.get(), pos, state);
  }

  public PendulumEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
    super(type, pos, state);

    ALTERNATE_TINT.set(GemstoneColor.NO_COLOR);
    SWING_ANIMATOR = new GravityInterpolator();
    COLOR_PHASE_ANIMATOR = new PhaseTintInterpolator();
  }

  public static void tick(Level level, BlockPos pos, BlockState state, BlockEntity entity) {
    if (level.isClientSide() || !(entity instanceof PendulumEntity pendulum)) return;
    if (!(state.getBlock() instanceof AmethystPendulumBlock)) return;

    long dayTime = level.getDayTime();
    if (pendulum.SWING_FRAME_OFFSET.get() < 0) initSwingOffset(pendulum, dayTime);

    int nextColorPhase;
    if (level.dimension() == Level.OVERWORLD) {
      nextColorPhase = calculateNextColorPhase(pendulum, dayTime);
      pendulum.IN_OVERWORLD.set(true);
    } else {
      nextColorPhase = getRandomColorPhase(pendulum, level);
      pendulum.IN_OVERWORLD.set(false);
    }

    int nextSwingFrame = calculateNextSwingFrame(pendulum, level, dayTime);
    if (level.dimension() == Level.OVERWORLD) handleChime(pendulum, level, pos);

    if (!pendulum.differentFrom(nextSwingFrame, nextColorPhase)) return;

    if (pendulum.getSwingFrame() != nextSwingFrame && phaseExtreme(nextSwingFrame)) {
      playTickTock(pendulum, level, pos);
    }

    pendulum.CURRENT_SWING_FRAME.set(nextSwingFrame);
    pendulum.CURRENT_COLOR_PHASE.set(nextColorPhase);
    setChanged(level, pos, state);

    ((ServerLevel) level).getChunkSource().blockChanged(pos);
  }

  public int getSwingFrame() {
    return CURRENT_SWING_FRAME.get();
  }

  public int getColorPhase() {
    return CURRENT_COLOR_PHASE.get();
  }

  public boolean inOverworld() {
    return IN_OVERWORLD.get();
  }

  public int getAlternateTint() {
    return ALTERNATE_TINT.get();
  }

  private boolean settled() {
    return !SWING_ANIMATOR.inProgress() && settled(getSwingFrame());
  }

  private static int calculateNextSwingFrame(PendulumEntity pendulum, Level level, long dayTime) {
    GameRules rules = ((ServerLevel) level).getGameRules();
    //? if <= 1.21.5
    boolean doDaylightCycle = rules.getRule(GameRules.RULE_DAYLIGHT).get();
    //? if >= 1.21.11
    //boolean doDaylightCycle = rules.get(GameRules.ADVANCE_TIME);

    if (doDaylightCycle) {
      pendulum.SWINGING.set(true);
      return toSwingFrame(dayTime - pendulum.SWING_FRAME_OFFSET.get());
    }

    if (pendulum.SWINGING.get() || !pendulum.settled()) {
      pendulum.SWING_FRAME_OFFSET.set(-1);
      return stopSwinging(pendulum);
    }

    return pendulum.getSwingFrame();
  }

  private static int stopSwinging(PendulumEntity pendulum) {
    GravityInterpolator animator = pendulum.SWING_ANIMATOR;

    if (!animator.isInitialized() || !animator.inProgress()) {
      if (pendulum.SWINGING.get()) animator.interp(pendulum.getSwingFrame());
    }

    int nextFrame = animator.step();
    if (!animator.inProgress()) pendulum.SWINGING.set(false);

    return nextFrame;
  }

  private static int calculateNextColorPhase(PendulumEntity pendulum, long dayTime) {
    PhaseTintInterpolator animator = pendulum.COLOR_PHASE_ANIMATOR;
    if (animator.inProgress()) return animator.step();

    int nextPhase = toColorPhase(dayTime);
    if (!animator.isInitialized()) {
      animator.interp(pendulum.getColorPhase(), nextPhase);
      return animator.step();
    }

    return nextPhase;
  }

  private static int getRandomColorPhase(PendulumEntity pendulum, Level level) {
    PhaseTintInterpolator animator = pendulum.COLOR_PHASE_ANIMATOR;
    if (animator.inProgress()) return animator.step();

    RandomSource rand = level.getRandom();
    int nextPhase = rand.nextInt(0, NUM_DAY_SUBPHASES) % NUM_DAY_SUBPHASES;
    animator.interp(pendulum.getColorPhase(), nextPhase);
    nextPhase = animator.step();

    if (level.dimension() == Level.NETHER) {
      pendulum.ALTERNATE_TINT.set(GemstoneColor.getNetherColor(nextPhase));
    } // TODO: tint for End dimension?

    return nextPhase;
  }

  private static void initSwingOffset(PendulumEntity pendulum, long dayTime) {
    pendulum.SWING_FRAME_OFFSET.set(Math.toIntExact(dayTime % PENDULUM_PERIOD_TICKS));
  }

  private static boolean settled(int swingFrame) {
    return (swingFrame % PENDULUM_HALF_PHASE) == PENDULUM_LOWEST_FRAME;
  }

  private static boolean phaseExtreme(int swingFrame) {
    return swingFrame == 0 || swingFrame == PENDULUM_HALF_PHASE;
  }

  private static boolean leftOfCenter(int swingFrame) {
    if (swingFrame <= PENDULUM_HALF_PHASE) return swingFrame < PENDULUM_LOWEST_FRAME;
    return PENDULUM_HALF_PHASE - (swingFrame % PENDULUM_HALF_PHASE) < PENDULUM_LOWEST_FRAME;
  }

  private static int toSwingFrame(long offsetDayTime) {
    long frameTime = offsetDayTime / PENDULUM_FRAME_TICKS;
    return Math.floorMod(frameTime, NUM_PENDULUM_FRAMES);
  }

  private static int toColorPhase(long dayTime) {
    return (int) (dayTime / DAY_SUBPHASE_TICKS) % NUM_DAY_SUBPHASES;
  }

  private boolean differentFrom(int swingFrame, int colorPhase) {
    return swingFrame != getSwingFrame() || colorPhase != getColorPhase();
  }

  private static void playTickTock(PendulumEntity pendulum, Level level, BlockPos pos) {
    float pitch = leftOfCenter(pendulum.getSwingFrame()) ? 1.15F : 0.85F;
    level.playSound(null, pos, ModSounds.CLOCK_TICK.get(), SoundSource.BLOCKS, 0.8F, pitch);
  }

  private static void handleChime(PendulumEntity pendulum, Level level, BlockPos pos) {
    if (!pendulum.SWINGING.get()) return;

    long offsetDayTime = level.getDayTime() - pendulum.SWING_FRAME_OFFSET.get();
    if (offsetDayTime < 0) offsetDayTime += AnalogClockFace.DAY_LENGTH_TICKS;
    offsetDayTime %= AnalogClockFace.DAY_LENGTH_TICKS;

    long chimePhaseTime = offsetDayTime % CHIME_PHASE_TICKS;
    if (chimePhaseTime >= CHIME_DURATION_TICKS) {
      pendulum.isChimeRinging = false;
      pendulum.chimeTicks = 0;
      return;
    }

    if (pendulum.isChimeRinging) {
      pendulum.chimeTicks++;
    } else {
      pendulum.isChimeRinging = true;
      pendulum.chimeTicks = (int) chimePhaseTime;
    }

    playChime(pendulum, level, pos, offsetDayTime);
  }

  private static void playChime(PendulumEntity pendulum, Level level, BlockPos pos, long dayTime) {
    if (pendulum.chimeTicks % CHIME_BEAT_TICKS != 0) return;

    Direction pendulumFacing = level.getBlockState(pos).getValue(AmethystPendulumBlock.FACING);
    BlockPos behindPendulum = pos.relative(pendulumFacing.getOpposite());
    BlockState behindPendulumState = level.getBlockState(behindPendulum);
    if (!behindPendulumState.is(Blocks.BELL)) return;

    int chimePhase = (int) (dayTime / CHIME_PHASE_TICKS);
    int chimeBeat = pendulum.chimeTicks / CHIME_BEAT_TICKS;

    List<Float> chimeNotes = ChimeJingle.get(chimePhase).notes;
    float pitch = chimeNotes.get(chimeBeat);

    level.playSound(null, pos, ModSounds.CLOCK_CHIME.get(), SoundSource.BLOCKS, 1F, pitch);
    level.playSound(null, pos, ModSounds.CHIME_RESONATE.get(), SoundSource.BLOCKS, 1.4F, pitch);

    Direction chimeStrike = pendulumFacing.getClockWise();
    if (level.getRandom().nextBoolean()) chimeStrike = chimeStrike.getOpposite();

    level.blockEvent(behindPendulum, behindPendulumState.getBlock(), 2, chimeStrike.get3DDataValue());
  }

  @Override
  protected void saveData(ExtraData output) {
    output.save(SWINGING);
    output.save(IN_OVERWORLD);
    output.save(ALTERNATE_TINT);
    output.save(SWING_FRAME_OFFSET);
    output.save(CURRENT_SWING_FRAME);
    if (COLOR_PHASE_ANIMATOR.isInitialized()) output.save(CURRENT_COLOR_PHASE);
  }

  @Override
  protected void loadData(ExtraData input) {
    input.load(SWINGING);
    input.load(IN_OVERWORLD);
    input.load(ALTERNATE_TINT);
    input.load(SWING_FRAME_OFFSET);
    input.load(CURRENT_SWING_FRAME);
    input.load(CURRENT_COLOR_PHASE);
  }

  static class PhaseTintInterpolator extends FrameInterpolator {
    public PhaseTintInterpolator() {
      super(new Config(0.5F, 2F, 60, NUM_DAY_SUBPHASES));
    }
  }
}

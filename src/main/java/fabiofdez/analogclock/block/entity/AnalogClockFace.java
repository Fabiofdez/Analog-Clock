package fabiofdez.analogclock.block.entity;

import fabiofdez.analogclock.ModBlockEntities;
import fabiofdez.analogclock.ModSounds;
import fabiofdez.analogclock.block.AnalogClockBlock;
import fabiofdez.analogclock.block.entity.properties.BlockEntityData;
import fabiofdez.analogclock.block.entity.properties.BlockEntityProp;
import fabiofdez.analogclock.block.entity.properties.BlockEntityProps;
import fabiofdez.analogclock.color.ClockFaceStyle;
import fabiofdez.analogclock.util.ClockTime;
import fabiofdez.analogclock.util.FrameInterpolator;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.time.LocalTime;
import java.util.UUID;

public class AnalogClockFace extends BaseBlockEntity {
  public static final long DAY_LENGTH_TICKS = 24000;
  public static final long HALF_DAY_LENGTH_TICKS = DAY_LENGTH_TICKS / 2;
  public static final long HOUR_LENGTH_TICKS = DAY_LENGTH_TICKS / 24;
  public static final long SUNRISE_TICK_OFFSET = 6000; // 6AM = dayTime 0
  public static final float SECONDS_PER_TICK = 24 * 60 * 60F / DAY_LENGTH_TICKS;

  public static final int CLOCK_HAND_FRAMES = 24;
  public static final int UNIT_HOUR_FRAMES = 2;
  public static final int HOUR_FRAMES_RADIX = 12;
  public static final int NUM_CLOCK_FRAMES = CLOCK_HAND_FRAMES * HOUR_FRAMES_RADIX;

  public static final String IN_GAME_ZONE_ID = "Zone/In-Game";

  private static final int INITIAL_CLOCK_FRAME = 3 * UNIT_HOUR_FRAMES * HOUR_FRAMES_RADIX; // 3:00
  private static final int BRUSH_DURATION_TICKS = 15;

  private final BlockEntityProp<Integer> CURRENT_FRAME = BlockEntityProps.INT.create("clockFrame");
  private final BlockEntityProp<Boolean> MANUAL_WINDING = BlockEntityProps.BOOLEAN.create("winding", false);
  private final BlockEntityProp<String> TIME_ZONE = BlockEntityProps.STRING.create("time_zone", IN_GAME_ZONE_ID);

  private final ClockHandsInterpolator HANDS_ANIMATOR;
  private String brushingPlayerUUID = null;
  private int brushEventTicks = 0;

  public AnalogClockFace(BlockPos pos, BlockState state) {
    super(ModBlockEntities.CLOCK_FACE_ENTITY.get(), pos, state);

    CURRENT_FRAME.set(INITIAL_CLOCK_FRAME);
    HANDS_ANIMATOR = new ClockHandsInterpolator();
  }

  public static void tick(Level level, BlockPos pos, BlockState state, BlockEntity entity) {
    if (level.isClientSide() || !(entity instanceof AnalogClockFace clockFace)) return;

    if (clockFace.isBeingBrushed()) handleBrushing(clockFace, level, pos, state);

    boolean winding = clockFace.HANDS_ANIMATOR.inProgress();
    boolean windingStopped = clockFace.isManuallyWinding() && !winding;
    if (windingStopped) clockFace.MANUAL_WINDING.set(false);

    int nextFrame;
    if (level.dimension() == Level.OVERWORLD) {
      nextFrame = calculateNextFrame(level, clockFace);
      if (nextFrame == clockFace.getClockFrame() && !windingStopped) return;

      if (winding) playWindUpTick(clockFace, level, pos);
    } else {
      nextFrame = goToRandomFrame(level, clockFace);
      if (nextFrame == clockFace.getClockFrame() && !windingStopped) return;
    }

    clockFace.CURRENT_FRAME.set(nextFrame);
    setChanged(level, pos, state);

    ((ServerLevel) level).getChunkSource().blockChanged(pos);
  }

  public boolean isBeingBrushed() {
    return brushingPlayerUUID != null;
  }

  public boolean isManuallyWinding() {
    return MANUAL_WINDING.get();
  }

  public String getTimeZone() {
    return TIME_ZONE.get();
  }

  public int getClockFrame() {
    return CURRENT_FRAME.get();
  }

  public int getHourFrame() {
    return (getClockFrame() / HOUR_FRAMES_RADIX) % NUM_CLOCK_FRAMES;
  }

  public int getMinuteFrame() {
    int currentFrame = getClockFrame();
    int hourPart = (currentFrame / HOUR_FRAMES_RADIX) % UNIT_HOUR_FRAMES;
    return (currentFrame % HOUR_FRAMES_RADIX) + (hourPart * HOUR_FRAMES_RADIX);
  }

  private static int calculateNextFrame(Level level, AnalogClockFace clockFace) {
    ClockHandsInterpolator animator = clockFace.HANDS_ANIMATOR;
    int nextFrame;

    if (animator.inProgress()) {
      if (animator.progress() >= 0.75F) {
        animator.interp(clockFace.getClockFrame(), toClockFrame(level, clockFace));
      }
      return animator.step();
    }

    nextFrame = toClockFrame(level, clockFace);

    if (!animator.isInitialized()) {
      animator.interp(clockFace.getClockFrame(), nextFrame);
      return animator.step();
    }

    return nextFrame;
  }

  private static int goToRandomFrame(Level level, AnalogClockFace clockFace) {
    ClockHandsInterpolator animator = clockFace.HANDS_ANIMATOR;
    if (animator.inProgress()) return animator.step();

    RandomSource rand = level.getRandom();
    int randomFrame = rand.nextInt(0, NUM_CLOCK_FRAMES) % NUM_CLOCK_FRAMES;
    animator.interp(clockFace.getClockFrame(), randomFrame);

    return animator.step();
  }

  private static int toClockFrame(Level level, AnalogClockFace clockFace) {
    boolean woundToInGameTime = clockFace.getTimeZone().equals(IN_GAME_ZONE_ID);

    long dayTime = woundToInGameTime ? level.getDayTime() : clockFace.getRealTime();
    long clockTime = (dayTime + SUNRISE_TICK_OFFSET) % HALF_DAY_LENGTH_TICKS;
    int frameOffset = Math.toIntExact((clockTime * CLOCK_HAND_FRAMES) / HOUR_LENGTH_TICKS);

    return frameOffset % NUM_CLOCK_FRAMES;
  }

  private long getRealTime() {
    String zoneId = getTimeZone();
    LocalTime time = LocalTime.now(ClockTime.getZoneId(zoneId));

    long ticksOfDay = (long) Math.floor(time.toSecondOfDay() / SECONDS_PER_TICK);
    if (ticksOfDay < SUNRISE_TICK_OFFSET) ticksOfDay += DAY_LENGTH_TICKS;
    ticksOfDay -= SUNRISE_TICK_OFFSET;

    return ticksOfDay % DAY_LENGTH_TICKS;
  }

  public boolean setTimeZone(String zoneId) {
    if (zoneId == null || zoneId.isEmpty()) return false;

    if (!zoneId.equals(IN_GAME_ZONE_ID)) {
      zoneId = ClockTime.getZoneId(zoneId).getId();
    }

    TIME_ZONE.set(zoneId);
    return true;
  }

  public boolean manuallyWindTo(String zoneId, Level level) {
    if (!setTimeZone(zoneId)) return false;

    HANDS_ANIMATOR.interp(getClockFrame(), toClockFrame(level, this));
    MANUAL_WINDING.set(true);

    return true;
  }

  private static void playWindUpTick(AnalogClockFace clockFace, Level level, BlockPos pos) {
    if (level == null || clockFace == null) return;

    RandomSource rand = level.getRandom();
    float chance = rand.nextFloat();
    float animationSpeed = clockFace.HANDS_ANIMATOR.speed(); // range: [1, 5]
    if (chance / animationSpeed > 0.5F) return;

    float pitch = (animationSpeed + 9) / 10; // range: [1, 1.4]
    pitch += (rand.nextFloat() * 0.4F) - 0.2F; // offset in range [-0.2, 0.2]

    float volume = (animationSpeed + 3) / 10; // range [0.4, 0.8]
    volume += (rand.nextFloat() * 0.2F) - 0.1F; // offset in range [-0.1, 0.1]

    level.playSound(null, pos, ModSounds.CLOCK_WIND.get(), SoundSource.BLOCKS, volume, pitch);
  }

  private static void handleBrushing(AnalogClockFace clockFace, Level level, BlockPos pos, BlockState state) {
    Player player = level.getPlayerByUUID(UUID.fromString(clockFace.brushingPlayerUUID));
    if (player == null) return;

    if (!player.isUsingItem()) {
      clockFace.brushingPlayerUUID = null;
      return;
    }

    clockFace.brushEventTicks++;

    if (clockFace.brushEventTicks > BRUSH_DURATION_TICKS) {
      level.setBlockAndUpdate(pos, state.setValue(AnalogClockBlock.FACE_TINT, ClockFaceStyle.FACE_NO_DYE));
      clockFace.brushingPlayerUUID = null;
    }
  }

  public void startBrushing(Player player) {
    brushingPlayerUUID = player.getStringUUID();
    brushEventTicks = 0;
  }

  @Override
  protected void saveData(BlockEntityData output) {
    output.save(CURRENT_FRAME);
    output.save(MANUAL_WINDING);
    output.save(TIME_ZONE);
  }

  @Override
  protected void loadData(BlockEntityData input) {
    input.load(CURRENT_FRAME);
    input.load(MANUAL_WINDING);
    input.load(TIME_ZONE);
  }

  static class ClockHandsInterpolator extends FrameInterpolator {
    public ClockHandsInterpolator() {
      super(new Config(NUM_CLOCK_FRAMES));
    }
  }
}

package fabiofdez.analogclock.util;

public abstract class FrameInterpolator {
  protected int startFrame;
  protected int frameDiff;
  protected int currentFrame;
  protected float progress;
  protected float currentSpeed;
  protected float step = 1;

  protected final Config config;
  protected boolean initialized;
  protected boolean inProgress;

  public FrameInterpolator() {
    this(new Config());
  }

  public FrameInterpolator(Config config) {
    this.config = config;
  }

  public void interp(int startFrame, int endFrame) {
    this.startFrame = startFrame;

    currentFrame = startFrame;
    frameDiff = nearestFrameDiff(startFrame, endFrame);

    if (frameDiff == 0) {
      currentFrame = endFrame;

      progress = 1F;
      inProgress = false;
    } else {
      float speed = (float) Math.abs(frameDiff) / config.TARGET_DURATION_TICKS;
      float dampenedSpeed = Math.clamp(speed * config.SPEED_DAMPER, config.MIN_SPEED, config.MAX_SPEED);

      step = Math.abs(dampenedSpeed / frameDiff);
      currentSpeed = dampenedSpeed;
      progress = 0F;
      initialized = true;
      inProgress = true;
    }
  }

  public int step() {
    if (!initialized || !inProgress) return currentFrame;

    progress += step;
    if (progress > 1F) progress = 1F;

    currentFrame = startFrame + Math.round(progress * frameDiff);
    if (currentFrame < 0) currentFrame += config.FRAME_RANGE;
    currentFrame %= config.FRAME_RANGE;

    if (progress == 1F) {
      inProgress = false;
    }

    return currentFrame;
  }

  public int nearestFrameDiff(int startFrame, int endFrame) {
    int positiveDiff = Math.floorMod(endFrame - startFrame, config.FRAME_RANGE);

    if (positiveDiff > config.HALF_FRAME_RANGE) {
      return positiveDiff - config.FRAME_RANGE;
    }

    return positiveDiff;
  }

  public float speed() {
    return currentSpeed;
  }

  public float progress() {
    return progress;
  }

  public boolean inProgress() {
    return inProgress;
  }

  public boolean isInitialized() {
    return initialized;
  }

  public static final class Config {
    public final float MIN_SPEED;
    public final float MAX_SPEED;
    public final float SPEED_DAMPER;
    public final int TARGET_DURATION_TICKS;

    public final int FRAME_RANGE;
    public final int HALF_FRAME_RANGE;

    public Config() {
      this(2);
    }

    public Config(int frameRange) {
      this(24, frameRange);
    }

    public Config(int targetDurationTicks, int frameRange) {
      this(1F, 5F, targetDurationTicks, frameRange);
    }

    public Config(float minSpeed, float maxSpeed, int targetDurationTicks, int frameRange) {
      this(minSpeed, maxSpeed, 0.8F, targetDurationTicks, frameRange);
    }

    public Config(float minSpeed, float maxSpeed, float speedDamper, int targetDurationTicks, int frameRange) {
      MIN_SPEED = minSpeed;
      MAX_SPEED = maxSpeed;
      SPEED_DAMPER = speedDamper;
      TARGET_DURATION_TICKS = targetDurationTicks;

      FRAME_RANGE = frameRange;
      HALF_FRAME_RANGE = frameRange - (frameRange / 2);
    }
  }
}

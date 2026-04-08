package fabiofdez.analogclock.util;

import fabiofdez.analogclock.entity.AnalogClockFace;
import net.minecraft.util.Mth;

public class ClockHandsInterpolator {
  protected static final int FRAME_RANGE = AnalogClockFace.NUM_CLOCK_FRAMES;
  protected static final int HALF_FRAME_RANGE = Math.round((float) FRAME_RANGE / 2);

  private static final float MIN_SPEED = 1F;
  private static final float MAX_SPEED = 5F;
  private static final float SPEED_DAMPER = 0.8F;
  private static final int TARGET_TIME_TICKS = 24;

  private int startFrame;
  private int frameDiff;
  private int currentFrame;
  private float progress;
  private float currentSpeed;
  private float step = 1;

  private boolean initialized;
  private boolean inProgress;

  public ClockHandsInterpolator() {
  }

  public void interp(int startFrame, int endFrame) {
    this.startFrame = startFrame;
    interpTo(endFrame);
  }

  public void interpTo(int endFrame) {
    currentFrame = startFrame;
    frameDiff = nearestFrameDiff(startFrame, endFrame);

    if (frameDiff == 0) {
      currentFrame = endFrame;

      progress = 1F;
      inProgress = false;
    } else {
      float speed = (float) Math.abs(frameDiff) / TARGET_TIME_TICKS;
      float dampenedSpeed = Math.clamp(speed * SPEED_DAMPER, MIN_SPEED, MAX_SPEED);

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
    if (currentFrame < 0) currentFrame += FRAME_RANGE;
    currentFrame %= FRAME_RANGE;

    if (progress == 1F) {
      inProgress = false;
    }

    return currentFrame;
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

  private static int nearestFrameDiff(int startTotalFrame, int endTotalFrame) {
    int forwardDiff = Mth.positiveModulo(endTotalFrame - startTotalFrame, FRAME_RANGE);

    if (forwardDiff > HALF_FRAME_RANGE) {
      return forwardDiff - FRAME_RANGE;
    }

    return forwardDiff;
  }
}

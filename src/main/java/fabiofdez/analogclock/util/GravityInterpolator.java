package fabiofdez.analogclock.util;

import fabiofdez.analogclock.entity.PendulumEntity;

import java.util.ArrayDeque;
import java.util.Deque;

public class GravityInterpolator extends FrameInterpolator {
  private static final int SWING_PHASE_TICKS = PendulumEntity.PENDULUM_HALF_PHASE * PendulumEntity.PENDULUM_FRAME_TICKS;
  private static final int SWING_FRAME_RANGE = PendulumEntity.PENDULUM_HALF_PHASE + 1;

  private final Deque<InterpolatorPhase> phases;
  private final int POSITIVE_SWING_MAX;

  public GravityInterpolator() {
    super(new Config(0.1F, 1F, SWING_PHASE_TICKS, SWING_FRAME_RANGE));

    phases = new ArrayDeque<>();
    POSITIVE_SWING_MAX = config.FRAME_RANGE - 1;
  }

  public void interp(int currentFrame) {
    int minFrame = currentFrame;
    if (minFrame > POSITIVE_SWING_MAX) {
      minFrame = 2 * (POSITIVE_SWING_MAX) - currentFrame;
    }

    int maxFrame = Math.floorMod(POSITIVE_SWING_MAX - minFrame, config.FRAME_RANGE);
    boolean positiveMovement = minFrame <= maxFrame;

    while (minFrame != maxFrame) {
      if (Math.abs(maxFrame - minFrame) < 2) break;

      boolean evenSwing = phases.size() % 2 == 0;

      if (positiveMovement) {
        phases.addLast(new InterpolatorPhase(evenSwing ? minFrame++ : maxFrame--, evenSwing ? maxFrame-- : minFrame++));
      } else {
        phases.addLast(new InterpolatorPhase(evenSwing ? minFrame-- : maxFrame++, evenSwing ? maxFrame++ : minFrame--));
      }
    }

    int lowestPoint = POSITIVE_SWING_MAX / 2;
    phases.addLast(new InterpolatorPhase(lowestPoint, lowestPoint));
  }

  @Override
  public int step() {
    if (inProgress() && progress != 1F) return super.step();

    if (phases.pollFirst() instanceof InterpolatorPhase(int start, int end)) {
      interp(start, end);
    }

    int nextFrame = super.step();
    inProgress = inProgress || !phases.isEmpty();

    return nextFrame;
  }

  @Override
  public int nearestFrameDiff(int startFrame, int endFrame) {
    return endFrame - startFrame;
  }

  record InterpolatorPhase(int start, int end) {
  }
}

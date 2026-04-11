package fabiofdez.analogclock.color;

import fabiofdez.analogclock.entity.PendulumEntity;
import net.minecraft.world.level.block.entity.BlockEntity;

public class GemstoneColor {
  public static final int NO_COLOR = 0xFFFFFF;

  public static final int SUNRISE = 0xEC8B60;
  public static final int NOON = 0x66D5F6;
  public static final int SUNSET = 0x895589;
  public static final int MIDNIGHT = 0x49346E;

  public static final int NETHER_RED1 = 0xD4205C;
  public static final int NETHER_RED2 = 0xE64530;
  public static final int NETHER_RED3 = 0xFF4C59;

  public static int getTint(BlockEntity entity) {
    if (!(entity instanceof PendulumEntity gemstone)) return opaque(NO_COLOR);

    if (!gemstone.inOverworld()) {
      return opaque(gemstone.getAlternateTint());
    }

    return getDayColor(gemstone.getColorPhase());
  }

  public static int getDayColor(int phase) {
    return getColorAlongGradient(phase, SUNRISE, NOON, SUNSET, MIDNIGHT);
  }

  public static int getNetherColor(int phase) {
    return getColorAlongGradient(phase, NETHER_RED1, NETHER_RED2, NETHER_RED1, NETHER_RED3);
  }

  private static int getColorAlongGradient(int phase, int STOP_1, int STOP_2, int STOP_3, int STOP_4) {
    float interphase = (phase % 6) / 6F;

    if (phase < 6) {
      return interpolate(interphase, STOP_1, STOP_2);
    } else if (phase < 12) {
      return interpolate(interphase, STOP_2, STOP_3);
    } else if (phase < 18) {
      return interpolate(interphase, STOP_3, STOP_4);
    } else {
      return interpolate(interphase, STOP_4, STOP_1);
    }
  }

  private static int interpolate(float t, int color1, int color2) {
    int r1 = (color1 >> 16) & 0xFF;
    int g1 = (color1 >> 8) & 0xFF;
    int b1 = color1 & 0xFF;

    int r2 = (color2 >> 16) & 0xFF;
    int g2 = (color2 >> 8) & 0xFF;
    int b2 = color2 & 0xFF;

    int r = (int) (r1 + (r2 - r1) * t);
    int g = (int) (g1 + (g2 - g1) * t);
    int b = (int) (b1 + (b2 - b1) * t);

    return opaque(r, g, b);
  }

  private static int opaque(int r, int g, int b) {
    return opaque((r << 16) | (g << 8) | b);
  }

  private static int opaque(int color) {
    return (0xFF << 24) | color;
  }
}

package fabiofdez.analogclock.color;

import fabiofdez.analogclock.block.entity.PendulumEntity;
import net.minecraft.util.ARGB;
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
    if (!(entity instanceof PendulumEntity gemstone)) return ARGB.opaque(NO_COLOR);

    if (!gemstone.inOverworld()) {
      return ARGB.opaque(gemstone.getAlternateTint());
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
      return ARGB.lerp(interphase, ARGB.opaque(STOP_1), ARGB.opaque(STOP_2));
    } else if (phase < 12) {
      return ARGB.lerp(interphase, ARGB.opaque(STOP_2), ARGB.opaque(STOP_3));
    } else if (phase < 18) {
      return ARGB.lerp(interphase, ARGB.opaque(STOP_3), ARGB.opaque(STOP_4));
    } else {
      return ARGB.lerp(interphase, ARGB.opaque(STOP_4), ARGB.opaque(STOP_1));
    }
  }
}

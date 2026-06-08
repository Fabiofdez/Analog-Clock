package fabiofdez.analogclock.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class ClockTime {
  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("O");

  public static ZoneId getLocal() {
    return ZoneId.systemDefault();
  }

  public static ZoneId getZoneId(String zoneId) {
    try {
      return ZoneId.of(zoneId);
    } catch (Exception ignored) {
      return ZoneId.systemDefault();
    }
  }

  public static String getOffset(String zoneId) {
    return getOffset(getZoneId(zoneId));
  }

  public static String getOffset(ZoneId zone) {
    ZoneOffset offset = zone.getRules().getOffset(Instant.now());
    return formatter.format(offset);
  }
}

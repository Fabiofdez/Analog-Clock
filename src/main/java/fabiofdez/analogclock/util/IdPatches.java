package fabiofdez.analogclock.util;

import fabiofdez.analogclock.AnalogClock;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class IdPatches {
  public static final String OLD_MOD_ID = "analog-clock";

  public static final Map<String, String> ID_REMAP = Map.of("gemstone", "pendulum");

  public static boolean outsideMod(@NotNull ResourceLocation id) {
    String namespace = id.getNamespace();
    return !namespace.equals(OLD_MOD_ID) && !namespace.equals(AnalogClock.MOD_ID);
  }

  public static boolean upToDate(@NotNull ResourceLocation id) {
    return !id.getNamespace().equals(OLD_MOD_ID) && ID_REMAP.get(id.getPath()) == null;
  }

  public static ResourceLocation update(@NotNull ResourceLocation id) {
    String oldPath = id.getPath();
    String newPath = ID_REMAP.get(oldPath);

    if (newPath == null) return AnalogClock.id(oldPath);
    return AnalogClock.id(newPath);
  }
}

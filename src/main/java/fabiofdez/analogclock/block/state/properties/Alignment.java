package fabiofdez.analogclock.block.state.properties;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum Alignment implements StringRepresentable {
  FRONT("front"),
  BACK("back");

  private final String name;

  Alignment(final String name) {
    this.name = name;
  }

  public String toString() {
    return this.name;
  }

  public @NotNull String getSerializedName() {
    return this.name;
  }
}

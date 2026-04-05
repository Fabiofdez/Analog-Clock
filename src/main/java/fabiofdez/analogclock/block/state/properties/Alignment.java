package fabiofdez.analogclock.block.state.properties;

import net.minecraft.util.StringRepresentable;

public enum Alignment implements StringRepresentable {
  FRONT("front"),
  BACK("back");

  private final String name;

  Alignment(final String string2) {
    this.name = string2;
  }

  public String toString() {
    return this.name;
  }

  public String getSerializedName() {
    return this.name;
  }
}

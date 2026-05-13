package fabiofdez.analogclock.color;

import fabiofdez.analogclock.ModBlocks;
import fabiofdez.analogclock.block.AnalogClockBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.ARGB;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ClockFaceStyle {
  public static final DyeColor FACE_NO_DYE = DyeColor.NO_COLOR;
  public static final Plating HANDS_NO_PLATING = Plating.NO_PLATING;

  public static int getColor(BlockState state, @Nullable BlockAndTintGetter ignoredGetter, @Nullable BlockPos ignoredPos, int tintIdx) {
    if (state.is(ModBlocks.ANALOG_CLOCK.get()) && tintIdx != 1) return FACE_NO_DYE.getColor();
    if (state.is(ModBlocks.INTERNAL_CLOCK_FACE.get()) && tintIdx != 0) return FACE_NO_DYE.getColor();

    return state.getValue(AnalogClockBlock.FACE_TINT).getColor();
  }

  public enum DyeColor implements StringRepresentable {
    NO_COLOR(null, 0xF2EDAA),

    RED(Items.RED_DYE, 0xF68259),
    ORANGE(Items.ORANGE_DYE, 0xFFC066),
    YELLOW(Items.YELLOW_DYE, 0xFFE25D),
    LIME(Items.LIME_DYE, 0xD7ED65),
    GREEN(Items.GREEN_DYE, 0xAECE71),
    CYAN(Items.CYAN_DYE, 0xA5E7C5),
    LIGHT_BLUE(Items.LIGHT_BLUE_DYE, 0xAFE7E7),
    BLUE(Items.BLUE_DYE, 0x869ACA),
    PURPLE(Items.PURPLE_DYE, 0xBE95C1),
    MAGENTA(Items.MAGENTA_DYE, 0xF27C8E),
    PINK(Items.PINK_DYE, 0xFFAAB4),
    BROWN(Items.BROWN_DYE, 0x8D5122),
    WHITE(Items.WHITE_DYE, 0xFFF8E9),
    LIGHT_GRAY(Items.LIGHT_GRAY_DYE, 0xB8A885),
    GRAY(Items.GRAY_DYE, 0x5E584E),
    BLACK(Items.BLACK_DYE, 0x2F291F);

    private static final Map<String, DyeColor> COLORS;
    private static final String NO_DYE_ID = "no_dye";

    private final Item dye;
    private final int hex;

    DyeColor(Item dye, int hex) {
      this.dye = dye;
      this.hex = hex;
    }

    public String dyeId() {
      return nameOf(dye);
    }

    public int getColor() {
      return ARGB.opaque(hex);
    }

    public static String nameOf(Item dye) {
      if (dye == null) return NO_DYE_ID;
      return BuiltInRegistries.ITEM.getKey(dye).getPath();
    }

    public static boolean has(Item dye) {
      return has(nameOf(dye));
    }

    public static boolean has(String dyeId) {
      return COLORS.containsKey(dyeId);
    }

    public static DyeColor getColorOf(Item dye) {
      return getColorOf(nameOf(dye));
    }

    public static DyeColor getColorOf(String dyeId) {
      if (!has(dyeId)) return NO_COLOR;

      return COLORS.get(dyeId);
    }

    @NotNull
    @Override
    public String getSerializedName() {
      return dyeId();
    }

    static {
      COLORS = Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(DyeColor::dyeId, Function.identity()));
    }
  }

  public enum Plating implements StringRepresentable {
    NO_PLATING(null, 0xFFFFFF),

    IRON(Items.IRON_NUGGET, 0xE7E8F1),
    //? >= 1.21.9
    //COPPER(Items.COPPER_NUGGET, 0xFFA668),
    GOLD(Items.GOLD_NUGGET, 0xFFC959);

    private static final Map<String, Plating> METALS;
    private static final String NO_PLATING_ID = "no_plating";

    private final Item plating;
    private final int hex;

    Plating(Item plating, int hex) {
      this.plating = plating;
      this.hex = hex;
    }

    public Item item() {
      return plating;
    }

    public String metalId() {
      return nameOf(plating);
    }

    public int getColor() {
      return ARGB.opaque(hex);
    }

    public static String nameOf(Item plating) {
      if (plating == null) return NO_PLATING_ID;
      return BuiltInRegistries.ITEM.getKey(plating).getPath();
    }

    public static boolean has(Item plating) {
      return has(nameOf(plating));
    }

    public static boolean has(String metalId) {
      return METALS.containsKey(metalId);
    }

    public static Plating getMetalOf(Item plating) {
      return getMetalOf(nameOf(plating));
    }

    public static Plating getMetalOf(String metalId) {
      if (!has(metalId)) return NO_PLATING;

      return METALS.get(metalId);
    }

    @NotNull
    @Override
    public String getSerializedName() {
      return metalId();
    }

    static {
      METALS = Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(Plating::metalId, Function.identity()));
    }
  }
}

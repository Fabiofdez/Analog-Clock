package fabiofdez.analogclock.block.entity.properties;

//? if < 1.21.11 {
import net.minecraft.nbt.CompoundTag;
//? } else {
/*import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
*///? }

public class BlockEntityData {
  //? < 1.21.11
  private CompoundTag tag;
  //? >= 1.21.11 {
  /*private ValueInput tag;
  private ValueOutput output;
  *///? }

  //? < 1.21.11
  public static BlockEntityData from(CompoundTag tag) {
    //? >= 1.21.11
    //public static BlockEntityData from(ValueInput tag) {
    BlockEntityData data = new BlockEntityData();
    data.tag = tag;

    return data;
  }

  //? >= 1.21.11 {
  /*public static BlockEntityData from(ValueOutput output) {
    BlockEntityData data = new BlockEntityData();
    data.output = output;

    return data;
  }
  *///? }

  public <T> void load(BlockEntityProp<T> prop) {
    prop.loadFrom(tag);
  }

  public <T> void save(BlockEntityProp<T> prop) {
    //? < 1.21.11
    prop.saveTo(tag);
    //? >= 1.21.11
    //prop.saveTo(output);
  }
}

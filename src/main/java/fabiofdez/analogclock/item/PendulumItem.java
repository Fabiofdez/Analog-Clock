package fabiofdez.analogclock.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public class PendulumItem extends BlockItem {
  public PendulumItem(Block block, Properties properties) {
    super(block, properties);
  }

  //? <= 1.21.1 {
  /*@Override
  public String getDescriptionId() {
    return this.getOrCreateDescriptionId();
  }
  *///? }
}

package fabiofdez.analogclock.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;

public class InvisibleBlock extends Block {
  public InvisibleBlock(BlockBehaviour.Properties properties) {
    super(properties.noOcclusion().pushReaction(PushReaction.DESTROY));
  }

  @Override
  protected RenderShape getRenderShape(BlockState state) {
    return RenderShape.INVISIBLE;
  }
}

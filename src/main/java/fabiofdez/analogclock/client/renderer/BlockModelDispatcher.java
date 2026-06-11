package fabiofdez.analogclock.client.renderer;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
//? <= 1.21.5
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
//? >= 26.1 {
/*import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
*///? }

public class BlockModelDispatcher {
  //? if <= 1.21.5 {
  private final BlockRenderDispatcher blockRenderer;

  private BlockModelDispatcher(BlockEntityRendererProvider.Context ctx) {
    this.blockRenderer = ctx.getBlockRenderDispatcher();
  }

  public BlockRenderDispatcher blockRenderer() {
    return blockRenderer;
  }
  //? } else if >= 26.1 {
  /*public static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();
  private final BlockModelResolver blockResolver;
  private BlockModelRenderState renderState;

  private BlockModelDispatcher(BlockEntityRendererProvider.Context ctx) {
    this.blockResolver = ctx.blockModelResolver();
  }

  public BlockModelRenderState renderState() {
    return renderState;
  }

  public void updateBlock(BlockModelRenderState renderState, BlockState state) {
    blockResolver.update(renderState, state, BlockModelDispatcher.BLOCK_DISPLAY_CONTEXT);
  }

  public void useBlock(BlockModelRenderState renderState) {
    this.renderState = renderState;
  }
  *///? } else {
  /*private BlockModelDispatcher(BlockEntityRendererProvider.Context ctx) {
  }
  *///? }

  public static BlockModelDispatcher from(BlockEntityRendererProvider.Context ctx) {
    return new BlockModelDispatcher(ctx);
  }
}

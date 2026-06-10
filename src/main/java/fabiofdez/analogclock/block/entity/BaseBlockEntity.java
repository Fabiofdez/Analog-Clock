package fabiofdez.analogclock.block.entity;

import fabiofdez.analogclock.block.entity.properties.BlockEntityData;
import net.minecraft.core.BlockPos;
//? > 1.20.1
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
//? >= 1.21.11 {
/*import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
*///? }
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BaseBlockEntity extends BlockEntity {

  public BaseBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
    super(type, pos, state);
  }

  protected abstract void saveData(BlockEntityData output);

  protected abstract void loadData(BlockEntityData input);

  @Override
      //? if <= 1.21.5
  protected final void saveAdditional(@NotNull CompoundTag output /*? if <= 1.21.5 && > 1.20.1 >> ') {' */, HolderLookup.Provider registryLookup) {
    //? if >= 1.21.11
    //protected final void saveAdditional(ValueOutput output) {

    saveData(BlockEntityData.from(output));

    super.saveAdditional(output /*? if <= 1.21.5 && > 1.20.1 >> ');' */, registryLookup);
  }

  @Override
      //? if <= 1.20.1 {
  /*public final void load(@NotNull CompoundTag input) {
   *///? } else if <= 1.21.5 {
  protected final void loadAdditional(CompoundTag input, HolderLookup.Provider registryLookup) {
    //? } else if >= 1.21.11 {
    /*protected void loadAdditional(ValueInput input) {
     *///? }

    //? <= 1.20.1
    //super.load(input);
    //? > 1.20.1
    super.loadAdditional(input /*? if <= 1.21.5 >> ');' */, registryLookup);

    loadData(BlockEntityData.from(input));
  }

  @Override
  public @NotNull CompoundTag getUpdateTag(/*? if > 1.20.1 >> ') {' */HolderLookup.Provider provider) {
    return saveWithoutMetadata(/*? if > 1.20.1 >> ');' */provider);
  }

  @Override
  public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }
}

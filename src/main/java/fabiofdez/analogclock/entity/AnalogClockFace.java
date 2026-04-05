package fabiofdez.analogclock.entity;

import fabiofdez.analogclock.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AnalogClockFace extends BlockEntity {
  private int lastHourFrame = 0;
  private int lastMinuteFrame = 0;

  public AnalogClockFace(BlockPos blockPos, BlockState blockState) {
    super(ModBlockEntities.CLOCK_FACE_ENTITY, blockPos, blockState);
    if (level == null) return;

    long dayTime = level.getDayTime();
    lastHourFrame = calculateHour(dayTime);
    lastMinuteFrame = calculateMinute(dayTime);
  }

  public static void tick(Level level, BlockPos pos, BlockState state, BlockEntity entity) {
    if (level.isClientSide) return;

    AnalogClockFace clockFace = (AnalogClockFace) entity;

    // day length = 24000
    // minute frame length = 1000 / 24 ~ 41.6 ticks
    // hour frame length = 12000 / 24 = 500 ticks

    // Time conversions:
    //       0 == 06:00 AM
    //    1000 == 07:00 AM
    //    1500 == 07:30 AM
    //    6000 == 12:00 PM
    //   12000 == 06:00 PM
    //   18000 == 12:00 AM

    long dayTime = level.getDayTime();
    int hourFrame = calculateHour(dayTime);
    int minuteFrame = calculateMinute(dayTime);

    if (hourFrame == clockFace.lastHourFrame && minuteFrame == clockFace.lastMinuteFrame) return;

    clockFace.lastHourFrame = hourFrame;
    clockFace.lastMinuteFrame = minuteFrame;

    setChanged(level, pos, state);

    ((ServerLevel) level)
        .getChunkSource()
        .blockChanged(pos);
  }

  private static int calculateHour(long dayTime) {
    long hourTickOffset = (dayTime % 12000) + 6000;
    return (int) (hourTickOffset / 500) % 24;
  }

  private static int calculateMinute(long dayTime) {
    long minuteTickOffset = (dayTime % 1000) * 24;
    return (int) (minuteTickOffset / 1000) % 24;
  }

  public int getHourFrame() {
    return lastHourFrame;
  }

  public int getMinuteFrame() {
    return lastMinuteFrame;
  }

  @Override
  protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
    nbt.putInt("hourFrame", lastHourFrame);
    nbt.putInt("minuteFrame", lastMinuteFrame);

    super.saveAdditional(nbt, registryLookup);
  }

  @Override
  protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
    super.loadAdditional(nbt, registryLookup);

    nbt
        .getInt("hourFrame")
        .ifPresent((num) -> lastHourFrame = num);
    nbt
        .getInt("minuteFrame")
        .ifPresent((num) -> lastMinuteFrame = num);
  }

  @Override
  public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider provider) {
    return saveWithoutMetadata(provider);
  }

  @Override
  public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }
}


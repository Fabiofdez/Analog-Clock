package fabiofdez.analogclock.item;

import fabiofdez.analogclock.AnalogClock;
import fabiofdez.analogclock.ModItems;
import fabiofdez.analogclock.block.entity.AnalogClockFace;
import fabiofdez.analogclock.util.ClockTime;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.Function;

//? if < 1.21 {
/*import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
*///? } else {
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//? }

//? fabric {
//? < 1.21
//import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
//? }

//? neoforge {
/*//? < 1.21.11
import net.neoforged.neoforge.network.PacketDistributor;
//? >= 1.21.11
//import net.neoforged.neoforge.client.network.ClientPacketDistributor;
*///? }

//? forge
//import fabiofdez.analogclock.network.PacketHandler;

public class ClockKeyItem extends Item {

  public ClockKeyItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult useOn(UseOnContext ctx) {
    Level level = ctx.getLevel();
    if (!level.isClientSide()) return InteractionResult.PASS;

    Player player = ctx.getPlayer();
    if (player == null) return InteractionResult.PASS;

    ItemStack usedStack = player.getItemInHand(ctx.getHand());
    if (!usedStack.is(ModItems.CLOCK_KEY.get())) return InteractionResult.PASS;

    BlockPos clickedPos = ctx.getClickedPos();
    BlockEntity targetedBE = level.getBlockEntity(clickedPos);
    if (!(targetedBE instanceof AnalogClockFace clockFace)) return InteractionResult.PASS;

    String zoneId = AnalogClockFace.IN_GAME_ZONE_ID;
    if (clockFace.getTimeZone().equals(zoneId)) zoneId = ClockTime.getLocal().getId();

    WindActionPayload payload = new WindActionPayload(zoneId, clickedPos);

    //? if fabric {
    //? < 1.21
    //ClientPlayNetworking.send(WindActionPayload.ID, payload.encode(PacketByteBufs.create()));
    //? >= 1.21
    ClientPlayNetworking.send(payload);
    //? } else {
    /*//? < 1.21
    //PacketHandler.sendToServer(payload);
    //? >= 1.21 && < 1.21.11
    PacketDistributor.sendToServer(payload);
    //? >= 1.21.11
    //ClientPacketDistributor.sendToServer(payload);
    *///? }

    return InteractionResult.SUCCESS;
  }

  public static void serverWindClock(Player player, Level level, WindActionPayload payload) {
    String zoneId = payload.zoneId();
    BlockPos pos = payload.blockPos();
    if (zoneId == null || zoneId.isEmpty() || pos == null || !level.hasChunkAt(pos)) return;

    BlockEntity blockEntity = level.getBlockEntity(pos);
    if (!(blockEntity instanceof AnalogClockFace clockFace)) return;

    boolean validZone = clockFace.manuallyWindTo(zoneId, level);
    if (!validZone) return;

    zoneId = clockFace.getTimeZone();
    MutableComponent windingMsg = Component.translatable(WindingMsg.TO_IN_GAME.getTranslationKey());

    if (!zoneId.equals(AnalogClockFace.IN_GAME_ZONE_ID)) {
      String zoneOffset = ClockTime.getOffset(zoneId);
      windingMsg = Component.translatable(WindingMsg.TO_REAL_WORLD.getTranslationKey(), zoneOffset);
    }

    //? < 26.1
    player.displayClientMessage(windingMsg.withStyle(ChatFormatting.GOLD), true);
    //? >= 26.1
    //player.sendOverlayMessage(windingMsg.withStyle(ChatFormatting.GOLD));
  }

  public record WindActionPayload(String zoneId, BlockPos blockPos) /*? if >= 1.21 >> '{' */ implements CustomPacketPayload {
    public static final ResourceLocation ID = AnalogClock.id("wind_clock");

    //? if < 1.21 {
  /*@SuppressWarnings("UnusedReturnValue")
    public FriendlyByteBuf encode(FriendlyByteBuf buf) {
      CompoundTag data = NbtUtils.writeBlockPos(blockPos);
      data.putString("zoneId", zoneId);

      return buf.writeNbt(data);
    }

    public static WindActionPayload decode(FriendlyByteBuf buf) {
      CompoundTag data = buf.readNbt();
      if (data == null) return new WindActionPayload(AnalogClockFace.IN_GAME_ZONE_ID, BlockPos.ZERO);

      String zoneId = data.getString("zoneId");
      BlockPos pos = NbtUtils.readBlockPos(data);

      return new WindActionPayload(zoneId, pos);
    }
    *///? } else {
    public static final CustomPacketPayload.Type<WindActionPayload> TYPE = new CustomPacketPayload.Type<>(ID);
    public static final StreamCodec<FriendlyByteBuf, WindActionPayload> CODEC = getCodec();

    @Override
    public Type<? extends CustomPacketPayload> type() {
      return TYPE;
    }

    private static StreamCodec<FriendlyByteBuf, WindActionPayload> getCodec() {
      return StreamCodec.composite(
          ByteBufCodecs.STRING_UTF8,
          WindActionPayload::zoneId,
          BlockPos.STREAM_CODEC,
          WindActionPayload::blockPos,
          WindActionPayload::new
      );
    }
    //? }
  }

  public enum WindingMsg {
    TO_IN_GAME("wind_in_game"),
    TO_REAL_WORLD("wind_real_world");

    private final Function<Item, String> predicate;

    WindingMsg(String eventName) {
      this.predicate = (item) -> AnalogClock.itemEventTranslatable(item, eventName);
    }

    public String getTranslationKey() {
      return this.predicate.apply(ModItems.CLOCK_KEY.get());
    }
  }
}

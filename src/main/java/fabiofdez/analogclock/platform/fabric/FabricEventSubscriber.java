package fabiofdez.analogclock.platform.fabric;

//? fabric {

import fabiofdez.analogclock.AnalogClock;
import fabiofdez.analogclock.ModBlocks;
import fabiofdez.analogclock.ModItems;
import fabiofdez.analogclock.item.ClockKeyItem;
//? >= 1.21
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class FabricEventSubscriber {

  public static void registerEvents() {
    AnalogClock.modifyCreativeTabs(ModBlocks::addCreative);
    AnalogClock.modifyCreativeTabs(ModItems::addCreative);

    //? if < 1.21 {
    /*ServerPlayNetworking.registerGlobalReceiver(
        ClockKeyItem.WindActionPayload.ID, (server, player, listener, buf, packetSender) -> {
          ClockKeyItem.WindActionPayload payload = ClockKeyItem.WindActionPayload.decode(buf);
          server.execute(() -> ClockKeyItem.serverWindClock(player, player.level(), payload));
        }
    );
    *///? } else {
    PayloadTypeRegistry.playC2S().register(ClockKeyItem.WindActionPayload.TYPE, ClockKeyItem.WindActionPayload.CODEC);
    ServerPlayNetworking.registerGlobalReceiver(
        ClockKeyItem.WindActionPayload.TYPE,
        (payload, context) -> ClockKeyItem.serverWindClock(context.player(), context.player().level(), payload)
    );
    //? }
  }
}
//?}

package fabiofdez.analogclock.network;

//? forge {

/*import fabiofdez.analogclock.AnalogClock;
import fabiofdez.analogclock.item.ClockKeyItem;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
  private static final String PROTOCOL_VERSION = "1";
  private static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder
      .named(ClockKeyItem.WindActionPayload.ID)
      .networkProtocolVersion(() -> PROTOCOL_VERSION)
      .serverAcceptedVersions((version) -> true)
      .clientAcceptedVersions((version) -> true)
      .simpleChannel();

  public static void register() {
    INSTANCE
        .messageBuilder(ClockKeyItem.WindActionPayload.class, 1, NetworkDirection.PLAY_TO_SERVER)
        .encoder(ClockKeyItem.WindActionPayload::encode)
        .decoder(ClockKeyItem.WindActionPayload::decode)
        .consumerMainThread((payload, context) -> {
          Player player = context.get().getSender();
          if (player == null) return;

          ClockKeyItem.serverWindClock(player, player.level(), payload);
        })
        .add();
  }

  public static void sendToServer(Object msg) {
    INSTANCE.send(PacketDistributor.SERVER.noArg(), msg);
  }
}
*///? }

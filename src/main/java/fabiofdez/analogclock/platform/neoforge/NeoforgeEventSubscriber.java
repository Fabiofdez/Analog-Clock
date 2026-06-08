package fabiofdez.analogclock.platform.neoforge;

//? neoforge {

/*import fabiofdez.analogclock.AnalogClock;
import fabiofdez.analogclock.ModBlocks;
import fabiofdez.analogclock.ModItems;
import fabiofdez.analogclock.item.ClockKeyItem;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = AnalogClock.MOD_ID/^? if < 1.21.11 >> ')' ^/ , bus = EventBusSubscriber.Bus.MOD)
public class NeoforgeEventSubscriber {

  @SubscribeEvent
  public static void modifyCreative(BuildCreativeModeTabContentsEvent event) {
    AnalogClock.modifyCreativeTabs(event, ModBlocks::addCreative);
    AnalogClock.modifyCreativeTabs(event, ModItems::addCreative);
  }

  @SubscribeEvent
  public static void registerNetworking(final RegisterPayloadHandlersEvent event) {
    final PayloadRegistrar registrar = event.registrar("1").executesOn(HandlerThread.MAIN);

    registrar.playToServer(
        ClockKeyItem.WindActionPayload.TYPE,
        ClockKeyItem.WindActionPayload.CODEC,
        (payload, context) -> ClockKeyItem.serverWindClock(context.player(), context.player().level(), payload)
    );
  }
}
*///?}

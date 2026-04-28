package fabiofdez.analogclock.platform.fabric;

//? fabric {

import fabiofdez.analogclock.platform.Platform;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;

public class FabricPlatform implements Platform {

	@Override
	public boolean isModLoaded(String modId) {
		return FabricLoader.getInstance().isModLoaded(modId);
	}

	@Override
	public ModLoader loader() {
		return ModLoader.FABRIC;
	}

	@Override
	public String mcVersion() {
    //? > 1.21.1
		return FabricLoader.getInstance().getRawGameVersion();
    //? <= 1.21.1
    //return Minecraft.getInstance().getLaunchedVersion();
	}

	@Override
	public boolean isDevelopmentEnvironment() {
		return FabricLoader.getInstance().isDevelopmentEnvironment();
	}
}
//?}

package p455w0rd.anvilfix;

import static p455w0rd.anvilfix.init.ModGlobals.*;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import p455w0rd.anvilfix.init.*;

@Mod(modid = MODID, name = NAME, version = VERSION, acceptedMinecraftVersions = "[1.12.2]")
public class AnvilFix {

	@Instance(MODID)
	public static AnvilFix INSTANCE;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		ModConfig.init();
		ModNetworking.init();
		NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, ModGuiHandler.getInstance());
	}

}

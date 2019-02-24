package p455w0rd.anvilfix;

import static p455w0rd.anvilfix.init.ModGlobals.*;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import p455w0rd.anvilfix.init.*;

@Mod(modid = MODID, name = NAME, version = VERSION, acceptedMinecraftVersions = "[1.12.2]")
public class AnvilFix {

	@Instance(MODID)
	public static AnvilFix INSTANCE;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		ModConfig.init();
		ModNetworking.init();
		if (FMLCommonHandler.instance().getSide().isClient()) {
			MinecraftForge.EVENT_BUS.register(ModEventsClient.class);
		}
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}

}

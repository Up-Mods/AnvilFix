package p455w0rd.anvilfix.init;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import p455w0rd.anvilfix.packets.PacketOpenContainer;
import p455w0rd.anvilfix.packets.PacketRenameItem;

/**
 * @author p455w0rd
 *
 */
public class ModNetworking {

	private static SimpleNetworkWrapper INSTANCE = null;

	public static SimpleNetworkWrapper getInstance() {
		if (INSTANCE == null) {
			INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(ModGlobals.MODID);
		}
		return INSTANCE;
	}

	public static void init() {
		getInstance().registerMessage(PacketOpenContainer.Handler.class, PacketOpenContainer.class, 0, Side.SERVER);
		getInstance().registerMessage(PacketRenameItem.Handler.class, PacketRenameItem.class, 1, Side.SERVER);
	}

}

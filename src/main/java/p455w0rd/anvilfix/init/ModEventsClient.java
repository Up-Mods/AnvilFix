package p455w0rd.anvilfix.init;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import p455w0rd.anvilfix.client.GuiRepairHacked;
import p455w0rd.anvilfix.init.ModConfig.Options;
import p455w0rd.anvilfix.packets.PacketOpenContainer;

/**
 * @author p455w0rd
 *
 */
public class ModEventsClient {

	@SubscribeEvent
	public static void onGuiOpen(GuiOpenEvent event) {
		if (Options.levelLimit == 40) {
			MinecraftForge.EVENT_BUS.unregister(ModEventsClient.class);
			return;
		}
		if (event.getGui() instanceof GuiRepair) {
			event.setGui(new GuiRepairHacked(Minecraft.getMinecraft().player.inventory, Minecraft.getMinecraft().world));
			BlockPos pos = GuiRepairHacked.position;
			if (pos != null && Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.ANVIL) {
				ModNetworking.getInstance().sendToServer(new PacketOpenContainer(pos));
			}
		}
	}

}

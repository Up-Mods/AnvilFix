package p455w0rd.anvilfix.init;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import p455w0rd.anvilfix.client.GuiRepairHacked;
import p455w0rd.anvilfix.container.ContainerRepairHacked;

/**
 * @author p455w0rd
 *
 */
public class ModGuiHandler implements IGuiHandler {

	private static ModGuiHandler instance;

	public static ModGuiHandler getInstance() {
		if (instance == null) {
			instance = new ModGuiHandler();
		}
		return instance;
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == 0) {
			return new ContainerRepairHacked(world, new BlockPos(x, y, z), player);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == 0) {
			return new GuiRepairHacked(player.inventory, world);
		}
		return null;
	}

}

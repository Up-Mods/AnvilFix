package p455w0rd.anvilfix.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import p455w0rd.anvilfix.container.ContainerRepairHacked;

/**
 * @author p455w0rd
 *
 */
public class PacketRenameItem implements IMessage {

	String newName;

	public PacketRenameItem() {

	}

	public PacketRenameItem(String newName) {
		this.newName = newName;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		newName = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, newName);
	}

	public static class Handler implements IMessageHandler<PacketRenameItem, IMessage> {
		@Override
		public IMessage onMessage(PacketRenameItem message, MessageContext ctx) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				EntityPlayer player = ctx.getServerHandler().player;
				if (player.openContainer instanceof ContainerRepairHacked) {
					ContainerRepairHacked containerrepair = (ContainerRepairHacked) player.openContainer;
					if (message.newName.length() <= 35) {
						containerrepair.updateItemName(message.newName);
					}
				}
			});
			return null;
		}
	}

}

package p455w0rd.anvilfix.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import p455w0rd.anvilfix.container.ContainerRepairHacked;

/**
 * @author p455w0rd
 *
 */
public class PacketOpenContainer implements IMessage {

	BlockPos pos;

	public PacketOpenContainer() {

	}

	public PacketOpenContainer(BlockPos pos) {
		this.pos = pos;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
	}

	public static class Handler implements IMessageHandler<PacketOpenContainer, IMessage> {
		@Override
		public IMessage onMessage(PacketOpenContainer message, MessageContext ctx) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				EntityPlayer player = ctx.getServerHandler().player;
				player.openContainer = new ContainerRepairHacked(player.getEntityWorld(), message.pos, player);
			});
			return null;
		}
	}

}
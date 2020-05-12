package net.morimori.imp.client.handler;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.network.NetworkEvent;
import net.morimori.imp.packet.CassetteDeckSyncMessage;
import net.morimori.imp.tileentity.CassetteDeckTileEntity;

public class CassetteDeckSyncMessageHandler {
	private static Minecraft mc = Minecraft.getInstance();

	public static void reversiveMessage(CassetteDeckSyncMessage message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().setPacketHandled(true);
		if (mc.world.dimension.getType().getId() != message.dim)
			return;

		TileEntity tileentity = mc.world.getTileEntity(message.pos);

		if (tileentity instanceof CassetteDeckTileEntity) {
			((CassetteDeckTileEntity) tileentity).clientSync(message);
		}
	}
}

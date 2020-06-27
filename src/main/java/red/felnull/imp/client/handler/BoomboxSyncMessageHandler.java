package red.felnull.imp.client.handler;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.network.NetworkEvent;
import red.felnull.imp.packet.BoomboxSyncMessage;
import red.felnull.imp.tileentity.BoomboxTileEntity;

public class BoomboxSyncMessageHandler {
	private static Minecraft mc = Minecraft.getInstance();

	public static void reversiveMessage(BoomboxSyncMessage message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().setPacketHandled(true);
		if (0 != message.dim)
			return;

		TileEntity tileentity = mc.world.getTileEntity(message.pos);

		if (tileentity instanceof BoomboxTileEntity) {
			((BoomboxTileEntity) tileentity).clientSync(message);
		}
	}
}

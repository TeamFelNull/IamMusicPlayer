package red.felnull.imp.client.handler;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.network.NetworkEvent;
import red.felnull.imp.packet.CassetteDeckSyncMessage;
import red.felnull.imp.tileentity.CassetteDeckTileEntity;

public class CassetteDeckSyncMessageHandler {
    private static Minecraft mc = Minecraft.getInstance();

    public static void reversiveMessage(CassetteDeckSyncMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().setPacketHandled(true);
        if (0 != message.dim)
            return;

        TileEntity tileentity = mc.world.getTileEntity(message.pos);

        if (tileentity instanceof CassetteDeckTileEntity) {
            ((CassetteDeckTileEntity) tileentity).clientSync(message);
        }
    }
}

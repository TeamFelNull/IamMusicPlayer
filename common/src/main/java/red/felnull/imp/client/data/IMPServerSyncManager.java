package red.felnull.imp.client.data;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;

import java.util.List;

public class IMPServerSyncManager {
    private static final Minecraft mc = Minecraft.getInstance();
    private static final IMPServerSyncManager INSTANCE = new IMPServerSyncManager();

    public static IMPServerSyncManager getInstance() {
        return INSTANCE;
    }

    public List<PlayerInfo> getOnlinePlayers() {
        ClientPacketListener cpl = mc.player.connection;
        return cpl.getOnlinePlayers().stream().toList();
    }

}

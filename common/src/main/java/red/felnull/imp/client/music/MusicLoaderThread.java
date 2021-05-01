package red.felnull.imp.client.music;

import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import red.felnull.imp.client.music.loader.IMusicPlayerLoader;
import red.felnull.imp.client.music.player.IMusicPlayer;
import red.felnull.imp.music.resource.MusicLocation;
import red.felnull.imp.packet.MusicResponseMessage;
import red.felnull.otyacraftengine.util.IKSGPacketUtil;

import java.util.UUID;

public class MusicLoaderThread extends Thread {
    private static final Logger LOGGER = LogManager.getLogger(MusicLoaderThread.class);
    private final UUID uuid;
    private final MusicLocation location;
    private final long startPosition;

    public MusicLoaderThread(UUID uuid, MusicLocation location, long startPosition) {
        this.setName("Music Loader Thread: " + location.getIdentifier());
        this.uuid = uuid;
        this.location = location;
        this.startPosition = startPosition;
    }

    @Override
    public void run() {
        if (!MusicPlayerRegistry.isContains(location.getLoaderName())) {
            LOGGER.error("Non existent music loader: " + location.getLoaderName());
            IKSGPacketUtil.sendToServerPacket(new MusicResponseMessage(MusicResponseMessage.Type.READY_FAILURE, uuid, 0));
            return;
        }

        IMusicPlayerLoader loader = MusicPlayerRegistry.getLoader(location.getLoaderName());
        IMusicPlayer player = null;
        long startTime = System.currentTimeMillis();
        try {
            player = loader.createMusicPlayer(location.getIdentifier());
            player.ready(startPosition);

        } catch (Exception ex) {
            LOGGER.error("Failed to load music: " + location.getIdentifier(), ex);
            IKSGPacketUtil.sendToServerPacket(new MusicResponseMessage(MusicResponseMessage.Type.READY_FAILURE, uuid, 0));
            if (player != null)
                player.destroy();
            return;
        }
        long eqTime = System.currentTimeMillis() - startTime;
        IKSGPacketUtil.sendToServerPacket(new MusicResponseMessage(MusicResponseMessage.Type.READY_COMPLETE, uuid, eqTime));
        MusicEngine.getInstance().musicPlayers.put(uuid, player);
    }
}

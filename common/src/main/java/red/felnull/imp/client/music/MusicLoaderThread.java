package red.felnull.imp.client.music;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import red.felnull.imp.api.client.IMPClientRegistry;
import red.felnull.imp.client.music.loader.IMusicLoader;
import red.felnull.imp.client.music.player.IMusicPlayer;
import red.felnull.imp.music.info.MusicPlayInfo;
import red.felnull.imp.music.resource.MusicLocation;
import red.felnull.imp.packet.MusicResponseMessage;
import red.felnull.otyacraftengine.util.IKSGPacketUtil;

import java.util.UUID;

public class MusicLoaderThread extends Thread {
    private static final Logger LOGGER = LogManager.getLogger(MusicLoaderThread.class);
    private final UUID uuid;
    private final MusicLocation location;
    private final long startPosition;
    private final long startTime;
    private final boolean autPlay;
    private final MusicPlayInfo autPlayInfo;
    private boolean stop;

    public MusicLoaderThread(UUID uuid, MusicLocation location, long startPosition, boolean autPlay, MusicPlayInfo autPlayInfo) {
        this.setName("Music Loader Thread: " + location.getIdentifier());
        this.uuid = uuid;
        this.location = location;
        this.startPosition = startPosition;
        this.autPlay = autPlay;
        this.autPlayInfo = autPlayInfo;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void run() {
        if (!IMPClientRegistry.isLoaderContains(location.getLoaderName())) {
            LOGGER.error("Non existent music loader: " + location.getLoaderName());
            if (stop || autPlay)
                return;
            IKSGPacketUtil.sendToServerPacket(new MusicResponseMessage(MusicResponseMessage.Type.READY_FAILURE, uuid, 0));
            return;
        }

        IMusicLoader loader = IMPClientRegistry.getLoader(location.getLoaderName());
        IMusicPlayer player = null;
        long startTime = System.currentTimeMillis();
        try {
            player = loader.createMusicPlayer(location);
            player.ready(startPosition);

        } catch (Exception ex) {
            LOGGER.error("Failed to load music: " + location.getIdentifier(), ex);
            if (!stop && !autPlay)
                IKSGPacketUtil.sendToServerPacket(new MusicResponseMessage(MusicResponseMessage.Type.READY_FAILURE, uuid, 0));
            if (player != null)
                player.destroy();
            return;
        }

        long eqTime = System.currentTimeMillis() - startTime;
        if (!stop && !autPlay)
            IKSGPacketUtil.sendToServerPacket(new MusicResponseMessage(MusicResponseMessage.Type.READY_COMPLETE, uuid, eqTime));
        if (stop)
            return;
        MusicEngine.getInstance().musicPlayers.put(uuid, new MusicEngine.MusicPlayingEntry(player, null));
        MusicEngine.getInstance().loaders.remove(uuid);
        if (autPlay && autPlayInfo != null)
            MusicEngine.getInstance().play(uuid, eqTime, autPlayInfo);
    }

    public void stopped() {
        stop = true;
    }

    public MusicLocation getLocation() {
        return location;
    }

    public long getStartPosition() {
        return startPosition;
    }

    public long getCurrentDelayStartPosition() {
        return startPosition + (System.currentTimeMillis() - startTime);
    }

    public MusicPlayInfo getAutPlayInfo() {
        return autPlayInfo;
    }
}

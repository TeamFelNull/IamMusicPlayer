package red.felnull.imp.client.handler;

import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import red.felnull.imp.client.data.MusicDownloader;
import red.felnull.imp.client.music.ClientWorldMusicManager;
import red.felnull.imp.data.IMPWorldData;
import red.felnull.imp.data.WorldMusicFileDataInfo;
import red.felnull.otyacraftengine.api.event.common.ResponseEvent;

public class ClientMusicHandler {
    @SubscribeEvent
    public static void onServerResponse(ResponseEvent.Server e) {
        if (e.getLocation().equals(IMPWorldData.WORLDMUSICFILEDATA)) {
            if (e.getId() == 0) {
                MusicDownloader.instance().WAITINFOUUID.put(e.getMessage(), new WorldMusicFileDataInfo(e.getData()));
            } else if (e.getId() == 1) {
                MusicDownloader.instance().WAITINFOUUID.put(e.getMessage(), new WorldMusicFileDataInfo(true));
            }
        }
    }

    @SubscribeEvent
    public static void onLogOut(WorldEvent.Unload e) {
        if (e.getWorld().isRemote()) {
            ClientWorldMusicManager.instance().stopAllMusicPlayer();
        }
    }
}

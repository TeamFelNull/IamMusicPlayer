package red.felnull.imp.client.handler;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import red.felnull.imp.client.data.MusicDownloader;
import red.felnull.imp.data.IMPWorldData;
import red.felnull.imp.data.WorldMusicFileDataInfo;
import red.felnull.otyacraftengine.api.event.common.ResponseEvent;

import java.util.UUID;

public class WorldMusicClientHandler {
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
}

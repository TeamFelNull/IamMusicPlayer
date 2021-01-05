package red.felnull.imp.handler;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import red.felnull.imp.data.IMPWorldData;
import red.felnull.imp.music.ServerWorldMusicManager;
import red.felnull.otyacraftengine.api.event.common.ResponseEvent;

import java.util.UUID;

public class WorldRingerHandler {
    @SubscribeEvent
    public static void onClientResponse(ResponseEvent.Client e) {
        if (e.getLocation().equals(IMPWorldData.MUSIC_RINGD)) {
            if (e.getId() == 0) {
                ServerWorldMusicManager.instance().loadingFinish(UUID.fromString(e.getMessage()), e.getPlayer());
            }
        }
    }
}

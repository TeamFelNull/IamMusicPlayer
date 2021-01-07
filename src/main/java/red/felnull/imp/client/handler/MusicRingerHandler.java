package red.felnull.imp.client.handler;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import red.felnull.imp.client.music.ClientWorldMusicManager;
import red.felnull.imp.client.music.MusicRinger;
import red.felnull.imp.data.IMPWorldData;
import red.felnull.otyacraftengine.api.event.common.ResponseEvent;

import java.util.UUID;

public class MusicRingerHandler {
    @SubscribeEvent
    public static void onResopnse(ResponseEvent.Server e) {
        if (e.getLocation().equals(IMPWorldData.WORLD_RINGD)) {
            if (e.getId() == 0) {
                ClientWorldMusicManager.instance().playMusicPlayer(UUID.fromString(e.getMessage()));
            } else if (e.getId() == 1) {
                ClientWorldMusicManager.instance().stopMusicPlayer(UUID.fromString(e.getMessage()));
            }
        }
    }
}

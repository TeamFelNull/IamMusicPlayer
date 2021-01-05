package red.felnull.imp.client.handler;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import red.felnull.imp.data.IMPWorldData;
import red.felnull.otyacraftengine.api.event.common.ResponseEvent;

public class MusicRingerHandler {
    @SubscribeEvent
    public static void onResopnse(ResponseEvent.Server e) {
        if (e.getLocation().equals(IMPWorldData.WORLD_RINGD)) {

        }
    }
}

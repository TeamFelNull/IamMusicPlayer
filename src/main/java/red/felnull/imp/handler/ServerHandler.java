package red.felnull.imp.handler;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import red.felnull.imp.data.IMPWorldData;
import red.felnull.imp.data.PlayListGuildManeger;
import red.felnull.otyacraftengine.api.event.common.ResponseEvent;

public class ServerHandler {
    @SubscribeEvent
    public static void onClientResponse(ResponseEvent.Client e) {

        if (e.getLocation().equals(IMPWorldData.PLAYLIST_REQUEST)) {
            if (e.getId() == 0) {
                PlayListGuildManeger.instance().joinPlayList(e.getPlayer(), e.getMessage());
            }
        }

    }
}

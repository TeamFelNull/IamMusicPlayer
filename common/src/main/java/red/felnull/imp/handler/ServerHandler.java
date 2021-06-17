package red.felnull.imp.handler;

import net.minecraft.resources.ResourceLocation;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.music.MusicManager;
import red.felnull.otyacraftengine.api.event.SimpleMessageEvent;

public class ServerHandler {
    public static void onSimpleMessage(SimpleMessageEvent.Server e) {
        if (e.getLocation().equals(new ResourceLocation(IamMusicPlayer.MODID, "simple_request"))) {
            MusicManager manager = MusicManager.getInstance();
            if (e.getId() == 0) {
                manager.addPlayerToMusicPlayList(e.getData().getUUID("UUID"), e.getPlayer().getGameProfile().getId());
            }
        }
    }
}

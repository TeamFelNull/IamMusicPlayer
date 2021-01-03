package red.felnull.imp.handler;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import red.felnull.imp.data.IMPWorldData;
import red.felnull.imp.data.PlayMusicManeger;
import red.felnull.imp.music.resource.PlayMusic;
import red.felnull.otyacraftengine.api.event.common.ReceiverEvent;
import red.felnull.otyacraftengine.data.SendReceiveLogger;

import java.util.HashMap;
import java.util.Map;

public class MusicReceiveHandler {

    public static final Map<String, SendReceiveLogger.SRResult> downloadble = new HashMap<>();

    @SubscribeEvent
    public static void onReceivePos(ReceiverEvent.Server.Pos e) {
        if (e.getLocation().equals(IMPWorldData.SERVER_MUSIC_DATA)) {
            if (PlayMusicManeger.waitingDownloads.containsKey(e.getName())) {
                PlayMusic playMusic = PlayMusicManeger.waitingDownloads.get(e.getName()).getMusic();
                PlayMusic.addPlayMusic(playMusic);
                playMusic.addPlayMusicToPlayList(PlayMusicManeger.waitingDownloads.get(e.getName()).getList());
                PlayMusicManeger.waitingDownloads.remove(e.getName());
            } else {
                downloadble.put(e.getName(), e.getReceiveResult());
            }
        }
    }
}

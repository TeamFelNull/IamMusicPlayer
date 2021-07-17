package red.felnull.imp.client.handler;

import net.minecraft.resources.ResourceLocation;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.client.data.IMPSyncClientManager;
import red.felnull.otyacraftengine.api.event.SimpleMessageEvent;
import red.felnull.otyacraftengine.api.event.client.SpriteRegiserEvent;

public class ClientHandler {
    public static void registSprite(SpriteRegiserEvent e) {
        e.registSprite(new ResourceLocation(IamMusicPlayer.MODID, "block/music_sharing_device/power_button_on"));
        e.registSprite(new ResourceLocation(IamMusicPlayer.MODID, "block/music_sharing_device/power_button_off"));
        e.registSprite(new ResourceLocation(IamMusicPlayer.MODID, "block/music_sharing_device/power_miniscreen_on"));
        e.registSprite(new ResourceLocation(IamMusicPlayer.MODID, "block/music_sharing_device/power_miniscreen_off"));
        e.registSprite(new ResourceLocation(IamMusicPlayer.MODID, "block/music_sharing_device/monitor"));
        e.registSprite(new ResourceLocation(IamMusicPlayer.MODID, "block/music_sharing_device/mousepad"));
        e.registSprite(new ResourceLocation(IamMusicPlayer.MODID, "block/music_sharing_device/mouse_hontai"));
        e.registSprite(new ResourceLocation(IamMusicPlayer.MODID, "block/music_sharing_device/mouse_wheel"));

        e.registSprite(new ResourceLocation(IamMusicPlayer.MODID, "item/cassette_tape/cassette_tape_base"));
        e.registSprite(new ResourceLocation(IamMusicPlayer.MODID, "item/cassette_tape/tape_core"));
        e.registSprite(new ResourceLocation(IamMusicPlayer.MODID, "item/cassette_tape/tape_roll"));
        e.registSprite(new ResourceLocation(IamMusicPlayer.MODID, "item/cassette_tape/label"));
    }

    public static void onSimpleMessage(SimpleMessageEvent.Client e) {
        if (e.getLocation().equals(new ResourceLocation(IamMusicPlayer.MODID, "sync_update"))) {
            if (e.getId() == 0) {
                IMPSyncClientManager.getInstance().resetMusicPlayListSync();
            }
        }
    }
}

package red.felnull.imp.client.handler;

import net.minecraft.resources.ResourceLocation;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.otyacraftengine.api.event.client.SpriteRegiserEvent;

public class ClientHandler {
    public static void registSprite(SpriteRegiserEvent e) {
        e.registSprite(new ResourceLocation(IamMusicPlayer.MODID, "block/music_sharing_device/power_button_on"));
        e.registSprite(new ResourceLocation(IamMusicPlayer.MODID, "block/music_sharing_device/power_button_off"));
    }
}

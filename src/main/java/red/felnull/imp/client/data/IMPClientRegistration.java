package red.felnull.imp.client.data;

import net.minecraft.util.ResourceLocation;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.client.renderer.handanimation.BoomboxHandAnimation;
import red.felnull.otyacraftengine.api.registries.OERegistries;

public class IMPClientRegistration {
    public static void init() {
        OERegistries.registrierHandAnimation(new ResourceLocation(IamMusicPlayer.MODID, "boombox"), new BoomboxHandAnimation());
    }
}

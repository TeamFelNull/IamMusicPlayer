package red.felnull.imp.client.handler;

import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import red.felnull.imp.IamMusicPlayer;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientHandler {

    @SubscribeEvent
    public static void onTextureStith(TextureStitchEvent.Pre e) {
        if (e.getMap().getTextureLocation().equals(AtlasTexture.LOCATION_BLOCKS_TEXTURE)) {
            e.addSprite(new ResourceLocation(IamMusicPlayer.MODID, "block/parabolic_antenna"));
            e.addSprite(new ResourceLocation(IamMusicPlayer.MODID, "block/kamesuta_antenna"));
        }
    }

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent e) {

    }
}

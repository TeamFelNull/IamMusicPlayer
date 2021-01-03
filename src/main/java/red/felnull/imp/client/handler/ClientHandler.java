package red.felnull.imp.client.handler;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientHandler {

    @SubscribeEvent
    public static void onTextureStith(TextureStitchEvent.Pre e) {
      /*  if (e.getMap().getTextureLocation().equals(AtlasTexture.LOCATION_BLOCKS_TEXTURE)) {
            e.addSprite(new ResourceLocation(IamMusicPlayer.MODID, "item/parabolic_antenna"));
            e.addSprite(new ResourceLocation(IamMusicPlayer.MODID, "item/kamesuta_antenna"));
        }*/
    }

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent e) {

    }
}

package red.felnull.imp.client.renderer.tileentity;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import red.felnull.imp.tileentity.IMPTileEntityTypes;

public class IMPTileEntityRenderers {
    public static void registerTileEntityRenderer() {
        ClientRegistry.bindTileEntityRenderer(IMPTileEntityTypes.MUSIC_SHARING_DEVICE, MusicSharingDeviceTileEntityRenderer::new);
    }
}

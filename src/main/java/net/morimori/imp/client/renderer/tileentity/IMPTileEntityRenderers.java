package net.morimori.imp.client.renderer.tileentity;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.morimori.imp.tileentity.IMPTileEntityTypes;

public class IMPTileEntityRenderers {
    public static void registerTileEntityRenderer() {
        ClientRegistry.bindTileEntityRenderer(IMPTileEntityTypes.BOOMBOX, BoomboxTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(IMPTileEntityTypes.SOUNDFILE_UPLOADER,
                SoundFileUploaderTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(IMPTileEntityTypes.CASSETTE_DECK, CassetteDeckTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(IMPTileEntityTypes.CASSETTE_STORING, CassetteStoringRenderer::new);

    }
}

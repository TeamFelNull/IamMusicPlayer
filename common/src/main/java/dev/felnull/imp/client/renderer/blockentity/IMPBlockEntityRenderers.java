package dev.felnull.imp.client.renderer.blockentity;

import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.felnull.imp.blockentity.IMPBlockEntitys;

public class IMPBlockEntityRenderers {
    public static void init() {
        BlockEntityRendererRegistry.register(IMPBlockEntitys.MUSIC_MANAGER.get(), MusicManagerBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(IMPBlockEntitys.CASSETTE_DECK.get(), CassetteDeckBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(IMPBlockEntitys.BOOMBOX.get(), BoomboxBlockEntityRenderer::new);
    }
}

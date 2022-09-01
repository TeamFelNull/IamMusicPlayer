package dev.felnull.imp.client.renderer.blockentity;

import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.felnull.imp.blockentity.IMPBlockEntities;

public class IMPBlockEntityRenderers {
    public static void init() {
        BlockEntityRendererRegistry.register(IMPBlockEntities.MUSIC_MANAGER.get(), MusicManagerBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(IMPBlockEntities.CASSETTE_DECK.get(), CassetteDeckBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(IMPBlockEntities.BOOMBOX.get(), BoomboxBlockEntityRenderer::new);
    }
}

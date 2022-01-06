package dev.felnull.imp.client.renderer.blockentity;

import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.felnull.imp.client.music.blockentity.IMPBlockEntitys;

public class IMPBlockEntityRenderers {
    public static void init() {
        BlockEntityRendererRegistry.register(IMPBlockEntitys.MUSIC_MANAGER, MusicManagerBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(IMPBlockEntitys.BOOMBOX, BoomboxBlockEntityRenderer::new);
    }
}

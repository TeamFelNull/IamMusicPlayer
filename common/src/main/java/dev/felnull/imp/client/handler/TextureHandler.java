package dev.felnull.imp.client.handler;

import dev.architectury.event.EventResult;
import dev.felnull.imp.client.renderer.PlayImageRenderer;
import dev.felnull.otyacraftengine.client.event.TextureEvent;
import org.jetbrains.annotations.NotNull;

public class TextureHandler {
    public static void init() {
        TextureEvent.CHECK_TEXTURE_URL.register(TextureHandler::onCheckURL);
        TextureEvent.SWAP_TEXTURE_URL.register(TextureHandler::onSwapURL);
    }

    private static void onSwapURL(String baseUrl, TextureEvent.TextureURLSwapper swapper) {
        var ret = PlayImageRenderer.getInstance().getSwapURL(baseUrl);
        if (ret != null)
            swapper.setURL(ret);
    }

    private static EventResult onCheckURL(@NotNull String url) {
        if (PlayImageRenderer.getInstance().isAllowURL(url))
            return EventResult.interruptTrue();
        return EventResult.pass();
    }
}

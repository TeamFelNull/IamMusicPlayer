package dev.felnull.imp.client.handler;

import dev.architectury.event.EventResult;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.otyacraftengine.api.event.client.FabricOBJLoaderEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

public class ClientHandler {
    private static final Minecraft mc = Minecraft.getInstance();

    public static void init() {
        FabricOBJLoaderEvent.LOAD.register(ClientHandler::objLoad);
    }

    public static EventResult objLoad(ResourceLocation resourceLocation) {
        if (IamMusicPlayer.MODID.equals(resourceLocation.getNamespace()))
            return EventResult.interruptTrue();

        return EventResult.pass();
    }
}

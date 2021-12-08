package dev.felnull.imp.client.handler;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.client.music.MusicSyncManager;
import dev.felnull.otyacraftengine.api.event.client.FabricOBJLoaderEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;

public class ClientHandler {
    private static final Minecraft mc = Minecraft.getInstance();

    public static void init() {
        FabricOBJLoaderEvent.LOAD.register(ClientHandler::objLoad);
        ClientLifecycleEvent.CLIENT_LEVEL_LOAD.register(ClientHandler::onClientLevelLoad);
    }

    public static EventResult objLoad(ResourceLocation resourceLocation) {
        if (IamMusicPlayer.MODID.equals(resourceLocation.getNamespace()))
            return EventResult.interruptTrue();

        return EventResult.pass();
    }

    public static void onClientLevelLoad(ClientLevel clientLevel) {
        MusicSyncManager.getInstance().reset();
    }

}

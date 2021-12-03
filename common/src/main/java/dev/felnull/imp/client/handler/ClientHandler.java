package dev.felnull.imp.client.handler;

import dev.architectury.event.EventResult;
import dev.felnull.otyacraftengine.api.event.client.FabricOBJLoaderEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;

public class ClientHandler {
    private static final Minecraft mc = Minecraft.getInstance();

    public static void init() {
        FabricOBJLoaderEvent.LOAD.register(ClientHandler::objLoad);
    }

    public static EventResult objLoad(ResourceLocation resourceLocation) {
        //  if (IamMusicPlayer.MODID.equals(resourceLocation.getNamespace()))
        if (resourceLocation instanceof ModelResourceLocation modelResourceLocation)
            System.out.println(modelResourceLocation);
        //return EventResult.interruptTrue();

        return EventResult.pass();
    }
}

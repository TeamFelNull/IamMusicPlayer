package red.felnull.imp.proxy;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import red.felnull.imp.data.IMPWorldData;
import red.felnull.imp.data.PlayListGuildManeger;
import red.felnull.imp.data.PlayMusicManeger;
import red.felnull.imp.handler.ServerHandler;
import red.felnull.imp.packet.PacketHandler;

public class CommonProxy {
    public void preInit() {
        PacketHandler.init();
        PlayListGuildManeger.init();
        PlayMusicManeger.init();
        IMPWorldData.register();
        MinecraftForge.EVENT_BUS.register(ServerHandler.class);
    }

    public void init() {

    }

    public void posInit() {

    }

    public Minecraft getMinecraft() {
        return null;
    }
}

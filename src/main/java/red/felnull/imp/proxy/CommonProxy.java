package red.felnull.imp.proxy;

import net.minecraft.client.Minecraft;
import red.felnull.imp.data.IMPWorldData;
import red.felnull.imp.data.PlayListGuildManeger;
import red.felnull.imp.packet.PacketHandler;

public class CommonProxy {
    public void preInit() {
        PacketHandler.init();
        PlayListGuildManeger.init();
        IMPWorldData.register();
    }

    public void init() {

    }

    public void posInit() {

    }

    public Minecraft getMinecraft() {
        return null;
    }
}

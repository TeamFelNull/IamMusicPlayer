package red.felnull.imp.proxy;

import net.minecraft.client.Minecraft;
import red.felnull.imp.data.PlayListGuildManeger;

public class CommonProxy {
    public void preInit() {
        PlayListGuildManeger.init();
    }

    public void init() {

    }

    public void posInit() {

    }

    public Minecraft getMinecraft() {
        return null;
    }
}

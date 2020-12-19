package red.felnull.imp.proxy;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import red.felnull.imp.data.IMPWorldData;
import red.felnull.imp.data.PlayListGuildManeger;
import red.felnull.imp.data.PlayMusicManeger;
import red.felnull.imp.ffmpeg.FFmpegManeger;
import red.felnull.imp.handler.MusicReceiveHandler;
import red.felnull.imp.handler.ServerHandler;
import red.felnull.imp.handler.WorldMusicServerHandler;
import red.felnull.imp.packet.PacketHandler;

public class CommonProxy {
    public void preInit() {
        FFmpegManeger.init();
        PacketHandler.init();
        PlayListGuildManeger.init();
        PlayMusicManeger.init();
        IMPWorldData.register();
        MinecraftForge.EVENT_BUS.register(ServerHandler.class);
        MinecraftForge.EVENT_BUS.register(MusicReceiveHandler.class);
        MinecraftForge.EVENT_BUS.register(WorldMusicServerHandler.class);
    }

    public void init() {
    }

    public void posInit() {
        FFmpegManeger.instance().check();
    }

    public Minecraft getMinecraft() {
        return null;
    }

    public void addFFmpegLoadToast() {
    }
}

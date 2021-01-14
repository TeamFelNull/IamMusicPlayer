package red.felnull.imp.proxy;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import red.felnull.imp.data.IMPWorldData;
import red.felnull.imp.data.PlayListGuildManeger;
import red.felnull.imp.data.PlayMusicManeger;
import red.felnull.imp.exception.IMPFFmpegException;
import red.felnull.imp.ffmpeg.FFmpegManeger;
import red.felnull.imp.handler.*;
import red.felnull.imp.music.ServerWorldMusicManager;
import red.felnull.imp.packet.PacketHandler;
import red.felnull.imp.recipe.ComposterRecipes;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class CommonProxy {
    public void preInit() {
        FFmpegManeger.init();
        PacketHandler.init();
        PlayListGuildManeger.init();
        PlayMusicManeger.init();
        IMPWorldData.register();
        ComposterRecipes.register();
        MinecraftForge.EVENT_BUS.register(ServerHandler.class);
        MinecraftForge.EVENT_BUS.register(MusicReceiveHandler.class);
        MinecraftForge.EVENT_BUS.register(WorldMusicServerHandler.class);
        MinecraftForge.EVENT_BUS.register(WorldRingerHandler.class);
        MinecraftForge.EVENT_BUS.register(TradeHandler.class);
    }

    public void init() {
        ServerWorldMusicManager.init();
    }

    public void posInit() {
        FFmpegManeger.instance().check();
    }

    public Minecraft getMinecraft() {
        return null;
    }

    public void addFFmpegLoadToast() {
    }

    public void addFFmpegErrorToast(IMPFFmpegException exception) {
    }

    public void addFFmpegTestFinishToast() {
    }

    public InputStream getFFmpegTestData() {
        try {
            return new URL("https://github.com/TeamFelnull/IamMusicPlayer/raw/master/ffmpeg/ffmpeg_testdata").openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

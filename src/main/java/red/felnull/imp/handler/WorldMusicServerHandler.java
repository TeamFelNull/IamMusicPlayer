package red.felnull.imp.handler;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import red.felnull.imp.data.IMPWorldData;
import red.felnull.imp.data.WorldMusicFileDataInfo;
import red.felnull.imp.util.MusicUtils;
import red.felnull.imp.util.PathUtils;
import red.felnull.otyacraftengine.api.ResponseSender;
import red.felnull.otyacraftengine.api.event.common.ResponseEvent;

import java.io.File;

public class WorldMusicServerHandler {
    @SubscribeEvent
    public static void onClientResponse(ResponseEvent.Client e) {
        if (e.getLocation().equals(IMPWorldData.WORLDMUSICFILEDATAINFO)) {
            if (e.getId() == 0) {
                try {
                    File musicfile = PathUtils.getWorldMusic(e.getData().getString("uuid")).toFile();
                    if (musicfile.exists()) {
                        WorldMusicFileDataInfo wmfd = new WorldMusicFileDataInfo(MusicUtils.getMP3MillisecondDuration(musicfile), musicfile.length());
                        ResponseSender.sendToClient(e.getPlayer(), IMPWorldData.WORLDMUSICFILEDATAINFO, 0, e.getMessage(), wmfd.write(new CompoundNBT()));
                    } else {
                        ResponseSender.sendToClient(e.getPlayer(), IMPWorldData.WORLDMUSICFILEDATAINFO, 1, e.getMessage(), new CompoundNBT());
                    }
                } catch (Exception ex) {
                    ResponseSender.sendToClient(e.getPlayer(), IMPWorldData.WORLDMUSICFILEDATAINFO, 1, e.getMessage(), new CompoundNBT());
                    ex.printStackTrace();
                }
            }
        }
    }
}

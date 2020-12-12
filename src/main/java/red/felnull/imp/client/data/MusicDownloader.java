package red.felnull.imp.client.data;


import net.minecraft.nbt.CompoundNBT;
import red.felnull.imp.data.IMPWorldData;
import red.felnull.imp.data.WorldMusicFileDataInfo;
import red.felnull.imp.exception.IMPWorldMusicException;
import red.felnull.otyacraftengine.api.ResponseSender;

import java.util.HashMap;
import java.util.UUID;

public class MusicDownloader {
    private static MusicDownloader INSTANCE;

    public HashMap<UUID, WorldMusicFileDataInfo> WAITINFOUUID = new HashMap<>();

    public static void init() {
        INSTANCE = new MusicDownloader();
    }

    public static MusicDownloader instance() {
        return INSTANCE;
    }

    public WorldMusicFileDataInfo getWorldMusicFileDataInfo(String uuid) throws InterruptedException, IMPWorldMusicException {
        if (Thread.currentThread().getName().equals("Render thread"))
            throw new IMPWorldMusicException("Render thread is NG");

        UUID rcuuid = UUID.randomUUID();
        WAITINFOUUID.put(rcuuid, null);
        CompoundNBT tag = new CompoundNBT();
        tag.putString("uuid", uuid);
        ResponseSender.sendToServer(IMPWorldData.WORLDMUSICFILEDATAINFO, 0, rcuuid.toString(), tag);
        long fristTime = System.currentTimeMillis();
        while (WAITINFOUUID.get(rcuuid) == null) {
            Thread.sleep(100);
            if (System.currentTimeMillis() - fristTime > 10000)
                throw new IMPWorldMusicException("Time out of response from world");
        }

        if (WAITINFOUUID.get(rcuuid).isError())
            throw new IMPWorldMusicException("World Music File is error");

        return WAITINFOUUID.get(rcuuid);
    }
}



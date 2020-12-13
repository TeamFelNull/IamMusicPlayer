package red.felnull.imp.client.data;


import net.minecraft.nbt.CompoundNBT;
import red.felnull.imp.data.IMPWorldData;
import red.felnull.imp.data.WorldMusicFileDataInfo;
import red.felnull.imp.exception.IMPWorldMusicException;
import red.felnull.otyacraftengine.api.ResponseSender;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MusicDownloader {
    private static MusicDownloader INSTANCE;

    public final Map<String, WorldMusicFileDataInfo> WAITINFOUUID = new HashMap<>();
    public final Map<UUID, byte[]> WORLDMUSICBYTE = new HashMap<>();

    public static void init() {
        INSTANCE = new MusicDownloader();
    }

    public static MusicDownloader instance() {
        return INSTANCE;
    }

    public WorldMusicFileDataInfo getWorldMusicFileDataInfo(String uuid) throws InterruptedException, IMPWorldMusicException {
        if (Thread.currentThread().getName().equals("Render thread"))
            throw new IMPWorldMusicException("Render thread is NG");

        WAITINFOUUID.put(uuid, null);
        CompoundNBT tag = new CompoundNBT();
        ResponseSender.sendToServer(IMPWorldData.WORLDMUSICFILEDATA, 0, uuid, tag);
        long fristTime = System.currentTimeMillis();
        while (WAITINFOUUID.get(uuid) == null) {
            Thread.sleep(100);
            if (System.currentTimeMillis() - fristTime > 10000)
                throw new IMPWorldMusicException("Time out of response from world");
        }

        if (WAITINFOUUID.get(uuid).isError())
            throw new IMPWorldMusicException("World Music File is error");

        return WAITINFOUUID.get(uuid);
    }

    public UUID byteRequest(String musicUUID, int begin) throws IMPWorldMusicException {

        UUID byteUUID = UUID.randomUUID();

        if (Thread.currentThread().getName().equals("Render thread"))
            throw new IMPWorldMusicException("Render thread is NG");

        CompoundNBT tag = new CompoundNBT();
        tag.putInt("begin", begin);
        tag.putString("byteuuid", byteUUID.toString());
        ResponseSender.sendToServer(IMPWorldData.WORLDMUSICFILEDATA, 1, musicUUID, tag);
        return byteUUID;
    }

    public void byteResponse(UUID byteUUID, byte[] data) {
        WORLDMUSICBYTE.put(byteUUID, data);
    }
/*
    public void addClientWorldMusic(String uuid, byte[] data, int begin) throws IMPWorldMusicException {

        if (!WAITINFOUUID.containsKey(uuid))
            throw new IMPWorldMusicException("No World Music Info Data");


        byte[] motdata = new byte[Math.toIntExact(WAITINFOUUID.get(uuid).getByteSize())];

        if (WORLDMUSIC.containsKey(uuid))
            motdata = WORLDMUSIC.get(motdata);

        System.arraycopy(data, 0, motdata, begin, data.length);

        WORLDMUSIC.put(uuid, motdata);
    }*/

}



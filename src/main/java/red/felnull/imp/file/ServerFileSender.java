package red.felnull.imp.file;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.network.PacketDistributor;
import red.felnull.imp.config.CommonConfig;
import red.felnull.imp.packet.ClientStopRequestMessage;
import red.felnull.imp.packet.PacketHandler;
import red.felnull.imp.packet.ServerSendSoundFileMessage;
import red.felnull.imp.util.FileLoader;

public class ServerFileSender extends Thread {

    public static Map<String, Map<Integer, ServerFileSender>> senderBuffer = new HashMap<String, Map<Integer, ServerFileSender>>();
    public static Map<String, Map<Integer, Boolean>> responseWaits = new HashMap<String, Map<Integer, Boolean>>();
    public static Map<String, Map<Integer, Boolean>> stop = new HashMap<String, Map<Integer, Boolean>>();

    private Path path;
    public String pluuid;
    public boolean downloaddolder;
    private MinecraftServer ms;
    public int id;

    public static int MaxSendCont = 5;

    public ServerFileSender(String uuid, int id, Path path, boolean downloaddolder, MinecraftServer mis) {
        this.path = path;
        this.downloaddolder = downloaddolder;
        this.pluuid = uuid;
        this.ms = mis;
        this.id = id;

    }

    public static boolean canSending(String uuid) {
        int sndid = -1;

        if (!senderBuffer.containsKey(uuid)) {
            senderBuffer.put(uuid, new HashMap<Integer, ServerFileSender>());
        }

        for (int c = 0; c < MaxSendCont; c++) {
            if (!senderBuffer.get(uuid).containsKey(c)) {
                sndid = c;
                break;
            }
        }

        return sndid != -1;
    }

    public static void stopSend(String uuid) {

        if (!senderBuffer.containsKey(uuid)) {
            senderBuffer.put(uuid, new HashMap<Integer, ServerFileSender>());
        }
        for (int c = 0; c < MaxSendCont; c++) {
            if (senderBuffer.get(uuid).containsKey(c)) {
                stopSend(uuid, c);
            }
        }
    }

    public static void stopSend(String uuid, int id) {
        if (!stop.containsKey(uuid)) {
            stop.put(uuid, new HashMap<Integer, Boolean>());
        }
        stop.get(uuid).put(id, true);

    }

    public static void startSender(String uuid, Path path, boolean downloaddolder, MinecraftServer ms) {
        int sndid = -1;
        if (!senderBuffer.containsKey(uuid)) {
            senderBuffer.put(uuid, new HashMap<Integer, ServerFileSender>());
        }

        if (!responseWaits.containsKey(uuid)) {
            responseWaits.put(uuid, new HashMap<Integer, Boolean>());
        }

        if (!stop.containsKey(uuid)) {
            stop.put(uuid, new HashMap<Integer, Boolean>());
        }
        for (int c = 0; c < MaxSendCont; c++) {
            if (!senderBuffer.get(uuid).containsKey(c)) {
                sndid = c;
                break;
            }
        }
        if (sndid != -1) {
            stop.get(uuid).put(sndid, false);
            ServerFileSender SFS = new ServerFileSender(uuid, sndid, path, downloaddolder, ms);
            SFS.start();
        }

    }

    public void run() {

        senderBuffer.get(pluuid).put(id, this);

        long logtime = System.currentTimeMillis();
        byte[] bytes = FileLoader.fileBytesReader(this.path);
        boolean frist = true;
        long time = System.currentTimeMillis();
        if (bytes == null) {

            finishSend();
            return;
        }

        //		int cont = 0;

        for (int i = 0; i < bytes.length; i += CommonConfig.SEND_BYTE.get()) {
            byte[] bi = new byte[bytes.length - i >= CommonConfig.SEND_BYTE.get() ? CommonConfig.SEND_BYTE.get()
                    : bytes.length - i];
            for (int c = 0; c < CommonConfig.SEND_BYTE.get(); c++) {
                if ((i + c) < bytes.length) {
                    bi[c] = bytes[i + c];
                    //	cont++;
                }
            }
            responseWaits.get(pluuid).put(id, true);
            PacketHandler.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> ms.getPlayerList().getPlayerByUUID(UUID.fromString(pluuid))),
                    new ServerSendSoundFileMessage(bi, this.id, frist, bytes.length, this.path.toString(),
                            downloaddolder, PlayList.getWorldPlaylistNBTDataString(ms, path, "UUID")));
            frist = false;

            try {

                while (responseWaits.get(pluuid).get(id)) {
                    Thread.sleep(1);

                    if (stop.get(pluuid).get(id)) {

                        PacketHandler.INSTANCE.send(
                                PacketDistributor.PLAYER
                                        .with(() -> ms.getPlayerList().getPlayerByUUID(UUID.fromString(pluuid))),
                                new ClientStopRequestMessage(id));
                        finishSend();
                        return;
                    }

                    if (System.currentTimeMillis() - time >= 10000) {

                        finishSend();
                        return;
                    }

                    if (System.currentTimeMillis() - logtime >= 5000) {
                        logtime = System.currentTimeMillis();

                    }
                }

            } catch (InterruptedException e) {

            }
            time = System.currentTimeMillis();
        }

        finishSend();
    }

    public void finishSend() {
        senderBuffer.get(pluuid).remove(id);
    }
}

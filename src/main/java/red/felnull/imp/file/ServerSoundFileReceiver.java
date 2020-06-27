package red.felnull.imp.file;

import net.minecraft.server.MinecraftServer;
import red.felnull.imp.client.file.ClientSoundFileSender;
import red.felnull.imp.sound.SoundData;
import red.felnull.imp.util.FileHelper;
import red.felnull.imp.util.FileLoader;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ServerSoundFileReceiver extends Thread {
    private static Map<String, Map<String, FileReceiverBuffer>> RECEIVS = new HashMap<String, Map<String, FileReceiverBuffer>>();
    private MinecraftServer ms;
    private String pluuid;
    private ClientSoundFileSender.SendFolderType type;
    private String name;
    private int cont;

    public ServerSoundFileReceiver(String uuid, String name, int bytelength, MinecraftServer ms, ClientSoundFileSender.SendFolderType type) {
        this.pluuid = uuid;
        this.ms = ms;
        this.type = type;
        this.name = name;

        if (!RECEIVS.containsKey(uuid)) {
            RECEIVS.put(uuid, new HashMap<String, FileReceiverBuffer>());
        }
        RECEIVS.get(uuid).put(name, new FileReceiverBuffer(bytelength, name));
    }

    public void receiveFinish() {
        RECEIVS.get(pluuid).remove(name);
    }

    public void run() {
        long time = System.currentTimeMillis();
        while (!RECEIVS.get(pluuid).get(name).isPerfectByte()) {

            if (RECEIVS.get(pluuid).get(name).stop || (cont == RECEIVS.get(pluuid).get(name).getCont()
                    && System.currentTimeMillis() - time >= 10000)) {
                receiveFinish();
                return;
            }
            if (cont != RECEIVS.get(pluuid).get(name).getCont()) {
                cont = RECEIVS.get(pluuid).get(name).getCont();
                time = System.currentTimeMillis();
            }
            try {
                sleep(10);
            } catch (InterruptedException e) {
            }
        }
        String iuuid = UUID.randomUUID().toString();
        if (type == ClientSoundFileSender.SendFolderType.MAIN) {
            FileLoader.fileBytesWriter(RECEIVS.get(pluuid).get(name).getBytes(),
                    FileHelper.getWorldPlayerPlayListDataPath(ms, pluuid).resolve(name));
            ImagePictuers.addPictuer(iuuid, FileHelper.getWorldPlayerPlayListDataPath(ms, pluuid).resolve(name), ms);
            PlayList.addPlayeyListFileNBT(ms, pluuid, name, pluuid,
                    new SoundData(FileHelper.getWorldPlayerPlayListDataPath(ms, pluuid).resolve(name), iuuid));
        } else if (type == ClientSoundFileSender.SendFolderType.EVERYONE) {
            FileLoader.fileBytesWriter(RECEIVS.get(pluuid).get(name).getBytes(),
                    FileHelper.getWorldEveryonePlayListDataPath(ms).resolve(name));

            String uuid = UUID.randomUUID().toString();
            ImagePictuers.addPictuer(uuid, FileHelper.getWorldPlayerPlayListDataPath(ms, pluuid).resolve(name), ms);
            PlayList.addPlayeyListFileNBT(ms, "everyone", name, pluuid,
                    new SoundData(FileHelper.getWorldEveryonePlayListDataPath(ms).resolve(name), uuid));
        }
        receiveFinish();
    }

    public static void addBufferBytes(String uuid, String name, byte[] bytes) {

        if (RECEIVS.containsKey(uuid) && RECEIVS.get(uuid).containsKey(name)) {
            RECEIVS.get(uuid).get(name).addBytes(bytes);
        }

    }

    public static void receiveStop(String uuid) {
        if (RECEIVS.containsKey(uuid)) {
            RECEIVS.entrySet().forEach(n -> receiveStop(uuid, n.getKey()));
        }
    }

    public static void receiveStop(String uuid, String name) {
        if (RECEIVS.containsKey(uuid) && RECEIVS.get(uuid).containsKey(name)) {
            RECEIVS.get(uuid).get(name).stop = true;
        }
    }

}

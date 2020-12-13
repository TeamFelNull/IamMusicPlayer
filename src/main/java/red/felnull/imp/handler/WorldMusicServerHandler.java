package red.felnull.imp.handler;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import red.felnull.imp.data.IMPWorldData;
import red.felnull.imp.data.WorldMusicFileDataInfo;
import red.felnull.imp.packet.PacketHandler;
import red.felnull.imp.packet.WorldMusicSendByteMessage;
import red.felnull.imp.util.MusicUtils;
import red.felnull.imp.util.PathUtils;
import red.felnull.otyacraftengine.api.ResponseSender;
import red.felnull.otyacraftengine.api.event.common.ResponseEvent;
import red.felnull.otyacraftengine.util.IKSGMath;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class WorldMusicServerHandler {
    public static int sendByteLength = 32 * 1024;
    public static final Map<String, byte[]> WORLDMUSIC_DATA = new HashMap<>();

    @SubscribeEvent
    public static void onClientResponse(ResponseEvent.Client e) {
        if (e.getLocation().equals(IMPWorldData.WORLDMUSICFILEDATA)) {
            if (e.getId() == 0) {
                try {
                    File musicfile = PathUtils.getWorldMusic(e.getMessage()).toFile();
                    if (musicfile.exists()) {
                        WorldMusicFileDataInfo wmfd = new WorldMusicFileDataInfo(MusicUtils.getMP3MillisecondDuration(musicfile), musicfile.length());
                        ResponseSender.sendToClient(e.getPlayer(), IMPWorldData.WORLDMUSICFILEDATA, 0, e.getMessage(), wmfd.write(new CompoundNBT()));
                    } else {
                        ResponseSender.sendToClient(e.getPlayer(), IMPWorldData.WORLDMUSICFILEDATA, 1, e.getMessage(), new CompoundNBT());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    ResponseSender.sendToClient(e.getPlayer(), IMPWorldData.WORLDMUSICFILEDATA, 1, e.getMessage(), new CompoundNBT());
                }
            } else if (e.getId() == 1) {
                try {
                    byte[] data = null;
                    if (WORLDMUSIC_DATA.containsKey(e.getMessage())) {
                        data = WORLDMUSIC_DATA.get(e.getMessage());
                    } else {
                        File musicfile = PathUtils.getWorldMusic(e.getMessage()).toFile();
                        if (musicfile.exists()) {
                            data = Files.readAllBytes(musicfile.toPath());
                            WORLDMUSIC_DATA.put(e.getMessage(), data);
                        }
                    }
                    if (data == null)
                        throw new FileNotFoundException();

                    int begin = e.getData().getInt("begin");

                    int nocori = data.length - begin;

                    byte[] sendData = new byte[sendByteLength <= nocori ? sendByteLength : nocori];

                    System.arraycopy(data, begin, sendData, 0, sendData.length);

                    PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(e::getPlayer), new WorldMusicSendByteMessage(e.getData().getString("byteuuid"), sendData));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}

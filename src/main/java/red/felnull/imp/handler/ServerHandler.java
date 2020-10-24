package red.felnull.imp.handler;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import red.felnull.imp.data.IMPWorldData;
import red.felnull.imp.data.PlayListGuildManeger;
import red.felnull.imp.util.PathUtil;
import red.felnull.otyacraftengine.api.ResponseSender;
import red.felnull.otyacraftengine.api.event.common.ReceiverEvent;
import red.felnull.otyacraftengine.api.event.common.ResponseEvent;
import red.felnull.otyacraftengine.data.SendReceiveLogger;
import red.felnull.otyacraftengine.util.IKSGDataUtil;
import red.felnull.otyacraftengine.util.IKSGFileLoadUtil;

import java.io.File;

public class ServerHandler {
    @SubscribeEvent
    public static void onClientResponse(ResponseEvent.Client e) {

        if (e.getLocation().equals(IMPWorldData.PLAYLIST_REQUEST)) {
            if (e.getId() == 0) {
                PlayListGuildManeger.instance().joinPlayList(e.getPlayer(), e.getMessage());
            }
        }

    }

    @SubscribeEvent
    public static void onReceiverPos(ReceiverEvent.Server.Pos e) {
        if (e.getLocation().equals(IMPWorldData.SERVER_MUSIC_DATA)) {
            sendMusicUploadResultState(e.getPlayer(), e.getName(), e.getReceiveResult(), "unziped");
            try {
                byte[] compdata = IKSGFileLoadUtil.fileBytesReader(PathUtil.getWorldTmpFolder().resolve(e.getName()));
                IKSGFileLoadUtil.fileBytesWriter(IKSGDataUtil.gzUnZipping(compdata), PathUtil.getWorldTmpFolder().resolve(e.getName() + "-tmp"));
                IKSGFileLoadUtil.deleteFile(PathUtil.getWorldTmpFolder().resolve(e.getName()));
                File file = PathUtil.getWorldTmpFolder().resolve(e.getName() + "-tmp").toFile();
                IKSGFileLoadUtil.createFolder(PathUtil.getWorldMusicFolder());
                file.renameTo(PathUtil.getWorldMusicFolder().resolve(e.getName()).toFile());
            } catch (Exception ex) {
            }
            sendMusicUploadResultState(e.getPlayer(), e.getName(), e.getReceiveResult(), "complete");
        }
    }

    private static void sendMusicUploadResultState(ServerPlayerEntity player, String uuid, SendReceiveLogger.SRResult result, String state) {
        CompoundNBT tag = new CompoundNBT();
        tag.putString("result", result.name());
        tag.putString("state", state);
        ResponseSender.sendToClient(player, IMPWorldData.SEND_MUSIC_RESPONSE, 0, uuid, tag);
    }


}

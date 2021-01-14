package red.felnull.imp.client.handler;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.network.NetworkEvent;
import red.felnull.imp.client.music.ClientWorldMusicManager;
import red.felnull.imp.client.music.MusicRinger;
import red.felnull.imp.data.IMPWorldData;
import red.felnull.imp.ffmpeg.FFmpegManeger;
import red.felnull.imp.packet.MusicRingMessage;
import red.felnull.otyacraftengine.api.ResponseSender;

import java.util.function.Supplier;

public class MusicRingMessageHandler {
    public static void reversiveMessage(MusicRingMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().setPacketHandled(true);

        MusicRinger ringer = new MusicRinger(message.uuid, message.music, message.musicPos);
        ClientWorldMusicManager.instance().addMusicPlayer(message.uuid, ringer);

        if (FFmpegManeger.instance().canUseFFmpeg()) {
            ringer.playWait(message.startPos);
        } else {
            CompoundNBT tag = new CompoundNBT();
            tag.putString("result", "noffmpeg");
            ResponseSender.sendToServer(IMPWorldData.MUSIC_RINGD, 0, message.uuid.toString(), tag);
        }
    }
}

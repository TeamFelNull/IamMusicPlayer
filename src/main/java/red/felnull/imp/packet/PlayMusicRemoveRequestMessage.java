package red.felnull.imp.packet;

import net.minecraft.network.PacketBuffer;

public class PlayMusicRemoveRequestMessage {
    public String name;

    public PlayMusicRemoveRequestMessage(String name) {
        this.name = name;
    }

    public static PlayMusicRemoveRequestMessage decodeMessege(PacketBuffer buffer) {
        return new PlayMusicRemoveRequestMessage(buffer.readString(32767));
    }

    public static void encodeMessege(PlayMusicRemoveRequestMessage messegeIn, PacketBuffer buffer) {
        buffer.writeString(messegeIn.name);
    }
}

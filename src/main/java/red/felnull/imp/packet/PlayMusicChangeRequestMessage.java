package red.felnull.imp.packet;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import red.felnull.imp.music.resource.PlayImage;

public class PlayMusicChangeRequestMessage {
    public String uuid;
    public String name;
    public PlayImage image;
    public String artist;
    public String album;
    public String year;
    public String genre;

    public PlayMusicChangeRequestMessage(String uuid, String name, PlayImage image, String artist, String album, String year, String genre) {
        this.uuid = uuid;
        this.name = name;
        this.image = image;
        this.artist = artist;
        this.album = album;
        this.year = year;
        this.genre = genre;
    }

    public static PlayMusicChangeRequestMessage decodeMessege(PacketBuffer buffer) {
        return new PlayMusicChangeRequestMessage(buffer.readString(32767), buffer.readString(32767), new PlayImage(buffer.readCompoundTag()), buffer.readString(32767), buffer.readString(32767), buffer.readString(32767), buffer.readString(32767));
    }

    public static void encodeMessege(PlayMusicChangeRequestMessage messegeIn, PacketBuffer buffer) {
        buffer.writeString(messegeIn.uuid);
        buffer.writeString(messegeIn.name);
        buffer.writeCompoundTag(messegeIn.image.write(new CompoundNBT()));
        buffer.writeString(messegeIn.artist);
        buffer.writeString(messegeIn.album);
        buffer.writeString(messegeIn.year);
        buffer.writeString(messegeIn.genre);
    }
}

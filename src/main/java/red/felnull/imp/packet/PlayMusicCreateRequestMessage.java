package red.felnull.imp.packet;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import red.felnull.imp.musicplayer.PlayImage;

public class PlayMusicCreateRequestMessage {
    public String name;
    public PlayImage image;
    public String musicUUID;
    public String artist;
    public String album;
    public String year;
    public String genre;
    public int bitrate;
    public long lethsec;

    public PlayMusicCreateRequestMessage(String name, PlayImage image, String musicUUID, String artist, String album, String year, String genre, int bitrate, long lethsec) {
        this.name = name;
        this.image = image;
        this.musicUUID = musicUUID;
        this.artist = artist;
        this.album = album;
        this.year = year;
        this.genre = genre;
        this.bitrate = bitrate;
        this.lethsec = lethsec;
    }

    public static PlayMusicCreateRequestMessage decodeMessege(PacketBuffer buffer) {
        return new PlayMusicCreateRequestMessage(buffer.readString(32767), new PlayImage(buffer.readCompoundTag()), buffer.readString(32767), buffer.readString(32767), buffer.readString(32767), buffer.readString(32767), buffer.readString(32767), buffer.readInt(), buffer.readLong());
    }

    public static void encodeMessege(PlayMusicCreateRequestMessage messegeIn, PacketBuffer buffer) {
        buffer.writeString(messegeIn.name);
        buffer.writeCompoundTag(messegeIn.image.write(new CompoundNBT()));
        buffer.writeString(messegeIn.musicUUID);
        buffer.writeString(messegeIn.artist);
        buffer.writeString(messegeIn.album);
        buffer.writeString(messegeIn.year);
        buffer.writeString(messegeIn.genre);
        buffer.writeInt(messegeIn.bitrate);
        buffer.writeLong(messegeIn.lethsec);
    }
}

package red.felnull.imp.packet;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import red.felnull.imp.music.resource.PlayImage;
import red.felnull.imp.music.resource.PlayLocation;

public class PlayMusicCreateRequestMessage {
    public String name;
    public String playListUUID;
    public PlayImage image;
    public PlayLocation musicLocation;
    public String artist;
    public String album;
    public String year;
    public String genre;
    public long lethInSecond;

    public PlayMusicCreateRequestMessage(String name, String playListUUID, PlayImage image, PlayLocation musicLocation, String artist, String album, String year, String genre, long lethInSecond) {
        this.name = name;
        this.playListUUID = playListUUID;
        this.image = image;
        this.musicLocation = musicLocation;
        this.artist = artist;
        this.album = album;
        this.year = year;
        this.genre = genre;
        this.lethInSecond = lethInSecond;
    }

    public static PlayMusicCreateRequestMessage decodeMessege(PacketBuffer buffer) {
        return new PlayMusicCreateRequestMessage(buffer.readString(32767), buffer.readString(32767), new PlayImage(buffer.readCompoundTag()), new PlayLocation(buffer.readCompoundTag()), buffer.readString(32767), buffer.readString(32767), buffer.readString(32767), buffer.readString(32767), buffer.readLong());
    }

    public static void encodeMessege(PlayMusicCreateRequestMessage messegeIn, PacketBuffer buffer) {
        buffer.writeString(messegeIn.name);
        buffer.writeString(messegeIn.playListUUID);
        buffer.writeCompoundTag(messegeIn.image.write(new CompoundNBT()));
        buffer.writeCompoundTag(messegeIn.musicLocation.write(new CompoundNBT()));
        buffer.writeString(messegeIn.artist);
        buffer.writeString(messegeIn.album);
        buffer.writeString(messegeIn.year);
        buffer.writeString(messegeIn.genre);
        buffer.writeLong(messegeIn.lethInSecond);
    }
}

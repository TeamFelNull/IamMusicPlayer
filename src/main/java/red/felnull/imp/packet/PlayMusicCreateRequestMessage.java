package red.felnull.imp.packet;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import red.felnull.imp.musicplayer.PlayImage;
import red.felnull.imp.musicplayer.PlayLocation;

public class PlayMusicCreateRequestMessage {
    public String name;
    public PlayImage image;
    public PlayLocation musicLocation;
    public String artist;
    public String album;
    public String year;
    public String genre;

    public PlayMusicCreateRequestMessage(String name, PlayImage image, PlayLocation musicLocation, String artist, String album, String year, String genre) {
        this.name = name;
        this.image = image;
        this.musicLocation = musicLocation;
        this.artist = artist;
        this.album = album;
        this.year = year;
        this.genre = genre;
    }

    public static PlayMusicCreateRequestMessage decodeMessege(PacketBuffer buffer) {
        return new PlayMusicCreateRequestMessage(buffer.readString(32767), new PlayImage(buffer.readCompoundTag()), new PlayLocation(buffer.readCompoundTag()), buffer.readString(32767), buffer.readString(32767), buffer.readString(32767), buffer.readString(32767));
    }

    public static void encodeMessege(PlayMusicCreateRequestMessage messegeIn, PacketBuffer buffer) {
        buffer.writeString(messegeIn.name);
        buffer.writeCompoundTag(messegeIn.image.write(new CompoundNBT()));
        buffer.writeCompoundTag(messegeIn.musicLocation.write(new CompoundNBT()));
        buffer.writeString(messegeIn.artist);
        buffer.writeString(messegeIn.album);
        buffer.writeString(messegeIn.year);
        buffer.writeString(messegeIn.genre);
    }
}

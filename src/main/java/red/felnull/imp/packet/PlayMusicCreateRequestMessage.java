package red.felnull.imp.packet;

import net.minecraft.network.PacketBuffer;

public class PlayMusicCreateRequestMessage {
    public String name;
    public String imageUUID;
    public int imageW;
    public int imageH;
    public String musicUUID;
    public String artist;
    public String album;
    public String year;
    public String genre;
    public int bitrate;
    public long lethsec;

    public PlayMusicCreateRequestMessage(String name, String imageUUID, int iw, int ih, String musicUUID, String artist, String album, String year, String genre, int bitrate, long lethsec) {
        this.name = name;
        this.imageUUID = imageUUID;
        this.imageW = iw;
        this.imageH = ih;
        this.musicUUID = musicUUID;
        this.artist = artist;
        this.album = album;
        this.year = year;
        this.genre = genre;
        this.bitrate = bitrate;
        this.lethsec = lethsec;
    }

    public static PlayMusicCreateRequestMessage decodeMessege(PacketBuffer buffer) {
        return new PlayMusicCreateRequestMessage(buffer.readString(32767), buffer.readString(32767), buffer.readInt(), buffer.readInt(), buffer.readString(32767), buffer.readString(32767), buffer.readString(32767), buffer.readString(32767), buffer.readString(32767), buffer.readInt(), buffer.readLong());
    }

    public static void encodeMessege(PlayMusicCreateRequestMessage messegeIn, PacketBuffer buffer) {
        buffer.writeString(messegeIn.name);
        buffer.writeString(messegeIn.imageUUID);
        buffer.writeInt(messegeIn.imageW);
        buffer.writeInt(messegeIn.imageH);
        buffer.writeString(messegeIn.musicUUID);
        buffer.writeString(messegeIn.artist);
        buffer.writeString(messegeIn.album);
        buffer.writeString(messegeIn.year);
        buffer.writeString(messegeIn.genre);
        buffer.writeInt(messegeIn.bitrate);
        buffer.writeLong(messegeIn.lethsec);
    }
}

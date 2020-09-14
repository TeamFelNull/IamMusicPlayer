package red.felnull.imp.musicplayer;

import net.minecraft.nbt.CompoundNBT;
import red.felnull.otyacraftengine.data.INBTReadWriter;

public class PlayMusic implements INBTReadWriter {
    private final String UUID;
    private String name;
    private String imageUUID;
    private int imageWidth;
    private int imageHeight;
    private String createPlayerName;
    private String createPlayerUUID;
    private String timeStamp;
    private String musicUUID;
    private String artist;
    private String album;
    private String year;
    private String genre;
    private int bitrate;
    private long lengthInMilliseconds;

    public PlayMusic(String UUID, String name, String imageUUID, int width, int height, String createPlayerName, String createPlayerUUID, String timeStamp, String musicUUID, String artist, String album, String year, String genre, int bitrate, long lengthInMilliseconds) {
        this.UUID = UUID;
        this.name = name;
        this.imageUUID = imageUUID;
        this.imageWidth = width;
        this.imageHeight = height;
        this.createPlayerName = createPlayerName;
        this.createPlayerUUID = createPlayerUUID;
        this.timeStamp = timeStamp;
        this.musicUUID = musicUUID;
        this.artist = artist;
        this.album = album;
        this.year = year;
        this.genre = genre;
        this.bitrate = bitrate;
        this.lengthInMilliseconds = lengthInMilliseconds;
    }

    public PlayMusic(String UUID, CompoundNBT tag) {
        this.UUID = UUID;
        read(tag);
    }

    @Override
    public void read(CompoundNBT tag) {
        this.name = tag.getString("Name");
        this.imageUUID = tag.getString("ImageUUID");
        this.imageWidth = tag.getInt("ImageWidth");
        this.imageHeight = tag.getInt("ImageHeight");
        this.createPlayerName = tag.getString("CreatePlayerName");
        this.createPlayerUUID = tag.getString("CreatePlayerUUID");
        this.timeStamp = tag.getString("TimeStamp");
        this.musicUUID = tag.getString("MusicUUID");
        this.artist = tag.getString("Artist");
        this.album = tag.getString("Album");
        this.year = tag.getString("Year");
        this.genre = tag.getString("Genre");
        this.bitrate = tag.getInt("Bitrate");
        this.lengthInMilliseconds = tag.getLong("LengthInMilliseconds");
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.putString("Name", this.name);
        tag.putString("ImageUUID", this.imageUUID);
        tag.putInt("ImageWidth", this.imageWidth);
        tag.putInt("ImageHeight", this.imageHeight);
        tag.putString("CreatePlayerName", this.createPlayerName);
        tag.putString("CreatePlayerUUID", this.createPlayerUUID);
        tag.putString("TimeStamp", this.timeStamp);
        tag.putString("MusicUUID", this.musicUUID);
        tag.putString("Artist", this.artist);
        tag.putString("Album", this.album);
        tag.putString("Year", this.year);
        tag.putString("Genre", this.genre);
        tag.putInt("Bitrate", this.bitrate);
        tag.putLong("LengthInMilliseconds", this.lengthInMilliseconds);
        return tag;
    }

    public String getUUID() {
        return UUID;
    }

    public String getName() {
        return name;
    }

    public String getImageUUID() {
        return imageUUID;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public String getCreatePlayerUUID() {
        return createPlayerUUID;
    }

    public String getCreatePlayerName() {
        return createPlayerName;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getMusicUUID() {
        return musicUUID;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getYear() {
        return year;
    }

    public String getGenre() {
        return genre;
    }

    public int getBitrate() {
        return bitrate;
    }

    public long getLengthInMilliseconds() {
        return lengthInMilliseconds;
    }
}

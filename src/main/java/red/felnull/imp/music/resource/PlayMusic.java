package red.felnull.imp.music.resource;

import net.minecraft.nbt.CompoundNBT;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.data.IMPWorldData;
import red.felnull.otyacraftengine.data.INBTReadWriter;
import red.felnull.otyacraftengine.data.WorldDataManager;
import red.felnull.otyacraftengine.util.IKSGNBTUtil;

import java.util.ArrayList;
import java.util.List;

public class PlayMusic implements INBTReadWriter {
    public static final PlayMusic EMPTY = new PlayMusic("de61ebfc-0451-483e-ba22-8f915214864e", "empty", PlayImage.EMPTY, "", "", "", PlayLocation.EMPTY, "", "", "", "", 0l);
    private final String UUID;
    private String name;
    private String createPlayerName;
    private String createPlayerUUID;
    private String timeStamp;
    private PlayLocation musicLocation;
    private String artist;
    private String album;
    private String year;
    private String genre;
    private long lengthInMilliseconds;
    private PlayImage image;

    public PlayMusic(String UUID, String name, PlayImage image, String createPlayerName, String createPlayerUUID, String timeStamp, PlayLocation musicLocation, String artist, String album, String year, String genre, long lengthInMilliseconds) {
        this.UUID = UUID;
        this.name = name;
        this.image = image;
        this.createPlayerName = createPlayerName;
        this.createPlayerUUID = createPlayerUUID;
        this.timeStamp = timeStamp;
        this.musicLocation = musicLocation;
        this.artist = artist;
        this.album = album;
        this.year = year;
        this.genre = genre;
        this.lengthInMilliseconds = lengthInMilliseconds;
    }

    public PlayMusic(String UUID, CompoundNBT tag) {
        this.UUID = UUID;
        read(tag);
    }

    @Override
    public void read(CompoundNBT tag) {
        this.name = tag.getString("Name");
        this.image = new PlayImage(tag.getCompound("Image"));
        this.createPlayerName = tag.getString("CreatePlayerName");
        this.createPlayerUUID = tag.getString("CreatePlayerUUID");
        this.timeStamp = tag.getString("TimeStamp");
        this.musicLocation = new PlayLocation(tag.getCompound("MusicLocation"));
        this.artist = tag.getString("Artist");
        this.album = tag.getString("Album");
        this.year = tag.getString("Year");
        this.genre = tag.getString("Genre");
        this.lengthInMilliseconds = tag.getLong("LengthInMilliseconds");
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.putString("Name", this.name);
        tag.put("Image", this.image.write(new CompoundNBT()));
        tag.putString("CreatePlayerName", this.createPlayerName);
        tag.putString("CreatePlayerUUID", this.createPlayerUUID);
        tag.putString("TimeStamp", this.timeStamp);
        tag.put("MusicLocation", this.musicLocation.write(new CompoundNBT()));
        tag.putString("Artist", this.artist);
        tag.putString("Album", this.album);
        tag.putString("Year", this.year);
        tag.putString("Genre", this.genre);
        tag.putLong("LengthInMilliseconds", this.lengthInMilliseconds);
        return tag;
    }

    public String getUUID() {
        return UUID;
    }

    public String getName() {
        return name;
    }

    public PlayImage getImage() {
        return image;
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

    public PlayLocation getMusicLocation() {
        return musicLocation;
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

    public long getLengthInMilliseconds() {
        return lengthInMilliseconds;
    }

    public void addPlayMusicToPlayList(PlayList plst) {
        addPlayMusicToPlayList(plst, this);
    }

    public static void addPlayMusic(PlayMusic music) {
        WorldDataManager.instance().getWorldData(IMPWorldData.PLAYMUSIC_DATA).getCompound("playmusics").put(music.getUUID(), music.write(new CompoundNBT()));
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setImage(PlayImage image) {
        this.image = image;
    }

    public static void addPlayMusicToPlayList(PlayList plst, PlayMusic pmusci) {
        IamMusicPlayer.LOGGER.info(pmusci.getCreatePlayerName() + " Add PlayMusic(" + pmusci.getName() + ") to PlayList(" + plst.getName() + ")");
        String listuuid = plst.getUUID();
        CompoundNBT pmtag = WorldDataManager.instance().getWorldData(IMPWorldData.PLAYMUSIC_DATA).getCompound("playlists");
        CompoundNBT pmutag = null;
        if (!pmtag.contains(listuuid)) {
            pmutag = new CompoundNBT();
        } else {
            pmutag = pmtag.getCompound(listuuid);
        }
        pmutag.put("playmusic", IKSGNBTUtil.addStringList(pmutag.getCompound("playmusic"), pmusci.getUUID()));
        pmtag.put(listuuid, pmutag);
    }

    public static PlayMusic getPlayMusicByUUID(String uuid) {
        CompoundNBT pmtag = WorldDataManager.instance().getWorldData(IMPWorldData.PLAYMUSIC_DATA).getCompound("playmusics");
        if (!pmtag.contains(uuid))
            return PlayMusic.EMPTY;
        return new PlayMusic(uuid, pmtag.getCompound(uuid));
    }

    public static List<PlayMusic> getIncludedPlayMusics(PlayList plist) {
        List<PlayMusic> list = new ArrayList<>();
        CompoundNBT pltag = WorldDataManager.instance().getWorldData(IMPWorldData.PLAYMUSIC_DATA).getCompound("playlists");
        String uuid = plist.getUUID();
        if (!pltag.contains(uuid))
            return list;
        List<String> sts = IKSGNBTUtil.readStringList(pltag.getCompound(uuid).getCompound("playmusic"));
        sts.forEach(n -> list.add(getPlayMusicByUUID(n)));
        return list;
    }

    public static void setPlayMusic(PlayMusic plst) {
        removePlayMusic(plst, false);
        addPlayMusic(plst);
    }

    public static void removePlayMusic(PlayMusic plst, boolean rejoin) {
        if (rejoin) {

        }
        WorldDataManager.instance().getWorldData(IMPWorldData.PLAYMUSIC_DATA).getCompound("playmusics").remove(plst.getUUID());
    }
    public boolean equals(Object obj) {

        if (obj == this)
            return true;

        return obj instanceof PlayMusic && ((PlayMusic) obj).getUUID().equals(this.getUUID());
    }
}

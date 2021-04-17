package red.felnull.imp.music.resource;

import net.minecraft.nbt.CompoundTag;
import red.felnull.otyacraftengine.data.ITAGSerializable;

public class MusicDetailed implements ITAGSerializable {
    private static final MusicDetailed EMPTY = new MusicDetailed("", "", "", "", "");
    private String author;
    private String album;
    private String year;
    private String genre;
    private String url;

    public MusicDetailed(CompoundTag tag) {
        this.load(tag);
    }

    public MusicDetailed(String author, String album, String year, String genre, String url) {
        this.author = author;
        this.album = album;
        this.year = year;
        this.genre = genre;
        this.url = url;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putString("Author", author);
        tag.putString("Album", album);
        tag.putString("Year", year);
        tag.putString("Genre", genre);
        tag.putString("Url", url);
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        author = tag.getString("Author");
        album = tag.getString("Album");
        year = tag.getString("Year");
        genre = tag.getString("Genre");
        url = tag.getString("Url");
    }

    public String getAlbum() {
        return album;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public String getUrl() {
        return url;
    }

    public String getYear() {
        return year;
    }
}

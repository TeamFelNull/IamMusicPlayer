package dev.felnull.imp.music.resource;

import dev.felnull.otyacraftengine.server.level.TagSerializable;
import net.minecraft.nbt.CompoundTag;

import java.util.Objects;

public class ImageInfo implements TagSerializable {
    public static final ImageInfo EMPTY = new ImageInfo(ImageType.EMPTY, "");
    private ImageType imageType;
    private String identifier;

    public ImageInfo() {
    }

    public ImageInfo(ImageType imageType, String identifier) {
        this.imageType = imageType;
        this.identifier = identifier;
    }

    @Override
    public void save(CompoundTag tag) {
        tag.putString("ImageType", this.imageType.getNmae());
        tag.putString("Identifier", this.identifier);
    }

    @Override
    public void load(CompoundTag tag) {
        this.imageType = ImageType.getImageTypeByName(tag.getString("ImageType"));
        this.identifier = tag.getString("Identifier");
    }

    public ImageType getImageType() {
        return imageType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public boolean isEmpty() {
        return this == EMPTY || this.imageType == ImageType.EMPTY;
    }

    @Override
    public String toString() {
        return "ImageInfo{" +
                "imageType=" + imageType +
                ", identifier='" + identifier + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageInfo imageInfo = (ImageInfo) o;
        return imageType == imageInfo.imageType && Objects.equals(identifier, imageInfo.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageType, identifier);
    }

    public static enum ImageType {
        EMPTY("empty"),
        URL("url"),
        PLAYER_FACE("player_face"),
        YOUTUBE_THUMBNAIL("youtube_thumbnail"),
        SOUND_CLOUD_ARTWORK("sound_cloud_artwork"),
        NETEASE_CLOUD_MUSIC_PICTURE("netease_cloud_music_picture");
        private final String name;

        private ImageType(String name) {
            this.name = name;
        }

        public String getNmae() {
            return name;
        }

        public static ImageType getImageTypeByName(String name) {
            for (ImageType it : values()) {
                if (it.getNmae().equals(name))
                    return it;
            }
            return EMPTY;
        }
    }
}

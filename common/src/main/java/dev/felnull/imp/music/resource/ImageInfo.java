package dev.felnull.imp.music.resource;

import dev.felnull.otyacraftengine.data.ITAGSerializable;
import net.minecraft.nbt.CompoundTag;

import java.util.Objects;

public class ImageInfo implements ITAGSerializable {
    public static final ImageInfo EMPTY = new ImageInfo(ImageType.EMPTY, "");
    private ImageType imageType;
    private String identifier;
    private float widthScale;
    private float heightScale;

    public ImageInfo() {
    }

    public ImageInfo(ImageType imageType, String identifier) {
        this(imageType, identifier, 1, 1);
    }

    public ImageInfo(ImageType imageType, String identifier, float widthScale, float heightScale) {
        this.imageType = imageType;
        this.identifier = identifier;
        this.widthScale = widthScale;
        this.heightScale = heightScale;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putString("ImageType", this.imageType.getNmae());
        tag.putString("Identifier", this.identifier);
        tag.putFloat("WidthScale", widthScale);
        tag.putFloat("HeightScale", heightScale);
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        this.imageType = ImageType.getImageTypeByName(tag.getString("ImageType"));
        this.identifier = tag.getString("Identifier");
        this.widthScale = tag.getFloat("WidthScale");
        this.heightScale = tag.getFloat("HeightScale");
    }

    public float getHeightScale() {
        return heightScale;
    }

    public float getWidthScale() {
        return widthScale;
    }

    public ImageType getImageType() {
        return imageType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public boolean isEmpty() {
        return this.imageType == ImageType.EMPTY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageInfo that = (ImageInfo) o;
        return Float.compare(that.widthScale, widthScale) == 0 && Float.compare(that.heightScale, heightScale) == 0 && imageType == that.imageType && Objects.equals(identifier, that.identifier);
    }

    @Override
    public String toString() {
        return "MusicImage{" +
                "imageType=" + imageType +
                ", identifier='" + identifier + '\'' +
                ", widthScale=" + widthScale +
                ", heightScale=" + heightScale +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageType, identifier, widthScale, heightScale);
    }

    public static enum ImageType {
        EMPTY("empty"),
        URL("url"),
        PLAYER_FACE("player_face"),
        YOUTUBE_THUMBNAIL("youtube_thumbnail");
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

package red.felnull.imp.data.resource;

import net.minecraft.nbt.CompoundTag;
import red.felnull.otyacraftengine.data.ITAGSerializable;

public class ImageInfo implements ITAGSerializable {
    public static final ImageInfo EMPTY = new ImageInfo(ImageType.STRING, "empty");
    private ImageType imageType;
    private String identifier;
    private float widthScale;
    private float heightScale;

    public ImageInfo(CompoundTag tag) {
        this.load(tag);
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

    public ImageType getImageType() {
        return imageType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public float getWidthScale() {
        return widthScale;
    }

    public float getHeightScale() {
        return heightScale;
    }

    public static enum ImageType {
        URL("url"),
        STRING("string"),
        PLAYER_FACE("playerface");
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
            return STRING;
        }
    }
}

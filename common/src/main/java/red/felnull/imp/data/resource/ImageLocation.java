package red.felnull.imp.data.resource;

import net.minecraft.nbt.CompoundTag;
import red.felnull.otyacraftengine.data.ITAGSerializable;

public class ImageLocation implements ITAGSerializable {
    public static final ImageLocation EMPTY = new ImageLocation(ImageType.STRING, "empty");
    private ImageType imageType;
    private String identifier;
    private CompoundTag data;

    public ImageLocation(CompoundTag tag) {
        this.load(tag);
    }

    public ImageLocation(ImageType imageType, String identifier) {
        this(imageType, identifier, new CompoundTag());
    }

    public ImageLocation(ImageType imageType, String identifier, CompoundTag data) {
        this.imageType = imageType;
        this.identifier = identifier;
        this.data = data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putString("ImageType", this.imageType.getNmae());
        tag.putString("Identifier", this.identifier);
        tag.put("Data", data);
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        this.imageType = ImageType.getImageTypeByName(tag.getString("ImageType"));
        this.identifier = tag.getString("Identifier");
        this.data = tag.getCompound("Data");
    }

    public ImageType getImageType() {
        return imageType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public CompoundTag getData() {
        return data;
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

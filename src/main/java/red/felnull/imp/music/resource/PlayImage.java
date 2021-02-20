package red.felnull.imp.music.resource;

import net.minecraft.nbt.CompoundNBT;
import red.felnull.otyacraftengine.data.INBTReadWriter;

import java.util.Objects;

public class PlayImage implements INBTReadWriter {
    public static final PlayImage EMPTY = new PlayImage(PlayImage.ImageType.STRING, "empty");
    private ImageType imageType;
    private String name;

    //イメージUUID、プレイヤー名、表示名、イメージURLのどれか
    public PlayImage(ImageType type, String str) {
        this.imageType = type;
        this.name = str;
    }

    public PlayImage(CompoundNBT tag) {
        read(tag);
    }

    @Override
    public void read(CompoundNBT tag) {
        this.imageType = ImageType.getImageTypeByName(tag.getString("ImageType"));
        this.name = tag.getString("Name");
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.putString("ImageType", this.imageType.getNmae());
        tag.putString("Name", this.name);
        return tag;
    }

    public ImageType getImageType() {
        return imageType;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayImage playImage = (PlayImage) o;
        return imageType == playImage.imageType && Objects.equals(name, playImage.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageType, name);
    }

    public enum ImageType {
        IMGAE("imgae"),
        STRING("string"),
        PLAYERFACE("playerface"),
        URLIMAGE("urlimage");
        private final String nmae;

        private ImageType(String name) {
            this.nmae = name;
        }

        public String getNmae() {
            return nmae;
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

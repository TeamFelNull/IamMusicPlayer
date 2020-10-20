package red.felnull.imp.musicplayer;

import net.minecraft.nbt.CompoundNBT;
import red.felnull.otyacraftengine.data.INBTReadWriter;

public class PlayImage implements INBTReadWriter {
    private ImageType imageType;
    private String name;

    //イメージUUID、プレイヤー名、表示名のどれか
    public PlayImage(ImageType type, String str) {
        this.imageType = type;
        this.name = str;
    }

    public PlayImage(CompoundNBT tag) {
        read(tag);
    }

    @Override
    public void read(CompoundNBT tag) {
        this.imageType = ImageType.valueOf(tag.getString("ImageType"));
        this.name = tag.getString("Name");
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.putString("ImageType", this.imageType.name());
        tag.putString("Name", this.name);
        return tag;
    }

    public ImageType getImageType() {
        return imageType;
    }

    public String getName() {
        return name;
    }

    public static enum ImageType {
        IMGAE,
        STRING,
        PLAYERFACE;
    }
}

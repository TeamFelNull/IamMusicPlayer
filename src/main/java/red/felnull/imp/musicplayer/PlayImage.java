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

    @Override
    public void read(CompoundNBT tag) {
        imageType = ImageType.valueOf(tag.getString("ImageType"));
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.putString("ImageType", imageType.name());
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

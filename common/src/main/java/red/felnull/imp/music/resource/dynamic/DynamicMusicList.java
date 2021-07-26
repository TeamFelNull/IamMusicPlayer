package red.felnull.imp.music.resource.dynamic;

import net.minecraft.nbt.CompoundTag;
import red.felnull.imp.data.resource.ImageInfo;
import red.felnull.imp.music.resource.Music;
import red.felnull.imp.util.NbtUtils;
import red.felnull.otyacraftengine.data.ITAGSerializable;

import java.util.ArrayList;
import java.util.List;

public class DynamicMusicList implements ITAGSerializable {
    private String name;
    private ImageInfo image;
    private List<Music> musics = new ArrayList<>();

    public DynamicMusicList(CompoundTag tag) {
        load(tag);
    }

    public DynamicMusicList(String name, ImageInfo image, List<Music> musics) {
        this.name = name;
        this.image = image;
        this.musics = musics;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putString("Name", name);
        tag.put("ImageInfo", image.save(new CompoundTag()));
        NbtUtils.writeMusics(tag, "Music", musics);
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        this.name = tag.getString("Name");
        this.image = new ImageInfo(tag.getCompound("ImageInfo"));
        NbtUtils.readMusics(tag, "Music", musics);
    }

    public String getName() {
        return name;
    }

    public ImageInfo getImage() {
        return image;
    }

    public List<Music> getMusics() {
        return musics;
    }
}

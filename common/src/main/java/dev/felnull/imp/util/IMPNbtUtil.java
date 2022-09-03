package dev.felnull.imp.util;

import dev.felnull.imp.music.resource.AuthorityInfo;
import dev.felnull.imp.music.resource.Music;
import dev.felnull.imp.music.resource.MusicPlayList;
import dev.felnull.otyacraftengine.server.level.TagSerializable;
import dev.felnull.otyacraftengine.util.OENbtUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class IMPNbtUtil {
    public static CompoundTag writeAuthority(CompoundTag tag, String name, Map<UUID, AuthorityInfo.AuthorityType> authority) {
        return OENbtUtils.writeUUIDKeyMap(tag, name, authority, n -> {
            CompoundTag taaa = new CompoundTag();
            taaa.putString("n", n.getName());
            return taaa;
        });
    }

    public static void readAuthority(CompoundTag tag, String name, Map<UUID, AuthorityInfo.AuthorityType> authority) {
        OENbtUtils.readUUIDKeyMap(tag, name, authority, n -> AuthorityInfo.AuthorityType.getByName(((CompoundTag) n).getString("n")), Tag.TAG_COMPOUND);
    }

    public static CompoundTag writeMusics(CompoundTag tag, String name, List<Music> musics) {
        return OENbtUtils.writeList(tag, name, musics, n -> {
            var rettag = new CompoundTag();
            rettag.put("m", n.createSavedTag());
            return rettag;
        });
    }

    public static void readMusics(CompoundTag tag, String name, List<Music> musics) {
        OENbtUtils.readList(tag, name, musics, n -> TagSerializable.loadSavedTag(((CompoundTag) n).getCompound("m"), new Music()), Tag.TAG_COMPOUND);
    }

    public static CompoundTag writeMusicPlayLists(CompoundTag tag, String name, List<MusicPlayList> musics) {
        return OENbtUtils.writeList(tag, name, musics, n -> {
            var rettag = new CompoundTag();
            rettag.put("m", n.createSavedTag());
            return rettag;
        });
    }

    public static void readMusicPlayLists(CompoundTag tag, String name, List<MusicPlayList> musics) {
        OENbtUtils.readList(tag, name, musics, n -> TagSerializable.loadSavedTag(((CompoundTag) n).getCompound("m"), new MusicPlayList()), Tag.TAG_COMPOUND);
    }
}

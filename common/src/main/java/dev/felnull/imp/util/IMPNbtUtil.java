package dev.felnull.imp.util;

import dev.felnull.imp.music.resource.AuthorityInfo;
import dev.felnull.imp.music.resource.Music;
import dev.felnull.imp.music.resource.MusicPlayList;
import dev.felnull.otyacraftengine.util.OENbtUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class IMPNbtUtil {
    public static CompoundTag writeAuthority(CompoundTag tag, String name, Map<UUID, AuthorityInfo.AuthorityType> authority) {
        return OENbtUtil.writeMap(tag, name, authority, NbtUtils::createUUID, n -> {
            CompoundTag taaa = new CompoundTag();
            taaa.putString("n", n.getName());
            return taaa;
        });
    }

    public static void readAuthority(CompoundTag tag, String name, Map<UUID, AuthorityInfo.AuthorityType> authority) {
        OENbtUtil.readMap(tag, name, authority, NbtUtils::loadUUID, n -> AuthorityInfo.AuthorityType.getByName(((CompoundTag) n).getString("n")));
    }

    public static CompoundTag writeMusics(CompoundTag tag, String name, List<Music> musics) {
        return OENbtUtil.writeList(tag, name, musics, n -> OENbtUtil.writeSerializable(new CompoundTag(), "m", n));
    }

    public static void readMusics(CompoundTag tag, String name, List<Music> musics) {
        OENbtUtil.readList(tag, name, musics, n -> OENbtUtil.readSerializable((CompoundTag) n, "m", new Music()));
    }

    public static CompoundTag writeMusicPlayLists(CompoundTag tag, String name, List<MusicPlayList> musics) {
        return OENbtUtil.writeList(tag, name, musics, n -> OENbtUtil.writeSerializable(new CompoundTag(), "m", n));
    }

    public static void readMusicPlayLists(CompoundTag tag, String name, List<MusicPlayList> musics) {
        OENbtUtil.readList(tag, name, musics, n -> OENbtUtil.readSerializable((CompoundTag) n, "m", new MusicPlayList()));
    }

    public static CompoundTag writeUUIDMap(CompoundTag tag, String name, Map<UUID, UUID> ids) {
        return OENbtUtil.writeMap(tag, name, ids, NbtUtils::createUUID, NbtUtils::createUUID);
    }

    public static void readUUIDMap(CompoundTag tag, String name, Map<UUID, UUID> ids) {
        OENbtUtil.readMap(tag, name, ids, NbtUtils::loadUUID, NbtUtils::loadUUID);
    }
}

package red.felnull.imp.util;

import net.minecraft.nbt.CompoundTag;
import red.felnull.imp.blockentity.MusicSharingDeviceBlockEntity;
import red.felnull.imp.data.resource.AdministratorInformation;
import red.felnull.imp.music.resource.Music;
import red.felnull.imp.music.resource.MusicPlayList;
import red.felnull.otyacraftengine.util.IKSGNbtUtil;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class NbtUtils {
    public static CompoundTag writeMusics(CompoundTag tag, String name, List<Music> musics) {
        return IKSGNbtUtil.writeList(tag, name, musics, n -> n.save(new CompoundTag()));
    }

    public static void readMusics(CompoundTag tag, String name, List<Music> musics) {
        IKSGNbtUtil.readList(tag, name, musics, n -> new Music((CompoundTag) n));
    }

    public static CompoundTag writeMusicPlayLists(CompoundTag tag, String name, List<MusicPlayList> musics) {
        return IKSGNbtUtil.writeList(tag, name, musics, n -> n.save(new CompoundTag()));
    }

    public static void readMusicPlayLists(CompoundTag tag, String name, List<MusicPlayList> musics) {
        IKSGNbtUtil.readList(tag, name, musics, n -> new MusicPlayList((CompoundTag) n));
    }

    public static CompoundTag writeAdminData(CompoundTag tag, String name, Map<UUID, AdministratorInformation.AuthorityType> adminData) {
        return IKSGNbtUtil.writeMap(tag, name, adminData, net.minecraft.nbt.NbtUtils::createUUID, n -> {
            CompoundTag taaa = new CompoundTag();
            taaa.putString("at", n.getNmae());
            return taaa;
        });
    }

    public static void readAdminData(CompoundTag tag, String name, Map<UUID, AdministratorInformation.AuthorityType> adminData) {
        IKSGNbtUtil.readMap(tag, name, adminData, net.minecraft.nbt.NbtUtils::loadUUID, n -> AdministratorInformation.AuthorityType.getAuthorityTypeByName(((CompoundTag) n).getString("at")), 10);
    }

    public static CompoundTag writeMSDPlayerScreenData(CompoundTag tag, String name, Map<UUID, MusicSharingDeviceBlockEntity.Screen> data) {
        return IKSGNbtUtil.writeMap(tag, name, data, net.minecraft.nbt.NbtUtils::createUUID, n -> {
            CompoundTag taaa = new CompoundTag();
            taaa.putString("dt", n.getSerializedName());
            return taaa;
        });
    }

    public static void readMSDPlayerScreenData(CompoundTag tag, String name, Map<UUID, MusicSharingDeviceBlockEntity.Screen> adminData) {
        IKSGNbtUtil.readMap(tag, name, adminData, net.minecraft.nbt.NbtUtils::loadUUID, n -> MusicSharingDeviceBlockEntity.Screen.getScreenByName(((CompoundTag) n).getString("dt")), 10);
    }
}

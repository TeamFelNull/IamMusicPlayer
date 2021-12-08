package dev.felnull.imp.util;

import dev.felnull.imp.music.resource.AuthorityInfo;
import dev.felnull.otyacraftengine.util.OENbtUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;

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
}

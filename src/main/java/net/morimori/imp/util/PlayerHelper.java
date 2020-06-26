package net.morimori.imp.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.SkullTileEntity;
import net.morimori.imp.file.PlayList;

public class PlayerHelper {
    public static Map<String, GameProfile> PlayerTextuerProfies = new HashMap<String, GameProfile>();

    public static String getUUID(MinecraftServer ms, String name) {
        return getUUID(ms.getPlayerList().getPlayerByUsername(name));
    }

    public static String getUUID(PlayerEntity pl) {
        return PlayerEntity.getUUID(pl.getGameProfile()).toString();
    }

    public static GameProfile getPlayerTextuerProfile(String name) {

        if (PlayerTextuerProfies.containsKey(name))
            return PlayerTextuerProfies.get(name);

        GameProfile gp = new GameProfile(UUID.fromString(PlayList.FakeUUID), name);
        PlayerTextuerProfies.put(name, gp);

        GameProfileLoader GPL = new GameProfileLoader(name);
        GPL.start();

        return gp;
    }
}

class GameProfileLoader extends Thread {
    private String name;

    public GameProfileLoader(String name) {
        this.name = name;
    }

    public void run() {
        GameProfile gp = PlayerHelper.PlayerTextuerProfies.get(name);
        PlayerHelper.PlayerTextuerProfies.put(name, SkullTileEntity.updateGameProfile(gp));
    }
}
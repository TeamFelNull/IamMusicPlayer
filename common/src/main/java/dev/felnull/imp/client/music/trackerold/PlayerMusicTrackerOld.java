package dev.felnull.imp.client.music.trackerold;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;
import java.util.function.Supplier;

public class PlayerMusicTrackerOld implements IMusicTrackerOld {
    private static final Minecraft mc = Minecraft.getInstance();
    private Vec3 pos;
    private final UUID playerID;
    private final boolean me;

    public PlayerMusicTrackerOld(CompoundTag tag) {
        this(new Vec3(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z")), tag.getUUID("id"));
    }

    public PlayerMusicTrackerOld(Vec3 pos, UUID playerID) {
        this.pos = pos;
        this.playerID = playerID;
        this.me = mc.player != null && mc.player.getGameProfile().getId().equals(playerID);
    }

    @Override
    public Supplier<Vec3> getPosition() {
        return () -> {
            if (me)
                return null;
            if (mc.level != null)
                for (AbstractClientPlayer player : mc.level.players()) {
                    if (player.getGameProfile().getId().equals(playerID)) {
                        this.pos = player.position();
                        break;
                    }
                }
            return pos;
        };
    }
}

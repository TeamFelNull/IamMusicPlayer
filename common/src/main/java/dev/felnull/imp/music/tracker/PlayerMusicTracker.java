package dev.felnull.imp.music.tracker;

import dev.architectury.utils.value.FloatSupplier;
import dev.felnull.imp.music.MusicSpeakerFixedInfo;
import dev.felnull.imp.music.MusicSpeakerInfo;
import dev.felnull.imp.music.SpatialType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.EntityGetter;

import java.util.UUID;

public class PlayerMusicTracker extends FixedMusicTracker {
    private final Player myPlayer;
    private final EntityGetter entityGetter;
    private final FloatSupplier deltaSupplier;
    private UUID playerId;

    public PlayerMusicTracker(Player myPlayer, EntityGetter entityGetter, FloatSupplier deltaSupplier) {
        this.myPlayer = myPlayer;
        this.entityGetter = entityGetter;
        this.deltaSupplier = deltaSupplier;
    }

    public PlayerMusicTracker(Player player, float volume, float range) {
        super(new MusicSpeakerInfo(player.position(), volume, range, new MusicSpeakerFixedInfo(-1, SpatialType.ENTRUST)));
        this.playerId = player.getGameProfile().getId();
        this.myPlayer = null;
        this.entityGetter = null;
        this.deltaSupplier = null;
    }

    @Override
    public MusicSpeakerInfo getSpeakerInfo() {
        var sp = super.getSpeakerInfo();
        if (myPlayer != null && entityGetter != null && playerId != null && deltaSupplier != null) {
            var pl = entityGetter.getPlayerByUUID(playerId);
            if (pl != null) {
                SpatialType spt = myPlayer.getGameProfile().getId().equals(playerId) ? SpatialType.DISABLE : sp.fixedInfo().spatialType();
                return new MusicSpeakerInfo(EntityMusicTracker.getEntityPosition(pl, deltaSupplier.getAsFloat()), sp.volume(), sp.range(), new MusicSpeakerFixedInfo(-1, spt));
            }
        }
        return sp;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        super.save(tag);
        tag.putUUID("PlayerId", playerId);
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.playerId = tag.getUUID("PlayerId");
    }
}

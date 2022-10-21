package dev.felnull.imp.music.tracker;

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
    private UUID playerId;

    public PlayerMusicTracker(Player myPlayer, EntityGetter entityGetter) {
        this.myPlayer = myPlayer;
        this.entityGetter = entityGetter;
    }

    public PlayerMusicTracker(Player player, float volume, float range) {
        super(new MusicSpeakerInfo(player.position(), volume, range, new MusicSpeakerFixedInfo(-1, SpatialType.ENTRUST)));
        this.playerId = player.getGameProfile().getId();
        this.myPlayer = null;
        this.entityGetter = null;
    }

    @Override
    public MusicSpeakerInfo getSpeakerInfo() {
        var sp = super.getSpeakerInfo();
        if (myPlayer != null && entityGetter != null && playerId != null) {
            var pl = entityGetter.getPlayerByUUID(playerId);
            if (pl != null) {
                SpatialType spt = myPlayer.getGameProfile().getId().equals(playerId) ? SpatialType.DISABLE : sp.fixedInfo().spatialType();
                return new MusicSpeakerInfo(pl.position(), sp.volume(), sp.range(), new MusicSpeakerFixedInfo(-1, spt));
            }
        }
        return sp;
    }

    @Override
    public void save(CompoundTag tag) {
        super.save(tag);
        tag.putUUID("PlayerId", playerId);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.playerId = tag.getUUID("PlayerId");
    }
}

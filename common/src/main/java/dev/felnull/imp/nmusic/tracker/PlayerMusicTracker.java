package dev.felnull.imp.nmusic.tracker;

import dev.felnull.imp.nmusic.MusicSpeakerFixedInfo;
import dev.felnull.imp.nmusic.MusicSpeakerInfo;
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
        super(new MusicSpeakerInfo(player.position(), volume, range, new MusicSpeakerFixedInfo(-1, false)));
        this.playerId = player.getGameProfile().getId();
        this.myPlayer = null;
        this.entityGetter = null;
    }

    @Override
    public MusicSpeakerInfo getSpeakerInfo() {
        var sp = super.getSpeakerInfo();
        if (myPlayer != null && entityGetter != null && playerId != null) {
            var pl = entityGetter.getPlayerByUUID(playerId);
            if (pl != null)
                return new MusicSpeakerInfo(pl.position(), sp.volume(), sp.range(), new MusicSpeakerFixedInfo(-1, myPlayer.getGameProfile().getId().equals(playerId)));
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

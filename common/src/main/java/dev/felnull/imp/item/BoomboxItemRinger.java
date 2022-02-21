package dev.felnull.imp.item;

import dev.felnull.imp.data.BoomboxData;
import dev.felnull.imp.music.resource.MusicSource;
import dev.felnull.imp.server.music.ringer.IMusicRinger;
import dev.felnull.imp.server.music.ringer.MusicRingManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class BoomboxItemRinger implements IMusicRinger {
    private final LivingEntity livingEntity;
    private final UUID uuid;
    private int lastInventory;

    public BoomboxItemRinger(LivingEntity livingEntity, UUID uuid) {
        this.livingEntity = livingEntity;
        this.uuid = uuid;
    }

    @Override
    public Component getRingerName(ServerLevel level) {
        return livingEntity.getDisplayName();
    }

    @Override
    public ServerLevel getRingerLevel() {
        return (ServerLevel) livingEntity.level;
    }

    @Override
    public UUID getRingerUUID() {
        return uuid;
    }

    @Override
    public boolean isRingerExist(ServerLevel level) {
        return livingEntity.isAlive() && !getBoombox().isEmpty();
    }

    @Override
    public boolean isRingerPlaying(ServerLevel level) {
        return getData().isPlaying();
    }

    @Override
    public void setRingerPlaying(ServerLevel level, boolean playing) {
        getData().setPlaying(playing);
    }

    @Override
    public @Nullable
    MusicSource getRingerMusicSource(ServerLevel level) {
        return isRingerStream() ? getData().getRadioSource() : getData().getMusicSource();
    }

    @Override
    public boolean isRingerLoop(ServerLevel level) {
        return getData().isLoop();
    }

    @Override
    public long getRingerPosition(ServerLevel level) {
        return getData().getMusicPosition();
    }

    @Override
    public void setRingerPosition(ServerLevel level, long position) {
        getData().setMusicPosition(position);
    }

    @Override
    public Pair<ResourceLocation, CompoundTag> getRingerTracker(ServerLevel level) {
        if (livingEntity instanceof Player player)
            return Pair.of(MusicRingManager.PLAYER_TRACKER, MusicRingManager.createPlayerTracker(player));
        return Pair.of(MusicRingManager.ENTITY_TRACKER, MusicRingManager.createEntityTracker(livingEntity));
    }

    @Override
    public @NotNull
    Vec3 getRingerSpatialPosition(ServerLevel level) {
        return livingEntity.position();
    }

    @Override
    public float getRingerVolume(ServerLevel level) {
        return getData().getRawVolume();
    }

    @Override
    public float getRingerRange(ServerLevel level) {
        return 90f * getData().getRawVolume();
    }

    @Override
    public boolean isRingerStream() {
        return getData().isRadioStream();
    }

    @NotNull
    private BoomboxData getData() {
        return BoomboxItem.getData(getBoombox());
    }

    @NotNull
    private ItemStack getBoombox() {
        for (EquipmentSlot value : EquipmentSlot.values()) {
            var item = livingEntity.getItemBySlot(value);
            if (uuid.equals(BoomboxItem.getUUID(item)))
                return item;
        }
        if (livingEntity instanceof Player player) {
            var li = player.getInventory().getItem(lastInventory);
            if (uuid.equals(BoomboxItem.getUUID(li)))
                return li;

            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                var item = player.getInventory().getItem(i);
                if (uuid.equals(BoomboxItem.getUUID(item))) {
                    lastInventory = i;
                    return item;
                }
            }
        }
        return ItemStack.EMPTY;
    }
}

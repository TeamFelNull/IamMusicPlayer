package dev.felnull.imp.item;

import dev.felnull.imp.data.BoomboxData;
import dev.felnull.imp.server.music.ringer.IBoomboxRinger;
import dev.felnull.imp.server.music.ringer.MusicRingManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class BoomboxItemRinger implements IBoomboxRinger {
    private final LivingEntity livingEntity;
    private final UUID uuid;
    private int lastInventory;

    public BoomboxItemRinger(LivingEntity livingEntity, UUID uuid) {
        this.livingEntity = livingEntity;
        this.uuid = uuid;
    }

    @Override
    public Component getRingerName() {
        return new TranslatableComponent("imp.ringer.have", getBoombox().getDisplayName(), livingEntity.getDisplayName());
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
    public boolean isRingerExist() {
        return livingEntity.isAlive() && !getBoombox().isEmpty();
    }

    @Override
    public Pair<ResourceLocation, CompoundTag> getRingerTracker() {
        if (livingEntity instanceof Player player)
            return Pair.of(MusicRingManager.PLAYER_TRACKER, MusicRingManager.createPlayerTracker(player));
        return Pair.of(MusicRingManager.ENTITY_TRACKER, MusicRingManager.createEntityTracker(livingEntity));
    }

    @Override
    public @NotNull
    Vec3 getRingerSpatialPosition() {
        return livingEntity.position();
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

    @Override
    public @NotNull BoomboxData getRingerBoomboxData() {
        return BoomboxItem.getData(getBoombox());
    }
}

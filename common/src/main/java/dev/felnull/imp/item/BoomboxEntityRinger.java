package dev.felnull.imp.item;

import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.data.BoomboxData;
import dev.felnull.imp.music.tracker.IMPMusicTrackers;
import dev.felnull.imp.music.tracker.MusicTrackerEntry;
import dev.felnull.imp.server.music.ringer.IBoomboxRinger;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class BoomboxEntityRinger implements IBoomboxRinger {
    private final Entity entity;
    private final UUID uuid;
    private int lastInventory;

    public BoomboxEntityRinger(Entity entity, UUID uuid) {
        this.entity = entity;
        this.uuid = uuid;
    }

    @Override
    public Component getRingerName() {
        if (entity instanceof ItemEntity)
            return new TranslatableComponent("imp.ringer.drop", getBoombox().getDisplayName());

        return new TranslatableComponent("imp.ringer.have", getBoombox().getDisplayName(), entity.getDisplayName());
    }

    @Override
    public ServerLevel getRingerLevel() {
        return (ServerLevel) entity.level;
    }

    @Override
    public UUID getRingerUUID() {
        return uuid;
    }

    @Override
    public boolean exists() {
        return canRing(entity) && !getBoombox().isEmpty();
    }

    @Override
    public MusicTrackerEntry getRingerTracker() {
        return IMPMusicTrackers.createEntityTracker(entity, getRingerVolume(), getRingerRange());
    }

    @Override
    public @NotNull
    Vec3 getRingerSpatialPosition() {
        return entity.position();
    }

    @NotNull
    private ItemStack getBoombox() {
        if (entity instanceof LivingEntity livingEntity) {
            for (EquipmentSlot value : EquipmentSlot.values()) {
                var item = livingEntity.getItemBySlot(value);
                if (uuid.equals(BoomboxItem.getRingerUUID(item)))
                    return item;
            }
        }
        if (entity instanceof Player player) {
            var li = player.getInventory().getItem(lastInventory);
            if (uuid.equals(BoomboxItem.getRingerUUID(li)))
                return li;

            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                var item = player.getInventory().getItem(i);
                if (uuid.equals(BoomboxItem.getRingerUUID(item))) {
                    lastInventory = i;
                    return item;
                }
            }
        }
        if (IamMusicPlayer.CONFIG.dropItemRing && entity instanceof ItemEntity itemEntity) {
            var item = itemEntity.getItem();
            if (uuid.equals(BoomboxItem.getRingerUUID(item)))
                return item;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull BoomboxData getRingerBoomboxData() {
        return BoomboxItem.getData(getBoombox());
    }

    public static boolean canRing(Entity entity) {
        if (!entity.isAlive()) return false;
        if (entity instanceof Player player)
            return !player.isSpectator();
        return true;
    }
}

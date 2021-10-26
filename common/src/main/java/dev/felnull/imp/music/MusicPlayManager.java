package dev.felnull.imp.music;

import dev.felnull.imp.IamMusicPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class MusicPlayManager {
    private static final MusicPlayManager INSTANCE = new MusicPlayManager();
    public static final ResourceLocation FIXED_TRACKER = new ResourceLocation(IamMusicPlayer.MODID, "fixed");
    public static final ResourceLocation ENTITY_TRACKER = new ResourceLocation(IamMusicPlayer.MODID, "entity");
    public static final ResourceLocation PLAYER_TRACKER = new ResourceLocation(IamMusicPlayer.MODID, "player");

    public static MusicPlayManager getInstance() {
        return INSTANCE;
    }

    public static CompoundTag createFixedTracker(Vec3 vec3) {
        var tag = new CompoundTag();
        tag.putDouble("x", vec3.x);
        tag.putDouble("y", vec3.y);
        tag.putDouble("z", vec3.z);
        return tag;
    }

    public static CompoundTag createEntityTracker(Entity entity) {
        var tag = new CompoundTag();
        var p = entity.position();
        tag.putDouble("x", p.x);
        tag.putDouble("y", p.y);
        tag.putDouble("z", p.z);
        tag.putInt("id", entity.getId());
        return tag;
    }

    public static CompoundTag createPlayerTracker(Player player) {
        var tag = new CompoundTag();
        var p = player.position();
        tag.putDouble("x", p.x);
        tag.putDouble("y", p.y);
        tag.putDouble("z", p.z);
        tag.putUUID("id", player.getGameProfile().getId());
        return tag;
    }
}

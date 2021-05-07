package red.felnull.imp.music.info.tracker;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import red.felnull.imp.IamMusicPlayer;

public class FixedMusicTracker extends MusicTracker {

    public FixedMusicTracker() {

    }

    public FixedMusicTracker(Vec3 position, float volume, float maxDistance) {
        super(position, volume, maxDistance);
    }

    @Override
    public ResourceLocation getRegistryName() {
        return new ResourceLocation(IamMusicPlayer.MODID, "fixed");
    }

}

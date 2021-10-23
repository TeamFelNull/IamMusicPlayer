package dev.felnull.imp.music;

import dev.felnull.imp.IamMusicPlayer;
import net.minecraft.resources.ResourceLocation;

public class MusicPlayManager {
    private static final MusicPlayManager INSTANCE = new MusicPlayManager();
    public static final ResourceLocation FIXED_TRACKER = new ResourceLocation(IamMusicPlayer.MODID, "fixed");
    public static final ResourceLocation ENTITY_TRACKER = new ResourceLocation(IamMusicPlayer.MODID, "entity");
    public static final ResourceLocation PLAYER_TRACKER = new ResourceLocation(IamMusicPlayer.MODID, "player");

    public static MusicPlayManager getInstance() {
        return INSTANCE;
    }
}

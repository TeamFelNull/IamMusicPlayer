package dev.felnull.imp.client.model;

import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.otyacraftengine.client.model.SpecialModelLoader;
import net.minecraft.resources.ResourceLocation;

public class IMPModels {
    public static final ResourceLocation MUSIC_MANAGER_ACCESS_LAMP = new ResourceLocation(IamMusicPlayer.MODID, "block/music_manager/music_manager_access_lamp");
    public static final ResourceLocation MUSIC_MANAGER_OFF_ACCESS_LAMP = new ResourceLocation(IamMusicPlayer.MODID, "block/music_manager/music_manager_off_access_lamp");

    public static final ResourceLocation BOOMBOX_HANDLE = new ResourceLocation(IamMusicPlayer.MODID, "block/boombox/boombox_handle");
    public static final ResourceLocation BOOMBOX_LID = new ResourceLocation(IamMusicPlayer.MODID, "block/boombox/boombox_lid");
    public static final ResourceLocation BOOMBOX_BUTTONS = new ResourceLocation(IamMusicPlayer.MODID, "block/boombox/boombox_buttons");

    public static final ResourceLocation PARABOLIC_ANTENNA = new ResourceLocation(IamMusicPlayer.MODID, "item/parabolic_antenna/parabolic_antenna");
    public static final ResourceLocation KAMESUTA_ANTENNA = new ResourceLocation(IamMusicPlayer.MODID, "item/parabolic_antenna/kamesuta_antenna");
    public static final ResourceLocation KATYOU_ANTENNA = new ResourceLocation(IamMusicPlayer.MODID, "item/parabolic_antenna/katyou_antenna");
    public static final ResourceLocation IKISUGI_ANTENNA = new ResourceLocation(IamMusicPlayer.MODID, "item/parabolic_antenna/ikisugi_antenna");
    public static final ResourceLocation FCOH_ANTENNA = new ResourceLocation(IamMusicPlayer.MODID, "item/parabolic_antenna/fcoh_antenna");

    public static void init() {
        register(MUSIC_MANAGER_ACCESS_LAMP);
        register(MUSIC_MANAGER_OFF_ACCESS_LAMP);
        register(BOOMBOX_HANDLE);
        register(BOOMBOX_LID);
        register(BOOMBOX_BUTTONS);
        register(PARABOLIC_ANTENNA);
        register(KAMESUTA_ANTENNA);
        register(KATYOU_ANTENNA);
        register(IKISUGI_ANTENNA);
        register(FCOH_ANTENNA);
    }

    private static void register(ResourceLocation location) {
        SpecialModelLoader.getInstance().registerLoadModel(location);
    }

}

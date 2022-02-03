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

    public static final ResourceLocation CASSETTE_TAPE_BASE_NORMAL_MODEL = new ResourceLocation(IamMusicPlayer.MODID, "item/cassette_tape/base_nomal");
    public static final ResourceLocation CASSETTE_TAPE_BASE_GLASS_MODEL = new ResourceLocation(IamMusicPlayer.MODID, "item/cassette_tape/base_glass");
    public static final ResourceLocation CASSETTE_TAPE_BASE_GLASS_COLOR_MODEL = new ResourceLocation(IamMusicPlayer.MODID, "item/cassette_tape/base_glass_color");
    public static final ResourceLocation CASSETTE_TAPE_MODEL = new ResourceLocation(IamMusicPlayer.MODID, "item/cassette_tape/tape");
    public static final ResourceLocation CASSETTE_TAPE_CONECTER = new ResourceLocation(IamMusicPlayer.MODID, "item/cassette_tape/tape_conecter");
    public static final ResourceLocation CASSETTE_TAPE_GLASS_MODEL = new ResourceLocation(IamMusicPlayer.MODID, "item/cassette_tape/glass");
    public static final ResourceLocation CASSETTE_TAPE_CORE_MODEL = new ResourceLocation(IamMusicPlayer.MODID, "item/cassette_tape/tape_core");
    public static final ResourceLocation CASSETTE_TAPE_CORE_AROUND_MODEL = new ResourceLocation(IamMusicPlayer.MODID, "item/cassette_tape/tape_core_around");
    public static final ResourceLocation CASSETTE_TAPE_ROLL_MODEL = new ResourceLocation(IamMusicPlayer.MODID, "item/cassette_tape/tape_roll");
    public static final ResourceLocation CASSETTE_TAPE_LABEL_MODEL = new ResourceLocation(IamMusicPlayer.MODID, "item/cassette_tape/label");
    public static final ResourceLocation CASSETTE_TAPE_LABEL_NO_IMAGE_MODEL = new ResourceLocation(IamMusicPlayer.MODID, "item/cassette_tape/label_no_image");

    public static final ResourceLocation ANTENNA = new ResourceLocation(IamMusicPlayer.MODID, "item/antenna/antenna");
    public static final ResourceLocation ANTENNA_TOP = new ResourceLocation(IamMusicPlayer.MODID, "item/antenna/antenna_top");
    public static final ResourceLocation ANTENNA_ROOT = new ResourceLocation(IamMusicPlayer.MODID, "item/antenna/antenna_root");

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
        register(CASSETTE_TAPE_BASE_NORMAL_MODEL);
        register(CASSETTE_TAPE_BASE_GLASS_MODEL);
        register(CASSETTE_TAPE_BASE_GLASS_COLOR_MODEL);
        register(CASSETTE_TAPE_MODEL);
        register(CASSETTE_TAPE_CONECTER);
        register(CASSETTE_TAPE_GLASS_MODEL);
        register(CASSETTE_TAPE_CORE_MODEL);
        register(CASSETTE_TAPE_CORE_AROUND_MODEL);
        register(CASSETTE_TAPE_ROLL_MODEL);
        register(CASSETTE_TAPE_LABEL_MODEL);
        register(CASSETTE_TAPE_LABEL_NO_IMAGE_MODEL);
        register(ANTENNA);
        register(ANTENNA_TOP);
        register(ANTENNA_ROOT);
    }

    private static void register(ResourceLocation location) {
        SpecialModelLoader.getInstance().registerLoadModel(location);
    }

}

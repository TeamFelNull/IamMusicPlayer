package dev.felnull.imp.client.model;

import dev.felnull.imp.IamMusicPlayer;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

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

    public static final ResourceLocation CASSETTE_DECK_LID = new ResourceLocation(IamMusicPlayer.MODID, "block/cassette_deck/cassette_deck_lid");

    public static final ResourceLocation MANUAL = new ResourceLocation(IamMusicPlayer.MODID, "item/manual/manual");
    public static final ResourceLocation MANUAL_TURNING = new ResourceLocation(IamMusicPlayer.MODID, "item/manual/manual_turning");

    public static void init(Consumer<ResourceLocation> addModel) {
        register(MUSIC_MANAGER_ACCESS_LAMP, addModel);
        register(MUSIC_MANAGER_OFF_ACCESS_LAMP, addModel);
        register(BOOMBOX_HANDLE, addModel);
        register(BOOMBOX_LID, addModel);
        register(BOOMBOX_BUTTONS, addModel);
        register(PARABOLIC_ANTENNA, addModel);
        register(KAMESUTA_ANTENNA, addModel);
        register(KATYOU_ANTENNA, addModel);
        register(IKISUGI_ANTENNA, addModel);
        register(FCOH_ANTENNA, addModel);
        register(CASSETTE_TAPE_BASE_NORMAL_MODEL, addModel);
        register(CASSETTE_TAPE_BASE_GLASS_MODEL, addModel);
        register(CASSETTE_TAPE_BASE_GLASS_COLOR_MODEL, addModel);
        register(CASSETTE_TAPE_MODEL, addModel);
        register(CASSETTE_TAPE_CONECTER, addModel);
        register(CASSETTE_TAPE_GLASS_MODEL, addModel);
        register(CASSETTE_TAPE_CORE_MODEL, addModel);
        register(CASSETTE_TAPE_CORE_AROUND_MODEL, addModel);
        register(CASSETTE_TAPE_ROLL_MODEL, addModel);
        register(CASSETTE_TAPE_LABEL_MODEL, addModel);
        register(CASSETTE_TAPE_LABEL_NO_IMAGE_MODEL, addModel);
        register(ANTENNA, addModel);
        register(ANTENNA_TOP, addModel);
        register(ANTENNA_ROOT, addModel);
        register(CASSETTE_DECK_LID, addModel);
        register(MANUAL, addModel);
        register(MANUAL_TURNING, addModel);
    }

    private static void register(ResourceLocation location, Consumer<ResourceLocation> addModel) {
        addModel.accept(location);
    }

}

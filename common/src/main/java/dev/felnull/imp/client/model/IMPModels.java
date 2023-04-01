package dev.felnull.imp.client.model;

import dev.felnull.imp.IMPHoliday;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.otyacraftengine.client.callpoint.ModelRegister;
import dev.felnull.otyacraftengine.client.model.ModelCollectiveRegister;
import dev.felnull.otyacraftengine.client.model.ModelHolder;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class IMPModels {
    private static final ModelCollectiveRegister REGISTER = ModelCollectiveRegister.create();

    public static final ModelHolder MUSIC_MANAGER_ACCESS_LAMP = REGISTER.register(modLoc("block/music_manager/music_manager_access_lamp"));
    public static final ModelHolder MUSIC_MANAGER_OFF_ACCESS_LAMP = REGISTER.register(modLoc("block/music_manager/music_manager_off_access_lamp"));

    public static final ModelHolder BOOMBOX_HANDLE = REGISTER.register(modLoc("block/boombox/boombox_handle"));
    public static final ModelHolder BOOMBOX_LID = REGISTER.register(modLoc("block/boombox/boombox_lid"));
    public static final ModelHolder BOOMBOX_BUTTONS = REGISTER.register(modLoc("block/boombox/boombox_buttons"));

    public static final ModelHolder PARABOLIC_ANTENNA = REGISTER.register(modLoc("item/parabolic_antenna/parabolic_antenna"));
    public static final ModelHolder KAMESUTA_ANTENNA = REGISTER.register(modLoc("item/parabolic_antenna/kamesuta_antenna"));
    public static final ModelHolder KATYOU_ANTENNA = REGISTER.register(modLoc("item/parabolic_antenna/katyou_antenna"));
    public static final ModelHolder IKISUGI_ANTENNA = REGISTER.register(modLoc("item/parabolic_antenna/ikisugi_antenna"));
    public static final ModelHolder FCOH_ANTENNA = REGISTER.register(modLoc("item/parabolic_antenna/fcoh_antenna"));
    @Nullable
    public static final ModelHolder XMAS_ANTENNA = Util.make(() -> IMPHoliday.isXmas() ? REGISTER.register(modLoc("item/parabolic_antenna/xmas/xmas_antenna")) : null);
    @Nullable
    public static final ModelHolder XMAS_ANTENNA_SIDE = Util.make(() -> IMPHoliday.isXmas() ? REGISTER.register(modLoc("item/parabolic_antenna/xmas/xmas_antenna_side")) : null);
    @Nullable
    public static final ModelHolder XMAS_ANTENNA_TAMA = Util.make(() -> IMPHoliday.isXmas() ? REGISTER.register(modLoc("item/parabolic_antenna/xmas/xmas_antenna_tama")) : null);
    @Nullable
    public static final ModelHolder APRIL_FOOL_ANTENNA = Util.make(() -> IMPHoliday.isAprilFool() ? REGISTER.register(modLoc("item/parabolic_antenna/april_fool/april_fool_antenna")) : null);

    public static final ModelHolder CASSETTE_TAPE_BASE_NORMAL_MODEL = REGISTER.register(modLoc("item/cassette_tape/base_nomal"));
    public static final ModelHolder CASSETTE_TAPE_BASE_GLASS_MODEL = REGISTER.register(modLoc("item/cassette_tape/base_glass"));
    public static final ModelHolder CASSETTE_TAPE_BASE_GLASS_COLOR_MODEL = REGISTER.register(modLoc("item/cassette_tape/base_glass_color"));
    public static final ModelHolder CASSETTE_TAPE_MODEL = REGISTER.register(modLoc("item/cassette_tape/tape"));
    public static final ModelHolder CASSETTE_TAPE_CONECTER = REGISTER.register(modLoc("item/cassette_tape/tape_conecter"));
    public static final ModelHolder CASSETTE_TAPE_GLASS_MODEL = REGISTER.register(modLoc("item/cassette_tape/glass"));
    public static final ModelHolder CASSETTE_TAPE_CORE_MODEL = REGISTER.register(modLoc("item/cassette_tape/tape_core"));
    public static final ModelHolder CASSETTE_TAPE_CORE_AROUND_MODEL = REGISTER.register(modLoc("item/cassette_tape/tape_core_around"));
    public static final ModelHolder CASSETTE_TAPE_ROLL_MODEL = REGISTER.register(modLoc("item/cassette_tape/tape_roll"));
    public static final ModelHolder CASSETTE_TAPE_LABEL_MODEL = REGISTER.register(modLoc("item/cassette_tape/label"));
    public static final ModelHolder CASSETTE_TAPE_LABEL_NO_IMAGE_MODEL = REGISTER.register(modLoc("item/cassette_tape/label_no_image"));

    public static final ModelHolder ANTENNA = REGISTER.register(modLoc("item/antenna/antenna"));
    public static final ModelHolder ANTENNA_TOP = REGISTER.register(modLoc("item/antenna/antenna_top"));
    public static final ModelHolder ANTENNA_ROOT = REGISTER.register(modLoc("item/antenna/antenna_root"));

    public static final ModelHolder CASSETTE_DECK_LID = REGISTER.register(modLoc("block/cassette_deck/cassette_deck_lid"));

    public static final ModelHolder MANUAL = REGISTER.register(modLoc("item/manual/manual"));
    public static final ModelHolder MANUAL_TURNING = REGISTER.register(modLoc("item/manual/manual_turning"));

    public static void init(ModelRegister modelRegister) {
        REGISTER.registering(modelRegister);
    }

    private static ResourceLocation modLoc(String name) {
        return new ResourceLocation(IamMusicPlayer.MODID, name);
    }
}

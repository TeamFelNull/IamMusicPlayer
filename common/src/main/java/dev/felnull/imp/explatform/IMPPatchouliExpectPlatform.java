package dev.felnull.imp.explatform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class IMPPatchouliExpectPlatform {
    @ExpectPlatform
    public static void openBookGUI(ServerPlayer player, ResourceLocation location) {
        throw new AssertionError();
    }


    @ExpectPlatform
    public static ResourceLocation getOpenBookGui() {
        throw new AssertionError();
    }
}

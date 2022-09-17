package dev.felnull.imp.integration;

import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.explatform.IMPPatchouliExpectPlatform;
import dev.felnull.otyacraftengine.integration.BaseIntegration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class PatchouliIntegration extends BaseIntegration {
    public static final PatchouliIntegration INSTANCE = new PatchouliIntegration();

    @Override
    public String getModId() {
        return "patchouli";
    }

    @Override
    public boolean isConfigEnabled() {
        return IamMusicPlayer.CONFIG.patchouliIntegration;
    }

    public void openBookGUI(ServerPlayer player, ResourceLocation location) {
        IMPPatchouliExpectPlatform.openBookGUI(player, location);
    }

    public ResourceLocation getOpenBookGui() {
        return IMPPatchouliExpectPlatform.getOpenBookGui();
    }
}

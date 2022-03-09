package dev.felnull.imp.integration;

import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.otyacraftengine.integration.PatchouliWrapper;

public class PatchouliIntegration {
    public static boolean isEnableIntegration() {
        return IamMusicPlayer.CONFIG.patchouliIntegration && PatchouliWrapper.isIntegrable();
    }
}

package red.felnull.imp.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import red.felnull.imp.api.register.IMPMusicPlayerRegister;
import red.felnull.imp.api.register.IMPMusicTrackerRegister;

public interface IIMPIntegration {
    @Environment(EnvType.CLIENT)
    default void registrationMusicPlayerLoader(IMPMusicPlayerRegister reg) {
    }

    default void registrationMusicTracker(IMPMusicTrackerRegister reg) {
    }
}

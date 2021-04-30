package red.felnull.imp.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import red.felnull.imp.api.register.IMPMusicPlayerRegister;

public interface IIMPIntegration {
    @Environment(EnvType.CLIENT)
    default void registrationMusicPlayerLoader(IMPMusicPlayerRegister reg) {
    }
}

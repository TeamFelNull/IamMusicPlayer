package red.felnull.imp.integration;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.client.handler.ClientHandler;
import red.felnull.imp.client.handler.RenderHandler;
import red.felnull.otyacraftengine.api.IOEIntegration;
import red.felnull.otyacraftengine.api.OEIntegration;
import red.felnull.otyacraftengine.api.register.OEClientHandlerRegister;
import red.felnull.otyacraftengine.api.register.OEModelLoaderPathRegister;

@OEIntegration
public class IMPOEIntegration implements IOEIntegration {

    @Override
    @Environment(EnvType.CLIENT)
    public void registrationClientHandler(OEClientHandlerRegister reg) {
        reg.register(ClientHandler.class);
        reg.register(RenderHandler.class);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void registrationModelLoaderPath(OEModelLoaderPathRegister reg) {
        reg.register(IamMusicPlayer.MODID);
    }
}

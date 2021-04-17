package red.felnull.imp.integration;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import red.felnull.imp.client.handler.ClientHandler;
import red.felnull.otyacraftengine.api.IOEIntegration;
import red.felnull.otyacraftengine.api.OEIntegration;
import red.felnull.otyacraftengine.api.register.OEHandlerRegister;

@OEIntegration
public class IamMusicPlayerOEIntegration implements IOEIntegration {

    @Override
    @Environment(EnvType.CLIENT)
    public void registrationClientHandler(OEHandlerRegister reg) {
        reg.register(ClientHandler.class);
    }
}

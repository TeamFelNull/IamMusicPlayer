package dev.felnull.imp.client.entrypoint;

import dev.felnull.imp.client.model.IMPModels;
import dev.felnull.otyacraftengine.client.callpoint.ClientCallPoint;
import dev.felnull.otyacraftengine.client.callpoint.ModelRegister;

@ClientCallPoint.Sign
public class IMPOEClientEntryPoint implements ClientCallPoint {
    @Override
    public void onModelRegistry(ModelRegister register) {
        IMPModels.init(register);
    }
}

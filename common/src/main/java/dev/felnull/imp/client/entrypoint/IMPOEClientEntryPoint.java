package dev.felnull.imp.client.entrypoint;

import dev.felnull.imp.client.model.IMPModels;
import dev.felnull.otyacraftengine.client.entrypoint.IOEClientEntryPoint;
import dev.felnull.otyacraftengine.client.entrypoint.OEClientEntryPoint;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

@OEClientEntryPoint
public class IMPOEClientEntryPoint implements IOEClientEntryPoint {
    @Override
    public void onModelRegistry(Consumer<ResourceLocation> addModel) {
        IMPModels.init(addModel);
    }
}

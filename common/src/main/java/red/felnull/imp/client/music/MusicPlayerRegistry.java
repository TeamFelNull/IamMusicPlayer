package red.felnull.imp.client.music;

import net.minecraft.resources.ResourceLocation;
import red.felnull.imp.api.IamMusicPlayerAPI;
import red.felnull.imp.api.register.IMPMusicPlayerRegister;
import red.felnull.imp.client.music.factory.IMusicPlayerFactory;
import red.felnull.otyacraftengine.api.register.OERegistries;

public class MusicPlayerRegistry {
    public static void init() {
        IMPMusicPlayerRegister musicPlayerRegister = new IMPMusicPlayerRegister();
        OERegistries.setRegistry(IMPMusicPlayerRegister.class, musicPlayerRegister);
        IamMusicPlayerAPI.getInstance().integrationConsumer(n -> {
            n.registrationMusicPlayerFactory(musicPlayerRegister);
        });

        OERegistries.getRegistry(IMPMusicPlayerRegister.class).getMap().values().forEach(IMusicPlayerFactory::init);
    }

    public static IMusicPlayerFactory getFactory(ResourceLocation location) {
        return OERegistries.getRegistry(IMPMusicPlayerRegister.class).getMap().get(location);
    }
}

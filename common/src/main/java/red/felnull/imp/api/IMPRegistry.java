package red.felnull.imp.api;

import net.minecraft.resources.ResourceLocation;
import red.felnull.imp.api.register.IMPMusicTrackerRegister;
import red.felnull.imp.music.info.tracker.MusicTracker;
import red.felnull.otyacraftengine.api.register.OERegistries;

import java.util.function.Supplier;

public class IMPRegistry {
    public static void init() {
        IMPMusicTrackerRegister trackerRegister = new IMPMusicTrackerRegister();
        OERegistries.setRegistry(IMPMusicTrackerRegister.class, trackerRegister);
        IamMusicPlayerAPI.getInstance().integrationConsumer(n -> {
            n.registrationMusicTracker(trackerRegister);
        });
    }

    public static Supplier<MusicTracker> getTracker(ResourceLocation location) {
        return OERegistries.getRegistry(IMPMusicTrackerRegister.class).getMap().get(location);
    }

    public static boolean isTrackerContains(ResourceLocation location) {
        return OERegistries.getRegistry(IMPMusicTrackerRegister.class).getMap().containsKey(location);
    }
}

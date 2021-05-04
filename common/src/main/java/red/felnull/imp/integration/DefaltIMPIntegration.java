package red.felnull.imp.integration;

import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.api.IIMPIntegration;
import red.felnull.imp.api.IMPIntegration;
import red.felnull.imp.api.register.IMPMusicPlayerRegister;
import red.felnull.imp.api.register.IMPMusicTrackerRegister;
import red.felnull.imp.client.music.loader.LavaPlayerLoader;
import red.felnull.imp.music.info.tracker.EntityMusicTracker;
import red.felnull.imp.music.info.tracker.FixedMusicTracker;

@IMPIntegration
public class DefaltIMPIntegration implements IIMPIntegration {
    @Override
    @Environment(EnvType.CLIENT)
    public void registrationMusicPlayerLoader(IMPMusicPlayerRegister reg) {
        reg.register(new ResourceLocation(IamMusicPlayer.MODID, "youtube"), new LavaPlayerLoader(new YoutubeAudioSourceManager(true)));
    }

    @Override
    public void registrationMusicTracker(IMPMusicTrackerRegister reg) {
        reg.register(new ResourceLocation(IamMusicPlayer.MODID, "fixed"), FixedMusicTracker::new);
        reg.register(new ResourceLocation(IamMusicPlayer.MODID, "entity"), EntityMusicTracker::new);
    }
}

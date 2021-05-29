package red.felnull.imp.integration;

import com.sedmelluq.discord.lavaplayer.container.MediaContainerRegistry;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.nico.NicoAudioSourceManager;
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
public class DefaultIMPIntegration implements IIMPIntegration {
    @Override
    @Environment(EnvType.CLIENT)
    public void registrationMusicPlayerLoader(IMPMusicPlayerRegister reg) {
        reg.register(new ResourceLocation(IamMusicPlayer.MODID, "http"), new LavaPlayerLoader("http", "https://cdn.discordapp.com/attachments/358878159615164416/839483788177702952/ikisugi.mp3", new HttpAudioSourceManager(MediaContainerRegistry.DEFAULT_REGISTRY)));
        reg.register(new ResourceLocation(IamMusicPlayer.MODID, "youtube"), new LavaPlayerLoader("youtube", "WQ8xRIHg9nU", new YoutubeAudioSourceManager(true)));
    }

    @Override
    public void registrationMusicTracker(IMPMusicTrackerRegister reg) {
        reg.register(new ResourceLocation(IamMusicPlayer.MODID, "fixed"), FixedMusicTracker::new);
        reg.register(new ResourceLocation(IamMusicPlayer.MODID, "entity"), EntityMusicTracker::new);
    }
}

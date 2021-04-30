package red.felnull.imp.integration;

import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.api.IIMPIntegration;
import red.felnull.imp.api.IMPIntegration;
import red.felnull.imp.api.register.IMPMusicPlayerRegister;
import red.felnull.imp.client.music.loader.LavaPlayerLoader;

@IMPIntegration
public class DefaltIMPIntegration implements IIMPIntegration {
    @Override
    @Environment(EnvType.CLIENT)
    public void registrationMusicPlayerLoader(IMPMusicPlayerRegister reg) {
        reg.register(new ResourceLocation(IamMusicPlayer.MODID, "youtube"), new LavaPlayerLoader(new YoutubeAudioSourceManager(true)));
    }
}

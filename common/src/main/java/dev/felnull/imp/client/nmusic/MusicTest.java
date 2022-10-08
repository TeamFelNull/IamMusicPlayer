package dev.felnull.imp.client.nmusic;

import dev.felnull.imp.client.nmusic.loader.LavaMusicLoader;
import dev.felnull.imp.client.nmusic.speaker.ALMusicSpeaker;
import dev.felnull.imp.music.resource.MusicSource;
import dev.felnull.imp.nmusic.tracker.IMPMusicTrackers;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.openal.AL11;

import java.util.UUID;

public class MusicTest {
    //https://sites.google.com/site/117florian/gijutsu-bunsho/openal
    //https://learn.microsoft.com/en-us/windows/win32/xaudio2/sound-cones
    //https://openal.org/documentation/openal-1.1-specification.pdf
    //https://projectvirtualworlds.wordpress.com/category/openal/
    //https://www.cnblogs.com/zeppelin5/p/10019395.html
    //https://fivedots.coe.psu.ac.th/~ad/jg2/ch13/joal.pdf
    //https://www.sitepoint.com/creating-fun-immersive-audio-experiences-web-audio/
    public static void test(int src) {


        System.out.println("test");

        AL11.alSource3f(src, AL11.AL_DIRECTION, 90, 180, 0);
        AL11.alSourcef(src, AL11.AL_CONE_INNER_ANGLE, 30);
        AL11.alSourcef(src, AL11.AL_CONE_OUTER_ANGLE, 60);
        AL11.alSourcef(src, AL11.AL_CONE_OUTER_GAIN, 0f);

    }

    public static void test2(Player player) throws Exception {
        var ms = new MusicSource("http", "", 114514);
        var ml = new LavaMusicLoader();
        ml.tryLoad(ms);
        var mp = ml.createMusicPlayer();
        mp.addSpeaker(UUID.randomUUID(), new ALMusicSpeaker(IMPMusicTrackerFactory.linked(IMPMusicTrackers.createFixedTracker(player.position(), true, 1, 10))));
        mp.load(0);
        mp.play(0);
    }
}

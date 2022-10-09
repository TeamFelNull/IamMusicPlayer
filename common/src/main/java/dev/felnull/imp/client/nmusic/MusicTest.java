package dev.felnull.imp.client.nmusic;

import dev.felnull.imp.client.nmusic.loader.LavaMusicLoader;
import dev.felnull.imp.client.nmusic.speaker.ALMusicSpeaker;
import dev.felnull.imp.music.resource.MusicSource;
import dev.felnull.imp.nmusic.tracker.IMPMusicTrackers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
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
        var ms = new MusicSource("http", "https://cdn.discordapp.com/attachments/358878159615164416/1026808752898322472/Atari_800_XL_ST-NICCC_2000.mp3", 114514);
        var ml = new LavaMusicLoader();
        ml.tryLoad(ms);
        var mp = ml.createMusicPlayer();
        mp.addSpeaker(UUID.randomUUID(), new ALMusicSpeaker(IMPMusicTrackerFactory.linked(IMPMusicTrackers.createFixedTracker(player.position(), false, 1, 10))));
        mp.load(0);
        mp.play(0);
    }

    public static void test3(Player player) throws Exception {
        var ms = new MusicSource("youtube", "", 114514);
        var me = MusicEngine.getInstance();
        var id = UUID.randomUUID();

        me.load(id, ms, 0, (success, time, error) -> {
            System.out.println(success + ":" + time + ":" + error);
            if (error != null)
                error.printStackTrace();
            boolean r = me.play(id, 0);
            System.out.println(r);

            /*
            new Thread(() -> {
                try {
                    Thread.sleep(1000 * 3);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                me.addSpeaker(id, UUID.randomUUID(), tracker);
            }).start();
             */

        });

        /*for (int i = 0; i < 5; i++) {
            var tracker = IMPMusicTrackerFactory.linked(IMPMusicTrackers.createFixedTracker(player.position().add(0, i, 0), false, 1, 10));
            boolean ret = me.addSpeaker(id, UUID.randomUUID(), tracker);
            System.out.println(ret);
        }*/

        try {
            double x = player.position().x();
            double y = player.position().y();
            double z = player.position().z();
            var ens = player.level.getEntitiesOfClass(Entity.class, new AABB(-30 + x, -30 + y, -30 + z, 30 + x, 30 + y, 30 + z));
            for (Entity en : ens) {
                if (en instanceof Player)
                    continue;
                var tracker = IMPMusicTrackerFactory.linked(IMPMusicTrackers.createEntityTracker(en, 1, 10));
                boolean ret = me.addSpeaker(id, UUID.randomUUID(), tracker);
                System.out.println("sp:" + ret);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

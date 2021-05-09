package red.felnull.imp.client.music.player;

import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.minecraft.world.phys.Vec3;
import red.felnull.imp.music.resource.MusicLocation;

public class LavaLineMusicPlayer extends LavaAbstractMusicPlayer {
    public LavaLineMusicPlayer(MusicLocation location, AudioPlayerManager audioPlayerManager, AudioDataFormat dataformat) {
        super(location, audioPlayerManager, dataformat);
    }

    @Override
    public void ready(long position) throws Exception {
        super.ready(position);
    }

    @Override
    public void play(long delay) {

    }

    @Override
    public void stop() {

    }

    @Override
    public void setPosition(long position) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void unpause() {

    }

    @Override
    public boolean playing() {
        return false;
    }

    @Override
    public boolean stopped() {
        return false;
    }

    @Override
    public void setSelfPosition(Vec3 vec3) {

    }

    @Override
    public void setVolume(float f) {

    }

    @Override
    public void linearAttenuation(float f) {

    }

    @Override
    public void disableAttenuation() {

    }
}

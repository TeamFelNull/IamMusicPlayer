package dev.felnull.imp.client.nmusic.speaker;

import dev.felnull.imp.client.nmusic.speaker.buffer.DirectMusicBuffer;
import dev.felnull.imp.client.nmusic.speaker.buffer.MusicBufferSpeakerData;
import dev.felnull.imp.nmusic.tracker.MusicTracker;

import java.util.List;

public class JavaSoundAPIMusicSpeaker extends BaseMusicSpeaker<DirectMusicBuffer> {

    public JavaSoundAPIMusicSpeaker(MusicTracker tracker) {
        super(tracker);
    }

    @Override
    public void tick() {

    }

    @Override
    public void insertAudio(DirectMusicBuffer data) {

    }

    @Override
    public DirectMusicBuffer createBuffer() {
        return new DirectMusicBuffer();
    }

    @Override
    public boolean isDead() {
        return false;
    }


    @Override
    public void destroy() throws Exception {

    }

    @Override
    public List<DirectMusicBuffer> pollBuffers() {
        return null;
    }

    @Override
    public MusicBufferSpeakerData getBufferSpeakerData() {
        return new MusicBufferSpeakerData(true);
    }
}

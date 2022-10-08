package dev.felnull.imp.client.nmusic.speaker;

import dev.felnull.imp.client.nmusic.speaker.buffer.ALMusicBuffer;
import dev.felnull.imp.client.nmusic.speaker.buffer.MusicBufferSpeakerData;
import dev.felnull.imp.nmusic.tracker.MusicTracker;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.openal.AL11;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import static org.lwjgl.openal.AL10.*;

public class ALMusicSpeaker extends BaseMusicSpeaker<ALMusicBuffer> {
    private final Queue<ALMusicBuffer> buffers = new ArrayDeque<>();
    private final int source;

    public ALMusicSpeaker(MusicTracker tracker) {
        super(tracker);
        this.source = alGenSources();
        alSourceUpdate();
    }

    @Override
    public void tick() {
        alSourceTick();
    }

    @Override
    public void insertAudio(ALMusicBuffer data) {
        buffers.add(data);
        alSourceQueueBuffers(source, data.getBuffer());
    }

    @Override
    public ALMusicBuffer createBuffer() {
        return new ALMusicBuffer();
    }


    @Override
    public void play() {
        alSourcePlay(source);
    }

    @Override
    public boolean isDead() {
        return false;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void destroy() throws Exception {
        alDeleteSources(source);
    }

    @Override
    public List<ALMusicBuffer> pollBuffers() {
        //使用済みバッファーデータの数を取得
        int proBuffs = alGetSourcei(source, AL11.AL_BUFFERS_PROCESSED);
        if (proBuffs >= 1) {
            List<ALMusicBuffer> proBuffers = new ArrayList<>();
            int[] bufIds = null;
            for (int i = 0; i < proBuffs; i++) {
                var buf = buffers.poll();
                if (buf != null) {
                    proBuffers.add(buf);
                    bufIds = ArrayUtils.add(bufIds, buf.getBuffer());
                }
            }
            if (bufIds != null)
                alSourceUnqueueBuffers(source, bufIds);

            return proBuffers;
        }
        return new ArrayList<>();
    }

    @Override
    public MusicBufferSpeakerData getBufferSpeakerData() {
        return new MusicBufferSpeakerData(getTracker().getSpeakerInfo().relative());
    }

    @Override
    public void update(MusicTracker tracker) {
        super.update(tracker);
        alSourceUpdate();
    }

    private void alSourceUpdate() {
        alSourcei(source, AL_LOOPING, AL_FALSE);
        alSourcei(source, AL_SOURCE_RELATIVE, AL_TRUE);
        // alSourcef(source, AL_PITCH, 1f);
    }

    private void alSourceTick() {

    }
}

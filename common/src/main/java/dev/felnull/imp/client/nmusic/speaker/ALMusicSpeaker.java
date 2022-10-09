package dev.felnull.imp.client.nmusic.speaker;

import dev.felnull.imp.client.nmusic.speaker.buffer.ALMusicBuffer;
import dev.felnull.imp.client.nmusic.speaker.buffer.MusicBufferSpeakerData;
import dev.felnull.imp.client.util.ALUtils;
import dev.felnull.imp.client.util.SoundMath;
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
        ALUtils.assertOnSoundThread();

        this.source = alGenSources();
        alSourceInit();
        alSourceUpdate();

        ALUtils.checkError();
    }

    @Override
    public void tick() {
        ALUtils.assertOnSoundThread();

        alSourceUpdate();

        ALUtils.checkError();
    }

    @Override
    public void pause() {
        super.pause();
        if (getPlayState() == AL11.AL_PLAYING)
            alSourcePause(this.source);
    }

    @Override
    public void resume() {
        super.resume();
        if (getPlayState() == AL11.AL_PAUSED)
            alSourcePlay(this.source);
    }

    private int getPlayState() {
        return alGetSourcei(this.source, AL11.AL_SOURCE_STATE);
    }

    @Override
    public void insertAudio(ALMusicBuffer data) {
        ALUtils.assertOnSoundThread();

        buffers.add(data);
        alSourceQueueBuffers(source, data.getBuffer());

        ALUtils.checkError();
    }

    @Override
    public ALMusicBuffer createBuffer() {
        return new ALMusicBuffer();
    }


    @Override
    public void play() {
        super.play();
        ALUtils.assertOnSoundThread();

        alSourcePlay(source);

        ALUtils.checkError();
    }

    @Override
    public boolean isDead() {
        return false;
    }

    @Override
    public void destroy() throws Exception {
        ALUtils.assertOnSoundThread();

        alDeleteSources(source);

        ALUtils.checkError();
    }

    @Override
    public List<ALMusicBuffer> pollBuffers() {
        ALUtils.assertOnSoundThread();

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

            ALUtils.checkError();
            return proBuffers;
        }

        ALUtils.checkError();
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

    private void alSourceInit() {
        ALUtils.assertOnSoundThread();

        alSourcei(source, AL_LOOPING, AL_FALSE);

        ALUtils.checkError();
    }

    private void alSourceUpdate() {
        ALUtils.assertOnSoundThread();

        var spi = getTracker().getSpeakerInfo();

        alSourcei(source, AL_SOURCE_RELATIVE, spi.relative() ? AL_TRUE : AL_FALSE);
        if (!spi.relative()) {
            alSource3f(source, AL_POSITION, (float) spi.position().x(), (float) spi.position().y(), (float) spi.position().z());
            alSourcef(source, AL11.AL_GAIN, spi.volume());
            linearAttenuation(spi.range());
        } else {
            alSourcef(source, AL11.AL_GAIN, (float) SoundMath.calculatePseudoAttenuation(spi.position(), spi.range(), spi.volume()));
        }

        ALUtils.checkError();
    }

    private void linearAttenuation(float r) {
        alSourcei(source, AL11.AL_DISTANCE_MODEL, 53251);
        alSourcef(source, AL11.AL_MAX_DISTANCE, r);
        alSourcef(source, AL11.AL_ROLLOFF_FACTOR, 1.0F);
        alSourcef(source, AL11.AL_REFERENCE_DISTANCE, 0.0F);
    }
}

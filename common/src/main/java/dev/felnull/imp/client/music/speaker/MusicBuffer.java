package dev.felnull.imp.client.music.speaker;

import dev.felnull.imp.client.music.AudioInfo;
import dev.felnull.imp.client.util.MusicUtils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.openal.AL11.*;

public class MusicBuffer {
    private final List<MusicSpeaker> insertedSpeakers = new ArrayList<>();
    private final int buffer;

    public MusicBuffer(ByteBuffer buffer, boolean relative, AudioInfo audioData) {
        MusicUtils.assertOnMusicTick();

        this.buffer = alGenBuffers();
        alBufferData(this.buffer, getFormat(relative ? audioData.channel() : 1, audioData.bit()), buffer, audioData.sampleRate());

        MusicUtils.checkALError();
    }

    public boolean canRelease() {
        return insertedSpeakers.isEmpty();
    }

    public void release() {
        if (!canRelease())
            throw new RuntimeException("Cannot be released because it is in use");

        MusicUtils.assertOnMusicTick();

        alDeleteBuffers(buffer);

        MusicUtils.checkALError();
    }

    private int getFormat(int channel, int bit) {
        if (channel == 1) {
            if (bit == 8) {
                return AL_FORMAT_MONO8;
            }
            if (bit == 16) {
                return AL_FORMAT_MONO16;
            }
        } else if (channel == 2) {
            if (bit == 8) {
                return AL_FORMAT_STEREO8;
            }

            if (bit == 16) {
                return AL_FORMAT_STEREO16;
            }
        }

        throw new RuntimeException("Unsupported format");
    }

    public int getBufferId() {
        return buffer;
    }

    protected void addSpeaker(MusicSpeaker speaker) {
        this.insertedSpeakers.add(speaker);
    }

    protected void removeSpeaker(MusicSpeaker speaker) {
        this.insertedSpeakers.remove(speaker);
    }
}

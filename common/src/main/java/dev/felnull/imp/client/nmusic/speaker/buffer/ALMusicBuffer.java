package dev.felnull.imp.client.nmusic.speaker.buffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL11;

import java.nio.ByteBuffer;

public class ALMusicBuffer implements MusicBuffer<ALMusicBuffer.ConvertedData> {
    private final int buffer;

    public ALMusicBuffer() {
        this.buffer = AL11.alGenBuffers();
    }

    public int getBuffer() {
        return buffer;
    }

    @Override
    public ConvertedData asyncConvertBuffer(byte[] data, int sampleRate, int channel, int bit) {
        return new ConvertedData(getBuffer(data), getFormat(channel, bit), sampleRate * channel);
    }

    @Override
    public void putBuffer(ConvertedData data) {
        AL11.alBufferData(this.buffer, data.format, data.buffer, data.frequency);
    }

    @Override
    public void release() {
        AL11.alDeleteBuffers(buffer);
    }

    private ByteBuffer getBuffer(byte[] array) {
        ByteBuffer audioBuffer2 = BufferUtils.createByteBuffer(array.length);
        audioBuffer2.put(array);
        audioBuffer2.flip();
        return audioBuffer2;
    }

    private int getFormat(int channel, int bit) {
        if (channel == 1) {
            if (bit == 8) {
                return AL11.AL_FORMAT_MONO8;
            }
            if (bit == 16) {
                return AL11.AL_FORMAT_MONO16;
            }
        } else if (channel == 2) {
            if (bit == 8) {
                return AL11.AL_FORMAT_STEREO8;
            }

            if (bit == 16) {
                return AL11.AL_FORMAT_STEREO16;
            }
        }
        return AL11.AL_FORMAT_MONO16;
    }

    public static record ConvertedData(ByteBuffer buffer, int format, int frequency) {
    }
}

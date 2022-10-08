package dev.felnull.imp.client.nmusic.speaker.buffer;

public class DirectMusicBuffer implements MusicBuffer<DirectMusicBuffer.DirectData> {
    private byte[] data;

    public byte[] getData() {
        return data;
    }

    @Override
    public DirectData asyncConvertBuffer(byte[] data, int sampleRate, int channel, int bit) {
        return new DirectData(data);
    }

    @Override
    public void putBuffer(DirectData data) {
        this.data = data.data;
    }

    @Override
    public void release() {
        this.data = null;
    }

    public static record DirectData(byte[] data) {
    }
}

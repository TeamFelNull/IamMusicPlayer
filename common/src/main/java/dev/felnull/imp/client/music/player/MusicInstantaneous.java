package dev.felnull.imp.client.music.player;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * 1秒間の音楽データ情報
 *
 * @param sampleTime
 * @param channelInstantaneous
 */
public record MusicInstantaneous(long sampleTime, List<ChannelInstantaneous> channelInstantaneous) {
    public static MusicInstantaneous create(long time, byte[] data, int channel, int bit) {
        List<ChannelInstantaneous> cis = new ArrayList<>();

        for (int i = 0; i < channel; i++)
            cis.add(new ChannelInstantaneous(new float[60]));

        if (bit != 16)
            throw new RuntimeException("Unsupported bit");

        int ovs = data.length / 2;
        int oct = ovs / 60 / channel;

        for (int i = 0; i < 60; i++) {
            float[] totals = new float[channel];
            for (int j = 0; j < oct; j++) {
                int l = (oct * i + j) * (2 * channel);
                for (int k = 0; k < channel; k++) {
                    int l1 = l + 2 * k;
                    int l2 = l1 + 1;

                    byte[] d = {data[l1], data[l2]};
                    short r = ByteBuffer.wrap(d).order(ByteOrder.LITTLE_ENDIAN).getShort();
                    float val = Math.abs((float) r / (float) Short.MAX_VALUE);
                    totals[k] += val;
                }
            }
            for (int j = 0; j < totals.length; j++) {
                cis.get(j).waves[i] = totals[j] / (float) (oct / channel);
            }
        }

        return new MusicInstantaneous(time, cis);
    }

    public static record ChannelInstantaneous(float[] waves) {
    }

    public float[] getWaves(int channel) {
        return channelInstantaneous.get(channel).waves;
    }
}

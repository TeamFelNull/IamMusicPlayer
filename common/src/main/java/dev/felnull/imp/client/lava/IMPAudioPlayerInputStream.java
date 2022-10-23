package dev.felnull.imp.client.lava;

import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.format.AudioDataFormatTools;
import com.sedmelluq.discord.lavaplayer.format.AudioPlayerInputStream;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.tools.ExceptionTools;
import com.sedmelluq.discord.lavaplayer.track.TrackStateListener;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BooleanSupplier;

public class IMPAudioPlayerInputStream extends AudioPlayerInputStream {
    private final AudioPlayer player;
    private final AudioDataFormat format;
    private final long timeout;
    private final boolean provideSilence;
    private final BooleanSupplier forceStop;
    private ByteBuffer current;


    public IMPAudioPlayerInputStream(AudioDataFormat format, AudioPlayer player, long timeout, boolean provideSilence, BooleanSupplier forceStop) {
        super(format, player, timeout, provideSilence);
        this.format = format;
        this.player = player;
        this.timeout = timeout;
        this.provideSilence = provideSilence;
        this.forceStop = forceStop;
    }

    @Override
    public int read() throws IOException {
        ensureAvailable();
        return current.get();
    }

    @Override
    public int read(byte[] buffer, int offset, int length) throws IOException {
        int currentOffset = offset;

        while (currentOffset < length) {
            ensureAvailable();

            int piece = Math.min(current.remaining(), length - currentOffset);
            current.get(buffer, currentOffset, piece);
            currentOffset += piece;
        }

        return currentOffset - offset;
    }

    @Override
    public int available() throws IOException {
        return current != null ? current.remaining() : 0;
    }

    public static AudioInputStream createForceStopStream(AudioPlayer player, AudioDataFormat format, long stuckTimeout, boolean provideSilence, BooleanSupplier forceStop) {
        AudioFormat jdkFormat = AudioDataFormatTools.toAudioFormat(format);
        return new AudioInputStream(new IMPAudioPlayerInputStream(format, player, stuckTimeout, provideSilence, forceStop), jdkFormat, -1);
    }

    private void ensureAvailable() throws IOException {
        while (available() == 0) {
            try {
                attemptRetrieveFrame();
            } catch (TimeoutException e) {
                notifyTrackStuck();
            } catch (InterruptedException e) {
                ExceptionTools.keepInterrupted(e);
                throw new InterruptedIOException();
            }

            if (available() == 0 && (provideSilence || forceStop.getAsBoolean())) {
                addFrame(format.silenceBytes());
                break;
            }
        }
    }

    private void attemptRetrieveFrame() throws TimeoutException, InterruptedException {
        AudioFrame frame = player.provide(timeout, TimeUnit.MILLISECONDS);

        if (frame != null) {
            if (!format.equals(frame.getFormat())) {
                throw new IllegalStateException("Frame read from the player uses a different format than expected.");
            }

            addFrame(frame.getData());
        } else if (!(provideSilence || forceStop.getAsBoolean())) {
            Thread.sleep(10);
        }
    }

    private void addFrame(byte[] data) {
        current = ByteBuffer.wrap(data);
    }

    private void notifyTrackStuck() {
        if (player instanceof TrackStateListener) {
            ((TrackStateListener) player).onTrackStuck(player.getPlayingTrack(), timeout);
        }
    }
}

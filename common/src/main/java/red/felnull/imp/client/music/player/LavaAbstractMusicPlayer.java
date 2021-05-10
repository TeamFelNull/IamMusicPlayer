package red.felnull.imp.client.music.player;

import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.format.AudioDataFormatTools;
import com.sedmelluq.discord.lavaplayer.format.AudioPlayerInputStream;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import red.felnull.imp.music.resource.MusicLocation;
import red.felnull.imp.throwable.InvalidIdentifierException;

import javax.sound.sampled.AudioInputStream;
import java.io.IOException;

public abstract class LavaAbstractMusicPlayer implements IMusicPlayer {
    private static final Logger LOGGER = LogManager.getLogger(LavaAbstractMusicPlayer.class);
    protected final MusicLocation musicLocation;
    protected final AudioPlayerManager audioPlayerManager;
    protected final AudioDataFormat dataformat;
    protected final AudioPlayer audioPlayer;
    protected long startPosition;
    protected long startTime;
    private boolean trackLoaded;
    private Exception exception;
    protected long duration;
    protected AudioInputStream stream;
    protected boolean stereo;
    protected float attenuation;
    protected Vec3 position = Vec3.ZERO;

    public LavaAbstractMusicPlayer(MusicLocation location, AudioPlayerManager audioPlayerManager, AudioDataFormat dataformat) {
        this.musicLocation = location;
        this.audioPlayerManager = audioPlayerManager;
        this.dataformat = dataformat;
        this.audioPlayer = audioPlayerManager.createPlayer();
    }

    @Override
    public void ready(long position) throws Exception {
        startPosition = position;
        this.trackLoaded = false;
        audioPlayerManager.loadItem(musicLocation.getIdentifier(), new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                track.setPosition(position);
                audioPlayer.startTrack(track, false);
                if (!track.getInfo().isStream)
                    duration = track.getDuration();

                try {

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                trackLoaded = true;
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                exception = new InvalidIdentifierException("ambiguous");
                trackLoaded = true;
            }

            @Override
            public void noMatches() {
                exception = new InvalidIdentifierException("nomatche");
                trackLoaded = true;
            }

            @Override
            public void loadFailed(FriendlyException ex) {
                exception = ex;
                trackLoaded = true;
            }
        });

        while (!trackLoaded) {
            Thread.sleep(20);
        }

        if (exception != null)
            throw exception;

        stream = AudioPlayerInputStream.createStream(audioPlayer, dataformat, dataformat.frameDuration(), false);

        stereo = AudioDataFormatTools.toAudioFormat(dataformat).getChannels() >= 2;
    }

    @Override
    public long getPosition() {
        return System.currentTimeMillis() - startTime + startPosition;
    }

    @Override
    public MusicLocation getMusicLocation() {
        return musicLocation;
    }

    @Override
    public void update() {

    }

    @Override
    public void play(long delay) {
        startTime = System.currentTimeMillis();
        startPosition += delay;
    }

    @Override
    public void setPosition(long position) {
        if (audioPlayer.getPlayingTrack() != null) {
            audioPlayer.getPlayingTrack().setPosition(position);
        }
    }

    @Override
    public void destroy() {
        startTime = 0;
        startPosition = 0;
        if (this.stream != null) {
            try {
                this.stream.close();
            } catch (IOException var2) {
                LOGGER.error("Failed to close audio stream", var2);
            }
            this.stream = null;
        }
        audioPlayer.destroy();
    }

    @Override
    public void setSelfPosition(Vec3 vec3) {
        position = vec3;
    }

    @Override
    public void linearAttenuation(float f) {
        attenuation = f;
    }
}

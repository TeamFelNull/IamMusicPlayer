package dev.felnull.imp.server.music.ringer;

import dev.felnull.imp.block.BoomboxData;
import dev.felnull.imp.music.resource.Music;
import dev.felnull.imp.music.resource.MusicSource;
import dev.felnull.imp.server.music.MusicManager;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Random;

public interface IBoomboxRinger extends IMusicRinger {
    Random rand = new Random();

    @NotNull
    public BoomboxData getRingerBoomboxData();

    @Override
    default public boolean isRingerPlaying() {
        return getRingerBoomboxData().isPlaying();
    }

    @Override
    default public void setRingerPlaying(boolean playing) {
        getRingerBoomboxData().setPlaying(playing);
    }

    @Override
    default public @Nullable MusicSource getRingerMusicSource() {
        if (isRingerStream())
            return getRingerBoomboxData().getRadioSource();
        if (getRingerBoomboxData().isRadioRemote()) {
            var m = getRingerBoomboxData().getSelectedMusic();
            if (m != null)
                return m.getSource();
        } else {
            return getRingerBoomboxData().getMusicSource();
        }
        return null;
    }

    @Override
    default public boolean isRingerLoop() {
        return getRingerBoomboxData().isLoop();
    }

    @Override
    default public long getRingerPosition() {
        return getRingerBoomboxData().getMusicPosition();
    }

    @Override
    default public void setRingerPosition(long position) {
        getRingerBoomboxData().setMusicPosition(position);
    }

    @Override
    default public float getRingerVolume() {
        return getRingerBoomboxData().getRawVolume();
    }

    @Override
    default public float getRingerRange() {
        return 90f * getRingerBoomboxData().getRawVolume();
    }

    @Override
    default public boolean isRingerStream() {
        return getRingerBoomboxData().isRadioStream();
    }

    @Override
    default public void ringerEnd() {
        if (getRingerBoomboxData().isRadioRemote()) {
            var con = getRingerBoomboxData().getContinuousType();
            if (con != BoomboxData.ContinuousType.NONE) {
                var m = getRingerBoomboxData().getSelectedMusic();
                if (m == null) return;
                var mm = MusicManager.getInstance();
                var pl = mm.getPlaylistByMusic(getRingerLevel().getServer(), m.getUuid());
                if (pl == null) return;
                var ml = new ArrayList<>(pl.getMusicList());
                if (ml.isEmpty() || ml.size() == 1) return;
                Music nm = null;
                if (con == BoomboxData.ContinuousType.ORDER) {
                    var nmi = ml.get((ml.indexOf(m.getUuid()) + 1) % ml.size());
                    nm = mm.getSaveData(getRingerLevel().getServer()).getMusics().get(nmi);
                } else if (con == BoomboxData.ContinuousType.RANDOM) {
                    ml.remove(m.getUuid());
                    var nmi = ml.get(rand.nextInt(ml.size()));
                    nm = mm.getSaveData(getRingerLevel().getServer()).getMusics().get(nmi);
                }
                if (nm != null)
                    getRingerBoomboxData().setSelectedMusic(nm);
            }
        }
    }

    @Override
    default boolean isRingerMute() {
        return getRingerBoomboxData().isMute();
    }

    @Override
    default boolean isRingerRemote() {
        return getRingerBoomboxData().isRadioRemote();
    }

    @Override
    @Nullable
    default String getRingerMusicAuthor() {
        var data = getRingerBoomboxData();
        if (data.isRadioRemote() && data.getSelectedMusic() != null)
            return data.getSelectedMusic().getAuthor();
        if (data.getCassetteTapeMusic() != null)
            return data.getCassetteTapeMusic().getAuthor();
        return null;
    }

    @Override
    default @NotNull ItemStack getRingerAntenna() {
        return getRingerBoomboxData().getAntenna();
    }
}

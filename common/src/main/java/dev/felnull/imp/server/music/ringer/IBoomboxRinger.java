package dev.felnull.imp.server.music.ringer;

import dev.felnull.imp.data.BoomboxData;
import dev.felnull.imp.music.resource.Music;
import dev.felnull.imp.music.resource.MusicSource;
import dev.felnull.imp.server.music.MusicManager;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Random;

public interface IBoomboxRinger extends IMusicRinger {
    Random rand = new Random();

    @NotNull
    public BoomboxData getRingerBoomboxData();

    @Override
    default public boolean isRingerPlaying(ServerLevel level) {
        return getRingerBoomboxData().isPlaying();
    }

    @Override
    default public void setRingerPlaying(ServerLevel level, boolean playing) {
        getRingerBoomboxData().setPlaying(playing);
    }

    @Override
    default public @Nullable MusicSource getRingerMusicSource(ServerLevel level) {
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
    default public boolean isRingerLoop(ServerLevel level) {
        return getRingerBoomboxData().isLoop();
    }

    @Override
    default public long getRingerPosition(ServerLevel level) {
        return getRingerBoomboxData().getMusicPosition();
    }

    @Override
    default public void setRingerPosition(ServerLevel level, long position) {
        getRingerBoomboxData().setMusicPosition(position);
    }

    @Override
    default public float getRingerVolume(ServerLevel level) {
        return getRingerBoomboxData().getRawVolume();
    }

    @Override
    default public float getRingerRange(ServerLevel level) {
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
                var pl = mm.getPlaylistByMusic(m.getUuid());
                if (pl == null) return;
                var ml = new ArrayList<>(pl.getMusicList());
                if (ml.isEmpty() || ml.size() == 1) return;
                Music nm = null;
                if (con == BoomboxData.ContinuousType.ORDER) {
                    var nmi = ml.get((ml.indexOf(m.getUuid()) + 1) % ml.size());
                    nm = mm.getSaveData().getMusics().get(nmi);
                } else if (con == BoomboxData.ContinuousType.RANDOM) {
                    ml.remove(m.getUuid());
                    var nmi = ml.get(rand.nextInt(ml.size()));
                    nm = mm.getSaveData().getMusics().get(nmi);
                }
                if (nm != null)
                    getRingerBoomboxData().setSelectedMusic(nm);
            }
        }
    }
}

package dev.felnull.imp.api;

import dev.felnull.imp.api.music.MusicRingerAccess;
import dev.felnull.imp.server.music.ringer.MusicRing;
import dev.felnull.imp.server.music.ringer.MusicRingManager;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * IamMusicPlayerのAPI
 * このAPIは過去バージョンとの互換性を維持します
 *
 * @author MORIMORI0317
 * @since 2.0
 */
public class IamMusicPlayerAPI {
    /**
     * 現在存在する音源の数
     *
     * @return 音源数
     */
    public static int getRingerCount() {
        AtomicInteger ct = new AtomicInteger();
        MusicRingManager.getInstance().getMusicRingers().values().stream().map(MusicRing::getRingers).map(Map::size).forEach(ct::addAndGet);
        return ct.get();
    }

    /**
     * 現在存在する音源の数
     *
     * @param level 取得したい音源のレベル
     * @return 音源数
     */
    public static int getRingerCount(ServerLevel level) {
        var rs = MusicRingManager.getInstance().getMusicRingers().get(level);
        if (rs != null)
            return rs.getRingers().size();
        return 0;
    }

    /**
     * 現在再生中の音源の数
     *
     * @return 音源の数
     */
    public static int getPlayingRingerCount() {
        AtomicInteger ct = new AtomicInteger();
        MusicRingManager.getInstance().getMusicRingers().entrySet().stream().map(n -> (int) n.getValue().getRingers().values().stream().filter(m -> m.isRingerPlaying(n.getKey())).count()).forEach(ct::addAndGet);
        return ct.get();
    }

    /**
     * 現在再生中の音源の数
     *
     * @param level 取得したい音源のレベル
     * @return 音源の数
     */
    public static int getPlayingRingerCount(ServerLevel level) {
        var rs = MusicRingManager.getInstance().getMusicRingers().get(level);
        if (rs != null)
            return (int) rs.getRingers().values().stream().filter(n -> n.isRingerPlaying(level)).count();
        return 0;
    }

    /**
     * 現在存在する音源を取得
     *
     * @return 音源
     */
    @NotNull
    public static List<MusicRingerAccess> getRingers() {
        List<MusicRingerAccess> pls = new ArrayList<>();
        MusicRingManager.getInstance().getMusicRingers().values().stream().map(MusicRing::getRingers).map(Map::values).forEach(n -> n.forEach(m -> pls.add(new MusicRingerAccess(m))));
        return Collections.unmodifiableList(pls);
    }

    /**
     * 現在存在する音源を取得
     *
     * @param level 取得したい音源のレベル
     * @return 音源
     */
    @NotNull
    public static List<MusicRingerAccess> getRingers(ServerLevel level) {

        List<MusicRingerAccess> pls = new ArrayList<>();
        var mr = MusicRingManager.getInstance().getMusicRingers().get(level);
        if (mr != null) {
            mr.getRingers().values().forEach(n -> pls.add(new MusicRingerAccess(n)));
        }
        return Collections.unmodifiableList(pls);
    }
}

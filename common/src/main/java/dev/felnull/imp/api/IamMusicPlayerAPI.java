package dev.felnull.imp.api;

import dev.felnull.imp.impl.IamMusicPlayerAPIImpl;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;

/**
 * IamMusicPlayerのAPI
 * 別のMODからIMP関係にアクセスする場合はこのAPIの利用を推奨します
 * このAPIは過去バージョンとの互換性を維持させてください。
 * Please maintain compatibility with past versions of this API.
 *
 * @author MORIMORI0317
 * @since 3.17
 */
public interface IamMusicPlayerAPI {
    static IamMusicPlayerAPI getInstance() {
        return IamMusicPlayerAPIImpl.INSTANCE;
    }

    /**
     * 現在存在する音源を取得
     * サーバー側でのみ呼び出し可能
     *
     * @return 音源
     */
    @Unmodifiable
    Collection<MusicRingerAccess> getRingers();

    /**
     * 現在存在する音源を取得
     * サーバー側でのみ呼び出し可能
     *
     * @param level 取得したい音源のレベル
     * @return 音源
     */
    @Unmodifiable
    Collection<MusicRingerAccess> getRingers(ServerLevel level);
}

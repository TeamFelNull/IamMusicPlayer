package dev.felnull.imp.api;

import dev.felnull.imp.impl.IamMusicPlayerAPIImpl;

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
}

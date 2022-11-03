package dev.felnull.imp.client.music.loader;

import dev.felnull.imp.client.music.media.IMPMusicMedias;
import dev.felnull.imp.client.neteasecloudmusic.NetEaseCloudMusicManager;
import dev.felnull.imp.music.resource.MusicSource;

public class NetEaseCloudMusicLoader extends LavaMusicLoader {
    @Override
    protected boolean isSupportMedia(MusicSource source) {
        return IMPMusicMedias.NETEASE_CLOUD_MUSIC.getName().equals(source.getLoaderType());
    }

    @Override
    protected String wrappedIdentifier(MusicSource source) throws Exception {
        return NetEaseCloudMusicManager.getInstance().getMp3Url(source.getIdentifier());
    }
}

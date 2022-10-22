package dev.felnull.imp.client.music.loader;

import dev.felnull.imp.client.music.media.IMPMusicMedias;
import dev.felnull.imp.client.util.NetEaseCloudMusicUtils;

public class NetEaseCloudMusicLoader extends LavaMusicLoader {
    @Override
    protected boolean isSupportMedia(String mediaName) {
        return IMPMusicMedias.NETEASE_CLOUD_MUSIC.getName().equals(mediaName);
    }

    @Override
    protected String wrappedIdentifier(String identifier) throws Exception {
        return NetEaseCloudMusicUtils.getMp3Url(identifier);
    }
}

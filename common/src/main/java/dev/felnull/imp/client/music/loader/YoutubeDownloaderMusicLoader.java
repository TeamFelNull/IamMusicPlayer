package dev.felnull.imp.client.music.loader;

import dev.felnull.imp.client.music.media.IMPMusicMedias;
import dev.felnull.imp.client.util.YoutubeUtil;
import dev.felnull.imp.music.resource.MusicSource;

public class YoutubeDownloaderMusicLoader extends LavaMusicLoader {
    @Override
    protected boolean isSupportMedia(MusicSource source) {
        return IMPMusicMedias.YOUTUBE.getName().equals(source.getLoaderType());
    }

    @Override
    protected String wrappedIdentifier(MusicSource source) throws Exception {
        if (source.isLive())
            return null;

        var url = YoutubeUtil.getYoutubeRawURL(source.getIdentifier());
        if (url == null)
            throw new RuntimeException("Failed to get Youtube URL");
        return url;
    }

    @Override
    public int priority() {
        return 1;
    }
}

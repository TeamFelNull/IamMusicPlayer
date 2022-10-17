package dev.felnull.imp.impl.client;

import dev.felnull.imp.api.client.IamMusicPlayerClientAPI;
import dev.felnull.imp.api.client.MusicEngineAccess;
import dev.felnull.imp.client.nmusic.MusicEngine;

public class IamMusicPlayerClientAPIImpl implements IamMusicPlayerClientAPI {
    public static final IamMusicPlayerClientAPIImpl INSTANCE = new IamMusicPlayerClientAPIImpl();

    @Override
    public MusicEngineAccess getMusicEngine() {
        return MusicEngine.getInstance();
    }
}

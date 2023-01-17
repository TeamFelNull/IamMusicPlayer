package dev.felnull.imp.impl;

import dev.felnull.imp.api.IamMusicPlayerAPI;
import dev.felnull.imp.api.music.MusicRingerAccess;
import dev.felnull.imp.server.music.ringer.MusicRingManager;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;

public class IamMusicPlayerAPIImpl implements IamMusicPlayerAPI {
    public static final IamMusicPlayerAPI INSTANCE = new IamMusicPlayerAPIImpl();

    @Unmodifiable
    @Override
    public Collection<MusicRingerAccess> getRingers() {
        return MusicRingManager.getInstance().getMusicRingers().values().stream().flatMap(n -> n.getRingers().values().stream()).map(n -> (MusicRingerAccess) n).toList();
    }

    @Unmodifiable
    @Override
    public Collection<MusicRingerAccess> getRingers(ServerLevel level) {
        var mr = MusicRingManager.getInstance().getMusicRing(level);
        return mr.getRingers().values().stream().map(n -> (MusicRingerAccess) n).toList();
    }
}

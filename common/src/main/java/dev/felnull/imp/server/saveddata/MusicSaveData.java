package dev.felnull.imp.server.saveddata;

import com.google.common.collect.Lists;
import dev.felnull.imp.music.resource.Music;
import dev.felnull.imp.music.resource.MusicPlayList;
import dev.felnull.imp.server.handler.ServerMessageHandler;
import dev.felnull.imp.util.IMPNbtUtil;
import dev.felnull.otyacraftengine.server.level.saveddata.OEBaseSavedData;
import dev.felnull.otyacraftengine.server.util.OESaveDataUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;

import java.util.*;

public class MusicSaveData extends OEBaseSavedData {
    private final Map<UUID, MusicPlayList> playLists = new HashMap<>();
    private final Map<UUID, Music> musics = new HashMap<>();

    public MusicSaveData() {
        setDirty();
    }

    public static MusicSaveData get(MinecraftServer server) {
        return OESaveDataUtils.getSaveData(server, "imp_music_data", MusicSaveData::new);
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        IMPNbtUtil.writeMusicPlayLists(tag, "PlayLists", Lists.newArrayList(playLists.values()));
        IMPNbtUtil.writeMusics(tag, "Musics", Lists.newArrayList(musics.values()));
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        playLists.clear();
        List<MusicPlayList> pls = new ArrayList<>();
        IMPNbtUtil.readMusicPlayLists(tag, "PlayLists", pls);
        pls.forEach(pl -> playLists.put(pl.getUuid(), pl));

        musics.clear();
        List<Music> ms = new ArrayList<>();
        IMPNbtUtil.readMusics(tag, "Musics", ms);
        ms.forEach(m -> musics.put(m.getUuid(), m));
    }

    public Map<UUID, Music> getMusics() {
        return musics;
    }

    public Map<UUID, MusicPlayList> getPlayLists() {
        return playLists;
    }

    @Override
    public void setDirty() {
        super.setDirty();
        ServerMessageHandler.onMusicDataUpdate();
    }
}

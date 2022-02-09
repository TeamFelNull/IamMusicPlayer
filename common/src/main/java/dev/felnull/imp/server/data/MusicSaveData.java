package dev.felnull.imp.server.data;

import com.google.common.collect.Lists;
import dev.felnull.imp.server.handler.ServerMessageHandler;
import dev.felnull.imp.music.resource.Music;
import dev.felnull.imp.music.resource.MusicPlayList;
import dev.felnull.imp.util.IMPNbtUtil;
import dev.felnull.otyacraftengine.data.OEBaseSaveData;
import net.minecraft.nbt.CompoundTag;

import java.util.*;

public class MusicSaveData extends OEBaseSaveData {
    private final Map<UUID, MusicPlayList> playLists = new HashMap<>();
    private final Map<UUID, Music> musics = new HashMap<>();

    public MusicSaveData() {
        setDirty();
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

    @Override
    public void clear() {
        playLists.clear();
        musics.clear();
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

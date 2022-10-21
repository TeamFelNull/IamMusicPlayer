package dev.felnull.imp.music.tracker;

import dev.felnull.imp.music.MusicSpeakerInfo;
import net.minecraft.nbt.CompoundTag;

public class FixedMusicTracker implements MusicTracker {
    private MusicSpeakerInfo speakerInfo = new MusicSpeakerInfo();

    public FixedMusicTracker() {
    }

    public FixedMusicTracker(MusicSpeakerInfo speakerInfo) {
        this.speakerInfo = speakerInfo;
    }

    @Override
    public void save(CompoundTag tag) {
        tag.put("SpeakerInfo", speakerInfo.toTag());
    }

    @Override
    public void load(CompoundTag tag) {
        this.speakerInfo = MusicSpeakerInfo.loadByTag(tag.getCompound("SpeakerInfo"));
    }

    @Override
    public MusicSpeakerInfo getSpeakerInfo() {
        return speakerInfo;
    }
}

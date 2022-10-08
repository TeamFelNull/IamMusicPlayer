package dev.felnull.imp.nmusic.tracker;

import dev.felnull.imp.nmusic.MusicSpeakerInfo;
import dev.felnull.otyacraftengine.server.level.TagSerializable;

/**
 * 音声の位置、情報などを追跡するため
 */
public interface MusicTracker extends TagSerializable {
    MusicSpeakerInfo getSpeakerInfo();
}

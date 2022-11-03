package dev.felnull.imp.music.tracker;

import dev.felnull.imp.music.MusicSpeakerInfo;
import dev.felnull.otyacraftengine.server.data.ITAGSerializable;

/**
 * 音声の位置、情報などを追跡するため
 */
public interface MusicTracker extends ITAGSerializable {
    MusicSpeakerInfo getSpeakerInfo();
}

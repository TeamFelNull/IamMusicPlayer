package dev.felnull.imp.client.integration;

import dev.architectury.platform.Platform;
import dev.felnull.imp.IamMusicPlayer;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SoundPhysicsRemasteredIntegration {
    public static final SoundPhysicsRemasteredIntegration INSTANCE = new SoundPhysicsRemasteredIntegration();
    private final Map<Pair<UUID, UUID>, SoundPhysicsRemasteredAudio> audios = new HashMap<>();


    public String getModId() {
        return "sound_physics_remastered";
    }


    public boolean isConfigEnabled() {
        return IamMusicPlayer.CONFIG.soundPhysicsRemasteredIntegration;
    }

    //https://github.com/henkelmax/sound-physics-remastered/blob/1.19.2/common/src/main/java/com/sonicether/soundphysics/integration/voicechat/AudioChannel.java
    public void onSound(UUID musicPlayerId, UUID speakerId, int source, Vec3 pos) {
        var k = Pair.of(musicPlayerId, speakerId);
        var e = audios.computeIfAbsent(k, n -> new SoundPhysicsRemasteredAudio());
        e.onSound(source, pos);
    }

    public void onDestroy(UUID musicPlayerId, UUID speakerId) {
        audios.remove(Pair.of(musicPlayerId, speakerId));
    }

    public boolean isEnable() {
        return Platform.isModLoaded(getModId()) && isConfigEnabled();
    }
}

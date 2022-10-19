package dev.felnull.imp.client.integration;

import dev.felnull.otyacraftengine.integration.BaseIntegration;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SoundPhysicsRemasteredIntegration extends BaseIntegration {
    public static final SoundPhysicsRemasteredIntegration INSTANCE = new SoundPhysicsRemasteredIntegration();
    private final Map<Pair<UUID, UUID>, SoundPhysicsRemasteredAudio> audios = new HashMap<>();
    private int cont;

    @Override

    public String getModId() {
        return "sound_physics_remastered";
    }

    @Override
    public boolean isConfigEnabled() {
        return true;
    }

    //https://github.com/henkelmax/sound-physics-remastered/blob/1.19.2/common/src/main/java/com/sonicether/soundphysics/integration/voicechat/AudioChannel.java
    public void onSound(UUID musicPlayerId, UUID speakerId, int source, Vec3 pos) {
        var k = Pair.of(musicPlayerId, speakerId);
        var e = audios.computeIfAbsent(k, n -> new SoundPhysicsRemasteredAudio(String.valueOf(cont++)));
        e.onSound(source, pos);
    }

    public void onDestroy(UUID musicPlayerId, UUID speakerId) {
        audios.remove(Pair.of(musicPlayerId, speakerId));
    }
}

package dev.felnull.imp.advancements;

import dev.architectury.registry.level.advancement.CriteriaTriggersRegistry;

public class IMPCriteriaTriggers {
    public static final AddMusicTrigger ADD_MUSIC = new AddMusicTrigger();
    public static final WriteCassetteTapeTrigger WRITE_CASSETTE_TAPE = new WriteCassetteTapeTrigger();
    public static final ListenToMusicTrigger LISTEN_TO_MUSIC = new ListenToMusicTrigger();

    public static void init() {
        CriteriaTriggersRegistry.register(ADD_MUSIC);
        CriteriaTriggersRegistry.register(WRITE_CASSETTE_TAPE);
        CriteriaTriggersRegistry.register(LISTEN_TO_MUSIC);
    }
}

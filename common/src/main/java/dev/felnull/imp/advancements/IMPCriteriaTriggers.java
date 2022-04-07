package dev.felnull.imp.advancements;

import net.minecraft.advancements.CriteriaTriggers;

public class IMPCriteriaTriggers {
    public static final AddMusicTrigger ADD_MUSIC = new AddMusicTrigger();
    public static final WriteCassetteTapeTrigger WRITE_CASSETTE_TAPE = new WriteCassetteTapeTrigger();
    public static final ListenToMusicTrigger LISTEN_TO_MUSIC = new ListenToMusicTrigger();

    public static void init() {
        CriteriaTriggers.register(ADD_MUSIC);
        CriteriaTriggers.register(WRITE_CASSETTE_TAPE);
        CriteriaTriggers.register(LISTEN_TO_MUSIC);
    }
}

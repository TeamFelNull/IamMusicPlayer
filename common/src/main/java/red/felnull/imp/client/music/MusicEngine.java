package red.felnull.imp.client.music;

public class MusicEngine {
    private static final MusicEngine INSTANCE = new MusicEngine();

    public static MusicEngine getInstance() {
        return INSTANCE;
    }

    public void tick(boolean paused) {
        System.out.println("tick");
    }

    public void tickNonPaused() {
        System.out.println("tickNonPaused");
    }
}

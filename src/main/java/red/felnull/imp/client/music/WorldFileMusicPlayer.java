package red.felnull.imp.client.music;

import red.felnull.imp.client.data.MusicDownloader;
import red.felnull.imp.data.WorldMusicFileDataInfo;
import red.felnull.imp.exception.IMPWorldMusicException;

public class WorldFileMusicPlayer implements IMusicPlayer {
    private final String uuid;
    private final WorldMusicFileDataInfo worldMusicInfo;

    public WorldFileMusicPlayer(String uuid) throws InterruptedException, IMPWorldMusicException {
        this.uuid = uuid;
        this.worldMusicInfo = MusicDownloader.instance().getWorldMusicFileDataInfo(uuid);
    }

    @Override
    public void play(long startMiliSecond) {
        System.out.println(getDuration());
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public long getCureentElapsed() {
        return 0;
    }

    @Override
    public long getDuration() {
        return worldMusicInfo.getDuration();
    }

    @Override
    public Object getMusicSource() {
        return uuid;
    }

}

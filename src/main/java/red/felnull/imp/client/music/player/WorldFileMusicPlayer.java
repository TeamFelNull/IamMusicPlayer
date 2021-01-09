package red.felnull.imp.client.music.player;

import javazoom.jl.player.advanced.AdvancedPlayer;
import red.felnull.imp.client.data.MusicDownloader;
import red.felnull.imp.client.music.InputStreamArrayEnumeration;
import red.felnull.imp.data.WorldMusicFileDataInfo;
import red.felnull.imp.exception.IMPWorldMusicException;

import java.io.ByteArrayInputStream;
import java.io.SequenceInputStream;
import java.util.UUID;

public class WorldFileMusicPlayer implements IMusicPlayer {
    private final String uuid;
    private final WorldMusicFileDataInfo worldMusicInfo;
    private final InputStreamArrayEnumeration byteEnumeration;

    private AdvancedPlayer player;
    private long startPlayTime;
    private long startPosition;
    private boolean stop;
    private boolean isReady;
    private long startZure;

    public WorldFileMusicPlayer(String uuid) throws InterruptedException, IMPWorldMusicException {
        this.uuid = uuid;
        this.worldMusicInfo = MusicDownloader.instance().getWorldMusicFileDataInfo(uuid);
        this.byteEnumeration = new InputStreamArrayEnumeration();
    }

    @Override
    public void ready(long startMiliSecond) {
        try {
            if (!this.isReady && player == null) {
                this.startPosition = startMiliSecond;
                this.byteEnumeration.clear();
                MusicReceiveThread mrt = new MusicReceiveThread(startMiliSecond);
                mrt.start();
                while (byteEnumeration.isEmpty()) {
                    Thread.sleep(100);
                    if (stop)
                        return;
                }
                if (stop)
                    return;
                this.player = new AdvancedPlayer(new SequenceInputStream(byteEnumeration));
                if (stop) {
                    this.player = null;
                    this.byteEnumeration.clear();
                    this.player = null;
                    return;
                }
                this.isReady = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            this.player = null;
            this.isReady = false;
        }
    }

    @Override
    public void playMisalignment(long zure) {
        try {
            this.stop = false;
            startZure = zure;
            MusicPlayThread playThread = new MusicPlayThread();
            playThread.start();
        } catch (Exception ex) {
            ex.printStackTrace();
            this.player = null;
            this.stop = true;
            this.byteEnumeration.clear();
        }
    }

    @Override
    public void play() {
        playMisalignment(0);
    }

    @Override
    public void playAndReady(long startMiliSecond) {
        ready(startMiliSecond);
        play();
    }

    @Override
    public void stop() {
        this.stop = true;
        if (player != null) {
            player.close();
        }
    }

    @Override
    public boolean isPlaying() {
        return player != null;
    }

    @Override
    public long getMaxMisalignment() {
        return 3 * 1000;
    }

    @Override
    public long getCureentElapsed() {
        if (player == null)
            return 0;

        return System.currentTimeMillis() - startPlayTime + startPosition;
    }

    @Override
    public long getDuration() {
        return worldMusicInfo.getDuration();
    }

    @Override
    public Object getMusicSource() {
        return uuid;
    }

    @Override
    public void setVolume(float vol) {
        if (player != null)
            player.setVolume(vol);
    }

    @Override
    public float getVolume() {
        if (player != null)
            return player.getVolume();
        return 0;
    }

    private class MusicPlayThread extends Thread {
        @Override
        public void run() {
            try {
                if (!stop) {
                    startPlayTime = System.currentTimeMillis();
                    player.play((int) startZure, Integer.MAX_VALUE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                player = null;
                byteEnumeration.clear();
            }
        }
    }

    private class MusicReceiveThread extends Thread {
        private final long startMilisecond;
        private int rcByteCont;
        private int rcCont;

        public MusicReceiveThread(long startMilisecond) {
            this.startMilisecond = startMilisecond;
        }

        @Override
        public void run() {
            float secParByte = (float) worldMusicInfo.getByteSize() / (float) worldMusicInfo.getDuration();
            int startbyte = (int) (secParByte * (float) startMilisecond);
            rcByteCont += startbyte;
            while (true) {
                try {
                    if (rcByteCont > worldMusicInfo.getByteSize())
                        break;
                    rqAddByte(rcByteCont);
                    if (stop)
                        return;
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void rqAddByte(int begin) {
            try {
                while (byteEnumeration.curentCont() <= rcCont - 3) {
                    sleep(100);
                    if (stop)
                        return;
                }
                UUID byteUuid = MusicDownloader.instance().byteRequest(uuid, begin);
                while (!MusicDownloader.instance().WORLDMUSICBYTE.containsKey(byteUuid)) {
                    sleep(100);
                    if (stop)
                        return;
                }
                byte[] rqdata = MusicDownloader.instance().WORLDMUSICBYTE.get(byteUuid);
                rcCont++;
                rcByteCont += rqdata.length;
                byteEnumeration.add(new ByteArrayInputStream(rqdata));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}

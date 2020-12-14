package red.felnull.imp.client.music;

import javazoom.jl.player.advanced.AdvancedPlayer;
import red.felnull.imp.client.data.MusicDownloader;
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

    public WorldFileMusicPlayer(String uuid) throws InterruptedException, IMPWorldMusicException {
        this.uuid = uuid;
        this.worldMusicInfo = MusicDownloader.instance().getWorldMusicFileDataInfo(uuid);
        this.byteEnumeration = new InputStreamArrayEnumeration();
    }

    @Override
    public void play(long startMiliSecond) {
        try {
            this.startPosition = startMiliSecond;
            this.stop = false;
            this.byteEnumeration.clear();
            MusicReceiveThread mrt = new MusicReceiveThread(startMiliSecond);
            mrt.start();
            while (byteEnumeration.isEmpty()) {
                Thread.sleep(100);
            }
            this.player = new AdvancedPlayer(new SequenceInputStream(byteEnumeration));
            WorldFileMusicPlayer.MusicPlayThread playThread = new WorldFileMusicPlayer.MusicPlayThread();
            playThread.start();
        } catch (Exception ex) {
            ex.printStackTrace();
            this.player = null;
            this.byteEnumeration.clear();
        }
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

    private class MusicPlayThread extends Thread {
        @Override
        public void run() {
            try {
                if (!stop) {
                    startPlayTime = System.currentTimeMillis();
                    player.play();
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

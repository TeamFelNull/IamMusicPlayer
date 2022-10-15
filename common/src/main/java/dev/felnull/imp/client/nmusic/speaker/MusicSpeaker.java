package dev.felnull.imp.client.nmusic.speaker;

import com.google.common.collect.ImmutableList;
import dev.felnull.imp.client.util.MusicUtils;
import dev.felnull.imp.client.util.SoundMath;
import dev.felnull.imp.nmusic.MusicSpeakerFixedInfo;
import dev.felnull.imp.nmusic.tracker.MusicTracker;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import static org.lwjgl.openal.AL11.*;

public class MusicSpeaker {
    private final Queue<MusicBuffer> buffers = new ArrayDeque<>();
    private final int source;
    private final MusicSpeakerFixedInfo fixedInfo;
    private MusicTracker tracker;
    private long startTime = -1;
    private long pauseTime = -1;
    private long stopTime = -1;
    private long totalPauseTime;
    private long scheduledStartTime;
    private long liveTime = System.currentTimeMillis();
    private boolean playStarted;

    public MusicSpeaker(MusicTracker tracker) {
        this.tracker = tracker;
        this.fixedInfo = tracker.getSpeakerInfo().fixedInfo();

        MusicUtils.assertOnMusicTick();

        this.source = alGenSources();
        alSourceInit();
        alSourceUpdate();

        MusicUtils.checkALError();
    }

    public boolean canPlay() {
        return !isDead() && !buffers.isEmpty() && getLiveTime() >= scheduledStartTime;
    }

    public boolean isPlaying() {
        return getPlayState() == AL_PLAYING || getPlayState() == AL_PAUSED;
    }

    public void setScheduledStartTime(long scheduledStartTime) {
        this.scheduledStartTime = getLiveTime() + scheduledStartTime;
    }

    /**
     * 毎Tick呼び出し
     */
    public void tick() {
        MusicUtils.assertOnMusicTick();

        alSourceUpdate();

        MusicUtils.checkALError();
    }

    public MusicTracker getTracker() {
        return tracker;
    }

    /**
     * 音声バッファーデータを挿入
     *
     * @param buffer 音声バッファーデータ
     */
    public void insertBuffer(MusicBuffer buffer) {
        buffer.addSpeaker(this);
        buffers.add(buffer);

        MusicUtils.assertOnMusicTick();

        alSourceQueueBuffers(source, buffer.getBufferId());

        MusicUtils.checkALError();
    }

    /**
     * 再生開始
     *
     * @param delay 遅れ
     */
    public void play(long delay) {
        this.playStarted = true;
        this.startTime = System.currentTimeMillis();

        MusicUtils.assertOnMusicTick();

        float secdelay = (float) delay / 1000f;
        alSourcef(source, AL_SEC_OFFSET, secdelay);
        alSourcePlay(source);

        MusicUtils.checkALError();
    }

    /**
     * 一時停止
     */
    public void pause() {
        this.pauseTime = System.currentTimeMillis();

        if (getPlayState() == AL_PLAYING) alSourcePause(this.source);
    }

    /**
     * 一時停止を解除
     */
    public void resume() {
        this.totalPauseTime += System.currentTimeMillis() - this.pauseTime;
        this.pauseTime = -1;

        if (getPlayState() == AL_PAUSED) alSourcePlay(this.source);
    }

    private int getPlayState() {
        return alGetSourcei(this.source, AL_SOURCE_STATE);
    }

    /**
     * スピーカーを破棄
     *
     * @return 挿入済みバッファーデータ
     * @throws Exception 例外
     */
    public List<MusicBuffer> destroy() throws Exception {
        if (isPlaying())
            stop();

        MusicUtils.assertOnMusicTick();

        alDeleteSources(source);

        MusicUtils.checkALError();

        var r = ImmutableList.copyOf(buffers);
        for (MusicBuffer musicBuffer : buffers) {
            musicBuffer.removeSpeaker(this);
        }
        return r;
    }

    /**
     * 停止、停止後は再開不可
     * {@link #destroy()}を呼び出し後に新しくスピーカーを生成してください
     */
    public void stop() {
        MusicUtils.assertOnMusicTick();

        if (getPlayState() != AL_STOPPED)
            stopTime = System.currentTimeMillis();

        alSourceStop(source);

        MusicUtils.checkALError();
    }

    /**
     * 音楽トラッカーを更新
     *
     * @param tracker 音楽トラッカー
     */
    public void setTracker(MusicTracker tracker) {
        this.tracker = tracker;
        if (!this.fixedInfo.equals(this.tracker.getSpeakerInfo().fixedInfo())) {
            stop();
            return;
        }

        alSourceUpdate();
    }

    private void alSourceInit() {
        MusicUtils.assertOnMusicTick();

        alSourcei(source, AL_LOOPING, AL_FALSE);

        MusicUtils.checkALError();
    }

    private void alSourceUpdate() {
        MusicUtils.assertOnMusicTick();

        var spi = tracker.getSpeakerInfo();

        alSourcei(source, AL_SOURCE_RELATIVE, fixedInfo.relative() ? AL_TRUE : AL_FALSE);
        if (!fixedInfo.relative()) {
            alSource3f(source, AL_POSITION, (float) spi.position().x(), (float) spi.position().y(), (float) spi.position().z());
            alSourcef(source, AL_GAIN, spi.volume());
            linearAttenuation(spi.range());
        } else {
            alSourcef(source, AL_GAIN, (float) SoundMath.calculatePseudoAttenuation(spi.position(), spi.range(), spi.volume()));
        }

        MusicUtils.checkALError();
    }

    private void linearAttenuation(float r) {
        alSourcei(source, AL_DISTANCE_MODEL, 53251);
        alSourcef(source, AL_MAX_DISTANCE, r);
        alSourcef(source, AL_ROLLOFF_FACTOR, 1.0F);
        alSourcef(source, AL_REFERENCE_DISTANCE, 0.0F);
    }

    public List<MusicBuffer> pollBuffers() {
        MusicUtils.assertOnMusicTick();

        //使用済みバッファーデータの数を取得
        int proBuffs = alGetSourcei(source, AL_BUFFERS_PROCESSED);
        List<MusicBuffer> proBuffers = new ArrayList<>();
        int[] bufIds = null;

        for (int i = 0; i < proBuffs; i++) {
            var buf = buffers.poll();
            if (buf != null) {
                proBuffers.add(buf);
                buf.removeSpeaker(this);
                bufIds = ArrayUtils.add(bufIds, buf.getBufferId());
            } else {
                break;
            }
        }

        if (bufIds != null) alSourceUnqueueBuffers(source, bufIds);

        MusicUtils.checkALError();
        return proBuffers;
    }

    /**
     * 再生時間を取得
     *
     * @return 再生時間
     */
    public long getPlayTime() {
        if (this.startTime < 0) return 0;

        long pt = this.totalPauseTime;
        if (this.pauseTime >= 0)
            pt += System.currentTimeMillis() - this.pauseTime;

        long ct = System.currentTimeMillis();
        if (stopTime >= 0) ct = stopTime;

        return ct - this.startTime - pt;
    }

    private long getLiveTime() {
        long pt = this.totalPauseTime;
        if (this.pauseTime >= 0)
            pt += System.currentTimeMillis() - this.pauseTime;

        return System.currentTimeMillis() - liveTime - pt;
    }

    /**
     * 再生が終わり利用不可かどうか
     *
     * @return 利用不可かどうか
     */
    public boolean isDead() {
        return playStarted && getPlayState() == AL_STOPPED;
    }

    /**
     * 固定情報を取得
     *
     * @return 固定情報
     */
    public MusicSpeakerFixedInfo getFixedInfo() {
        return fixedInfo;
    }
}

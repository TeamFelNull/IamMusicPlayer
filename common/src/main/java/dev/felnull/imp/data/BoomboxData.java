package dev.felnull.imp.data;

import dev.felnull.imp.item.CassetteTapeItem;
import dev.felnull.imp.item.IMPItems;
import dev.felnull.imp.music.resource.ImageInfo;
import dev.felnull.imp.music.resource.Music;
import dev.felnull.imp.music.resource.MusicSource;
import dev.felnull.imp.server.music.MusicManager;
import dev.felnull.imp.server.music.ringer.IMusicRinger;
import dev.felnull.imp.util.IMPItemUtil;
import dev.felnull.imp.util.IMPNbtUtil;
import dev.felnull.otyacraftengine.util.OENbtUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class BoomboxData {
    private final Map<UUID, UUID> playerSelectPlaylists = new HashMap<>();
    private final DataAccess access;
    private MonitorType monitorType = MonitorType.OFF;
    private MonitorType lastMonitorType = MonitorType.OFF;
    private boolean handleRaising = true;
    private boolean lidOpen;
    private int handleRaisedProgressOld = getHandleRaisedMax();
    private int handleRaisedProgress = getHandleRaisedMax();
    private int lidOpenProgressOld;
    private int lidOpenProgress;
    private int parabolicAntennaProgressOld;
    private int parabolicAntennaProgress;
    private int antennaProgressOld;
    private int antennaProgress;
    private ItemStack lastCassetteTape = ItemStack.EMPTY;
    private ItemStack oldCassetteTape = ItemStack.EMPTY;
    private boolean changeCassetteTape;
    private boolean noForceChangeCassetteTape;
    private boolean noChangeCassetteTape;
    private boolean playing;
    private int volume = 150;
    private boolean loop;
    private boolean mute;
    private long musicPosition;
    private boolean loadingMusic;
    private String radioUrl = "";
    private MusicSource radioSource = MusicSource.EMPTY;
    private ImageInfo radioImage = ImageInfo.EMPTY;
    private String radioName = "";
    private String radioAuthor = "";
    private Music selectedMusic;
    private ContinuousType continuousType = ContinuousType.NONE;
    private boolean radioStartFlg;

    public BoomboxData(@NotNull BoomboxData.DataAccess access) {
        this.access = access;
    }

    public void tick(Level level) {
        this.handleRaisedProgressOld = this.handleRaisedProgress;
        this.lidOpenProgressOld = this.lidOpenProgress;
        this.parabolicAntennaProgressOld = this.parabolicAntennaProgress;
        this.antennaProgressOld = this.antennaProgress;

        if (isUseAntenna() && isRadioStream())
            this.antennaProgress = Mth.clamp(this.antennaProgress + 1, 0, 30);
        else
            this.antennaProgress = Mth.clamp(this.antennaProgress - 1, 0, 30);

        if (this.handleRaising) {
            if (this.handleRaisedProgress < getHandleRaisedMax())
                this.handleRaisedProgress++;
        } else {
            if (this.handleRaisedProgress > 0)
                this.handleRaisedProgress--;
        }

        if (this.lidOpen) {
            if (this.lidOpenProgress < this.getLidOpenProgressMax())
                this.lidOpenProgress++;
        } else {
            if (this.lidOpenProgress > 0)
                this.lidOpenProgress--;
        }

        if (isPower() && isUseAntenna() && isAntennaExist() && getAntenna().is(IMPItems.PARABOLIC_ANTENNA))
            this.parabolicAntennaProgress += 2;

        if (!level.isClientSide()) {
            if (getRinger() != null)
                loadingMusic = getRinger().isRingerWait();

            if (isPower() && monitorType == MonitorType.OFF)
                monitorType = MonitorType.PLAYBACK;

            if (!isPower() && monitorType != MonitorType.OFF)
                monitorType = MonitorType.OFF;

            if (monitorType != lastMonitorType) {
                lastMonitorType = monitorType;
                setMusicPosition(0);
                setPlaying(false);
            }

            if (!canPlay()) {
                setMusicPosition(0);
                setPlaying(false);
            }

            if (!isRadioRemote()) {
                playerSelectPlaylists.clear();
                setSelectedMusic(null);
                setContinuousType(ContinuousType.NONE);
            }

            if (radioStartFlg) {
                setRadioStartFlg(false);
                setPlaying(true);
            }

            if (monitorType == MonitorType.REMOTE_PLAYBACK && getSelectedMusic() == null)
                monitorType = MonitorType.REMOTE_PLAYBACK_SELECT;

            if (monitorType == MonitorType.RADIO && getRadioSource().isEmpty())
                monitorType = MonitorType.RADIO_SELECT;

            if ((isRadio() && !isAntennaExist()) || (isRadioRemote() && !IMPItemUtil.isRemotePlayBackAntenna(getAntenna())) || (isRadioStream() && IMPItemUtil.isRemotePlayBackAntenna(getAntenna()))) {
                monitorType = MonitorType.PLAYBACK;
            }

            if (!isRadioStream()) {
                setRadioImage(ImageInfo.EMPTY);
                setRadioSource(MusicSource.EMPTY);
                setRadioAuthor("");
                setRadioName("");
                setRadioUrl("");
            }

            if (monitorType != MonitorType.RADIO_SELECT)
                setRadioUrl("");

            if (!ItemStack.matches(this.lastCassetteTape, this.getCassetteTape()))
                changeCassetteTape(this.lastCassetteTape);
            this.lastCassetteTape = this.getCassetteTape().copy();

            if (this.changeCassetteTape) {
                if (!isLidOpen())
                    startLidOpen(true, level);

                if (getLidOpenProgress() >= getLidOpenProgressMax()) {
                    changeCassetteTape = false;
                    startLidOpen(false, level);
                }
            }
        }
    }

    public boolean canPlay() {
        boolean canPlayFlg = monitorType == MonitorType.PLAYBACK && isMusicCassetteTapeExist();
        boolean canPlayFlg2 = monitorType == MonitorType.RADIO && !getRadioSource().isEmpty();
        boolean canPlayFlg3 = monitorType == MonitorType.REMOTE_PLAYBACK;
        return canPlayFlg || canPlayFlg2 || canPlayFlg3;
    }

    public CompoundTag onInstruction(ServerPlayer player, String name, int num, CompoundTag data) {
        if ("buttons_press".equals(name)) {
            ButtonType type = ButtonType.getByName(data.getString("Type"));
            switch (type) {
                case POWER -> setPower(!isPower());
                case LOOP -> setLoop(!isLoop());
                case VOL_DOWN -> {
                    if (isPower())
                        setVolume(Math.max(volume - 10, 0));
                }
                case VOL_UP -> {
                    if (isPower())
                        setVolume(Math.min(volume + 10, 300));
                    setMute(false);
                }
                case VOL_MUTE -> setMute(!isMute());
                case VOL_MAX -> {
                    if (isPower())
                        setVolume(300);
                    setMute(false);
                }
                case RADIO -> {
                    if (isRadio()) {
                        setMonitorType(MonitorType.PLAYBACK);
                    } else {
                        setPower(true);
                        setRadioMode();
                    }
                }
                case START -> {
                    if (isMusicCassetteTapeExist() || isRadio()) {
                        setPower(true);
                        setPlaying(true);
                    }
                }
                case STOP -> {
                    if (isPower()) {
                        setPlaying(false);
                        setMusicPosition(0);
                    }
                }
                case PAUSE -> {
                    if (isPower()) {
                        setPlaying(false);
                        if (isRadioStream())
                            setMusicPosition(0);
                    }
                }
            }
            return null;
        } else if ("set_volume".equals(name)) {
            if (isPower())
                setVolume(data.getInt("volume"));
            return null;
        } else if ("set_playing".equals(name)) {
            if (isPower()) {
                boolean pl = data.getBoolean("playing");
                setPlaying(pl);
                if (!pl)
                    setMusicPosition(0);
            }
            return null;
        } else if ("set_pause".equals(name)) {
            if (isPower())
                setPlaying(false);
            return null;
        } else if ("set_loop".equals(name)) {
            if (isPower())
                setLoop(data.getBoolean("loop"));
            return null;
        } else if ("restat_and_set_position".equals(name)) {
            if (isPower())
                setMusicPositionAndRestart(data.getLong("position"));
            return null;
        } else if ("set_radio_url".equals(name)) {
            if (isPower())
                setRadioUrl(data.getString("url"));
            return null;
        } else if ("set_monitor".equals(name)) {
            if (isPower()) {
                var m = MonitorType.getByName(data.getString("name"));
                setMonitorType(m);
                if ((m == MonitorType.RADIO && getRadioSource() != null && !getRadioSource().isEmpty()) || (m == MonitorType.REMOTE_PLAYBACK && getSelectedMusic() != null && !getSelectedMusic().getSource().isEmpty()))
                    setRadioStartFlg(true);
            }
            return null;
        } else if ("set_radio_source".equals(name)) {
            if (isPower())
                setRadioSource(OENbtUtil.readSerializable(data, "source", new MusicSource()));
            return null;
        } else if ("set_radio_image".equals(name)) {
            if (isPower())
                setRadioImage(OENbtUtil.readSerializable(data, "image", new ImageInfo()));
            return null;
        } else if ("set_radio_name".equals(name)) {
            if (isPower())
                setRadioName(data.getString("name"));
            return null;
        } else if ("set_radio_author".equals(name)) {
            if (isPower())
                setRadioAuthor(data.getString("author"));
            return null;
        } else if ("set_selected_play_list".equals(name)) {
            if (isPower() && isRadioRemote()) {
                if (data.contains("pl")) {
                    var uuid = data.getUUID("pl");
                    var pl = MusicManager.getInstance().getSaveData().getPlayLists().get(uuid);
                    if (pl != null && pl.getAuthority().getAuthorityType(player.getGameProfile().getId()).isMoreReadOnly())
                        setSelectedPlayList(player, uuid);
                } else {
                    setSelectedPlayList(player, null);
                }
            }
            return null;
        } else if ("set_selected_music".equals(name)) {
            if (isPower() && isRadioRemote()) {
                if (data.contains("m")) {
                    var uuid = data.getUUID("m");
                    var m = MusicManager.getInstance().getSaveData().getMusics().get(uuid);
                    if (m != null) {
                        var pl = MusicManager.getInstance().getPlaylistByMusic(m.getUuid());
                        if (pl != null && pl.getAuthority().getAuthorityType(player.getGameProfile().getId()).isMoreReadOnly()) {
                            setSelectedMusic(m);
                        }
                    }
                } else {
                    setSelectedMusic(null);
                }
            }
            return null;
        } else if ("set_continuous_type".equals(name)) {
            if (isPower() && isRadioRemote()) {
                setContinuousType(ContinuousType.getByName(data.getString("type")));
            }
            return null;
        }
        return null;
    }

    public CompoundTag save(CompoundTag tag, boolean absolutely, boolean sync) {
        tag.putString("MonitorType", this.monitorType.getName());
        tag.putBoolean("HandleRaising", this.handleRaising);
        tag.putBoolean("LidOpen", this.lidOpen);
        tag.putBoolean("Playing", playing);
        tag.putInt("Volume", this.volume);
        tag.putBoolean("Loop", this.loop);
        tag.putBoolean("Mute", this.mute);
        tag.putLong("RingerPosition", this.musicPosition);
        tag.putString("RadioUrl", this.radioUrl);
        OENbtUtil.writeSerializable(tag, "RadioSource", this.radioSource);
        OENbtUtil.writeSerializable(tag, "RadioImage", this.radioImage);
        tag.putString("RadioName", this.radioName);
        tag.putString("RadioAuthor", this.radioAuthor);
        IMPNbtUtil.writeUUIDMap(tag, "PlayerSelectPlaylists", playerSelectPlaylists);
        if (this.selectedMusic != null)
            OENbtUtil.writeSerializable(tag, "SelectedMusic", this.selectedMusic);
        tag.putString("ContinuousType", this.continuousType.getName());


        if (absolutely) {
            tag.putInt("HandleRaisedProgressOld", this.handleRaisedProgressOld);
            tag.putInt("HandleRaisedProgress", this.handleRaisedProgress);
            tag.putInt("LidOpenProgressOld", this.lidOpenProgressOld);
            tag.putInt("LidOpenProgress", this.lidOpenProgress);
            tag.putInt("ParabolicAntennaProgressOld", this.parabolicAntennaProgressOld);
            tag.putInt("ParabolicAntennaProgress", this.parabolicAntennaProgress);
            tag.putInt("AntennaProgressOld", this.antennaProgressOld);
            tag.putInt("AntennaProgress", this.antennaProgress);
            tag.put("LastCassetteTape", this.lastCassetteTape.save(new CompoundTag()));
            tag.putBoolean("NoForceChangeCassetteTape", this.noForceChangeCassetteTape);
            tag.putBoolean("NoChangeCassetteTape", this.noChangeCassetteTape);
            tag.putString("LastMonitorType", this.lastMonitorType.getName());
        }

        if (absolutely || sync) {
            tag.putBoolean("ChangeCassetteTape", this.changeCassetteTape);
            tag.put("OldCassetteTape", this.oldCassetteTape.save(new CompoundTag()));
            tag.putBoolean("LoadingMusic", this.loadingMusic);
            tag.putBoolean("RadioStartFlg", this.radioStartFlg);
        }

        return tag;
    }

    public void load(CompoundTag tag, boolean absolutely, boolean sync) {
        this.monitorType = MonitorType.getByName(tag.getString("MonitorType"));
        this.handleRaising = tag.getBoolean("HandleRaising");
        this.lidOpen = tag.getBoolean("LidOpen");
        this.playing = tag.getBoolean("Playing");
        if (tag.contains("Volume"))
            this.volume = tag.getInt("Volume");
        this.loop = tag.getBoolean("Loop");
        this.mute = tag.getBoolean("Mute");
        this.musicPosition = tag.getLong("RingerPosition");
        this.radioUrl = tag.getString("RadioUrl");
        this.radioSource = OENbtUtil.readSerializable(tag, "RadioSource", new MusicSource());
        this.radioImage = OENbtUtil.readSerializable(tag, "RadioImage", new ImageInfo());
        this.radioName = tag.getString("RadioName");
        this.radioAuthor = tag.getString("RadioAuthor");
        IMPNbtUtil.readUUIDMap(tag, "PlayerSelectPlaylists", playerSelectPlaylists);
        this.continuousType = ContinuousType.getByName(tag.getString("ContinuousType"));

        if (tag.contains("SelectedMusic"))
            this.selectedMusic = OENbtUtil.readSerializable(tag, "SelectedMusic", new Music());

        if (absolutely) {
            this.handleRaisedProgressOld = tag.getInt("HandleRaisedProgressOld");
            this.handleRaisedProgress = tag.getInt("HandleRaisedProgress");
            this.lidOpenProgressOld = tag.getInt("LidOpenProgressOld");
            this.lidOpenProgress = tag.getInt("LidOpenProgress");
            this.parabolicAntennaProgressOld = tag.getInt("ParabolicAntennaProgressOld");
            this.parabolicAntennaProgress = tag.getInt("ParabolicAntennaProgress");
            this.antennaProgressOld = tag.getInt("AntennaProgressOld");
            this.antennaProgress = tag.getInt("AntennaProgress");
            this.lastCassetteTape = ItemStack.of(tag.getCompound("LastCassetteTape"));
            this.noForceChangeCassetteTape = tag.getBoolean("NoForceChangeCassetteTape");
            this.noChangeCassetteTape = tag.getBoolean("NoChangeCassetteTape");
            this.lastMonitorType = MonitorType.getByName(tag.getString("LastMonitorType"));
        }

        if (absolutely || sync) {
            this.changeCassetteTape = tag.getBoolean("ChangeCassetteTape");
            this.oldCassetteTape = ItemStack.of(tag.getCompound("OldCassetteTape"));
            this.loadingMusic = tag.getBoolean("LoadingMusic");
            this.radioStartFlg = tag.getBoolean("RadioStartFlg");
        }

        if (!sync) {
            if (this.handleRaising)
                this.handleRaisedProgress = getHandleRaisedMax();
            if (this.lidOpen)
                this.lidOpenProgress = getLidOpenProgressMax();
        }

        if (!absolutely && !sync) {
            this.noForceChangeCassetteTape = true;
        }
    }

    public void setRadioSource(MusicSource radioSource) {
        this.radioSource = radioSource;
        update();
    }

    public void setRadioImage(ImageInfo radioImage) {
        this.radioImage = radioImage;
        update();
    }

    public MusicSource getRadioSource() {
        return radioSource;
    }

    public ImageInfo getRadioImage() {
        return radioImage;
    }

    public void setRadioUrl(String radioUrl) {
        this.radioUrl = radioUrl;
        update();
    }

    public void setRadioName(String radioName) {
        this.radioName = radioName;
        update();
    }

    public void setRadioAuthor(String radioAuthor) {
        this.radioAuthor = radioAuthor;
        update();
    }

    public String getRadioName() {
        return radioName;
    }

    public String getRadioAuthor() {
        return radioAuthor;
    }

    public String getRadioUrl() {
        return radioUrl;
    }

    public boolean isLoadingMusic() {
        return loadingMusic;
    }

    public long getMusicPosition() {
        return musicPosition;
    }

    public void setMusicPosition(long position) {
        this.musicPosition = position;
        if (isMusicCassetteTapeExist() && !isRadioRemote()) {
            var m = getMusicSource();
            if (m != null) {
                var nc = CassetteTapeItem.setTapePercentage(getCassetteTape().copy(), (float) position / (float) m.getDuration());
                if (!ItemStack.matches(nc, getCassetteTape())) {
                    setNoChangeCassetteTape(true);
                    setCassetteTape(nc);
                }
            }
        }
        update();
    }

    public void setCassetteTape(ItemStack stack) {
        access.setCassetteTape(stack);
    }

    public MusicSource getMusicSource() {
        if (isMusicCassetteTapeExist()) {
            var m = CassetteTapeItem.getMusic(getCassetteTape());
            if (m != null)
                return m.getSource();
        }
        return null;
    }

    public void setMusicPositionAndRestart(long position) {
        setMusicPosition(position);
        if (getRinger() != null) {
            getRinger().ringerRestart();
            update();
        }
    }

    public float getRawVolume() {
        return (float) getVolume() / 300f;
    }

    public void setRadioMode() {
        if (isAntennaExist()) {
            if (IMPItemUtil.isRemotePlayBackAntenna(getAntenna())) {
                setMonitorType(MonitorType.REMOTE_PLAYBACK_SELECT);
            } else {
                setMonitorType(MonitorType.RADIO_SELECT);
            }
        }
        update();
    }

    public boolean isRadio() {
        return isRadioStream() || isRadioRemote();
    }

    public boolean isRadioStream() {
        return monitorType == MonitorType.RADIO_SELECT || monitorType == MonitorType.RADIO;
    }

    public boolean isRadioRemote() {
        return monitorType == MonitorType.REMOTE_PLAYBACK || monitorType == MonitorType.REMOTE_PLAYBACK_SELECT;
    }

    public void changeCassetteTape(ItemStack old) {
        if (noForceChangeCassetteTape) {
            noForceChangeCassetteTape = false;
            return;
        }

        if (noChangeCassetteTape && CassetteTapeItem.isSameCassetteTape(old, getCassetteTape())) {
            noChangeCassetteTape = false;
            return;
        }

        this.oldCassetteTape = old;
        if (!isRadio()) {
            setMusicPosition(0);
            setPlaying(false);
        }
        if (!(getCassetteTape().isEmpty() && isLidOpen()))
            this.changeCassetteTape = true;
    }

    public void setPower(boolean power) {
        access.setPower(power);
    }

    public boolean isChangeCassetteTape() {
        return changeCassetteTape;
    }

    private boolean isMusicCassetteTapeExist() {
        return isCassetteTapeExist() && CassetteTapeItem.getMusic(getCassetteTape()) != null;
    }

    private boolean isCassetteTapeExist() {
        return IMPItemUtil.isCassetteTape(getCassetteTape());
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setNoChangeCassetteTape(boolean noChangeCassetteTape) {
        this.noChangeCassetteTape = noChangeCassetteTape;
        update();
    }

    public void setPlaying(boolean playing) {
        if (canPlay() || !playing) {
            this.playing = playing;
            update();
        }
    }

    public boolean isNoChangeCassetteTape() {
        return noChangeCassetteTape;
    }

    public boolean isNoForceChangeCassetteTape() {
        return noForceChangeCassetteTape;
    }

    public ItemStack getOldCassetteTape() {
        return oldCassetteTape;
    }

    public ItemStack getLastCassetteTape() {
        return lastCassetteTape;
    }

    public void setMonitorType(MonitorType monitorType) {
        this.monitorType = monitorType;
        update();
    }

    public MonitorType getMonitorType() {
        return monitorType;
    }

    public boolean isUseAntenna() {
        return isRadio();
    }

    public boolean isAntennaExist() {
        return IMPItemUtil.isAntenna(getAntenna());
    }

    @Nullable
    public IMusicRinger getRinger() {
        return access.getRinger();
    }

    public boolean cycleLidOpen(Level level) {
        boolean flg = lidOpenProgress >= getLidOpenProgressMax();
        boolean flg2 = lidOpenProgress <= 0;
        if (!flg && !flg2)
            return false;
        if (flg) {
            startLidOpen(false, level);
        }
        if (flg2) {
            startLidOpen(true, level);
        }
        return true;
    }

    public void setContinuousType(ContinuousType continuousType) {
        this.continuousType = continuousType;
        update();
    }

    public ContinuousType getContinuousType() {
        return continuousType;
    }

    public void setSelectedMusic(Music selectedMusic) {
        this.selectedMusic = selectedMusic;
        update();
    }

    public Music getSelectedMusic() {
        return selectedMusic;
    }

    @Nullable
    public Music getCassetteTapeMusic() {
        if (IMPItemUtil.isCassetteTape(getCassetteTape()))
            return CassetteTapeItem.getMusic(getCassetteTape());
        return null;
    }

    public void startLidOpen(boolean open, Level level) {
        setLidOpen(open);
        var pos = getPosition();
        level.playSound(null, pos.x(), pos.y(), pos.z(), isLidOpen() ? SoundEvents.WOODEN_DOOR_OPEN : SoundEvents.WOODEN_DOOR_CLOSE, SoundSource.BLOCKS, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
    }

    public boolean isLidOpen() {
        return lidOpen;
    }

    public void setLidOpen(boolean lidOpen) {
        this.lidOpen = lidOpen;
        update();
    }

    public void setSelectedPlayList(@NotNull ServerPlayer player, UUID selectedPlayList) {
        if (selectedPlayList != null) {
            playerSelectPlaylists.put(player.getGameProfile().getId(), selectedPlayList);
        } else {
            playerSelectPlaylists.remove(player.getGameProfile().getId());
        }
        update();
    }

    @Nullable
    public UUID getSelectedPlayList(@NotNull Player player) {
        return playerSelectPlaylists.get(player.getGameProfile().getId());
    }

    public ItemStack getCassetteTape() {
        return access.getCassetteTape();
    }

    public ItemStack getAntenna() {
        return access.getAntenna();
    }

    public int getLidOpenProgressMax() {
        return 10;
    }

    public int getHandleRaisedProgress() {
        return handleRaisedProgress;
    }

    public float getHandleRaisedProgress(float partialTicks) {
        return Mth.lerp(partialTicks, handleRaisedProgressOld, handleRaisedProgress);
    }

    public int getParabolicAntennaProgress() {
        return parabolicAntennaProgress;
    }

    public float getParabolicAntennaProgress(float partialTicks) {
        return Mth.lerp(partialTicks, parabolicAntennaProgressOld, parabolicAntennaProgress);
    }

    public int getLidOpenProgress() {
        return lidOpenProgress;
    }

    public float getLidOpenProgress(float partialTicks) {
        return Mth.lerp(partialTicks, lidOpenProgressOld, lidOpenProgress);
    }

    public boolean isLoop() {
        return loop;
    }

    public boolean isMute() {
        return mute;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
        update();
    }

    public void setMute(boolean mute) {
        this.mute = mute;
        update();
    }

    public void setVolume(int volume) {
        this.volume = volume;
        update();
    }

    public int getVolume() {
        return volume;
    }

    public int getAntennaProgress() {
        return antennaProgress;
    }

    public float getAntennaProgress(float partialTicks) {
        return Mth.lerp(partialTicks, antennaProgressOld, antennaProgress);
    }

    public boolean isHandleRaising() {
        return handleRaising;
    }

    public int getHandleRaisedMax() {
        return 10;
    }

    public boolean cycleRaisedHandle() {
        boolean flg = getHandleRaisedProgress() >= getHandleRaisedMax();
        boolean flg2 = getHandleRaisedProgress() <= 0;
        if (!flg && !flg2)
            return false;
        if (flg) {
            setHandleRaising(false);
        }
        if (flg2) {
            setHandleRaising(true);
        }
        return true;
    }

    public void setHandleRaising(boolean handleRaising) {
        this.handleRaising = handleRaising;
        update();
    }

    public void setHandleRaisedProgressOld(int handleRaisedProgressOld) {
        this.handleRaisedProgressOld = handleRaisedProgressOld;
    }

    public int getHandleRaisedProgressOld() {
        return handleRaisedProgressOld;
    }

    public void setHandleRaisedProgress(int handleRaisedProgress) {
        this.handleRaisedProgress = handleRaisedProgress;
    }

    public void setAntennaProgressOld(int antennaProgressOld) {
        this.antennaProgressOld = antennaProgressOld;
    }

    public boolean isPower() {
        return access.isPower();
    }

    public Vec3 getPosition() {
        return access.getPosition();
    }

    public void update() {
        access.dataUpdate(this);
    }

    public static enum MonitorType {
        OFF("off"),
        PLAYBACK("playback"),
        REMOTE_PLAYBACK("remote_playback"),
        REMOTE_PLAYBACK_SELECT("remote_playback_select"),
        RADIO("radio"),
        RADIO_SELECT("radio_select");
        private final String name;

        private MonitorType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static MonitorType getByName(String name) {
            for (MonitorType value : values()) {
                if (value.getName().equals(name))
                    return value;
            }
            return MonitorType.OFF;
        }
    }

    public void setRadioStartFlg(boolean radioStartFlg) {
        this.radioStartFlg = radioStartFlg;
        update();
    }

    public static interface DataAccess {
        ItemStack getCassetteTape();

        ItemStack getAntenna();

        boolean isPower();

        void setPower(boolean power);

        IMusicRinger getRinger();

        Vec3 getPosition();

        void setCassetteTape(ItemStack stack);

        void dataUpdate(BoomboxData data);
    }


    public Buttons getButtons() {
        return new Buttons(isRadio(), isPlaying(), !isPlaying() && getMusicPosition() > 0, isLoop(), isMute(), !isMute() && volume >= 300);
    }

    public static record Buttons(boolean radio, boolean start, boolean pause, boolean loop,
                                 boolean volMute, boolean volMax) {
        public static final Buttons EMPTY = new Buttons(false, false, false, false, false, false);
    }

    public static enum ButtonType {
        NONE("none", n -> false),
        POWER("power", n -> false),
        RADIO("radio", n -> n.radio()),
        START("start", n -> n.start()),
        PAUSE("pause", n -> n.pause()),
        STOP("stop", n -> false),
        LOOP("loop", n -> n.loop()),
        VOL_DOWN("volDown", n -> false),
        VOL_UP("volUp", n -> false),
        VOL_MUTE("volMute", n -> n.volMute()),
        VOL_MAX("volMax", n -> n.volMax());
        private final String name;
        private final Component component;
        private final Function<Buttons, Boolean> getter;

        private ButtonType(String name, Function<Buttons, Boolean> getter) {
            this.name = name;
            this.component = new TranslatableComponent("imp.button.boombox." + name);
            this.getter = getter;
        }

        public String getName() {
            return name;
        }

        public Component getComponent() {
            return component;
        }

        public boolean getState(Buttons buttons) {
            return getter.apply(buttons);
        }

        public static ButtonType getByName(String name) {
            for (ButtonType value : values()) {
                if (value.getName().equals(name))
                    return value;
            }
            return NONE;
        }
    }

    public static enum ContinuousType {
        NONE("none"),
        ORDER("order"),
        RANDOM("random");
        private final String name;
        private final Component component;

        private ContinuousType(String name) {
            this.name = name;
            this.component = new TranslatableComponent("imp.text.continuous." + name);
        }

        public Component getComponent() {
            return component;
        }

        public String getName() {
            return name;
        }

        public static ContinuousType getByName(String name) {
            for (ContinuousType value : values()) {
                if (value.getName().equals(name))
                    return value;
            }
            return NONE;
        }
    }
}

package dev.felnull.imp.blockentity;

import dev.felnull.imp.advancements.IMPCriteriaTriggers;
import dev.felnull.imp.block.IMPBlocks;
import dev.felnull.imp.inventory.CassetteDeckMenu;
import dev.felnull.imp.item.CassetteTapeItem;
import dev.felnull.imp.music.resource.Music;
import dev.felnull.imp.music.resource.MusicSource;
import dev.felnull.imp.music.tracker.IMPMusicTrackers;
import dev.felnull.imp.music.tracker.MusicTrackerEntry;
import dev.felnull.imp.server.music.MusicManager;
import dev.felnull.imp.server.music.ringer.IMusicRinger;
import dev.felnull.imp.util.IMPItemUtil;
import dev.felnull.imp.util.IMPNbtUtil;
import dev.felnull.otyacraftengine.util.OENbtUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CassetteDeckBlockEntity extends IMPBaseEntityBlockEntity implements IMusicRinger {
    private NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
    private final Map<UUID, UUID> playerSelectPlaylists = new HashMap<>();
    private UUID myPlayerSelectPlaylist;
    private Music music = null;
    private MonitorType monitor = MonitorType.OFF;
    private ItemStack oldCassetteTape = ItemStack.EMPTY;
    private final UUID ringerUUID = UUID.randomUUID();
    private boolean changeCassetteTape;
    private boolean lidOpen;
    private int lidOpenProgressOld;
    private int lidOpenProgress;
    private int cassetteWriteProgress;
    private int volume = 150;
    private boolean mute;
    private boolean playing;
    private long position;
    private boolean loop;
    private boolean loadingMusic;

    public CassetteDeckBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IMPBlockEntities.CASSETTE_DECK.get(), blockPos, blockState);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, CassetteDeckBlockEntity blockEntity) {

        blockEntity.lidOpenProgressOld = blockEntity.lidOpenProgress;

        if (blockEntity.lidOpen) {
            if (blockEntity.lidOpenProgress < blockEntity.getLidOpenProgressAll())
                blockEntity.lidOpenProgress++;
        } else {
            if (blockEntity.lidOpenProgress > 0)
                blockEntity.lidOpenProgress--;
        }

        if (!level.isClientSide()) {

            if (blockEntity.isPowered()) {
                if (blockEntity.monitor == MonitorType.OFF)
                    blockEntity.setMonitor(MonitorType.MENU);
            } else {
                if (blockEntity.monitor != MonitorType.OFF)
                    blockEntity.setMonitor(MonitorType.OFF);
            }

            if ((blockEntity.getMusic() == null || blockEntity.getCassetteTape().isEmpty() || IMPItemUtil.isAntenna(blockEntity.getCassetteTape())) && blockEntity.monitor == MonitorType.WRITE_EXECUTION)
                blockEntity.setMonitor(MonitorType.WRITE);

            if (blockEntity.monitor == MonitorType.WRITE_EXECUTION) {
                if (blockEntity.getCassetteWriteProgress() >= blockEntity.getCassetteWriteProgressAll()) {
                    blockEntity.writeCassetteTape();
                    blockEntity.setMonitor(MonitorType.WRITE);
                    blockEntity.setCassetteWriteProgress(0);
                } else {
                    blockEntity.setCassetteWriteProgress(blockEntity.getCassetteWriteProgress() + 1);
                }
            } else {
                if (blockEntity.getCassetteWriteProgress() != 0)
                    blockEntity.setCassetteWriteProgress(0);
            }

            if (blockEntity.monitor != MonitorType.PLAYBACK || !blockEntity.hasMusicCassetteTape()) {
                blockEntity.setRingerPosition(0);
                if (blockEntity.isPlaying())
                    blockEntity.setPlaying(false);
            }

            if (blockEntity.changeCassetteTape) {
                if (!blockEntity.isLidOpen())
                    blockEntity.startLidOpen(true);

                if (blockEntity.lidOpenProgress >= blockEntity.getLidOpenProgressAll()) {
                    blockEntity.changeCassetteTape = false;
                    blockEntity.startLidOpen(false);
                }
            }
            blockEntity.loadingMusic = blockEntity.isRingerWait();
            blockEntity.ringerTick();
            blockEntity.sync();
        }
    }

    private boolean canWriteCassetteTape() {
        return getMusic() != null && !getCassetteTape().isEmpty() && IMPItemUtil.isCassetteTape(getCassetteTape());
    }

    private void writeCassetteTape() {
        if (canWriteCassetteTape()) {
            CassetteTapeItem.setMusic(getCassetteTape(), getMusic());
            setChanged();
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.lidOpen = tag.getBoolean("LidOpen");
        if (this.lidOpen)
            lidOpenProgress = getLidOpenProgressAll();
        this.monitor = MonitorType.getByName(tag.getString("Monitor"));
        IMPNbtUtil.readUUIDMap(tag, "PlayerSelectPlaylists", playerSelectPlaylists);
        if (tag.contains("Music"))
            this.music = OENbtUtil.readSerializable(tag, "Music", new Music());
        this.cassetteWriteProgress = tag.getInt("CassetteWriteProgress");
        this.volume = tag.getInt("Volume");
        this.mute = tag.getBoolean("Mute");
        this.playing = tag.getBoolean("Playing");
        this.position = tag.getLong("Position");
        this.loop = tag.getBoolean("Loop");
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putBoolean("LidOpen", this.lidOpen);
        tag.putString("Monitor", monitor.getName());
        IMPNbtUtil.writeUUIDMap(tag, "PlayerSelectPlaylists", playerSelectPlaylists);
        if (this.music != null)
            OENbtUtil.writeSerializable(tag, "Music", music);
        tag.putInt("CassetteWriteProgress", this.cassetteWriteProgress);
        tag.putInt("Volume", this.volume);
        tag.putBoolean("Mute", this.mute);
        tag.putBoolean("Playing", this.playing);
        tag.putLong("Position", this.position);
        tag.putBoolean("Loop", this.loop);
        tag.putBoolean("LoadingMusic", this.loadingMusic);
    }

    @Override
    public CompoundTag getSyncData(ServerPlayer player, CompoundTag tag) {
        tag.putBoolean("LidOpen", this.lidOpen);
        tag.put("OldCassetteTape", this.oldCassetteTape.save(new CompoundTag()));
        tag.putBoolean("ChangeCassetteTape", this.changeCassetteTape);
        tag.putString("Monitor", monitor.getName());
        if (playerSelectPlaylists.containsKey(player.getGameProfile().getId()))
            tag.putUUID("PlayerSelectPlaylist", playerSelectPlaylists.get(player.getGameProfile().getId()));
        if (this.music != null)
            OENbtUtil.writeSerializable(tag, "Music", music);
        tag.putInt("CassetteWriteProgress", this.cassetteWriteProgress);
        tag.putInt("Volume", this.volume);
        tag.putBoolean("Mute", this.mute);
        tag.putBoolean("Playing", this.playing);
        tag.putLong("Position", this.position);
        tag.putBoolean("Loop", this.loop);
        tag.putBoolean("LoadingMusic", this.loadingMusic);
        return super.getSyncData(player, tag);
    }

    @Override
    public void setItem(int i, ItemStack stack) {
        if (i == 0)
            onCassetteTapeChange(stack, getCassetteTape());
        setItemNoChange(i, stack);
    }

    public void setItemNoChange(int i, ItemStack stack) {
        super.setItem(i, stack);
    }

    public boolean isLoadingMusic() {
        return loadingMusic;
    }

    @Override
    public void onSync(CompoundTag tag) {
        super.onSync(tag);
        this.lidOpen = tag.getBoolean("LidOpen");
        this.oldCassetteTape = ItemStack.of(tag.getCompound("OldCassetteTape"));
        this.changeCassetteTape = tag.getBoolean("ChangeCassetteTape");
        this.monitor = MonitorType.getByName(tag.getString("Monitor"));
        if (tag.contains("PlayerSelectPlaylist"))
            this.myPlayerSelectPlaylist = tag.getUUID("PlayerSelectPlaylist");
        if (tag.contains("Music"))
            this.music = OENbtUtil.readSerializable(tag, "Music", new Music());
        else
            this.music = null;
        this.cassetteWriteProgress = tag.getInt("CassetteWriteProgress");
        this.volume = tag.getInt("Volume");
        this.mute = tag.getBoolean("Mute");
        this.playing = tag.getBoolean("Playing");
        this.position = tag.getLong("Position");
        this.loop = tag.getBoolean("Loop");
        this.loadingMusic = tag.getBoolean("LoadingMusic");
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
        setChanged();
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
        setChanged();
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
        setChanged();
    }

    public boolean isMute() {
        return mute;
    }

    public void setMute(boolean mute) {
        this.mute = mute;
        setChanged();
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        if (this.volume != volume)
            setMute(false);
        this.volume = Mth.clamp(volume, 0, 300);
        setChanged();
    }

    public UUID getMyPlayerSelectPlaylist() {
        return myPlayerSelectPlaylist;
    }

    public void setPlayerSelectPlayList(ServerPlayer player, UUID uuid) {
        if (uuid != null)
            this.playerSelectPlaylists.put(player.getGameProfile().getId(), uuid);
        else
            this.playerSelectPlaylists.remove(player.getGameProfile().getId());
        setChanged();
    }

    public int getCassetteWriteProgress() {
        return cassetteWriteProgress;
    }

    public void setCassetteWriteProgress(int cassetteWriteProgress) {
        this.cassetteWriteProgress = cassetteWriteProgress;
        if (!getCassetteTape().isEmpty() && IMPItemUtil.isCassetteTape(getCassetteTape())) {
            CassetteTapeItem.setTapePercentage(getCassetteTape(), (float) cassetteWriteProgress / (float) getCassetteWriteProgressAll());
        }
        setChanged();
    }

    public int getLidOpenProgressAll() {
        return 10;
    }

    public MonitorType getMonitor() {
        return monitor;
    }

    public float getLidOpenProgress(float partialTicks) {
        return Mth.lerp(partialTicks, lidOpenProgressOld, lidOpenProgress);
    }

    public void startLidOpen(boolean open) {
        setLidOpen(open);
        level.playSound(null, getBlockPos(), isLidOpen() ? SoundEvents.WOODEN_DOOR_OPEN : SoundEvents.WOODEN_DOOR_CLOSE, SoundSource.BLOCKS, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
    }

    public void setLidOpen(boolean lidOpen) {
        this.lidOpen = lidOpen;
        setChanged();
    }

    public boolean isLidOpen() {
        return lidOpen;
    }

    public boolean isChangeCassetteTape() {
        return changeCassetteTape;
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected Component getDefaultName() {
        return IMPBlocks.CASSETTE_DECK.get().getName();
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return new CassetteDeckMenu(i, inventory, getBlockPos(), this);
    }

    @Override
    public boolean canPlaceItem(int i, ItemStack itemStack) {
        if (i == 0)
            return IMPItemUtil.isCassetteTape(itemStack);
        return super.canPlaceItem(i, itemStack);
    }

    public Music getMusic() {
        return music;
    }

    public ItemStack getCassetteTape() {
        return getItem(0);
    }

    public ItemStack getOldCassetteTape() {
        return oldCassetteTape;
    }

    protected void onCassetteTapeChange(ItemStack newItem, ItemStack oldItem) {
        if (monitor == MonitorType.WRITE_EXECUTION)
            setMonitor(MonitorType.WRITE);
        setRingerPosition(0);
        setPlaying(false);

        this.oldCassetteTape = oldItem;
        this.changeCassetteTape = true;
    }

    public void setMusic(Music music) {
        this.music = music;
        setChanged();
    }

    public int getCassetteWriteProgressAll() {
        return 200;
    }

    public void setMonitor(MonitorType monitor) {
        this.monitor = monitor;
        setChanged();
    }

    @Override
    public CompoundTag onInstruction(ServerPlayer player, String name, int num, CompoundTag data) {
        if ("monitor".equals(name)) {
            this.monitor = MonitorType.getByName(data.getString("name"));
            if (this.monitor == MonitorType.WRITE_EXECUTION && canWriteCassetteTape())
                IMPCriteriaTriggers.WRITE_CASSETTE_TAPE.trigger(player, getCassetteTape());
            return null;
        } else if ("select_playlist".equals(name)) {
            if (data.contains("uuid")) {
                var uuid = data.getUUID("uuid");
                var pl = MusicManager.getInstance().getSaveData().getPlayLists().get(uuid);
                if (pl != null && pl.getAuthority().getAuthorityType(player.getGameProfile().getId()).isMoreReadOnly())
                    setPlayerSelectPlayList(player, uuid);
            } else {
                setPlayerSelectPlayList(player, null);
            }
            return null;
        } else if ("set_music".equals(name)) {
            if (data.contains("music")) {
                var mm = MusicManager.getInstance();
                var m = mm.getSaveData().getMusics().get(data.getUUID("music"));
                if (m != null) {
                    var pl = mm.getPlaylistByMusic(m.getUuid());
                    if (pl != null && pl.getAuthority().getAuthorityType(player.getGameProfile().getId()).isMoreReadOnly())
                        setMusic(m);
                }
            }
            return null;
        } else if ("set_volume".equals(name)) {
            if (isPowered())
                setVolume(data.getInt("volume"));
        } else if ("set_mute".equals(name)) {
            if (isPowered())
                setMute(data.getBoolean("mute"));
        } else if ("set_playing".equals(name)) {
            if (isPowered()) {
                boolean pl = data.getBoolean("playing");
                setPlaying(pl);
                if (!pl)
                    setRingerPosition(0);
            }
        } else if ("set_pause".equals(name)) {
            if (isPowered())
                setPlaying(false);
        } else if ("restat_and_set_position".equals(name)) {
            if (isPowered())
                setMusicPositionAndRestart(data.getLong("position"));
            return null;
        } else if ("set_loop".equals(name)) {
            if (isPowered())
                setLoop(data.getBoolean("loop"));
        }
        return super.onInstruction(player, name, num, data);
    }

    public void setMusicPositionAndRestart(long position) {
        setRingerPosition(position);
        ringerRestart();
    }

    @Override
    public Component getRingerName() {
        return getDefaultName();
    }

    @Override
    public ServerLevel getRingerLevel() {
        return (ServerLevel) level;
    }

    @Override
    public UUID getRingerUUID() {
        return ringerUUID;
    }

    @Override
    public boolean exists() {
        if (getLevel() == null || level != getLevel()) return false;
        return getBlockPos() != null && level.getBlockEntity(getBlockPos()) == this;
    }

    @Override
    public boolean isRingerPlaying() {
        return isPlaying();
    }

    @Override
    public void setRingerPlaying(boolean playing) {
        setPlaying(playing);
    }

    private boolean hasCassetteTape() {
        return !getCassetteTape().isEmpty() && IMPItemUtil.isCassetteTape(getCassetteTape());
    }

    private boolean hasMusicCassetteTape() {
        return hasCassetteTape() && CassetteTapeItem.getMusic(getCassetteTape()) != null;
    }

    @Override
    public @Nullable MusicSource getRingerMusicSource() {
        return hasMusicCassetteTape() ? CassetteTapeItem.getMusic(getCassetteTape()).getSource() : null;
    }

    @Override
    public boolean isRingerLoop() {
        return isLoop();
    }

    @Override
    public long getRingerPosition() {
        return getPosition();
    }

    @Override
    public void setRingerPosition(long position) {
        setPosition(position);
        if (hasMusicCassetteTape()) {
            var m = getRingerMusicSource();
            if (m != null) {
                var nc = CassetteTapeItem.setTapePercentage(getCassetteTape().copy(), (float) position / (float) m.getDuration());
                setItemNoChange(0, nc);
            }
        }
        setChanged();
    }

    @Override
    public MusicTrackerEntry getRingerTracker() {
        return IMPMusicTrackers.createFixedTracker(getRingerSpatialPosition(), getRingerVolume(), getRingerRange());
    }

    @Override
    public @NotNull Vec3 getRingerSpatialPosition() {
        return new Vec3(getBlockPos().getX() + 0.5, getBlockPos().getY() + 0.5, getBlockPos().getZ() + 0.5);
    }

    public float getRawVolume() {
        return (float) getVolume() / 300f;
    }

    @Override
    public float getRingerVolume() {
        return getRawVolume();
    }

    @Override
    public float getRingerRange() {
        return 90f * getRawVolume();
    }

    @Override
    public boolean isRingerStream() {
        return false;
    }

    @Override
    public boolean isRingerMute() {
        return isMute();
    }

    public static enum MonitorType {
        OFF("off"),
        MENU("menu"),
        WRITE("write"),
        PLAYBACK("playback"),
        WRITE_EXECUTION("write_execution");
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
}

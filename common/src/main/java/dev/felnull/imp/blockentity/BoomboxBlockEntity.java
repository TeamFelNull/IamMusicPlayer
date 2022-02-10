package dev.felnull.imp.blockentity;

import dev.felnull.imp.block.BoomboxBlock;
import dev.felnull.imp.block.IMPBlocks;
import dev.felnull.imp.inventory.BoomboxMenu;
import dev.felnull.imp.item.CassetteTapeItem;
import dev.felnull.imp.item.IMPItems;
import dev.felnull.imp.music.resource.MusicSource;
import dev.felnull.imp.server.music.ringer.IMusicRinger;
import dev.felnull.imp.server.music.ringer.MusicRingManager;
import dev.felnull.imp.util.IMPItemUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
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
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Function;

public class BoomboxBlockEntity extends IMPBaseEntityBlockEntity implements IMusicRinger {
    private boolean handleRaising = true;
    private int handleRaisedProgressOld = getHandleRaisedAll();
    private int handleRaisedProgress = getHandleRaisedAll();
    private boolean lidOpen;
    private int lidOpenProgressOld;
    private int lidOpenProgress;
    private int parabolicAntennaProgressOld;
    private int parabolicAntennaProgress;
    private int antennaProgressOld;
    private int antennaProgress;
    private NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);
    private ItemStack lastCassetteTape = ItemStack.EMPTY;
    private ItemStack oldCassetteTape = ItemStack.EMPTY;
    private boolean changeCassetteTape;
    private int volume = 100;
    private boolean loop;
    private boolean mute;
    private boolean radio;
    private boolean noChangeCassetteTape;
    private boolean playing;
    private final UUID ringerUUID = UUID.randomUUID();
    private long ringerPosition;

    public BoomboxBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IMPBlockEntitys.BOOMBOX, blockPos, blockState);
    }

    @Override
    protected Component getDefaultName() {
        return IMPBlocks.BOOMBOX.getName();
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return new BoomboxMenu(i, inventory, this, getBlockPos(), ItemStack.EMPTY, null);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.handleRaising = tag.getBoolean("HandleRaising");
        if (this.handleRaising)
            handleRaisedProgress = getHandleRaisedAll();
        this.lidOpen = tag.getBoolean("LidOpen");
        if (this.lidOpen)
            lidOpenProgress = getLidOpenProgressAll();
        this.volume = tag.getInt("Volume");
        this.loop = tag.getBoolean("Loop");
        this.mute = tag.getBoolean("Mute");
        this.radio = tag.getBoolean("Radio");
        this.playing = tag.getBoolean("Playing");
        this.ringerPosition = tag.getLong("RingerPosition");
        noChangeCassetteTape = true;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        super.save(tag);
        tag.putBoolean("HandleRaising", this.handleRaising);
        tag.putBoolean("LidOpen", this.lidOpen);
        tag.putInt("Volume", this.volume);
        tag.putBoolean("Loop", this.loop);
        tag.putBoolean("Mute", this.mute);
        tag.putBoolean("Radio", this.radio);
        tag.putBoolean("Playing", this.playing);
        tag.putLong("RingerPosition", this.ringerPosition);
        return tag;
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, BoomboxBlockEntity blockEntity) {

        blockEntity.handleRaisedProgressOld = blockEntity.handleRaisedProgress;
        blockEntity.lidOpenProgressOld = blockEntity.lidOpenProgress;
        blockEntity.parabolicAntennaProgressOld = blockEntity.parabolicAntennaProgress;
        blockEntity.antennaProgressOld = blockEntity.antennaProgress;

        blockEntity.antennaProgress = Mth.clamp(blockEntity.antennaProgress + (blockEntity.isRadio() ? 1 : -1), 0, 30);

        if (blockEntity.isPower() && blockEntity.isRadio() && IMPItemUtil.isAntenna(blockEntity.getAntenna()) && !blockEntity.getAntenna().is(IMPItems.ANTENNA))
            blockEntity.parabolicAntennaProgress += 2;

        if (blockEntity.handleRaising) {
            if (blockEntity.handleRaisedProgress < blockEntity.getHandleRaisedAll())
                blockEntity.handleRaisedProgress++;
        } else {
            if (blockEntity.handleRaisedProgress > 0)
                blockEntity.handleRaisedProgress--;
        }

        if (blockEntity.lidOpen) {
            if (blockEntity.lidOpenProgress < blockEntity.getLidOpenProgressAll())
                blockEntity.lidOpenProgress++;
        } else {
            if (blockEntity.lidOpenProgress > 0)
                blockEntity.lidOpenProgress--;
        }

        if (!level.isClientSide()) {
            var monitor = MonitorType.getTypeByBE(blockEntity);
            if (blockEntity.isRadio()) {
                if (blockEntity.getAntenna().isEmpty() || !IMPItemUtil.isAntenna(blockEntity.getAntenna()))
                    blockEntity.setRadio(false);
            }

            if (monitor != MonitorType.PLAYING || blockEntity.getCassetteTape().isEmpty() || !IMPItemUtil.isCassetteTape(blockEntity.getCassetteTape())) {
                blockEntity.setRingerPosition((ServerLevel) level, 0);
                if (blockEntity.isPlaying())
                    blockEntity.setPlaying(false);
            }

            blockEntity.setRaisedHandleState(blockEntity.handleRaisedProgress >= blockEntity.getHandleRaisedAll());

            if (!ItemStack.matches(blockEntity.lastCassetteTape, blockEntity.getCassetteTape()))
                blockEntity.changeCassetteTape(blockEntity.lastCassetteTape);

            blockEntity.lastCassetteTape = blockEntity.getCassetteTape().copy();

            if (blockEntity.changeCassetteTape) {

                if (!blockEntity.isLidOpen())
                    blockEntity.startLidOpen(true);

                if (blockEntity.lidOpenProgress >= blockEntity.getLidOpenProgressAll()) {
                    blockEntity.changeCassetteTape = false;
                    blockEntity.startLidOpen(false);
                }
            }

            blockEntity.ringerTick((ServerLevel) level);
            blockEntity.sync();
        }
    }

    @Override
    public CompoundTag getSyncData(ServerPlayer player, CompoundTag tag) {
        tag.putBoolean("HandleRaising", this.handleRaising);
        tag.putBoolean("LidOpen", this.lidOpen);
        tag.putInt("Volume", this.volume);
        tag.putBoolean("Loop", this.loop);
        tag.putBoolean("Mute", this.mute);
        tag.putBoolean("Radio", this.radio);
        tag.putBoolean("Playing", this.playing);
        tag.put("OldCassetteTape", this.oldCassetteTape.save(new CompoundTag()));
        tag.putBoolean("ChangeCassetteTape", this.changeCassetteTape);
        tag.putLong("RingerPosition", this.ringerPosition);
        return super.getSyncData(player, tag);
    }

    @Override
    public void onSync(CompoundTag tag) {
        super.onSync(tag);
        this.handleRaising = tag.getBoolean("HandleRaising");
        this.lidOpen = tag.getBoolean("LidOpen");
        this.volume = tag.getInt("Volume");
        this.loop = tag.getBoolean("Loop");
        this.mute = tag.getBoolean("Mute");
        this.oldCassetteTape = ItemStack.of(tag.getCompound("OldCassetteTape"));
        this.changeCassetteTape = tag.getBoolean("ChangeCassetteTape");
        this.radio = tag.getBoolean("Radio");
        this.playing = tag.getBoolean("Playing");
        this.ringerPosition = tag.getLong("RingerPosition");
    }

    @Override
    public boolean canPlaceItem(int i, ItemStack itemStack) {
        return (i == 0 && IMPItemUtil.isCassetteTape(itemStack)) || (i == 1 && IMPItemUtil.isAntenna(itemStack));
    }

    public boolean isMute() {
        return mute;
    }

    public void setMute(boolean mute) {
        this.mute = mute;
        setChanged();
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
        setChanged();
    }

    public boolean isLoop() {
        return loop;
    }

    public boolean isChangeCassetteTape() {
        return changeCassetteTape;
    }

    public ItemStack getOldCassetteTape() {
        return oldCassetteTape;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
        setChanged();
    }

    public void changeCassetteTape(ItemStack old) {
        if (noChangeCassetteTape) {
            noChangeCassetteTape = false;
            return;
        }

        setRingerPosition(getRingerLevel(), 0);
        setPlaying(false);

        this.oldCassetteTape = old;
        if (!(getCassetteTape().isEmpty() && isLidOpen()))
            this.changeCassetteTape = true;
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }

    public int getHandleRaisedAll() {
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

    public boolean isRadio() {
        return this.radio;
    }

    public void setRadio(boolean radio) {
        this.radio = radio;
        setChanged();
    }

    public int getAntennaProgress() {
        return antennaProgress;
    }

    public float getAntennaProgress(float partialTicks) {
        return Mth.lerp(partialTicks, antennaProgressOld, antennaProgress);
    }

    public float getParabolicAntennaProgress(float partialTicks) {
        return Mth.lerp(partialTicks, parabolicAntennaProgressOld, parabolicAntennaProgress);
    }

    public void setRaisedHandleState(boolean raised) {
        var bs = getBlockState().setValue(BoomboxBlock.RAISED, raised);
        getLevel().setBlock(getBlockPos(), bs, 2);
    }

    public boolean isRaisedHandleState() {
        return getBlockState().getValue(BoomboxBlock.RAISED);
    }

    public boolean isHandleRaising() {
        return handleRaising;
    }

    public boolean cycleRaisedHandle() {
        boolean flg = handleRaisedProgress >= getHandleRaisedAll();
        boolean flg2 = handleRaisedProgress <= 0;
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
        setChanged();
    }

    public void setLidOpen(boolean lidOpen) {
        this.lidOpen = lidOpen;
        setChanged();
    }

    public boolean isLidOpen() {
        return lidOpen;
    }

    public int getLidOpenProgress() {
        return lidOpenProgress;
    }

    public float getLidOpenProgress(float partialTicks) {
        return Mth.lerp(partialTicks, lidOpenProgressOld, lidOpenProgress);
    }

    public int getLidOpenProgressAll() {
        return 10;
    }

    public int getVolume() {
        return Mth.clamp(this.volume, 0, 300);
    }

    public float getRawVolume() {
        return (float) getVolume() / 300f;
    }

    public void setVolume(int volume) {
        this.volume = volume;
        setChanged();
    }

    @Override
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
                        setRadio(false);
                    } else {
                        if (!getAntenna().isEmpty() && IMPItemUtil.isAntenna(getAntenna()))
                            setRadio(true);
                    }
                }
                case START -> {
                    if (isMusicCassetteTapeExist()) {
                        setPower(true);
                        setPlaying(true);
                    }
                }
                case STOP -> {
                    if (isPower() && isPlaying()) {
                        setPlaying(false);
                        setRingerPosition(player.getLevel(), 0);
                    }
                }
                case PAUSE -> {
                    if (isPower() && isPlaying()) {
                        setPlaying(false);
                    }
                }
            }
            return null;
        } else if ("set_volume".equals(name)) {
            setVolume(data.getInt("volume"));
            return null;
        }
        return super.onInstruction(player, name, num, data);
    }

    public void startLidOpen(boolean open) {
        setLidOpen(open);
        level.playSound(null, getBlockPos(), isLidOpen() ? SoundEvents.WOODEN_DOOR_OPEN : SoundEvents.WOODEN_DOOR_CLOSE, SoundSource.BLOCKS, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
    }

    private boolean isMusicCassetteTapeExist() {
        return isCassetteTapeExist() && CassetteTapeItem.getMusic(getCassetteTape()) != null;
    }

    private boolean isCassetteTapeExist() {
        return !getCassetteTape().isEmpty() && IMPItemUtil.isCassetteTape(getCassetteTape());
    }

    public boolean cycleLidOpen() {
        boolean flg = lidOpenProgress >= getLidOpenProgressAll();
        boolean flg2 = lidOpenProgress <= 0;
        if (!flg && !flg2)
            return false;
        if (flg) {
            startLidOpen(false);
        }
        if (flg2) {
            startLidOpen(true);
        }
        return true;
    }

    public ItemStack getCassetteTape() {
        return getItem(0);
    }

    public Buttons getButtons() {
        return new Buttons(isRadio(), isPlaying(), false, isLoop(), volume <= 0 || isMute(), !isMute() && volume >= 300);
    }

    public ItemStack getAntenna() {
        return getItem(1);
    }

    @Override
    public Component getRingerName(ServerLevel level) {
        return getDefaultName();
    }

    @Override
    public UUID getRingerUUID() {
        return ringerUUID;
    }

    @Override
    public boolean isRingerExist(ServerLevel level) {
        if (getLevel() == null || level != getLevel()) return false;
        return getBlockPos() != null && level.getBlockEntity(getBlockPos()) == this;
    }

    @Override
    public boolean isRingerPlaying(ServerLevel level) {
        return isPlaying();
    }

    @Override
    public void setRingerPlaying(ServerLevel level, boolean playing) {
        setPlaying(playing);
    }

    @Override
    public @Nullable MusicSource getRingerMusicSource(ServerLevel level) {
        if (isMusicCassetteTapeExist()) {
            var m = CassetteTapeItem.getMusic(getCassetteTape());
            if (m != null)
                return m.getSource();
        }
        return null;
    }

    @Override
    public boolean isRingerLoop(ServerLevel level) {
        return isLoop();
    }

    @Override
    public long getRingerPosition(ServerLevel level) {
        return this.ringerPosition;
    }

    public long getMusicPosition() {
        return ringerPosition;
    }

    @Override
    public ServerLevel getRingerLevel() {
        return (ServerLevel) this.level;
    }

    @Override
    public void setRingerPosition(ServerLevel level, long position) {
        this.ringerPosition = position;
        if (isMusicCassetteTapeExist()) {
            var m = getRingerMusicSource(level);
            if (m != null) {
                var nc = CassetteTapeItem.setTapePercentage(getCassetteTape().copy(), (float) position / (float) m.getDuration());
                if (!ItemStack.matches(nc, getCassetteTape()))
                    noChangeCassetteTape = true;
                setItem(0, nc);
            }
        }
        setChanged();
    }

    @Override
    public Pair<ResourceLocation, CompoundTag> getRingerTracker(ServerLevel level) {
        return Pair.of(MusicRingManager.FIXED_TRACKER, MusicRingManager.createFixedTracker(getRingerVec3Position(level)));
    }

    @Override
    public @NotNull Vec3 getRingerVec3Position(ServerLevel level) {
        return new Vec3(getBlockPos().getX() + 0.5, getBlockPos().getY() + 0.5, getBlockPos().getZ() + 0.5);
    }

    @Override
    public float getRingerVolume(ServerLevel level) {
        return getRawVolume();
    }

    @Override
    public float getRingerRange(ServerLevel level) {
        return 30f;
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

    public MonitorType getMonitorType() {
        return MonitorType.getTypeByBE(this);
    }

    public static enum MonitorType {
        OFF("off"),
        PLAYING("playing"),
        RADIO("radio");

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

        public static MonitorType getTypeByData(boolean power, boolean radio) {
            if (power) {
                if (radio)
                    return RADIO;
                return PLAYING;
            }
            return OFF;
        }

        public static MonitorType getTypeByBE(BoomboxBlockEntity blockEntity) {
            return getTypeByData(blockEntity.isPower(), blockEntity.isRadio());
        }
    }
}

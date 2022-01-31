package dev.felnull.imp.blockentity;

import dev.felnull.imp.block.BoomboxBlock;
import dev.felnull.imp.block.IMPBlocks;
import dev.felnull.imp.inventory.BoomboxMenu;
import dev.felnull.imp.util.IMPItemUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;

public class BoomboxBlockEntity extends IMPBaseEntityBlockEntity {
    protected boolean handleRaising = true;
    protected int handleRaisedProgressOld = getHandleRaisedAll();
    protected int handleRaisedProgress = getHandleRaisedAll();
    protected boolean lidOpen;
    protected int lidOpenProgressOld;
    protected int lidOpenProgress;
    protected NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);
    private int volume = 100;
    private boolean loop;
    private boolean mute;

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
        this.lidOpen = tag.getBoolean("LidOpen");
        this.volume = tag.getInt("Volume");
        this.loop = tag.getBoolean("Loop");
        this.mute = tag.getBoolean("Mute");
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        super.save(tag);
        tag.putBoolean("HandleRaising", this.handleRaising);
        tag.putBoolean("LidOpen", this.lidOpen);
        tag.putInt("Volume", this.volume);
        tag.putBoolean("Loop", this.loop);
        tag.putBoolean("Mute", this.mute);
        return tag;
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, BoomboxBlockEntity blockEntity) {

        blockEntity.handleRaisedProgressOld = blockEntity.handleRaisedProgress;
        blockEntity.lidOpenProgressOld = blockEntity.lidOpenProgress;

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
            blockEntity.setRaisedHandleState(blockEntity.handleRaisedProgress >= blockEntity.getHandleRaisedAll());
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
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
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
            handleRaising = false;
        }
        if (flg2) {
            handleRaising = true;
        }
        return true;
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
        return Mth.clamp(this.volume, 0, 200);
    }

    public float getRawVolume() {
        return (float) getVolume() / 100f;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    @Override
    public CompoundTag onInstruction(ServerPlayer player, String name, int num, CompoundTag data) {
        if ("ButtonsPress".equals(name)) {
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
                        setVolume(Math.min(volume + 10, 200));
                    setMute(false);
                }
                case VOL_MUTE -> setMute(!isMute());
                case VOL_MAX -> {
                    if (isPower())
                        setVolume(200);
                    setMute(false);
                }
            }
            return null;
        }
        return super.onInstruction(player, name, num, data);
    }

    public boolean cycleLidOpen() {
        boolean flg = lidOpenProgress >= getLidOpenProgressAll();
        boolean flg2 = lidOpenProgress <= 0;
        if (!flg && !flg2)
            return false;
        if (flg) {
            lidOpen = false;
        }
        if (flg2) {
            lidOpen = true;
        }
        return true;
    }

    public ItemStack getCassetteTape() {
        return getItem(0);
    }

    public Buttons getButtons() {
        return new Buttons(false, false, false, false, isLoop(), volume <= 0 || isMute(), !isMute() && volume >= 200);
    }

    public ItemStack getAntenna() {
        return getItem(1);
    }

    public static record Buttons(boolean radio, boolean start, boolean pause, boolean stop, boolean loop,
                                 boolean volMute, boolean volMax) {
        public static final Buttons EMPTY = new Buttons(false, false, false, false, false, false, false);
    }

    public static enum ButtonType {
        NONE("none", n -> false),
        POWER("power", n -> false),
        RADIO("radio", n -> n.radio()),
        START("start", n -> n.start()),
        PAUSE("pause", n -> n.pause()),
        STOP("stop", n -> n.stop()),
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
}

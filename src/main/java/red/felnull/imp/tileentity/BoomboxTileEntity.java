package red.felnull.imp.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import red.felnull.imp.block.BoomboxBlock;
import red.felnull.imp.block.propertie.BoomboxMode;
import red.felnull.imp.container.BoomboxContainer;
import red.felnull.imp.music.resource.PlayMusic;
import red.felnull.imp.util.ItemHelper;

import java.util.UUID;

public class BoomboxTileEntity extends IMPAbstractEquipmentTileEntity implements IMusicPlayerTileEntity {
    protected NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
    private UUID mPlayerUUID;
    private long currentPlayPos;

    public BoomboxTileEntity() {
        super(IMPTileEntityTypes.BOOMBOX);
        this.mPlayerUUID = UUID.randomUUID();
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.boombox");
    }

    @Override
    public CompoundNBT instructionFromClient(ServerPlayerEntity player, String s, CompoundNBT tag) {
        if (s.equals("Mode")) {
            setMode(BoomboxMode.getScreenByName(tag.getString("name")));
        }
        return super.instructionFromClient(player, s, tag);
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new BoomboxContainer(id, player, this, getPos());
    }

    public BoomboxMode getMode() {
        return getBlockState().get(BoomboxBlock.BOOMBOX_MODE);
    }

    public void setMode(BoomboxMode mode) {
        setBlockState(getBlockState().with(BoomboxBlock.BOOMBOX_MODE, mode));
    }

    @Override
    public void readByIKSG(BlockState state, CompoundNBT tag) {
        super.readByIKSG(state, tag);
        this.currentPlayPos = tag.getLong("CurrentPlayPos");
        this.mPlayerUUID = UUID.fromString(tag.getString("MusicPlayerUUID"));
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.putLong("CurrentPlayPos", currentPlayPos);
        tag.putString("MusicPlayerUUID", mPlayerUUID.toString());
        return super.write(tag);
    }

    @Override
    public void tick() {
        super.tick();
        if (getMode() == BoomboxMode.PLAY) {
            if (!isPlaying())
                play(currentPlayPos);
        }
    }

    public ItemStack getCassetteTape() {
        return getItems().get(0);
    }

    @Override
    public boolean canPlay() {
        return getMode() == BoomboxMode.PLAY;
    }

    @Override
    public void stoped() {
        setMode(BoomboxMode.NONE);
    }

    @Override
    public long getCurrentPlayPosition() {
        return currentPlayPos;
    }

    @Override
    public void setCurrentPlayPosition(long position) {
        this.currentPlayPos = position;
    }

    @Override
    public UUID getMusicPlayerUUID() {
        return mPlayerUUID;
    }

    @Override
    public PlayMusic getMusic() {
        return ItemHelper.getPlayMusicByItem(getCassetteTape());
    }
}

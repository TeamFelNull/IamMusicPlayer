package red.felnull.imp.tileentity;

import java.util.UUID;

import net.minecraft.block.BlockState;
import net.minecraft.inventory.IClearable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.network.PacketDistributor;
import red.felnull.imp.block.BoomboxBlock;
import red.felnull.imp.block.IMPBlocks;
import red.felnull.imp.packet.BoomboxSyncMessage;
import red.felnull.imp.packet.PacketHandler;
import red.felnull.imp.sound.*;
import red.felnull.imp.util.SoundHelper;

public class BoomboxTileEntity extends TileEntity implements IClearable, ITickableTileEntity, INewSoundPlayer {

    protected ItemStack cassette = ItemStack.EMPTY;
    public int openProgress;

    private long lasttime;
    private long position;
    private float volume;

    private boolean redstone;
    private boolean canplay;

    private String uuid;

    public BoomboxTileEntity() {
        super(IMPTileEntityTypes.BOOMBOX);
        uuid = UUID.randomUUID().toString();
    }

    @Override
    public void clear() {
        this.setCassette(ItemStack.EMPTY);
        this.openProgress = 0;

    }

    @Override
    public void func_230337_a_(BlockState state, CompoundNBT tag) {
        super.func_230337_a_(state, tag);

        if (tag.contains("CassetteItem", 10))
            this.setCassette(ItemStack.read(tag.getCompound("CassetteItem")));

        this.openProgress = tag.getInt("OpenProgress");
        this.position = tag.getLong("Position");
        this.volume = tag.getFloat("Volume");

        this.canplay = tag.getBoolean("CanPlay");
        this.redstone = tag.getBoolean("RedStone");

    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        super.write(tag);

        if (!this.getCassette().isEmpty())
            tag.put("CassetteItem", this.getCassette().write(new CompoundNBT()));

        tag.putInt("OpenProgress", this.openProgress);
        tag.putLong("Position", this.position);
        tag.putLong("LastTime", this.lasttime);
        tag.putFloat("Volume", this.volume);

        tag.putBoolean("CanPlay", this.canplay);
        tag.putBoolean("RedStone", this.redstone);

        return tag;
    }

    public ItemStack getCassette() {
        return this.cassette;
    }

    public void setCassette(ItemStack item) {
        this.cassette = item;
        this.markDirty();
    }

    @Override
    public void tick() {
        SoundHelper.soundPlayerTick(this, this.world);
        if (!world.isRemote) {
            sendClientSyncPacket();

            WorldSoundKey wsk = new WorldSoundKey(WorldPlayListSoundData.getWorldPlayListData(this.getCassette()));
            boolean flagca = wsk.isServerExistence(this.getWorld().getServer());

            this.canplay = SoundHelper.canPlay(getCassette()) && flagca;

            if (this.getBlockState().get(BoomboxBlock.OPEN)) {
                if (this.openProgress < 30)
                    this.openProgress++;
            } else {
                if (this.openProgress > 0)
                    this.openProgress--;
            }

            if (this.openProgress == 0 && !this.getBlockState().get(BoomboxBlock.OPEN) && this.canPlayed()
                    && this.openProgress == 0) {
                boolean flag = this.world.isBlockPowered(pos);
                if (flag) {
                    this.world.setBlockState(pos, this.getBlockState().with(BoomboxBlock.ON, true));
                    redstone = true;
                }

                if (!flag && redstone) {
                    this.world.setBlockState(pos, this.getBlockState().with(BoomboxBlock.ON, false));
                    redstone = false;
                }

            } else {
                redstone = false;
            }

        }

    }

    public void sendClientSyncPacket() {

        Chunk ch = (Chunk) this.world.getChunk(pos);

        PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> ch),
                new BoomboxSyncMessage(0, this.pos, getCassette(), this.openProgress, this.position, this.lasttime, this.volume, this.canplay));

    }

    public void clientSync(BoomboxSyncMessage message) {
        this.cassette = message.cassette;
        this.openProgress = message.openProgress;
        this.position = message.position;
        this.lasttime = message.lasttime;
        this.volume = message.volume;
        this.canplay = message.canplay;
    }

    @Override
    public PlayData getSound() {

        return new PlayData(new WorldSoundKey(WorldPlayListSoundData.getWorldPlayListData(this.getCassette())));
    }

    @Override
    public SoundPos getSoundPos() {

        return new SoundPos(this.pos);
    }

    @Override
    public boolean canPlayed() {

        return this.canplay;
    }

    @Override
    public boolean isPlayed() {

        return this.getBlockState().get(BoomboxBlock.ON);
    }

    @Override
    public void setPlayed(boolean play) {
        world.setBlockState(this.pos, this.getBlockState().with(BoomboxBlock.ON, play));
    }

    @Override
    public float getVolume() {
        float invol = this.getBlockState().get(BoomboxBlock.VOLUME);

        return invol / 16;
    }

    @Override
    public void setVolume(float volume) {
        world.setBlockState(this.pos, this.getBlockState().with(BoomboxBlock.VOLUME, (int) (32 * volume)));
    }

    @Override
    public long getPosition() {

        return this.position;
    }

    @Override
    public void setPosition(long position) {
        this.position = position;
    }

    @Override
    public boolean canExistence() {

        boolean flag1 = this.world.getBlockState(this.pos).getBlock() == IMPBlocks.BOOMBOX;
        @SuppressWarnings("deprecation")
        boolean flag2 = this.world.isBlockLoaded(this.pos);
        return flag1 && flag2;
    }

    @Override
    public long getLastTime() {

        return this.lasttime;
    }

    @Override
    public void setLastTime(long lasttime) {
        this.lasttime = lasttime;
    }

    @Override
    public boolean isLoop() {

        return this.getWorld().isBlockPowered(this.pos);
    }

    @Override
    public boolean isReset() {

        return this.getBlockState().get(BoomboxBlock.OPEN);
    }

    @Override
    public String getUuid() {

        return uuid;
    }

}
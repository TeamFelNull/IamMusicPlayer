package red.felnull.imp.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import red.felnull.imp.block.BoomboxBlock;
import red.felnull.imp.block.propertie.BoomboxMode;
import red.felnull.imp.container.BoomboxContainer;
import red.felnull.imp.music.resource.PlayMusic;
import red.felnull.imp.util.ItemHelper;

import java.util.Objects;
import java.util.UUID;

public class BoomboxTileEntity extends IMPAbstractEquipmentTileEntity implements IMusicPlayerTileEntity {
    private UUID mPlayerUUID;
    private long currentPlayPos;
    private boolean playWaiting;
    private boolean musicLoop;
    private int musicVolume;
    private boolean musicVolumeMute;

    public BoomboxTileEntity() {
        super(IMPTileEntityTypes.BOOMBOX);
        this.mPlayerUUID = UUID.randomUUID();
        this.musicVolume = 100;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.boombox");
    }

    @Override
    public CompoundNBT instructionFromClient(ServerPlayerEntity player, String s, CompoundNBT tag) {
        if (s.equals("Mode")) {
            setMode(BoomboxMode.getScreenByName(tag.getString("name")));
        } else if (s.equals("Stop")) {
            musciPlayStop();
        } else if (s.equals("Loop")) {
            setMusicLoop(tag.getBoolean("enble"));
        } else if (s.equals("VolumeUp")) {
            if (getMusicVolumeN() + 10 <= 200)
                setMusicVolume(getMusicVolumeN() + 10);
        } else if (s.equals("VolumeDown")) {
            if (getMusicVolumeN() - 10 >= 0)
                setMusicVolume(getMusicVolumeN() - 10);
        } else if (s.equals("VolumeMute")) {
            setMusicVolumeMute(tag.getBoolean("enble"));
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
        this.playWaiting = tag.getBoolean("PlayWaiting");
        this.musicLoop = tag.getBoolean("MusicLoop");
        this.musicVolume = tag.getInt("MusicVolume");
        this.musicVolumeMute = tag.getBoolean("MusicVolumeMute");
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.putLong("CurrentPlayPos", currentPlayPos);
        tag.putBoolean("PlayWaiting", playWaiting);
        tag.putBoolean("MusicLoop", musicLoop);
        tag.putInt("MusicVolume", musicVolume);
        tag.putBoolean("MusicVolumeMute", musicVolumeMute);
        return super.write(tag);
    }

    @Override
    protected int getInventorySize() {
        return 1;
    }

    @Override
    public void tick() {
        super.tick();
        if (!world.isRemote()) {
            if (getMode() == BoomboxMode.PLAY) {
                if (!isMusicPlaying())
                    musicPlay();
            }

            if (getMode() == BoomboxMode.PAUSE && getCassetteTape().isEmpty()) {
                setMode(BoomboxMode.NONE);
            }

            if (getMode() == BoomboxMode.NONE || getCassetteTape().isEmpty()) {
                setCurrentMusicPlayPosition(0);
            }

            if (!isOn()) {
                setMode(BoomboxMode.NONE);
            }

            playWaiting = isMusicPlayWaiting();
        }
    }

    public void musciPlayStop() {
        setMode(BoomboxMode.NONE);
    }

    public ItemStack getCassetteTape() {
        return getItems().get(0);
    }

    public boolean isPlayWaiting() {
        return playWaiting;
    }

    public void setMusicVolume(int musicVolume) {
        this.musicVolume = musicVolume;
    }

    public int getMusicVolumeN() {
        return this.musicVolume;
    }

    public void setMusicLoop(boolean loop) {
        this.musicLoop = loop;
    }

    @Override
    public boolean canMusicPlay() {
        return getWorld().loadedTileEntityList.contains(this) && getWorld().isBlockLoaded(getPos()) && getMode() == BoomboxMode.PLAY && !getCassetteTape().isEmpty() && isOn();
    }

    public void setMusicVolumeMute(boolean musicVolumeMute) {
        this.musicVolumeMute = musicVolumeMute;
    }

    public boolean isMusicVolumeMute() {
        return musicVolumeMute;
    }

    @Override
    public void musicPlayed() {

    }

    @Override
    public void musicStoped() {
        setMode(BoomboxMode.NONE);
    }

    @Override
    public long getCurrentMusicPlayPosition() {
        return currentPlayPos;
    }

    @Override
    public void setCurrentMusicPlayPosition(long position) {
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


    @Override
    public Vector3d getMusicPos() {
        return new Vector3d(getPos().getX() + 0.5f, getPos().getY() + 0.5f, getPos().getZ() + 0.5f);
    }

    @Override
    public ResourceLocation getMusicDimension() {
        return Objects.requireNonNull(getWorld()).getDimensionKey().getLocation();
    }

    @Override
    public boolean isMusicLoop() {
        return musicLoop;
    }

    @Override
    public float getMusicVolume() {
        return isMusicVolumeMute() ? 0 : (float) getMusicVolumeN() / 100f;
    }
}

package net.morimori.imp.tileentity;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.network.PacketDistributor;
import net.morimori.imp.block.SoundfileUploaderBlock;
import net.morimori.imp.client.screen.SoundFileUploaderMonitorTextures;
import net.morimori.imp.client.screen.SoundFileUploaderWindwos;
import net.morimori.imp.container.SoundFileUploaderContainer;
import net.morimori.imp.packet.PacketHandler;
import net.morimori.imp.packet.SoundFileUploaderSyncMessage;
import net.morimori.imp.util.ItemHelper;
import net.morimori.imp.util.PlayerHelper;

public class SoundFileUploaderTileEntity extends LockableTileEntity
		implements ITickableTileEntity {
	protected NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
	public int rotationPitch;//縦方向最大90度
	public int rotationYaw;//横方向最大360度
	public Map<String, String> playerstager = new HashMap<String, String>();

	private boolean inversionPitch;

	private String playeruuid;
	private String fliepath;

	public SoundFileUploaderTileEntity() {
		super(IMPTileEntityTypes.SOUNDFILE_UPLOADER);
		playeruuid = "nonplayer";
		fliepath = "nonfile";

	}

	public String getFliePath() {

		return this.fliepath.isEmpty() ? "null" : this.fliepath;
	}

	public void setFliePath(String path) {
		this.fliepath = path;
	}

	public String getUsePlayerUUID() {

		return this.playeruuid.isEmpty() ? "null" : this.playeruuid;
	}

	public void setUsePlayerUUID(String uuid) {
		this.playeruuid = uuid;
	}

	public void setUsePlayerUUID(PlayerEntity pl) {
		setUsePlayerUUID(PlayerHelper.getUUID(pl));
	}

	@Override
	public void tick() {
		if (!world.isRemote) {
			if (this.getBlockState().get(SoundfileUploaderBlock.ON)) {
				if (this.getBlockState()
						.get(SoundfileUploaderBlock.SOUNDFILE_UPLOADER_MONITOR) == SoundFileUploaderMonitorTextures.OFF) {
					world.setBlockState(this.pos,
							this.getBlockState().with(SoundfileUploaderBlock.SOUNDFILE_UPLOADER_MONITOR,
									SoundFileUploaderMonitorTextures.ON));
				}

				if (this.getBlockState()
						.get(SoundfileUploaderBlock.SOUNDFILE_UPLOADER_WINDWOS) != SoundFileUploaderWindwos.NO_ANTENA) {
					if (!ItemHelper.isAntenna(getAntenna())) {
						world.setBlockState(this.pos,
								this.getBlockState().with(SoundfileUploaderBlock.SOUNDFILE_UPLOADER_WINDWOS,
										SoundFileUploaderWindwos.NO_ANTENA));
						this.setUsePlayerUUID("null");
						this.setFliePath("null");
					}
				} else {
					if (ItemHelper.isAntenna(getAntenna())) {
						world.setBlockState(this.pos,
								this.getBlockState().with(SoundfileUploaderBlock.SOUNDFILE_UPLOADER_WINDWOS,
										SoundFileUploaderWindwos.DESKTOP));
					}
				}

				if (ItemHelper.isAntenna(getAntenna())) {
					this.rotationYaw += 2;
					while (this.rotationYaw > 360) {
						this.rotationYaw -= 360;
					}
					if (!inversionPitch) {
						if (50 >= rotationPitch) {
							this.rotationPitch += 2;
						} else {
							this.inversionPitch = true;
						}
					} else {
						if (-50 <= rotationPitch) {
							this.rotationPitch -= 2;
						} else {
							this.inversionPitch = false;
						}
					}
				}

			} else {
				world.setBlockState(this.pos,
						this.getBlockState().with(SoundfileUploaderBlock.SOUNDFILE_UPLOADER_MONITOR,
								SoundFileUploaderMonitorTextures.OFF));
				world.setBlockState(this.pos,
						this.getBlockState().with(SoundfileUploaderBlock.SOUNDFILE_UPLOADER_WINDWOS,
								SoundFileUploaderWindwos.NONE));
			}

			sendClientSyncPacket();
		}

	}

	@Override
	public void clear() {
		this.setAntenna(ItemStack.EMPTY);
	}

	public NonNullList<ItemStack> getItems() {
		return this.items;
	}

	public ItemStack getAntenna() {
		return this.getItems().get(0);
	}

	public void setAntenna(ItemStack item) {
		this.setItem(0, item);
	}

	public void setItem(int id, ItemStack item) {
		this.items.set(id, item);
		this.markDirty();
	}

	@Override
	public void read(CompoundNBT tag) {
		super.read(tag);

		ItemStackHelper.loadAllItems(tag, items);

		this.rotationPitch = tag.getInt("RotationPitch");
		this.rotationYaw = tag.getInt("RotationYaw");
		this.inversionPitch = tag.getBoolean("InversionPitch");
		this.playeruuid = tag.getString("PlayerUUID");
		this.fliepath = tag.getString("FilePath");

		CompoundNBT ptmnbt = tag.getCompound("PlayersTager");
		for (String key : ptmnbt.keySet()) {
			playerstager.put(key, ptmnbt.getString(key));
		}

	}

	@Override
	public CompoundNBT write(CompoundNBT tag) {
		super.write(tag);

		tag.putInt("RotationPitch", this.rotationPitch);
		tag.putInt("RotationYaw", this.rotationYaw);
		tag.putBoolean("InversionPitch", this.inversionPitch);
		tag.putString("PlayerUUID", this.playeruuid);
		tag.putString("FilePath", this.fliepath);

		CompoundNBT ptmnbt = new CompoundNBT();
		for (Entry<String, String> ent : playerstager.entrySet()) {
			ptmnbt.putString(ent.getKey(), ent.getValue());
		}
		tag.put("PlayersTager", ptmnbt);

		CompoundNBT tag2 = ItemStackHelper.saveAllItems(tag, items);

		return tag2;
	}

	@Override
	public int getSizeInventory() {

		return 1;
	}

	@Override
	public boolean isEmpty() {

		return this.getItems().stream().allMatch(ItemStack::isEmpty);
	}

	@Override
	public ItemStack getStackInSlot(int var1) {

		return this.getItems().get(var1);
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if (index == 0)
			return ItemHelper.isAntenna(stack);

		return true;
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {

		return ItemStackHelper.getAndSplit(this.items, index, count);
	}

	@Override
	public boolean isUsableByPlayer(PlayerEntity pl) {

		if (this.world.getTileEntity(this.pos) != this) {
			return false;
		} else {
			return pl.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D,
					(double) this.pos.getZ() + 0.5D) <= 64.0D;
		}
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.items, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		ItemStack itemstack = this.items.get(index);
		boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack)
				&& ItemStack.areItemStackTagsEqual(stack, itemstack);
		this.items.set(index, stack);
		if (stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}
		if (flag)
			this.markDirty();
	}

	@Override
	protected ITextComponent getDefaultName() {

		return new TranslationTextComponent("block.iammusicplayer.soundfile_uploader");
	}

	@Override
	protected Container createMenu(int id, PlayerInventory player) {
		return new SoundFileUploaderContainer(id, player, this, this.getPos());
	}

	public void sendClientSyncPacket() {
		Chunk ch = (Chunk) this.world.getChunk(pos);
		PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> ch),
				new SoundFileUploaderSyncMessage(this.world.dimension.getDimension().getType().getId(), this.pos,
						this.items, this.rotationPitch, this.rotationYaw, this.playeruuid, this.fliepath,
						this.playerstager));
	}

	public void clientSync(SoundFileUploaderSyncMessage message) {
		this.items = message.items;
		this.rotationPitch = message.pitch;
		this.rotationYaw = message.yaw;
		this.playeruuid = message.playeruuid;
		this.fliepath = message.path;
		this.playerstager = message.playerstager;
	}

}

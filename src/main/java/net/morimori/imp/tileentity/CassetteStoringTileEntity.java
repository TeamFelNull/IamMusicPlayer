package net.morimori.imp.tileentity;

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
import net.morimori.imp.container.CassetteStoringContainer;
import net.morimori.imp.packet.CassetteStoringSyncMessage;
import net.morimori.imp.packet.PacketHandler;

public class CassetteStoringTileEntity extends LockableTileEntity
		implements ITickableTileEntity {
	protected NonNullList<ItemStack> items = NonNullList.withSize(16, ItemStack.EMPTY);

	public CassetteStoringTileEntity() {
		super(IMPTileEntityTypes.CASSETTE_STORING);
	}

	@Override
	public void read(CompoundNBT tag) {
		super.read(tag);
		ItemStackHelper.loadAllItems(tag, items);
	}

	@Override
	public CompoundNBT write(CompoundNBT tag) {
		super.write(tag);

		CompoundNBT tag2 = ItemStackHelper.saveAllItems(tag, items);

		return tag2;
	}
	public void setItem(int id, ItemStack item) {
		this.items.set(id, item);
		this.markDirty();
	}
	public ItemStack getCassette(int num) {
		return getItems().get(num);
	}

	public NonNullList<ItemStack> getItems() {
		return this.items;
	}

	@Override
	public int getSizeInventory() {

		return 16;
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
	public ItemStack decrStackSize(int index, int count) {

		return ItemStackHelper.getAndSplit(this.items, index, count);
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
	public boolean isUsableByPlayer(PlayerEntity pl) {

		if (this.world.getTileEntity(this.pos) != this) {
			return false;
		} else {
			return pl.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D,
					(double) this.pos.getZ() + 0.5D) <= 64.0D;
		}
	}

	@Override
	public void clear() {
		items.clear();
	}

	@Override
	public void tick() {

		if (!this.world.isRemote) {
			sendClientSyncPacket();
		}

	}

	@Override
	protected ITextComponent getDefaultName() {

		return new TranslationTextComponent("block.ikisugimusicplayer.cassette_storing");
	}

	@Override
	protected Container createMenu(int id, PlayerInventory player) {
		return new CassetteStoringContainer(id, player, this, this.getPos());
	}

	public void sendClientSyncPacket() {
		Chunk ch = (Chunk) this.world.getChunk(pos);
		PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> ch),
				new CassetteStoringSyncMessage(this.world.dimension.getDimension().getType().getId(), this.pos,
						this.items));
	}

	public void clientSync(CassetteStoringSyncMessage message) {
		this.items = message.items;
	}
}

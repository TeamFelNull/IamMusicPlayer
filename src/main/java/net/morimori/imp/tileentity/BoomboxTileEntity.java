package net.morimori.imp.tileentity;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.IClearable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;
import net.morimori.imp.IamMusicPlayer;
import net.morimori.imp.block.BoomboxBlock;
import net.morimori.imp.packet.BoomboxSyncMessage;
import net.morimori.imp.packet.PacketHandler;
import net.morimori.imp.sound.SoundPlayer;
import net.morimori.imp.sound.SoundPos;
import net.morimori.imp.sound.SoundWaitThread;
import net.morimori.imp.sound.WorldPlayListSoundData;
import net.morimori.imp.util.PlayerHelper;
import net.morimori.imp.util.SoundHelper;

public class BoomboxTileEntity extends TileEntity implements IClearable, ITickableTileEntity, ISoundPlayer {

	protected ItemStack cassette = ItemStack.EMPTY;
	public int openProgress;
	public Set<String> lisnFinishedPlayers = new HashSet<String>();

	public BoomboxTileEntity() {
		super(IMPTileEntityTypes.BOOMBOX);
	}

	@Override
	public void clear() {
		this.setCassette(ItemStack.EMPTY);
		this.openProgress = 0;
		if (SoundWaitThread.posplayMap.containsKey(this.pos)) {
			SoundWaitThread.posplayMap.get(this.pos).stopPlayer();
			SoundWaitThread.removePosMap(this.pos);
		}
	}

	@Override
	public void read(CompoundNBT tag) {
		super.read(tag);

		if (tag.contains("CassetteItem", 10))
			this.setCassette(ItemStack.read(tag.getCompound("CassetteItem")));

		this.openProgress = tag.getInt("OpenProgress");

		CompoundNBT pfmnbt = tag.getCompound("LisnFinishedPlayers");
		lisnFinishedPlayers.clear();
		for (String key : pfmnbt.keySet()) {
			lisnFinishedPlayers.add(pfmnbt.getString(key));
		}

	}

	@Override
	public CompoundNBT write(CompoundNBT tag) {
		super.write(tag);

		if (!this.getPlayCassette().isEmpty())
			tag.put("CassetteItem", this.getPlayCassette().write(new CompoundNBT()));

		tag.putInt("OpenProgress", this.openProgress);

		CompoundNBT ptmnbt = new CompoundNBT();
		int cont = 0;
		for (String uuid : lisnFinishedPlayers) {
			ptmnbt.putString(String.valueOf(cont), uuid);
			cont++;
		}
		tag.put("LisnFinishedPlayers", ptmnbt);

		return tag;
	}

	public ItemStack getPlayCassette() {
		return this.cassette;
	}

	public void setCassette(ItemStack item) {
		this.cassette = item;
		this.markDirty();
	}

	@Override
	public void tick() {

		if (!world.isRemote) {
			sendClientSyncPacket();
			if (this.getBlockState().get(BoomboxBlock.OPEN)) {
				if (this.openProgress < 30)
					this.openProgress++;
			} else {
				if (this.openProgress > 0)
					this.openProgress--;
			}

			if (this.openProgress == 0) {
				if (!this.getBlockState().get(BoomboxBlock.OPEN) && SoundHelper.canPlay(this.getPlayCassette())
						&& this.openProgress == 0) {
					boolean flag = this.world.isBlockPowered(pos);
					if (flag) {
						this.world.setBlockState(pos, this.getBlockState().with(BoomboxBlock.ON, true));
					}
				}
			}

			if (isSoundStop()) {
				lisnFinishedPlayers.clear();
			}

		} else {
			playSound();
		}
	}

	public void sendClientSyncPacket() {

		Chunk ch = (Chunk) this.world.getChunk(pos);

		PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> ch),
				new BoomboxSyncMessage(this.world.dimension.getDimension().getType().getId(), this.pos, getPlayCassette(),
						this.openProgress, this.lisnFinishedPlayers));

	}

	public void clientSync(BoomboxSyncMessage message) {
		this.cassette = message.cassette;
		this.openProgress = message.openProgress;
		this.lisnFinishedPlayers = message.lisnFinishedPlayers;
	}

	@Override
	public boolean isSoundStop() {

		return !(SoundHelper.canPlay(getPlayCassette()) && this.getBlockState().get(BoomboxBlock.ON));
	}

	@Override
	public boolean isLoopPlay() {

		return this.world.isBlockPowered(this.pos);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void playSound() {
		if (world.isRemote) {
			if (SoundHelper.canPlay(getPlayCassette()) && this.getBlockState().get(BoomboxBlock.ON)) {
				Minecraft mc = IamMusicPlayer.proxy.getMinecraft();
				int vol = this.getBlockState().get(BoomboxBlock.VOLUME);

				if (!lisnFinishedPlayers.contains(PlayerHelper.getUUID(mc.player))) {
					if (new SoundPos(this.pos).canLisn(100 / 32 * vol)) {
						if (!SoundWaitThread.posplayMap.containsKey(this.pos)) {
							SoundWaitThread.posplayMap.put(this.pos,
									new SoundPlayer(WorldPlayListSoundData.getWorldPlayListData(getPlayCassette()),
											new SoundPos(this.pos), 3f / 32f * (float) vol, 100 / 32 * vol));
						} else {
							SoundWaitThread.posplayMap.get(this.pos).setVolume(4f / 32f * (float) vol);
							SoundWaitThread.posplayMap.get(this.pos).setMaxDistance(150 / 32 * vol);

						}
					}
				}
			}
		}
	}
}
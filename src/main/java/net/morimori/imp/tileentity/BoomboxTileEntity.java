package net.morimori.imp.tileentity;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.inventory.IClearable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.network.PacketDistributor;
import net.morimori.imp.block.BoomboxBlock;
import net.morimori.imp.block.IMPBlocks;
import net.morimori.imp.packet.BoomboxSyncMessage;
import net.morimori.imp.packet.PacketHandler;
import net.morimori.imp.sound.INewSoundPlayer;
import net.morimori.imp.sound.PlayData;
import net.morimori.imp.sound.SoundPos;
import net.morimori.imp.sound.SoundWaitThread;
import net.morimori.imp.sound.WorldPlayListSoundData;
import net.morimori.imp.sound.WorldSoundKey;
import net.morimori.imp.util.SoundHelper;

public class BoomboxTileEntity extends TileEntity implements IClearable, ITickableTileEntity, INewSoundPlayer {

	protected ItemStack cassette = ItemStack.EMPTY;
	public int openProgress;
	public Set<String> lisnFinishedPlayers = new HashSet<String>();

	private long position;
	private float volume;

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
		this.position = tag.getLong("Position");
		this.volume = tag.getFloat("Volume");

		CompoundNBT pfmnbt = tag.getCompound("LisnFinishedPlayers");
		lisnFinishedPlayers.clear();
		for (String key : pfmnbt.keySet()) {
			lisnFinishedPlayers.add(pfmnbt.getString(key));
		}

	}

	@Override
	public CompoundNBT write(CompoundNBT tag) {
		super.write(tag);

		if (!this.getCassette().isEmpty())
			tag.put("CassetteItem", this.getCassette().write(new CompoundNBT()));

		tag.putInt("OpenProgress", this.openProgress);
		tag.putLong("Position", this.position);
		tag.putFloat("Volume", this.volume);

		CompoundNBT ptmnbt = new CompoundNBT();
		int cont = 0;
		for (String uuid : lisnFinishedPlayers) {
			ptmnbt.putString(String.valueOf(cont), uuid);
			cont++;
		}
		tag.put("LisnFinishedPlayers", ptmnbt);

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
				if (!this.getBlockState().get(BoomboxBlock.OPEN) && SoundHelper.canPlay(this.getCassette())
						&& this.openProgress == 0) {
					boolean flag = this.world.isBlockPowered(pos);
					if (flag) {
						this.world.setBlockState(pos, this.getBlockState().with(BoomboxBlock.ON, true));
					}
				}
			}

			//	if (isSoundStop()) {
			//			lisnFinishedPlayers.clear();
			//		}

		} else {
			SoundHelper.soundPlayerTick(this);
		}
	}

	public void sendClientSyncPacket() {

		Chunk ch = (Chunk) this.world.getChunk(pos);

		PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> ch),
				new BoomboxSyncMessage(this.world.dimension.getDimension().getType().getId(), this.pos,
						getCassette(), this.openProgress, this.lisnFinishedPlayers, this.position, this.volume));

	}

	public void clientSync(BoomboxSyncMessage message) {
		this.cassette = message.cassette;
		this.openProgress = message.openProgress;
		this.lisnFinishedPlayers = message.lisnFinishedPlayers;
		this.position = message.position;
		this.volume = message.volume;
	}
	/*
	@Override
	public boolean isSoundStop() {

		return !(SoundHelper.canPlay(getCassette()) && this.getBlockState().get(BoomboxBlock.ON));
	}

	@Override
	public boolean isLoopPlay() {

		return this.world.isBlockPowered(this.pos);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void playSound() {
		if (world.isRemote) {
			if (SoundHelper.canPlay(getCassette()) && this.getBlockState().get(BoomboxBlock.ON)) {
				Minecraft mc = IamMusicPlayer.proxy.getMinecraft();
				int vol = this.getBlockState().get(BoomboxBlock.VOLUME);

				if (!lisnFinishedPlayers.contains(PlayerHelper.getUUID(mc.player))) {
					if (new SoundPos(this.pos).canLisn(100 / 32 * vol)) {
						if (!SoundWaitThread.posplayMap.containsKey(this.pos)) {
							SoundWaitThread.posplayMap.put(this.pos,
									new SoundPlayer(WorldPlayListSoundData.getWorldPlayListData(getCassette()),
											new SoundPos(this.pos), 3f / 32f * (float) vol, 100 / 32 * vol));
						} else {
							SoundWaitThread.posplayMap.get(this.pos).setVolume(4f / 32f * (float) vol);
							SoundWaitThread.posplayMap.get(this.pos).setMaxDistance(150 / 32 * vol);

						}
					}
				}
			}
		}
	}*/

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

		return SoundHelper.canPlay(getCassette());
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

		return flag1;
	}

}
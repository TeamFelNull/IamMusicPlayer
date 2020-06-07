package net.morimori.imp.sound;

import java.nio.file.Path;

import net.minecraft.client.Minecraft;
import net.minecraft.util.SoundCategory;
import net.morimori.imp.IamMusicPlayer;
import net.morimori.imp.file.PlayList;
import net.morimori.imp.packet.PacketHandler;
import net.morimori.imp.packet.SoundPlayMessage;
import net.morimori.imp.util.FileHelper;

public class SoundPlayer extends Thread {

	protected WorldPlayListSoundData worldplaylistsounddata;
	protected SoundPos soundpos;
	protected float volume;
	private SoundThread thread;
	private int maxdistance;
	public boolean played;
	private static Minecraft mc = Minecraft.getInstance();
	private boolean stop = false;

	public SoundPlayer(WorldPlayListSoundData wplsd, SoundPos sppos, float volume, int maxdis) {
		this.worldplaylistsounddata = wplsd;
		this.soundpos = sppos;
		this.volume = volume;
		this.maxdistance = maxdis;
	}

	public void stopPlayer() {
		if (thread != null) {
			stop = true;
			thread.stopSound();

		}
	}

	public void setMaxDistance(int max) {
		maxdistance = max;
	}

	public void setVolume(float vol) {
		volume = vol;
	}

	public void run() {
		played = true;
		IamMusicPlayer.LOGGER.info(
				"Play Start : " + worldplaylistsounddata.getFolderName() + ":" + worldplaylistsounddata.getName());

		while (true) {
			thread = new SoundThread(FileHelper.getClientCurrentServerPlaylistPath()
					.resolve(worldplaylistsounddata.getFolderName()).resolve(worldplaylistsounddata.getName())
					.toString());
			thread.start();
			while (!thread.finish) {
				float disk = (float) (maxdistance
						- soundpos.distance(mc.player.func_226277_ct_(), mc.player.func_226278_cu_() + 1,
								mc.player.func_226281_cx_()));
				float disvol = (float) (disk <= 0 ? 0 : disk / maxdistance);
				thread.setVolume(SoundWaitThread.AllSoundVolume * volume * disvol
						* mc.gameSettings.getSoundLevel(SoundCategory.MASTER));
				try {
					sleep(1);
				} catch (InterruptedException e) {

				}

				if (mc.player == null) {
					stopPlayer();
				}
			}

			if (stop || !((mc.world.getTileEntity(soundpos.getBlockPos()) instanceof ISoundPlayer)
					? ((ISoundPlayer) mc.world.getTileEntity(soundpos.getBlockPos())).isLoopPlay()
					: false)) {
				stop = false;
				break;
			}

		}
		PacketHandler.INSTANCE.sendToServer(
				new SoundPlayMessage(mc.world.getDimension().getType().getId(), soundpos.getBlockPos(), 0));

	}

	public static void play(WorldPlayListSoundData wplsd, SoundPos sppos, float volume, int maxdis) {
		Path soundath = FileHelper.getClientCurrentServerPlaylistPath().resolve(wplsd.getFolderName())
				.resolve(wplsd.getName());
		if (!soundath.toFile().exists()) {
			SoundWaitThread.addDwonloadWait(wplsd);
			SoundWaitThread.dawonloadwaitedplayers.put(new SoundPlayer(wplsd, sppos, volume, maxdis), wplsd);
		} else if (!PlayList.getClientPlaylistNBTdata(soundath, "UUID").equals(wplsd.getUUID())) {
			SoundWaitThread.addDwonloadWait(wplsd);
			SoundWaitThread.dawonloadwaitedplayers.put(new SoundPlayer(wplsd, sppos, volume, maxdis), wplsd);
		} else {
			SoundPlayer sp = new SoundPlayer(wplsd, sppos, volume, maxdis);
			sp.start();
		}

	}
}

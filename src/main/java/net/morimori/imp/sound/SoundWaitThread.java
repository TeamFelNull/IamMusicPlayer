package net.morimori.imp.sound;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.morimori.imp.IamMusicPlayer;
import net.morimori.imp.block.IMPBlocks;
import net.morimori.imp.file.ClientFileReceiver;
import net.morimori.imp.file.ClientFileSender;
import net.morimori.imp.file.FileReceiverBuffer;
import net.morimori.imp.file.PlayList;
import net.morimori.imp.packet.ClientResponseMessage;
import net.morimori.imp.packet.PacketHandler;
import net.morimori.imp.util.FileHelper;

public class SoundWaitThread extends Thread {

	public static Set<WorldPlayListSoundData> downloadwaitedfiles = new HashSet<WorldPlayListSoundData>();
	public static Map<SoundPlayer, WorldPlayListSoundData> dawonloadwaitedplayers = new HashMap<SoundPlayer, WorldPlayListSoundData>();
	private static Minecraft ms = Minecraft.getInstance();
	public static Map<BlockPos, SoundPlayer> posplayMap = new HashMap<BlockPos, SoundPlayer>();
	private static Minecraft mc = Minecraft.getInstance();
	public static boolean cheking = false;

	public void run() {
		while (true) {
			cheking = true;

			ClientFileSender.reservationDownloading();

			if (ms.player != null) {
				PacketHandler.INSTANCE.sendToServer(new ClientResponseMessage(2, 0, "null"));
			} else {
				posplayMap.clear();
				downloadwaitedfiles.clear();
				posplayMap.clear();
			}
			if (ClientFileReceiver.canReceiving && !downloadwaitedfiles.isEmpty()) {
				for (WorldPlayListSoundData df : downloadwaitedfiles) {
					PacketHandler.INSTANCE.sendToServer(new ClientResponseMessage(1, 0,
							Paths.get(df.getFolderName()).resolve(df.getName()).toString()));
					downloadwaitedfiles.remove(df);
					break;
				}
			}

			if (!posplayMap.isEmpty()) {
				try {
					for (Entry<BlockPos, SoundPlayer> maps : posplayMap.entrySet()) {
						boolean flag1 = !(mc.world.getBlockState(maps.getKey()).getBlock() == IMPBlocks.BOOMBOX
								|| mc.world.getBlockState(maps.getKey()).getBlock() == IMPBlocks.CASSETTE_DECK);

						boolean flag2 = false;

						if (mc.world.getTileEntity(maps.getKey()) instanceof ISoundPlayer) {
							flag2 = ((ISoundPlayer) mc.world.getTileEntity(maps.getKey())).isSoundStop();
						}

						if (flag1 || flag2) {
							if (SoundWaitThread.posplayMap.containsKey(maps.getKey())) {
								SoundWaitThread.posplayMap.get(maps.getKey()).stopPlayer();
								SoundWaitThread.removePosMap(maps.getKey());
							}
						}
						if (!maps.getValue().played && !dawonloadwaitedplayers.containsKey(maps.getValue())) {

							Path soundath = FileHelper.getClientCurrentServerPlaylistPath()
									.resolve(maps.getValue().worldplaylistsounddata.getFolderName())
									.resolve(maps.getValue().worldplaylistsounddata.getName());
							if (!soundath.toFile().exists()) {
								addDwonloadWait(maps.getValue().worldplaylistsounddata);
								dawonloadwaitedplayers.put(maps.getValue(),
										maps.getValue().worldplaylistsounddata);
							} else if (!PlayList.getClientPlaylistNBTdata(soundath, "UUID")
									.equals(maps.getValue().worldplaylistsounddata.getUUID())) {
								addDwonloadWait(maps.getValue().worldplaylistsounddata);
								dawonloadwaitedplayers.put(maps.getValue(),
										maps.getValue().worldplaylistsounddata);
							} else {
								try {
									maps.getValue().start();
								} catch (Exception e) {
								}

							}

						}
					}
				} catch (Exception e) {
				}
			}

			try {
				sleep(1);
			} catch (InterruptedException e) {

			}

		}

	}

	public static void removePosMap(BlockPos pos) {
		if (!posplayMap.isEmpty()) {
			try {
				posplayMap.entrySet().stream().filter(set -> {
					return set.getKey().equals(pos);
				}).forEach(maps -> {
					posplayMap.remove(maps.getKey());
				});
			} catch (Exception e) {
			}

		}

	}

	public static void finishedDownload(WorldPlayListSoundData worlddata) {
		if (!dawonloadwaitedplayers.isEmpty()) {
			try {
				for (Entry<SoundPlayer, WorldPlayListSoundData> df : dawonloadwaitedplayers.entrySet()) {
					if (df.getValue().equals(worlddata)) {

						boolean flag1 = mc.world.getBlockState(df.getKey().soundpos.getBlockPos())
								.getBlock() == IMPBlocks.BOOMBOX
								|| mc.world.getBlockState(df.getKey().soundpos.getBlockPos())
										.getBlock() == IMPBlocks.CASSETTE_DECK;
						boolean flag2 = false;
						boolean flag3 = false;

						if (mc.world.getTileEntity(df.getKey().soundpos.getBlockPos()) instanceof ISoundPlayer) {
							flag2 = !((ISoundPlayer) mc.world.getTileEntity(df.getKey().soundpos.getBlockPos()))
									.isSoundStop();
							flag3 = WorldPlayListSoundData.getWorldPlayListData(
									((ISoundPlayer) mc.world.getTileEntity(df.getKey().soundpos.getBlockPos()))
											.getPlayCassette())
									.equals(worlddata);

						}

						if (flag1 && flag2 && flag3) {
							df.getKey().start();
						}

						dawonloadwaitedplayers.remove(df.getKey());
					}
				}

			} catch (Exception e) {
			}
		}
	}

	public static void addDwonloadWait(WorldPlayListSoundData wplsd) {
		if (!isAlreadyWait(wplsd)) {
			downloadwaitedfiles.add(wplsd);
		}
	}

	public static boolean isAlreadyWait(WorldPlayListSoundData wplsd) {

		boolean flag1 = false;
		if (!downloadwaitedfiles.isEmpty()) {
			for (WorldPlayListSoundData df : downloadwaitedfiles) {
				if (df.getName().equals(wplsd.getName())
						&& df.getFolderName().equals(wplsd.getFolderName())) {
					flag1 = true;
					break;
				}
			}
		}
		boolean flag2 = false;
		if (!ClientFileReceiver.receiverBufer.isEmpty()) {
			for (Entry<Integer, FileReceiverBuffer> dd : ClientFileReceiver.receiverBufer.entrySet()) {
				Path filepath = Paths.get(dd.getValue().filepath);
				if (filepath.toFile().getName().equals(wplsd.getName())
						&& filepath.getParent().toFile().getName().equals(wplsd.getFolderName())) {
					flag2 = true;
				}
			}
		}
		return flag1 || flag2;
	}

	public static void startSoundWaiter() {
		IamMusicPlayer.LOGGER.info("Start Sound Waiter Thread");
		SoundWaitThread SWT = new SoundWaitThread();
		SWT.start();
	}

}

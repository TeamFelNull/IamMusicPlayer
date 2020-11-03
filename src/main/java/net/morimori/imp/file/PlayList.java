package net.morimori.imp.file;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.network.PacketDistributor;
import net.morimori.imp.IamMusicPlayer;
import net.morimori.imp.config.CommonConfig;
import net.morimori.imp.packet.PacketHandler;
import net.morimori.imp.packet.WorldPlaylistMessage;
import net.morimori.imp.sound.SoundData;
import net.morimori.imp.util.FileHelper;
import net.morimori.imp.util.FileLoader;
import net.morimori.imp.util.StringHelper;

public class PlayList {

	public static File[] worldPlayList;
	public static int worldPlayListSize;

	public static String[] worldPlayListNames;

	public static Map<File, String> worldPlayListUUIDs = new HashMap<File, String>();
	public static Map<File, String> worldPlayListNamesMap = new HashMap<File, String>();

	public static String FakeUUID = "11451419-1981-0364-364931-000000000000";
	public static String FakePlayerName = "UnknowOfUnknowInUnknowFromUnknow";

	public static boolean isExistsClientPlaylistFile(String name) {
		if (FileHelper.getClientPlayFileDataPath().toFile().listFiles(getSoundFileFilter()) != null) {
			for (File file : FileHelper.getClientPlayFileDataPath().toFile().listFiles(getSoundFileFilter())) {
				if (file.getName().equals(name))
					return true;
			}
		}
		return false;
	}

	public static boolean isExistsWorldPlaylistFile(File file) {

		if (file != null) {
			return isExistsWorldPlaylistFile(file.getName());
		} else {
			return false;
		}
	}

	public static boolean isExistsWorldPlaylistFile(String name) {
		if (worldPlayList != null) {
			for (File file : worldPlayList) {
				if (file.getName().equals(name))
					return true;
			}
		}
		return false;
	}

	public static void createClientPlayList() {
		FileLoader.createFolder(FileHelper.getClientPlayFileDataPath());
	}

	public static void syncdEveryoneServerWorldList(ServerPlayerEntity pl) {
		File[] filse = PlayList.getWorldSoundFileList(pl.getServer());
		String[] sts = new String[filse.length];
		int size = 0;
		for (int c = 0; c < filse.length; c++) {
			sts[c] = filse[c].getPath();
			size += filse[c].length();
		}

		String[] gps = new String[filse.length];
		for (int c = 0; c < filse.length; c++) {

			String plname = getWorldPlaylistNBTDataString(pl.getServer(), filse[c].toPath(), "PlayerName");

			gps[c] = plname != null ? plname : "null";
		}

		String[] ups = new String[filse.length];
		for (int c = 0; c < filse.length; c++) {
			String upname = getWorldPlaylistNBTDataString(pl.getServer(), filse[c].toPath(), "PlayerUUID");
			ups[c] = upname != null ? upname : "null";
		}

		PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> pl),
				new WorldPlaylistMessage(sts, gps, ups, size));
	}

	public static void syncPlayerServerWorldList(ServerPlayerEntity pl) {
		File[] filse = PlayList.getWorldSoundFileList(pl);

		if (filse != null) {

			String[] sts = new String[filse.length];
			int size = 0;
			for (int c = 0; c < filse.length; c++) {
				sts[c] = filse[c].getPath();
				size += filse[c].length();
			}

			String[] gps = new String[filse.length];

			for (int c = 0; c < filse.length; c++) {

				gps[c] = FakePlayerName;
			}

			String[] ups = new String[filse.length];
			for (int c = 0; c < filse.length; c++) {
				ups[c] = FakeUUID;
			}

			PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> pl),
					new WorldPlaylistMessage(sts, gps, ups, size));
		}
	}

	public static File[] getClientSoundFileList() {
		File[] list = FileHelper.getClientPlayFileDataPath().toFile().listFiles(getSoundFileFilter());
		return list;
	}

	public static File[] getWorldSoundFileList(PlayerEntity pl) {
		File[] list = FileHelper.getWorldPlayerPlayListDataPath(pl).toFile().listFiles(getSoundFileFilter());
		return list;
	}

	public static FileFilter getSoundFileFilter() {
		return new FileFilter() {

			@Override
			public boolean accept(File pathname) {

				boolean flag1 = !pathname.isDirectory();
				boolean flag2 = StringHelper.getExtension(pathname.getName()).equals("mp3");
				boolean flag3 = pathname.length() <= (CommonConfig.MAX_LIMIT.get() * 1024 * 1024);

				return flag1 && flag2 && flag3;
			}
		};

	}

	public static File[] getWorldSoundFileList(MinecraftServer ms) {
		File[] list = FileHelper.getWorldEveryonePlayListDataPath(ms).toFile().listFiles(getSoundFileFilter());
		return list;
	}

	public static String getClientPlaylistNBTdata(Path path, String nbtname) {
		CompoundNBT tag = FileLoader.fileNBTReader(FileHelper.getClientCurrentServerPlaylistNBTDataPath());

		if (tag == null) {
			return null;
		}

		if (!tag.contains(path.getParent().toFile().getName()))
			return null;

		CompoundNBT pltag = (CompoundNBT) tag.get(path.getParent().toFile().getName());

		if (!pltag.contains(path.toFile().getName()))
			return null;

		CompoundNBT stag = (CompoundNBT) pltag.get(path.toFile().getName());

		if (!stag.contains(nbtname))
			return null;

		return stag.getString(nbtname);

	}

	public static String getWorldPlaylistNBTDataString(MinecraftServer ms, Path path, String nbtname) {
		return getWorldPlaylistNBTDataString(ms, path.getParent().toFile().getName(), path.toFile().getName(), nbtname);
	}

	public static String getWorldPlaylistNBTDataString(MinecraftServer ms, String folname, String filename,
			String nbtname) {
		CompoundNBT tag = FileLoader.fileNBTReader(FileHelper.getWorldPlayListNBTDataPath(ms));

		if (tag == null) {
			return null;
		}

		if (!tag.contains(folname))
			return null;

		CompoundNBT pltag = (CompoundNBT) tag.get(folname);

		if (!pltag.contains(filename))
			return null;

		CompoundNBT stag = (CompoundNBT) pltag.get(filename);

		if (!stag.contains(nbtname))
			return null;

		return stag.getString(nbtname);
	}

	public static SoundData getWorldPlaylistNBTDataSoundData(MinecraftServer ms, Path path, String nbtname) {

		return getWorldPlaylistNBTDataSoundData(ms, path.getParent().toFile().getName(), path.toFile().getName(),
				nbtname);
	}

	public static SoundData getWorldPlaylistNBTDataSoundData(MinecraftServer ms, String folname, String filename,
			String nbtname) {
		CompoundNBT tag = FileLoader.fileNBTReader(FileHelper.getWorldPlayListNBTDataPath(ms));

		if (tag == null) {
			return null;
		}

		if (!tag.contains(folname))
			return null;

		CompoundNBT pltag = (CompoundNBT) tag.get(folname);

		if (!pltag.contains(filename))
			return null;

		CompoundNBT stag = (CompoundNBT) pltag.get(filename);

		if (!stag.contains(nbtname))
			return null;

		return new SoundData((CompoundNBT) stag.get(nbtname));
	}

	public static void deletePlayeyListFileNBT(MinecraftServer ms, String playlistname, String filename) {
		CompoundNBT tag = FileLoader.fileNBTReader(FileHelper.getWorldPlayListNBTDataPath(ms));

		if (tag == null) {
			return;
		}

		if (!tag.contains(playlistname))
			return;

		CompoundNBT pltag = (CompoundNBT) tag.get(playlistname);

		if (!pltag.contains(filename))
			return;

		pltag.remove(filename);

		tag.put(playlistname, pltag);

		FileLoader.fileNBTWriter(tag, FileHelper.getWorldPlayListNBTDataPath(ms));
	}

	public static void addClientPlayeyListFileNBT(String foldername,
			String filename, String uuid) {
		CompoundNBT tag = FileLoader.fileNBTReader(FileHelper.getClientCurrentServerPlaylistNBTDataPath());
		CompoundNBT fotag = tag.contains(foldername) ? (CompoundNBT) tag.get(foldername)
				: new CompoundNBT();

		CompoundNBT stag = fotag.contains(filename)
				? (CompoundNBT) fotag.get(filename)
				: new CompoundNBT();

		stag.putString("UUID", uuid);

		fotag.put(filename, stag);

		tag.put(foldername, fotag);

		FileLoader.fileNBTWriter(tag, FileHelper.getClientCurrentServerPlaylistNBTDataPath());

	}

	public static void addPlayeyListFileNBT(MinecraftServer ms, String playlistname, String filename,
			String playeruuid, SoundData sd) {
		CompoundNBT tag = FileLoader.fileNBTReader(FileHelper.getWorldPlayListNBTDataPath(ms));

		CompoundNBT pltag = tag.contains(playlistname) ? (CompoundNBT) tag.get(playlistname)
				: new CompoundNBT();

		CompoundNBT stag = pltag.contains(filename)
				? (CompoundNBT) pltag.get(filename)
				: new CompoundNBT();

		if (!stag.contains("UUID"))
			stag.putString("UUID", UUID.randomUUID().toString());

		if (!stag.contains("PlayerUUID")) {
			stag.putString("PlayerUUID", playeruuid);

		}

		if (!stag.contains("PlayerName")) {

			try {
				stag.putString("PlayerName",
						ms.getPlayerList().getPlayerByUUID(UUID.fromString(playeruuid)).getDisplayName().getString());
			} catch (Exception e) {
				stag.putString("PlayerName", playeruuid);
			}
		}
		if (!stag.contains("SoundData"))
			stag.put("SoundData", sd.writeNBT(new CompoundNBT()));

		pltag.put(filename, stag);

		tag.put(playlistname, pltag);

		FileLoader.fileNBTWriter(tag, FileHelper.getWorldPlayListNBTDataPath(ms));
	}

	public static void checkWorldPlayListNBT(MinecraftServer ms) {
		CompoundNBT tag = FileLoader.fileNBTReader(FileHelper.getWorldPlayListNBTDataPath(ms));
		for (String playlists : tag.keySet()) {
			if (!playlists.equals("Timestamp"))
				checkWorldPlayListNBT(ms, playlists);
		}

	}

	public static void deleteWorldPlayListSoundFile(MinecraftServer ms, String folder, String name) {

		FileLoader.deleteFile(FileHelper.getWorldPlayerPlayListDataPath(ms, folder).resolve(name).toFile());
		deletePlayeyListFileNBT(ms, folder, name);
	}

	public static void checkWorldPlayListNBT(MinecraftServer ms, String playlistname) {
		if (!FileHelper.getWorldPlayListDataPath(ms).resolve(playlistname).toFile().exists()) {
			CompoundNBT tag = FileLoader.fileNBTReader(FileHelper.getWorldPlayListNBTDataPath(ms));
			if (tag.contains(playlistname)) {
				tag.remove(playlistname);
				FileLoader.fileNBTWriter(tag, FileHelper.getWorldPlayListNBTDataPath(ms));
			}
			return;
		}

		CompoundNBT tag = FileLoader.fileNBTReader(FileHelper.getWorldPlayListNBTDataPath(ms));
		for (String sounds : ((CompoundNBT) tag.get(playlistname)).keySet()) {
			checkWorldPlayListNBT(ms, playlistname, sounds);
		}
	}

	public static void checkWorldPlayListNBT(MinecraftServer ms, String playlistname, String soundfilename) {
		IamMusicPlayer.LOGGER.info("Checking Sound NBT Data : " + playlistname + ":" + soundfilename);
		if (!FileHelper.getWorldPlayListDataPath(ms).resolve(playlistname).toFile().exists()) {
			CompoundNBT tag = FileLoader.fileNBTReader(FileHelper.getWorldPlayListNBTDataPath(ms));
			if (tag.contains(playlistname)) {
				tag.remove(playlistname);
				FileLoader.fileNBTWriter(tag, FileHelper.getWorldPlayListNBTDataPath(ms));
			}
			return;
		}

		if (!FileHelper.getWorldPlayListDataPath(ms).resolve(playlistname).resolve(soundfilename).toFile().exists()) {
			CompoundNBT tag = FileLoader.fileNBTReader(FileHelper.getWorldPlayListNBTDataPath(ms));
			if (tag.contains(playlistname)) {
				CompoundNBT pltag = (CompoundNBT) tag.get(playlistname);
				if (pltag.contains(soundfilename)) {
					IamMusicPlayer.LOGGER
							.error("Nonsense NBT Data Deleted : " + playlistname + ":" + soundfilename);
					pltag.remove(soundfilename);
					tag.put(playlistname, pltag);
					FileLoader.fileNBTWriter(tag, FileHelper.getWorldPlayListNBTDataPath(ms));
				}
			}
			return;
		}
	}

	public static void checkWorldPlayList(MinecraftServer ms) {
		File[] playlistfiles = FileHelper.getWorldPlayListDataPath(ms).toFile().listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		});

		if (playlistfiles == null)
			return;

		for (File playlists : playlistfiles) {
			checkWorldPlayList(ms, playlists.getName());
		}
	}

	public static void checkWorldPlayList(MinecraftServer ms, String playlistname) {

		File[] soundlist = FileHelper.getWorldPlayListDataPath(ms).resolve(playlistname).toFile()
				.listFiles(getSoundFileFilter());
		if (soundlist != null) {
			for (File soundfile : soundlist) {
				checkWorldPlayList(ms, playlistname, soundfile.getName());
			}
		}
	}

	public static void checkWorldPlayList(MinecraftServer ms, String playlistname, String soundfilename) {
		File soundfile = FileHelper.getWorldPlayListDataPath(ms).resolve(playlistname).resolve(soundfilename).toFile();

		if (soundfile != null) {
			IamMusicPlayer.LOGGER
					.info("Checking Sound Data : " + playlistname + ":" + soundfilename);
			CompoundNBT tag = FileLoader.fileNBTReader(FileHelper.getWorldPlayListNBTDataPath(ms));

			if (tag == null) {
				return;
			}

			CompoundNBT pltag = tag.contains(playlistname) ? (CompoundNBT) tag.get(playlistname)
					: new CompoundNBT();

			CompoundNBT stag = pltag.contains(soundfile.getName())
					? (CompoundNBT) pltag.get(soundfile.getName())
					: new CompoundNBT();

			if (!stag.contains("UUID"))
				stag.putString("UUID", UUID.randomUUID().toString());

			if (!stag.contains("PlayerUUID"))
				stag.putString("PlayerUUID", FakeUUID);

			if (!stag.contains("PlayerName"))
				stag.putString("PlayerName", FakePlayerName);

			if (!stag.contains("SoundData")) {
				String uuid = UUID.randomUUID().toString();
				ImagePictuers.addPictuer(uuid,
						FileHelper.getWorldPlayListDataPath(ms).resolve(playlistname).resolve(soundfilename), ms);
				stag.put("SoundData", new SoundData(
						FileHelper.getWorldPlayListDataPath(ms).resolve(playlistname).resolve(soundfilename),
						uuid).writeNBT(new CompoundNBT()));

			}

			if (stag.contains("SoundData")) {

				SoundData sd = new SoundData((CompoundNBT) stag.get("SoundData"));

				if (sd.album_image_uuid != null && !sd.album_image_uuid.equals("null")) {

					if (!FileHelper.getWorldPictuerPath(ms).resolve(sd.album_image_uuid + ".png").toFile().exists()) {
						ImagePictuers.addPictuer(sd.album_image_uuid, soundfile.toPath(), ms);

					}

				}

			}

			pltag.put(soundfile.getName(), stag);

			tag.put(playlistname, pltag);

			FileLoader.fileNBTWriter(tag, FileHelper.getWorldPlayListNBTDataPath(ms));
		}
	}

	public static void checkWorldPlayLists(MinecraftServer ms, boolean thread) {
		if (!thread) {
			checkWorldPlayList(ms);
			checkWorldPlayListNBT(ms);
		} else {
			CheckTheread CT = new CheckTheread(ms);
			CT.start();
		}
	}
}

class CheckTheread extends Thread {
	private MinecraftServer ms;

	public CheckTheread(MinecraftServer ms) {
		this.ms = ms;
	}

	public void run() {
		PlayList.checkWorldPlayList(ms);
		PlayList.checkWorldPlayListNBT(ms);
	}
}

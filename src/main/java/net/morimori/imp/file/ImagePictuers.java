package net.morimori.imp.file;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import net.minecraft.server.MinecraftServer;
import net.morimori.imp.util.FileHelper;
import net.morimori.imp.util.FileLoader;
import net.morimori.imp.util.PictuerUtil;

public class ImagePictuers {
	public static void addPictuer(String uuid, Path sopath, MinecraftServer ms) {
		Mp3File mfile;
		try {
			mfile = new Mp3File(sopath.toString());
			ID3v2 tag = mfile.getId3v2Tag();

			if (tag.getAlbumImage() != null) {

				byte[] motobytes = tag.getAlbumImage();

				BufferedImage motos = PictuerUtil.getImage(motobytes, null);
				int size = 128;
				float w = motos.getWidth();
				float h = motos.getHeight();

				int aw = 10;
				int ah = 10;

				if (w == h) {
					aw = size;
					ah = size;
				} else if (w > h) {
					aw = size;
					ah = (int) ((float) size * (h / w));
				} else if (w > h) {
					aw = (int) ((float) size * (w / h));
					ah = size;
				}

				addPictuer(uuid, PictuerUtil.setSize(motobytes, aw, ah), ms);
			}
		} catch (UnsupportedTagException | InvalidDataException | IOException e) {

		}

	}

	public static void addPictuer(String uuid, byte[] pictuer, MinecraftServer ms) {
		FileLoader.fileBytesWriter(pictuer, FileHelper.getWorldPictuerPath(ms).resolve(uuid + ".png"));
	}

	public static byte[] readPictuer(String uuid, MinecraftServer ms) {
		return FileLoader.fileBytesReader(FileHelper.getWorldPictuerPath(ms).resolve(uuid + ".png"));
	}
}

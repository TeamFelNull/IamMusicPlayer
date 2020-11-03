package net.morimori.imp.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class PictuerUtil {
	private static Map<String, BufferedImage> byteloactions = new HashMap<String, BufferedImage>();

	public static BufferedImage getImage(String uuid) {
		File picfile = FileHelper.getClientPictuerCashPath().resolve(uuid + ".png").toFile();
		if (picfile.exists() && picfile != null) {

			if (byteloactions.containsKey(uuid)) {
				return byteloactions.get(uuid);
			}

			return getImage(FileLoader.fileBytesReader(picfile.toPath()), uuid);
		}

		return null;
	}

	public static BufferedImage getImage(byte[] bytes, String uuid) {
		if (uuid != null) {
			if (byteloactions.containsKey(uuid)) {
				return byteloactions.get(uuid);
			}
		}
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		try {
			BufferedImage bi = ImageIO.read(bis);
			if (uuid != null) {
				byteloactions.put(uuid, bi);
			}

			return bi;
		} catch (IOException e) {
			return null;
		}
	}

	public static byte[] setSize(byte[] bytes, int width, int height) {

		BufferedImage image = getImage(bytes, null);

		BufferedImage outimage = new BufferedImage(width, height, image.getType());

		outimage.createGraphics().drawImage(image.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING), 0, 0,
				width, height, null);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		BufferedOutputStream bos = new BufferedOutputStream(baos);

		try {
			ImageIO.write(outimage, "png", bos);
		} catch (IOException e) {

		}

		return baos.toByteArray();
	}
}

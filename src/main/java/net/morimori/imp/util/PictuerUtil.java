package net.morimori.imp.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PictuerUtil {

	public static BufferedImage getImage(byte[] bytes) {

		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		try {
			BufferedImage bi = ImageIO.read(bis);
			return bi;
		} catch (IOException e) {
			return null;
		}
	}

	public static byte[] setSize(byte[] bytes, int width, int height) {

		BufferedImage image = getImage(bytes);

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

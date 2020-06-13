package net.morimori.imp.util;

import java.sql.Timestamp;
import java.util.regex.Pattern;

import net.minecraft.client.Minecraft;

public class StringHelper {

	public static String slipString(String name, boolean left, int slipc) {
		String st = name;
		for (int c = 0; c < slipc; c++) {
			st = slipString(st, left);
		}
		return st;
	}

	public static String getExtension(String name) {

		String[] filenames = name.split(Pattern.quote("."));
		if (filenames.length != 1) {
			return filenames[filenames.length - 1];
		}

		return "";
	}

	public static String deleteExtension(String name) {

		String[] filenames = name.split(Pattern.quote("."));

		if (filenames.length == 1)
			return name;

		String oname = name.substring(0, name.length() - filenames[filenames.length - 1].length() - 1);

		return oname;
	}

	public static String slipString(String name, boolean left) {

		String outst = "";

		char[] oc = name.toCharArray();
		if (left) {
			for (int c = 0; c < oc.length; c++) {

				if (c != oc.length - 1)
					outst += oc[c + 1];
				else
					outst += oc[0];
			}
		} else {
			for (int c = -1; c < oc.length; c++) {

				if (c == -1) {
					outst += oc[oc.length - 1];
				} else {
					if (c != oc.length - 1)
						outst += oc[c];
				}
			}
		}
		return outst;
	}

	public static String characterLimit(Minecraft mc, String st, int leth) {
		return characterLimit(mc, st, leth, true);

	}

	public static String characterLimit(Minecraft mc, String st, int leth, boolean isdot) {

		if (mc.fontRenderer.getStringWidth(st) <= leth)
			return st;

		int stle = leth - (isdot ? mc.fontRenderer.getStringWidth("...") : 0);

		String sto = "";

		char[] sc = st.toCharArray();

		for (int c = 0; c < sc.length; c++) {
			String stm = sto;
			sto += sc[c];
			if (mc.fontRenderer.getStringWidth(sto) >= stle) {

				return isdot ? stm + "..." : stm;
			}
		}

		return sto;
	}

	public static String fileCapacityNotation(long bytesize) {

		if (bytesize < 1024) {
			return bytesize + " Byte";
		} else if (bytesize < 1024 * 1024) {
			return deleteLastZero(IMPMath.roundDown(bytesize / 1024, 2)) + " KB";
		} else {
			return deleteLastZero(IMPMath.roundDown((float) bytesize / 1024 / 1024, 2)) + " MB";
		}
	}

	public static String fileCapacityNotation(int bytesize) {

		if (bytesize < 1024) {
			return bytesize + " Byte";
		} else if (bytesize < 1024 * 1024) {
			return deleteLastZero(IMPMath.roundDown(bytesize / 1024, 2)) + " KB";
		} else {
			return deleteLastZero(IMPMath.roundDown((float) bytesize / 1024 / 1024, 2)) + " MB";
		}
	}

	public static String deleteLastZero(float f) {

		if (String.valueOf(f).substring(String.valueOf(f).length() - 1).equals("0")) {

			if (String.valueOf(f).substring(String.valueOf(f).length() - 2, String.valueOf(f).length() - 1)
					.equals(".")) {
				return String.valueOf(f).substring(0, String.valueOf(f).length() - 2);
			}

			return String.valueOf(f).substring(0, String.valueOf(f).length() - 1);
		}

		return String.valueOf(f);

	}

	public static String getTimeStamp() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		return timestamp.toString();
	}

	public static String getPercentage(int all, int co) {

		return Math.round(((float) co / (float) all) * 100) + " %";
	}

	public static String getTimeDisplay(long milisec) {

		long byou = milisec / 1000;

		if (byou < 60) {
			return String.valueOf(byou);
		}

		long hun = byou / 60;
		long amabyou = byou - hun * 60;
		if (hun < 60) {

			if (amabyou == 0) {
				return String.valueOf(hun);
			}

			return hun + ":" + amabyou;
		}

		long zikan = hun / 60;
		long amahun = hun - zikan * 60;

		if (amabyou == 0 && amahun == 0) {
			return String.valueOf(zikan);
		}

		if (amabyou == 0) {
			return zikan + ":" + amahun;
		}

		if (amahun == 0) {
			return zikan + ":" + amabyou;
		}

		return zikan + ":" + amahun + ":" + amabyou;

	}
}

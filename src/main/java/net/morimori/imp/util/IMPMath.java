package net.morimori.imp.util;

public class IMPMath {
	public static float roundDown(float num, int dwon) {

		for (int c = 0; c < dwon + 2; c++) {
			if (String.valueOf(num).toCharArray().length >= dwon + 2 - c) {
				return Float.valueOf(String.valueOf(num).substring(0, dwon + 2 - c));
			}
		}
		return 0;
	}

	public static double positiveDouble(double n) {
		double r = n;
		if (r <= -1)
			r *= -1;
		return r;
	}

	public static int stringDecimalConverter(String name) {

		int k = 0;
		int m = 1;
		for (char c : name.toCharArray()) {
			k = k + ((int) c * m);
			m = m + 3;
		}
		return k;
	}
}

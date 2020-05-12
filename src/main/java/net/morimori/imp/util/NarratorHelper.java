package net.morimori.imp.util;

import net.minecraft.client.gui.chat.NarratorChatListener;

public class NarratorHelper {
	public static void say(String word) {

		NarratorChatListener.INSTANCE.func_216864_a(word);
	}
}

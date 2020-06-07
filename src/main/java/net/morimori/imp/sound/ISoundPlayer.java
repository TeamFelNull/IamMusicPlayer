package net.morimori.imp.sound;

import net.minecraft.item.ItemStack;

public interface ISoundPlayer {

	boolean isSoundStop();

	boolean isLoopPlay();

	ItemStack getPlayCassette();

	void playSound();
}

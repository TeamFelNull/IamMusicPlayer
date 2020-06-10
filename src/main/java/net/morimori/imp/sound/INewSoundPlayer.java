package net.morimori.imp.sound;

public interface INewSoundPlayer {

	PlayData getSound();

	SoundPos getSoundPos();

	boolean canPlayed();

	boolean isPlayed();

	void setPlayed(boolean play);

	float getVolume();

	void setVolume(float volume);

	long getPosition();

	void setPosition(long position);

	long getLastTime();

	void setLastTime(long position);

	boolean canExistence();

	boolean isLoop();

}

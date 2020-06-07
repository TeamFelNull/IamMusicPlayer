package net.morimori.imp.sound;

public interface INewSoundPlayer {

	WorldPlayListSoundData getSound();

	SoundPos getSoundPos();

	boolean canPlayed();

	boolean isPlayed();

	void setPlayed(boolean play);

	float getVolume();

	void setVolume(float volume);

	long getPosition();

	void setPosition(long position);

}

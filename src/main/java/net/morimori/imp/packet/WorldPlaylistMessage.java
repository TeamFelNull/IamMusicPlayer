package net.morimori.imp.packet;

import net.minecraft.network.PacketBuffer;

public class WorldPlaylistMessage {

	public String[] playlist;
	public String[] PlayerNames;
	public String[] PlayerUUIDs;
	public int playlistsize;

	public WorldPlaylistMessage(String[] playlist, String[] plnames, String[] uuid, int size) {
		this.playlist = playlist;
		this.PlayerNames = plnames;
		this.PlayerUUIDs = uuid;
		this.playlistsize = size;

	}

	public static WorldPlaylistMessage decodeMessege(PacketBuffer buffer) {
		int pcont = buffer.readInt();
		String[] pllist = new String[pcont];
		for (int c = 0; c < pcont; c++) {
			pllist[c] = buffer.readString(32767);
		}
		int gcont = buffer.readInt();
		String[] gllist = new String[gcont];
		for (int c = 0; c < gcont; c++) {
			gllist[c] = buffer.readString(32767);
		}
		int ucont = buffer.readInt();
		String[] ullist = new String[ucont];
		for (int c = 0; c < ucont; c++) {
			ullist[c] = buffer.readString(32767);
		}
		return new WorldPlaylistMessage(pllist, gllist, ullist, buffer.readInt());
	}

	public static void encodeMessege(WorldPlaylistMessage messegeIn, PacketBuffer buffer) {

		int pcont = messegeIn.playlist.length;
		buffer.writeInt(pcont);
		for (int c = 0; c < pcont; c++) {
			buffer.writeString(messegeIn.playlist[c]);
		}

		int gcont = messegeIn.playlist.length;
		buffer.writeInt(gcont);
		for (int c = 0; c < gcont; c++) {
			buffer.writeString(messegeIn.PlayerNames[c]);
		}

		int ucont = messegeIn.playlist.length;
		buffer.writeInt(ucont);
		for (int c = 0; c < ucont; c++) {
			buffer.writeString(messegeIn.PlayerUUIDs[c]);
		}

		buffer.writeInt(messegeIn.playlistsize);
	}
}

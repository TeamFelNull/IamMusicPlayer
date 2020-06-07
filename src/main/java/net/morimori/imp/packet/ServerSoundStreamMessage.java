package net.morimori.imp.packet;

import net.minecraft.network.PacketBuffer;
import net.morimori.imp.sound.WorldSoundKey;

public class ServerSoundStreamMessage {
	public String key;
	public WorldSoundKey wsk;
	public int offsetpos;
	public boolean stop;

	public ServerSoundStreamMessage(String key, WorldSoundKey wsk, int offsetpos, boolean stop) {
		this.key = key;
		this.wsk = wsk;
		this.offsetpos = offsetpos;
		this.stop = stop;
	}

	public static ServerSoundStreamMessage decodeMessege(PacketBuffer buffer) {
		return new ServerSoundStreamMessage(buffer.readString(32767),
				new WorldSoundKey(buffer.readString(32767), buffer.readString(32767), buffer.readString(32767)),
				buffer.readInt(), buffer.readBoolean());
	}

	public static void encodeMessege(ServerSoundStreamMessage messegeIn, PacketBuffer buffer) {
		buffer.writeString(messegeIn.key);
		buffer.writeString(messegeIn.wsk.getFolder());
		buffer.writeString(messegeIn.wsk.getName());
		buffer.writeString(messegeIn.wsk.getUUID());
		buffer.writeInt(messegeIn.offsetpos);
		buffer.writeBoolean(messegeIn.stop);

	}
}

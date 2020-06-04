package net.morimori.imp.packet;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.morimori.imp.IamMusicPlayer;
import net.morimori.imp.client.handler.BoomboxSyncMessageHandler;
import net.morimori.imp.client.handler.CassetteDeckSyncMessageHandler;
import net.morimori.imp.client.handler.CassetteStoringSyncMessageHandler;
import net.morimori.imp.client.handler.ClientStopRequestMessageHandler;
import net.morimori.imp.client.handler.ServerClientDataSyncMessageHandler;
import net.morimori.imp.client.handler.ServerResponseMessageHandler;
import net.morimori.imp.client.handler.ServerSendSoundFileMessageHandler;
import net.morimori.imp.client.handler.SoundFileUploaderSyncMessageHandler;
import net.morimori.imp.client.handler.WorldPlaylistMessageHandler;
import net.morimori.imp.handler.CassetteDeckMessageHandler;
import net.morimori.imp.handler.ClientResponseMessageHandler;
import net.morimori.imp.handler.ClientSendSoundFileMessageHandler;
import net.morimori.imp.handler.SoundFileUploaderMessageHandler;
import net.morimori.imp.handler.SoundPlayMessageHandler;

public class PacketHandler {
	public static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder
			.named(new ResourceLocation(IamMusicPlayer.MODID, "imp_channel"))
			.clientAcceptedVersions(a -> true).serverAcceptedVersions(a -> true)
			.networkProtocolVersion(() -> PROTOCOL_VERSION)
			.simpleChannel();

	private static int integer = -1;

	private static int next() {
		integer++;
		return integer;
	}

	public static void init() {

		INSTANCE.registerMessage(next(), BoomboxSyncMessage.class, BoomboxSyncMessage::encodeMessege,
				BoomboxSyncMessage::decodeMessege, BoomboxSyncMessageHandler::reversiveMessage);

		INSTANCE.registerMessage(next(), ClientSendSoundFileMessage.class, ClientSendSoundFileMessage::encodeMessege,
				ClientSendSoundFileMessage::decodeMessege, ClientSendSoundFileMessageHandler::reversiveMessage);

		INSTANCE.registerMessage(next(), SoundFileUploaderSyncMessage.class,
				SoundFileUploaderSyncMessage::encodeMessege,
				SoundFileUploaderSyncMessage::decodeMessege, SoundFileUploaderSyncMessageHandler::reversiveMessage);

		INSTANCE.registerMessage(next(), SoundFileUploaderMessage.class, SoundFileUploaderMessage::encodeMessege,
				SoundFileUploaderMessage::decodeMessege, SoundFileUploaderMessageHandler::reversiveMessage);

		INSTANCE.registerMessage(next(), WorldPlaylistMessage.class, WorldPlaylistMessage::encodeMessege,
				WorldPlaylistMessage::decodeMessege, WorldPlaylistMessageHandler::reversiveMessage);

		INSTANCE.registerMessage(next(), ServerResponseMessage.class, ServerResponseMessage::encodeMessege,
				ServerResponseMessage::decodeMessege, ServerResponseMessageHandler::reversiveMessage);

		INSTANCE.registerMessage(next(), ServerSendSoundFileMessage.class, ServerSendSoundFileMessage::encodeMessege,
				ServerSendSoundFileMessage::decodeMessege, ServerSendSoundFileMessageHandler::reversiveMessage);

		INSTANCE.registerMessage(next(), ClientResponseMessage.class, ClientResponseMessage::encodeMessege,
				ClientResponseMessage::decodeMessege, ClientResponseMessageHandler::reversiveMessage);

		INSTANCE.registerMessage(next(), ClientStopRequestMessage.class, ClientStopRequestMessage::encodeMessege,
				ClientStopRequestMessage::decodeMessege, ClientStopRequestMessageHandler::reversiveMessage);

		INSTANCE.registerMessage(next(), CassetteDeckSyncMessage.class, CassetteDeckSyncMessage::encodeMessege,
				CassetteDeckSyncMessage::decodeMessege, CassetteDeckSyncMessageHandler::reversiveMessage);

		INSTANCE.registerMessage(next(), CassetteDeckMessage.class, CassetteDeckMessage::encodeMessege,
				CassetteDeckMessage::decodeMessege, CassetteDeckMessageHandler::reversiveMessage);

		INSTANCE.registerMessage(next(), SoundPlayMessage.class, SoundPlayMessage::encodeMessege,
				SoundPlayMessage::decodeMessege, SoundPlayMessageHandler::reversiveMessage);

		INSTANCE.registerMessage(next(), CassetteStoringSyncMessage.class, CassetteStoringSyncMessage::encodeMessege,
				CassetteStoringSyncMessage::decodeMessege, CassetteStoringSyncMessageHandler::reversiveMessage);

		INSTANCE.registerMessage(next(), ServerClientDataSyncMessage.class, ServerClientDataSyncMessage::encodeMessege,
				ServerClientDataSyncMessage::decodeMessege, ServerClientDataSyncMessageHandler::reversiveMessage);

	}
}

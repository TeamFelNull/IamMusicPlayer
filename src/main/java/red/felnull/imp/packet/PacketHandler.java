package red.felnull.imp.packet;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.client.file.ClientSoundFileSender;
import red.felnull.imp.client.handler.*;
import red.felnull.imp.handler.*;

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

        INSTANCE.registerMessage(next(), SoundFileUploaderSyncMessage.class,
                SoundFileUploaderSyncMessage::encodeMessege, SoundFileUploaderSyncMessage::decodeMessege,
                SoundFileUploaderSyncMessageHandler::reversiveMessage);

        INSTANCE.registerMessage(next(), CassetteDeckSyncMessage.class, CassetteDeckSyncMessage::encodeMessege,
                CassetteDeckSyncMessage::decodeMessege, CassetteDeckSyncMessageHandler::reversiveMessage);

        INSTANCE.registerMessage(next(), CassetteStoringSyncMessage.class, CassetteStoringSyncMessage::encodeMessege,
                CassetteStoringSyncMessage::decodeMessege, CassetteStoringSyncMessageHandler::reversiveMessage);

        INSTANCE.registerMessage(next(), ClientSendSoundFileMessage.class, ClientSendSoundFileMessage::encodeMessege,
                ClientSendSoundFileMessage::decodeMessege, ClientSendSoundFileMessageHandler::reversiveMessage);

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

        INSTANCE.registerMessage(next(), CassetteDeckMessage.class, CassetteDeckMessage::encodeMessege,
                CassetteDeckMessage::decodeMessege, CassetteDeckMessageHandler::reversiveMessage);

        INSTANCE.registerMessage(next(), ServerClientDataSyncMessage.class, ServerClientDataSyncMessage::encodeMessege,
                ServerClientDataSyncMessage::decodeMessege, ServerClientDataSyncMessageHandler::reversiveMessage);

        INSTANCE.registerMessage(next(), ServerSoundStreamMessage.class, ServerSoundStreamMessage::encodeMessege,
                ServerSoundStreamMessage::decodeMessege, ServerSoundStreamMessageHandler::reversiveMessage);

        INSTANCE.registerMessage(next(), ClientSoundStreamMessage.class, ClientSoundStreamMessage::encodeMessege,
                ClientSoundStreamMessage::decodeMessege, ClientSoundStreamMessageHandler::reversiveMessage);

    }
}

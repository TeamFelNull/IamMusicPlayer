package red.felnull.imp.packet;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.handler.PlayListCreateRequestMessageHandler;

public class PacketHandler {
    public static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(IamMusicPlayer.MODID, IamMusicPlayer.MODID + "_channel")).clientAcceptedVersions(a -> true).serverAcceptedVersions(a -> true).networkProtocolVersion(() -> PROTOCOL_VERSION).simpleChannel();
    private static int integer = -1;

    private static int next() {
        integer++;
        return integer;
    }

    public static void init() {
        //プレイリスト作成リクエスト
        INSTANCE.registerMessage(next(), PlayListCreateRequestMessage.class, PlayListCreateRequestMessage::encodeMessege, PlayListCreateRequestMessage::decodeMessege, PlayListCreateRequestMessageHandler::reversiveMessage);
    }
}

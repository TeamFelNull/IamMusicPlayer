package red.felnull.imp.packet;

import red.felnull.imp.client.handler.MusicClientInstructionMessageHandler;
import red.felnull.imp.client.handler.SyncResourceResponseMessageHandler;
import red.felnull.imp.handler.MusicResponseMessageHandler;
import red.felnull.imp.handler.PlayListCreateMessageHandler;
import red.felnull.imp.handler.PlayMusicCreateMessageHandler;
import red.felnull.imp.handler.SyncResourceRequestMessageHandler;
import red.felnull.otyacraftengine.util.IKSGPacketUtil;

public class IMPPackets {
    public static void init() {
        IKSGPacketUtil.registerSendToClientPacket(MusicClientInstructionMessage.class, new MusicClientInstructionMessageHandler());
        IKSGPacketUtil.registerSendToClientPacket(SyncResourceResponseMessage.class, new SyncResourceResponseMessageHandler());

        IKSGPacketUtil.registerSendToServerPacket(MusicResponseMessage.class, new MusicResponseMessageHandler());
        IKSGPacketUtil.registerSendToServerPacket(PlayListCreateMessage.class, new PlayListCreateMessageHandler());
        IKSGPacketUtil.registerSendToServerPacket(PlayMusicCreateMessage.class, new PlayMusicCreateMessageHandler());
        IKSGPacketUtil.registerSendToServerPacket(SyncResourceRequestMessage.class, new SyncResourceRequestMessageHandler());
    }
}

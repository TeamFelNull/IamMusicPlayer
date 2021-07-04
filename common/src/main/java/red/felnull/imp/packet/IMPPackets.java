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
        IKSGPacketUtil.registerSendToClientPacket(MusicClientInstructionMessage.class, MusicClientInstructionMessage::new, new MusicClientInstructionMessageHandler());
        IKSGPacketUtil.registerSendToClientPacket(SyncResourceResponseMessage.class, SyncResourceResponseMessage::new, new SyncResourceResponseMessageHandler());

        IKSGPacketUtil.registerSendToServerPacket(MusicResponseMessage.class, MusicResponseMessage::new, new MusicResponseMessageHandler());
        IKSGPacketUtil.registerSendToServerPacket(PlayListCreateMessage.class, PlayListCreateMessage::new, new PlayListCreateMessageHandler());
        IKSGPacketUtil.registerSendToServerPacket(PlayMusicCreateMessage.class, PlayMusicCreateMessage::new, new PlayMusicCreateMessageHandler());
        IKSGPacketUtil.registerSendToServerPacket(SyncResourceRequestMessage.class, SyncResourceRequestMessage::new, new SyncResourceRequestMessageHandler());
    }
}

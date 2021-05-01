package red.felnull.imp.packet;

import red.felnull.imp.client.handler.MusicClientInstructionMessageHandler;
import red.felnull.imp.handler.MusicResponseMessageHandler;
import red.felnull.otyacraftengine.util.IKSGPacketUtil;

public class IMPPackets {
    public static void init() {
        IKSGPacketUtil.registerSendToClientPacket(MusicClientInstructionMessage.class, new MusicClientInstructionMessageHandler());

        IKSGPacketUtil.registerSendToServerPacket(MusicResponseMessage.class, new MusicResponseMessageHandler());
    }
}

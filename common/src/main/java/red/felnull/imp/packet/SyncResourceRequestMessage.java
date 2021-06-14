package red.felnull.imp.packet;

import net.minecraft.network.FriendlyByteBuf;
import red.felnull.otyacraftengine.packet.IPacketMessage;

public class SyncResourceRequestMessage implements IPacketMessage {
    public SyncType type;

    public SyncResourceRequestMessage() {

    }

    public SyncResourceRequestMessage(SyncType type) {
        this.type = type;
    }

    @Override
    public void decode(FriendlyByteBuf byteBuf) {
        this.type = SyncType.getTypeByName(byteBuf.readUtf(32767));
    }

    @Override
    public void encode(FriendlyByteBuf byteBuf) {
        byteBuf.writeUtf(this.type.getSerializedName());
    }

}

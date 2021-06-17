package red.felnull.imp.packet;

import net.minecraft.network.FriendlyByteBuf;
import red.felnull.otyacraftengine.packet.IPacketMessage;
import red.felnull.otyacraftengine.util.IKSGPlayerUtil;

import java.util.UUID;

public class SyncResourceRequestMessage implements IPacketMessage {
    public SyncType type;
    public UUID uuid;

    public SyncResourceRequestMessage() {

    }

    public SyncResourceRequestMessage(SyncType type, UUID uuid) {
        this.type = type;
        this.uuid = uuid;
    }

    public SyncResourceRequestMessage(SyncType type) {
        this(type, IKSGPlayerUtil.getFakeUUID());
    }

    @Override
    public void decode(FriendlyByteBuf byteBuf) {
        this.type = SyncType.getTypeByName(byteBuf.readUtf(32767));
        this.uuid = byteBuf.readUUID();
    }

    @Override
    public void encode(FriendlyByteBuf byteBuf) {
        byteBuf.writeUtf(this.type.getSerializedName());
        byteBuf.writeUUID(this.uuid);
    }

}

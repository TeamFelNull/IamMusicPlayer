package red.felnull.imp.packet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import red.felnull.otyacraftengine.packet.IPacketMessage;
import red.felnull.otyacraftengine.util.IKSGPlayerUtil;

import java.util.UUID;

public class SyncResourceResponseMessage implements IPacketMessage {
    public SyncType type;
    public CompoundTag data;
    public UUID uuid;

    public SyncResourceResponseMessage() {

    }

    public SyncResourceResponseMessage(SyncType type, CompoundTag data) {
        this(type, data, IKSGPlayerUtil.getFakeUUID());
    }

    public SyncResourceResponseMessage(SyncType type, CompoundTag data, UUID uuid) {
        this.type = type;
        this.data = data;
        this.uuid = uuid;
    }

    @Override
    public void decode(FriendlyByteBuf byteBuf) {
        this.type = SyncType.getTypeByName(byteBuf.readUtf(32767));
        this.data = byteBuf.readNbt();
        this.uuid = byteBuf.readUUID();
    }

    @Override
    public void encode(FriendlyByteBuf byteBuf) {
        byteBuf.writeUtf(this.type.getSerializedName());
        byteBuf.writeNbt(data);
        byteBuf.writeUUID(this.uuid);
    }
}

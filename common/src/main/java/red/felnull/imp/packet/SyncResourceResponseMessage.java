package red.felnull.imp.packet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import red.felnull.otyacraftengine.packet.IPacketMessage;

public class SyncResourceResponseMessage implements IPacketMessage {
    public SyncType type;
    public CompoundTag data;

    public SyncResourceResponseMessage() {

    }

    public SyncResourceResponseMessage(SyncType type, CompoundTag data) {
        this.type = type;
        this.data = data;
    }

    @Override
    public void decode(FriendlyByteBuf byteBuf) {
        this.type = SyncType.getTypeByName(byteBuf.readUtf(32767));
        this.data = byteBuf.readNbt();
    }

    @Override
    public void encode(FriendlyByteBuf byteBuf) {
        byteBuf.writeUtf(this.type.getSerializedName());
        byteBuf.writeNbt(data);
    }
}

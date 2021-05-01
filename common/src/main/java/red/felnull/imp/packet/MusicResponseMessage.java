package red.felnull.imp.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.StringRepresentable;
import red.felnull.otyacraftengine.packet.IPacketMessage;

import java.util.UUID;

public class MusicResponseMessage implements IPacketMessage {
    public Type type;
    public UUID uuid;
    public long time;

    public MusicResponseMessage() {

    }

    public MusicResponseMessage(Type type, UUID uuid, long time) {
        this.type = type;
        this.uuid = uuid;
        this.time = time;
    }

    @Override
    public void decode(FriendlyByteBuf byteBuf) {
        this.type = Type.getTypeByName(byteBuf.readUtf(32767));
        this.uuid = byteBuf.readUUID();
        this.time = byteBuf.readLong();
    }

    @Override
    public void encode(FriendlyByteBuf byteBuf) {
        byteBuf.writeUtf(this.type.getSerializedName());
        byteBuf.writeUUID(this.uuid);
        byteBuf.writeLong(this.time);
    }

    public static enum Type implements StringRepresentable {
        NONE("none"),
        READY_COMPLETE("ready_complete"),
        READY_FAILURE("ready_failure");
        private final String name;

        Type(String name) {
            this.name = name;
        }

        public static Type getTypeByName(String name) {
            for (Type sc : values()) {
                if (sc.getSerializedName().equals(name))
                    return sc;
            }
            return NONE;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}

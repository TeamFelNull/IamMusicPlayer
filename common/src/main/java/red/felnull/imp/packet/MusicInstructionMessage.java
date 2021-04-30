package red.felnull.imp.packet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.StringRepresentable;
import red.felnull.imp.music.resource.MusicLocation;
import red.felnull.otyacraftengine.packet.IPacketMessage;

import java.util.UUID;

public class MusicInstructionMessage implements IPacketMessage {
    public Type type;
    public UUID uuid;
    public long time;
    public MusicLocation location;

    public MusicInstructionMessage() {

    }

    public MusicInstructionMessage(Type type, UUID uuid, long time, MusicLocation location) {
        this.type = type;
        this.uuid = uuid;
        this.time = time;
        this.location = location;
    }

    @Override
    public void decode(FriendlyByteBuf byteBuf) {
        this.type = Type.getTypeByName(byteBuf.readUtf(32767));
        this.uuid = byteBuf.readUUID();
        this.time = byteBuf.readLong();
        this.location = new MusicLocation(byteBuf.readAnySizeNbt());
    }

    @Override
    public void encode(FriendlyByteBuf byteBuf) {
        byteBuf.writeUtf(this.type.getSerializedName());
        byteBuf.writeUUID(this.uuid);
        byteBuf.writeLong(this.time);
        byteBuf.writeNbt(this.location.save(new CompoundTag()));
    }

    public static enum Type implements StringRepresentable {
        NONE("none"),
        READY("ready"),
        PLAY("play");
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

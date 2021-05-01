package red.felnull.imp.packet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.StringRepresentable;
import red.felnull.imp.music.info.MusicPlayInfo;
import red.felnull.imp.music.resource.MusicLocation;
import red.felnull.otyacraftengine.packet.IPacketMessage;

import java.util.UUID;

public class MusicClientInstructionMessage implements IPacketMessage {
    public Type type;
    public UUID uuid;
    public long time;
    public MusicLocation location;
    public MusicPlayInfo info;

    public MusicClientInstructionMessage() {

    }

    public MusicClientInstructionMessage(Type type, UUID uuid, long time, MusicPlayInfo info) {
        this(type, uuid, time, MusicLocation.EMPTY, info);
    }

    public MusicClientInstructionMessage(Type type, UUID uuid, long time, MusicLocation location) {
        this(type, uuid, time, location, MusicPlayInfo.EMPTY);
    }

    public MusicClientInstructionMessage(Type type, UUID uuid, long time, MusicLocation location, MusicPlayInfo info) {
        this.type = type;
        this.uuid = uuid;
        this.time = time;
        this.location = location;
        this.info = info;
    }

    @Override
    public void decode(FriendlyByteBuf byteBuf) {
        this.type = Type.getTypeByName(byteBuf.readUtf(32767));
        this.uuid = byteBuf.readUUID();
        this.time = byteBuf.readLong();
        this.location = new MusicLocation(byteBuf.readAnySizeNbt());
        this.info = new MusicPlayInfo(byteBuf.readAnySizeNbt());
    }

    @Override
    public void encode(FriendlyByteBuf byteBuf) {
        byteBuf.writeUtf(this.type.getSerializedName());
        byteBuf.writeUUID(this.uuid);
        byteBuf.writeLong(this.time);
        byteBuf.writeNbt(this.location.save(new CompoundTag()));
        byteBuf.writeNbt(this.info.save(new CompoundTag()));
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

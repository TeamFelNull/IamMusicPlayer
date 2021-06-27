package red.felnull.imp.packet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import red.felnull.imp.data.resource.ImageInfo;
import red.felnull.imp.music.resource.MusicSource;
import red.felnull.otyacraftengine.packet.IPacketMessage;

import java.util.UUID;

public class PlayMusicCreateMessage implements IPacketMessage {
    public String name;
    public MusicSource source;
    public ImageInfo image;
    public UUID playListID;

    public PlayMusicCreateMessage() {

    }

    public PlayMusicCreateMessage(String name, MusicSource source, ImageInfo image, UUID playListID) {
        this.name = name;
        this.source = source;
        this.image = image;
        this.playListID = playListID;
    }

    @Override
    public void decode(FriendlyByteBuf buf) {
        this.name = buf.readUtf(32767);
        this.source = new MusicSource(buf.readAnySizeNbt());
        this.image = new ImageInfo(buf.readAnySizeNbt());
        this.playListID = buf.readUUID();
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(name, 32767);
        buf.writeNbt(source.save(new CompoundTag()));
        buf.writeNbt(image.save(new CompoundTag()));
        buf.writeUUID(playListID);
    }
}

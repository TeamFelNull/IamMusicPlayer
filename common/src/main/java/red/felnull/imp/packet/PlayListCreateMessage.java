package red.felnull.imp.packet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import red.felnull.imp.data.resource.AdministratorInformation;
import red.felnull.imp.data.resource.ImageInfo;
import red.felnull.imp.util.NbtUtils;
import red.felnull.otyacraftengine.packet.IPacketMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayListCreateMessage implements IPacketMessage {
    public String name;
    public boolean publiced;
    public ImageInfo image;
    public Map<UUID, AdministratorInformation.AuthorityType> adminData = new HashMap<>();

    public PlayListCreateMessage() {

    }

    public PlayListCreateMessage(String name, boolean publiced, ImageInfo image, Map<UUID, AdministratorInformation.AuthorityType> adminData) {
        this.name = name;
        this.publiced = publiced;
        this.image = image;
        this.adminData = adminData;
    }

    @Override
    public void decode(FriendlyByteBuf buf) {
        this.name = buf.readUtf(32767);
        this.publiced = buf.readBoolean();
        this.image = new ImageInfo(buf.readAnySizeNbt());
        NbtUtils.readAdminData(buf.readAnySizeNbt(), "AdminData", adminData);
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(name, 32767);
        buf.writeBoolean(publiced);
        buf.writeNbt(image.save(new CompoundTag()));
        buf.writeNbt(NbtUtils.writeAdminData(new CompoundTag(), "AdminData", adminData));
    }
}

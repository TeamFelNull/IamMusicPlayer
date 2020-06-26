package net.morimori.imp.packet;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;

public class SoundFileUploaderSyncMessage {
    public int dim;
    public BlockPos pos;
    public NonNullList<ItemStack> items;
    public int pitch;
    public int yaw;
    public String playeruuid;
    public String path;
    public Map<String, String> playerstager;

    public SoundFileUploaderSyncMessage(int dimID, BlockPos postion, NonNullList<ItemStack> Item, int Pitch, int Yaw,
                                        String pluuid, String flpath, Map<String, String> playerstager) {
        this.dim = dimID;
        this.pos = postion;
        this.items = Item;
        this.pitch = Pitch;
        this.yaw = Yaw;
        this.playeruuid = pluuid;
        this.path = flpath;
        this.playerstager = playerstager;
    }

    public static SoundFileUploaderSyncMessage decodeMessege(PacketBuffer buffer) {
        NonNullList<ItemStack> itemsa = NonNullList.withSize(1, ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(buffer.readCompoundTag(), itemsa);

        return new SoundFileUploaderSyncMessage(buffer.readInt(),
                new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt()),
                itemsa, buffer.readInt(), buffer.readInt(), buffer.readString(32767),
                buffer.readString(32767), readMap(buffer));
    }

    private static Map<String, String> readMap(PacketBuffer buffer) {

        Map<String, String> stmap = new HashMap<String, String>();
        CompoundNBT ptmnbt = buffer.readCompoundTag();
        for (String key : ptmnbt.keySet()) {
            stmap.put(key, ptmnbt.getString(key));
        }

        return stmap;
    }

    public static void encodeMessege(SoundFileUploaderSyncMessage messegeIn, PacketBuffer buffer) {

        buffer.writeCompoundTag(ItemStackHelper.saveAllItems(new CompoundNBT(), messegeIn.items));
        buffer.writeInt(messegeIn.dim);
        buffer.writeInt(messegeIn.pos.getX());
        buffer.writeInt(messegeIn.pos.getY());
        buffer.writeInt(messegeIn.pos.getZ());
        buffer.writeInt(messegeIn.pitch);
        buffer.writeInt(messegeIn.yaw);
        buffer.writeString(messegeIn.playeruuid);
        buffer.writeString(messegeIn.path);

        CompoundNBT ptmnbt = new CompoundNBT();
        for (Entry<String, String> ent : messegeIn.playerstager.entrySet()) {
            ptmnbt.putString(ent.getKey(), ent.getValue());
        }
        buffer.writeCompoundTag(ptmnbt);
    }
}

package red.felnull.imp.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import red.felnull.imp.data.PlayListGuildManeger;
import red.felnull.imp.data.PlayMusicManeger;
import red.felnull.imp.musicplayer.PlayList;
import red.felnull.otyacraftengine.util.IKSGNBTUtil;
import red.felnull.otyacraftengine.util.IKSGPlayerUtil;

import java.util.HashMap;
import java.util.Map;

public abstract class IMPAbstractPAPLEquipmentTileEntity extends IMPAbstractPAEquipmentTileEntity {
    private Map<String, String> plyerLastScreens = new HashMap<>();

    protected IMPAbstractPAPLEquipmentTileEntity(TileEntityType<?> typeIn) {
        super(typeIn);
    }

    public String getLastPlayList(PlayerEntity pl) {
        String uuid = IKSGPlayerUtil.getUUID(pl);
        if (plyerLastScreens.containsKey(uuid))
            return plyerLastScreens.get(uuid);
        return "";
    }

    @Override
    public void readByIKSG(BlockState state, CompoundNBT tag) {
        super.readByIKSG(state, tag);
        this.plyerLastScreens = IKSGNBTUtil.readStringMap(tag.getCompound("PlyerLastScreens"));
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        super.write(tag);
        tag.put("PlyerLastScreens", IKSGNBTUtil.writeStringMap(new CompoundNBT(), this.plyerLastScreens));
        return tag;
    }

    @Override
    public CompoundNBT instructionFromClient(ServerPlayerEntity player, String s, CompoundNBT tag) {
        if (s.equals("LastPlayListSet")) {
            setLastPlayList(player, tag.getString("uuid"));
        } else if (s.equals("PlayListUpdate")) {
            return getUpdatePlayListTag(player);
        } else if (s.equals("PlayMusicUpdate")) {
            return getUpdatePlayMusicTag(player, tag.getString("uuid"));
        }
        return super.instructionFromClient(player, s, tag);
    }

    private CompoundNBT getUpdatePlayMusicTag(ServerPlayerEntity playerEntity, String uuid) {
        CompoundNBT tag = new CompoundNBT();
        tag.putString("uuid", uuid);
        tag.put("list", PlayMusicManeger.instance().getAllPlayMusicNBT(playerEntity, PlayList.getPlayListByUUID(uuid)));
        return tag;
    }

    private CompoundNBT getUpdatePlayListTag(ServerPlayerEntity player) {
        return PlayListGuildManeger.instance().getJoinedPlayListsNBT(player);
    }

    public void setLastPlayList(PlayerEntity pl, String uuid) {
        plyerLastScreens.put(IKSGPlayerUtil.getUUID(pl), uuid);
    }
}

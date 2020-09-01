package red.felnull.imp.musicplayer;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import red.felnull.imp.data.IMPWorldData;
import red.felnull.otyacraftengine.data.WorldDataManager;
import red.felnull.otyacraftengine.util.IKSGPlayerUtil;

public class PlayList implements INBTReadWriter {
    private final String UUID;
    private String name;
    private String imageUUID;

    public PlayList(String UUID, String name, String imageUUID) {
        this.UUID = UUID;
        this.name = name;
        this.imageUUID = imageUUID;
    }

    @Override
    public void read(CompoundNBT tag) {
        this.name = tag.getString("Name");
        this.imageUUID = tag.getString("ImageUUID");
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.putString("Name", this.name);
        tag.putString("ImageUUID", this.imageUUID);
        return tag;
    }

    public String getName() {
        return name;
    }

    public String getImageUUID() {
        return imageUUID;
    }

    public String getUUID() {
        return UUID;
    }

    public static void addPlayList(PlayList plst) {
        WorldDataManager.instance().getWorldData(IMPWorldData.PLAYLIST_DATA).getCompound("playlists").put(plst.getUUID(), plst.write(new CompoundNBT()));
    }

    public static void addPlayer(ServerPlayerEntity player, PlayList list) {
        String pluuid = IKSGPlayerUtil.getUUID(player);
        CompoundNBT pltag = WorldDataManager.instance().getWorldData(IMPWorldData.PLAYLIST_DATA).getCompound("players");
        CompoundNBT plutag = null;
        if (!pltag.contains(pluuid)) {
            plutag = new CompoundNBT();
        } else {
            plutag = pltag.getCompound(pluuid);
        }
        plutag.lis
        pltag.put(pluuid, plutag);
    }

    public void addPlayer(ServerPlayerEntity playerEntity) {
        addPlayer(playerEntity, this);
    }
}

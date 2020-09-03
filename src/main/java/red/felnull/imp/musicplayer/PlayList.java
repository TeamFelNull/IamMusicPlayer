package red.felnull.imp.musicplayer;

import com.google.gson.Gson;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import red.felnull.imp.data.IMPWorldData;
import red.felnull.otyacraftengine.data.WorldDataManager;
import red.felnull.otyacraftengine.util.IKSGPlayerUtil;

import java.util.ArrayList;
import java.util.List;

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
        plutag.put("playlist", addStringList(new CompoundNBT(), list.getUUID()));
        pltag.put(pluuid, plutag);
    }

    public void addPlayer(ServerPlayerEntity playerEntity) {
        addPlayer(playerEntity, this);
    }

    private static final Gson gson = new Gson();

    public static CompoundNBT writeStringList(CompoundNBT tag, List<String> strs) {
        String json = gson.toJson(strs);
        tag.putString("StringList", json);
        return tag;
    }

    public static List<String> readStringList(CompoundNBT tag) {
        List<String> list = new ArrayList();
        String json = tag.getString("StringList");
        if (json.isEmpty()) {
            return list;
        }
        list.addAll(gson.fromJson(json, list.getClass()));
        return list;
    }

    public static CompoundNBT addStringList(CompoundNBT tag, String str) {
        List<String> strs = readStringList(tag);
        strs.add(str);
        writeStringList(tag, strs);
        return tag;
    }
}

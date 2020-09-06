package red.felnull.imp.musicplayer;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import red.felnull.imp.data.IMPWorldData;
import red.felnull.otyacraftengine.data.INBTReadWriter;
import red.felnull.otyacraftengine.data.WorldDataManager;
import red.felnull.otyacraftengine.util.IKSGNBTUtil;
import red.felnull.otyacraftengine.util.IKSGPlayerUtil;

public class PlayList implements INBTReadWriter {
    private final String UUID;
    private String name;
    private String imageUUID;
    private int imageWidth;
    private int imageHeight;
    private String createPlayerName;
    private String createPlayerUUID;
    private String timeStamp;
    private boolean Anyone;

    public PlayList(String UUID, CompoundNBT tag) {
        this.UUID = UUID;
        read(tag);
    }

    public PlayList(String UUID, String name, String imageUUID, int width, int height, String createPlayerName, String createPlayerUUID, String timeStamp) {
        this.UUID = UUID;
        this.name = name;
        this.imageUUID = imageUUID;
        this.imageWidth = width;
        this.imageHeight = height;
        this.createPlayerName = createPlayerName;
        this.createPlayerUUID = createPlayerUUID;
        this.timeStamp = timeStamp;
    }

    @Override
    public void read(CompoundNBT tag) {
        this.name = tag.getString("Name");
        this.imageUUID = tag.getString("ImageUUID");
        this.imageWidth = tag.getInt("ImageWidth");
        this.imageHeight = tag.getInt("ImageHeight");
        this.createPlayerName = tag.getString("CreatePlayerName");
        this.createPlayerUUID = tag.getString("CreatePlayerUUID");
        this.timeStamp = tag.getString("TimeStamp");
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.putString("Name", this.name);
        tag.putString("ImageUUID", this.imageUUID);
        tag.putInt("ImageWidth", this.imageWidth);
        tag.putInt("ImageHeight", this.imageHeight);
        tag.putString("CreatePlayerName", this.createPlayerName);
        tag.putString("CreatePlayerUUID", this.createPlayerUUID);
        tag.putString("TimeStamp", this.timeStamp);
        return tag;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public int getImageWidth() {
        return imageWidth;
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

    public boolean isAnyone() {
        return true;
    }

    public String getCreatePlayerName() {
        return createPlayerName;
    }

    public String getCreatePlayerUUID() {
        return createPlayerUUID;
    }

    public String getTimeStamp() {
        return timeStamp;
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
        plutag.put("playlist", IKSGNBTUtil.addStringList(plutag.getCompound("playlist"), list.getUUID()));
        pltag.put(pluuid, plutag);
    }

    public void addPlayer(ServerPlayerEntity playerEntity) {
        addPlayer(playerEntity, this);
    }


}

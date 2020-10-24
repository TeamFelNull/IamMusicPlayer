package red.felnull.imp.musicplayer;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.data.IMPWorldData;
import red.felnull.otyacraftengine.data.INBTReadWriter;
import red.felnull.otyacraftengine.data.WorldDataManager;
import red.felnull.otyacraftengine.util.IKSGNBTUtil;
import red.felnull.otyacraftengine.util.IKSGPlayerUtil;

import java.util.ArrayList;
import java.util.List;

public class PlayList implements INBTReadWriter {
    public static final String ALL_UUID = "6681f205-ea7c-4fee-9149-fbe8226143c6";
    public static final PlayList ALL = new PlayList(ALL_UUID, "all", new PlayImage(PlayImage.ImageType.STRING, "all"), "", "", "", false);
    private final String UUID;
    private String name;
    private String createPlayerName;
    private String createPlayerUUID;
    private String timeStamp;
    private boolean Anyone;
    private PlayImage image;

    public PlayList(String UUID, CompoundNBT tag) {
        this.UUID = UUID;
        read(tag);
    }

    public PlayList(String UUID, String name, PlayImage image, String createPlayerName, String createPlayerUUID, String timeStamp, boolean anyone) {
        this.UUID = UUID;
        this.name = name;
        this.createPlayerName = createPlayerName;
        this.createPlayerUUID = createPlayerUUID;
        this.timeStamp = timeStamp;
        this.Anyone = anyone;
        this.image = image;
    }

    @Override
    public void read(CompoundNBT tag) {
        this.name = tag.getString("Name");
        this.createPlayerName = tag.getString("CreatePlayerName");
        this.createPlayerUUID = tag.getString("CreatePlayerUUID");
        this.timeStamp = tag.getString("TimeStamp");
        this.Anyone = tag.getBoolean("Anyone");
        this.image = new PlayImage(tag.getCompound("Image"));
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.putString("Name", this.name);
        tag.putString("CreatePlayerName", this.createPlayerName);
        tag.putString("CreatePlayerUUID", this.createPlayerUUID);
        tag.putString("TimeStamp", this.timeStamp);
        tag.putBoolean("Anyone", this.Anyone);
        tag.put("Image", this.image.write(new CompoundNBT()));
        return tag;
    }


    public String getName() {
        return name;
    }

    public PlayImage getImage() {
        return image;
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
        IamMusicPlayer.LOGGER.info(IKSGPlayerUtil.getUserName(player) + " Join PlayList(" + list.getName() + ")");
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

    public static PlayList getPlayListByUUID(String uuid) {

        CompoundNBT pltag = WorldDataManager.instance().getWorldData(IMPWorldData.PLAYLIST_DATA).getCompound("playlists");

        if (!pltag.contains(uuid))
            return null;


        return new PlayList(uuid, pltag.getCompound(uuid));
    }

    public static List<PlayList> getJoinedPlayLists(ServerPlayerEntity player) {
        List<PlayList> list = new ArrayList<>();
        CompoundNBT pltag = WorldDataManager.instance().getWorldData(IMPWorldData.PLAYLIST_DATA).getCompound("players");
        String uuid = IKSGPlayerUtil.getUUID(player);
        if (!pltag.contains(uuid))
            return list;
        List<String> sts = IKSGNBTUtil.readStringList(pltag.getCompound(uuid).getCompound("playlist"));
        sts.forEach(n -> list.add(getPlayListByUUID(n)));
        return list;
    }

    public boolean equals(Object obj) {

        if (obj == this)
            return true;

        if (obj instanceof PlayList && ((PlayList) obj).getUUID().equals(this.getUUID()))
            return true;

        return false;
    }

}

package red.felnull.imp.client.gui.screen;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import red.felnull.imp.musicplayer.PlayList;
import red.felnull.imp.musicplayer.PlayMusic;
import red.felnull.imp.tileentity.IMPAbstractPAPLEquipmentTileEntity;
import red.felnull.otyacraftengine.util.ClockTimer;

import java.util.ArrayList;
import java.util.List;

public abstract class IMPAbstractPLEquipmentScreen<T extends Container> extends IMPAbstractEquipmentScreen<T> implements IMusicPlayListScreen {

    private final List<PlayList> jonedAllPlaylists = new ArrayList<>();
    private final List<PlayMusic> currentPlayMusics = new ArrayList<>();
    private PlayList currentPlayList;
    private ClockTimer updateTimer;
    private boolean isFristMLUpdate;

    public IMPAbstractPLEquipmentScreen(T screenContainer, PlayerInventory playerInventory, ITextComponent titleIn) {
        super(screenContainer, playerInventory, titleIn);
        currentPlayList = PlayList.ALL;
    }

    @Override
    public void initByIKSG() {
        super.initByIKSG();
        updateAll();
        timerSet();
    }

    @Override
    public void updatePlayList() {
        instruction("PlayListUpdate", new CompoundNBT());
    }

    @Override
    public void updatePlayMusic() {
        CompoundNBT tag = new CompoundNBT();
        tag.putString("uuid", currentPlayList.getUUID());
        instruction("PlayMusicUpdate", tag);
    }


    @Override
    protected void insPower(boolean on) {
        super.insPower(on);
        updateAll();
    }

    @Override
    public PlayList getCurrentSelectedPlayList() {
        return currentPlayList;
    }

    @Override
    public void setCurrentSelectedPlayList(PlayList list) {
        currentPlayList = list;
    }

    @Override
    public List<PlayList> getJonedAllPlayLists() {
        return jonedAllPlaylists;
    }

    @Override
    public List<PlayMusic> getCurrentPLPlayMusic() {
        return currentPlayMusics;
    }

    private void timerSet() {
        this.updateTimer = new ClockTimer(n -> this.isOpend());
        this.updateTimer.addTask("updateplaylist", new ClockTimer.ITask() {
            @Override
            public boolean isStop(ClockTimer clockTimer) {
                return false;
            }

            @Override
            public void run(ClockTimer clockTimer) {
                updateAll();
            }

            @Override
            public long time(ClockTimer clockTimer) {
                return 3000;
            }
        });
    }

    public void insLastPlayList() {
        CompoundNBT tag = new CompoundNBT();
        tag.putString("uuid", currentPlayList.getUUID());
        instruction("LastPlayListSet", tag);
    }

    private String getLastPlayListUUID() {
        String pathst = ((IMPAbstractPAPLEquipmentTileEntity) getTileEntity()).getLastPlayList(getMinecraft().player);
        return pathst;
    }

    @Override
    public void onCloseByIKSG() {
        super.onCloseByIKSG();
        if (getCurrentSelectedPlayList() != null)
            insLastPlayList();
    }

    public void removeLastPlayList() {
        instruction("LastPlayListSet", new CompoundNBT());
    }

    @Override
    public void instructionReturn(String name, CompoundNBT tag) {
        if (name.equals("PlayListUpdate")) {
            jonedAllPlaylists.clear();
            for (String pltagst : tag.keySet()) {
                jonedAllPlaylists.add(new PlayList(pltagst, tag.getCompound(pltagst)));
            }
            if (!isFristMLUpdate) {
                String lastPlayMusic = getLastPlayListUUID();
                if (!lastPlayMusic.isEmpty()) {
                    PlayList list = PlayList.ALL;
                    for (PlayList pl : jonedAllPlaylists) {
                        if (pl.getUUID().equals(lastPlayMusic)) {
                            list = pl;
                            break;
                        }
                    }
                    this.currentPlayList = list;
                    if (this.currentPlayList != PlayList.ALL) {
                        updatePlayMusic();
                    }
                } else {
                    this.currentPlayList = PlayList.ALL;
                }
                isFristMLUpdate = true;
            }
        } else if (name.equals("PlayMusicUpdate")) {
            if (currentPlayList.getUUID().equals(tag.getString("uuid"))) {
                CompoundNBT taga = tag.getCompound("list");
                currentPlayMusics.clear();
                for (String pmtagst : taga.keySet()) {
                    currentPlayMusics.add(new PlayMusic(pmtagst, taga.getCompound(pmtagst)));
                }
            }
        }
    }
}

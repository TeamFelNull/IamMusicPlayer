package red.felnull.imp.item;

import red.felnull.imp.music.resource.dynamic.DynamicMusicList;
import red.felnull.otyacraftengine.item.IkisugiTooltipComponent;

public class CassetteTapeTooltip implements IkisugiTooltipComponent {
    private DynamicMusicList musicList;

    public CassetteTapeTooltip(DynamicMusicList musicList) {
        this.musicList = musicList;
    }

    public DynamicMusicList getMusicList() {
        return musicList;
    }
}

package red.felnull.imp.client.gui.screen.msdscreen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.TranslatableComponent;
import red.felnull.imp.data.resource.AdministratorInformation;
import red.felnull.imp.data.resource.ImageLocation;
import red.felnull.imp.music.resource.MusicPlayList;
import red.felnull.imp.music.resource.MusicPlayListDetailed;
import red.felnull.otyacraftengine.client.gui.screen.IkisugiContainerScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MSDPlayListScreen extends MSDBaseScreen {
    private PlayListSelectionList playListSelectionList;

    public MSDPlayListScreen(IkisugiContainerScreen<?> screen) {
        super(new TranslatableComponent("imp.msdscreen.playlist.title"), screen);
    }

    @Override
    protected void init() {
        this.playListSelectionList = new PlayListSelectionList(this.minecraft);
        addWidget(this.playListSelectionList);
    }

    @Override
    public void render(PoseStack poseStack, int i, int j, float f) {
        super.render(poseStack, i, j, f);
        this.playListSelectionList.render(poseStack, i, j, f);
    }


    public class PlayListSelectionList extends ObjectSelectionList<PlayListSelectionList.PlayListEntry> {

        public PlayListSelectionList(Minecraft minecraft) {
            super(minecraft, 18, 101, 9, 110, 18);
            setLeftPos(1);
            Map<UUID, AdministratorInformation.AuthorityType> players = new HashMap<>();
            players.put(Minecraft.getInstance().player.getGameProfile().getId(), AdministratorInformation.AuthorityType.READ_ONLY);

            UUID uuidpl = UUID.randomUUID();
            MusicPlayList mpl = new MusicPlayList(uuidpl, "aikisugiList", new MusicPlayListDetailed(false), new ImageLocation(ImageLocation.ImageType.STRING, "TEST"), new AdministratorInformation(false, players), new ArrayList<>());
            this.addEntry(new PlayListEntry(mpl));
        }

        public class PlayListEntry extends ObjectSelectionList.Entry<PlayListEntry> {
            private final MusicPlayList playList;

            public PlayListEntry(MusicPlayList playList) {
                this.playList = playList;
            }

            @Override
            public void render(PoseStack poseStack, int i, int j, int k, int l, int m, int n, int o, boolean bl, float f) {
                String string = playList.getName();
                MSDPlayListScreen.this.font.drawShadow(poseStack, string, (float) (MSDPlayListScreen.PlayListSelectionList.this.width / 2 - MSDPlayListScreen.this.font.width(string) / 2), (float) (j + 1), 16777215, true);
            }
        }
    }
}

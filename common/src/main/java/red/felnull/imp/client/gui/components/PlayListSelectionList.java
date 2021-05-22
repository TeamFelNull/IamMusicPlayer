package red.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import red.felnull.otyacraftengine.client.gui.components.IkisugiSelectionList;
import red.felnull.otyacraftengine.util.IKSGDokataUtil;

public class PlayListSelectionList extends IkisugiSelectionList<PlayListSelectionList.Entry> {

    public PlayListSelectionList(Minecraft minecraft, int x, int y, int width, int height, int selectHeight) {
        super(minecraft, x, y, width, height, selectHeight);
        for (IKSGDokataUtil.Dokata value : IKSGDokataUtil.Dokata.values()) {
     //       addEntry(new Entry(value.getSerializedName()));
        }
    }

    public class Entry extends IkisugiSelectionList.Entry<PlayListSelectionList.Entry> {
        public Entry(String name) {
            super(name);
        }

        @Override
        public void render(PoseStack poseStack, int i, int j, int k, int l, int m, int n, int o, boolean bl, float f) {
            Minecraft.getInstance().font.drawShadow(poseStack, getName(), (float) (width / 2 - Minecraft.getInstance().font.width(getName()) / 2), (float) (j + 1), 16777215, true);
        }

        @Override
        public Component getNarration() {
            return new TextComponent(getName());
        }
    }
}

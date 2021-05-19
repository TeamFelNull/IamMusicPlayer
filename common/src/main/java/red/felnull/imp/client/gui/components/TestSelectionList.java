package red.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import red.felnull.otyacraftengine.client.gui.components.IkisugiSelectionList;

public class TestSelectionList extends IkisugiSelectionList<TestSelectionList.Entry> {
    public TestSelectionList(Minecraft minecraft, int x, int y, int width, int height, int selectHeight) {
        super(minecraft, x, y, width, height, selectHeight);

        for (int i = 0; i < 30; i++) {
            addEntry(new Entry("Ikisug:" + i));
        }

    }

    public class Entry extends IkisugiSelectionList.Entry<TestSelectionList.Entry> {
        public Entry(String name) {
            super(name);
        }

        @Override
        public void render(PoseStack poseStack, int i, int j, int k, int l, int m, int n, int o, boolean bl, float f) {
            Minecraft.getInstance().font.drawShadow(poseStack, getName(), (float) (width / 2 - Minecraft.getInstance().font.width(getName()) / 2), (float) (j + 1), 16777215, true);
        }
    }
}

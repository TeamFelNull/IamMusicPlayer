package red.felnull.imp.client.gui.components.monitor;

import net.minecraft.network.chat.TextComponent;
import red.felnull.imp.blockentity.MusicSharingDeviceBlockEntity;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;
import red.felnull.otyacraftengine.client.gui.components.TestFixedButtonsList;
import red.felnull.otyacraftengine.util.IKSGDokataUtil;

import java.util.ArrayList;
import java.util.List;

public class MSDDebugMonitor extends MSDBaseMonitor {
    public final List<String> testList = new ArrayList<>();

    public MSDDebugMonitor(MusicSharingDeviceBlockEntity.Screen msdScreen, MusicSharingDeviceScreen parentScreen, int x, int y, int width, int height) {
        super(new TextComponent("Debug"), msdScreen, parentScreen, x, y, width, height);
        this.testList.add("ikisugi");
        IKSGDokataUtil.Dokata[] var1 = IKSGDokataUtil.Dokata.values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            IKSGDokataUtil.Dokata value = var1[var3];
            this.testList.add(value.getSerializedName().substring(0, 10));
        }

        this.testList.add("ｳｧｧ!!ｵﾚﾓｲｯﾁｬｳｩｩｩ!!!ｳｳｳｳｳｳｳｳｳｩｩｩｩｩｩｩｩｳｳｳｳｳｳｳ!ｲｨｨｲｨｨｨｲｲｲｨｲｲｲ!!");
    }

    @Override
    public void init() {
        super.init();
        this.addRenderableWidget(new TestFixedButtonsList(x, y, 29, 100, 5, new TextComponent("Test List"), this.testList, TextComponent::new, (n) -> {
            System.out.println(n.item());
        }));
    }
}

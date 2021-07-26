package red.felnull.imp.client.gui.tooltip;

import red.felnull.imp.item.CassetteTapeTooltip;
import red.felnull.otyacraftengine.client.util.IKSGClientUtil;

public class IMPTooltips {
    public static void init() {
        IKSGClientUtil.registerClientToolTip(CassetteTapeTooltip.class, n -> new ClientCassetteTapeTooltip(((CassetteTapeTooltip) n).getMusicList()));
    }
}

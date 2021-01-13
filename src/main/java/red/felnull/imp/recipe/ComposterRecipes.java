package red.felnull.imp.recipe;

import red.felnull.imp.item.IMPItems;
import red.felnull.otyacraftengine.util.IKSGRegistryUtil;

public class ComposterRecipes {
    public static void register() {
        IKSGRegistryUtil.registerCompostable(1.0F, IMPItems.KATYOU_ANTENNA);
        IKSGRegistryUtil.registerCompostable(1.0F, IMPItems.IKISUGI_ANTENNA);
    }
}

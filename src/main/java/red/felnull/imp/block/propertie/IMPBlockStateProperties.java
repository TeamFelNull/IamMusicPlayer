package red.felnull.imp.block.propertie;

import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;

public class IMPBlockStateProperties {
    public static final BooleanProperty ON = BooleanProperty.create("on");
    public static final BooleanProperty WALL = BooleanProperty.create("wall");
    public static final EnumProperty<BoomboxMode> BOOMBOX_MODE = EnumProperty.create("mode", BoomboxMode.class);
}

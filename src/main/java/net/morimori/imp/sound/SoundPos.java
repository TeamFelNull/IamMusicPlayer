package net.morimori.imp.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.morimori.imp.util.IMPMath;

public class SoundPos {
    private static Minecraft mc = Minecraft.getInstance();
    public double x;
    public double y;
    public double z;

    public SoundPos(BlockPos pos) {
        this.x = pos.getX() + 0.5d;
        this.y = pos.getY() + 0.5d;
        this.z = pos.getZ() + 0.5d;
    }

    public SoundPos(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BlockPos getBlockPos() {
        return new BlockPos(x, y, z);
    }

    public boolean equals(SoundPos sppos) {

        return sppos.x == this.x && sppos.y == this.y && sppos.z == this.z;
    }

    public double distance(double x, double y, double z) {

        double xk = IMPMath.positiveDouble(x - this.x);
        double yk = IMPMath.positiveDouble(y - this.y);
        double zk = IMPMath.positiveDouble(z - this.z);

        double d2d = Math.sqrt((xk * xk) + (zk * zk));

        return Math.sqrt((d2d * d2d) + (yk * yk));
    }

    public boolean canLisn(double x, double y, double z, int maxdistance) {

        return this.distance(x, y, z) <= maxdistance;
    }

    public boolean canLisn(int maxdistance) {

        return this.distance(mc.player.func_226277_ct_(), mc.player.func_226278_cu_() + 1,
                mc.player.func_226281_cx_()) <= maxdistance;
    }
}

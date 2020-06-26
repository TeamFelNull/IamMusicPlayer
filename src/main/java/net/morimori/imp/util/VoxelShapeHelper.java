package net.morimori.imp.util;

import net.minecraft.block.Block;
import net.minecraft.util.math.shapes.VoxelShape;

public class VoxelShapeHelper {
    public static VoxelShape makeCuboidShaoe0(double x1, double y1, double z1, double x2, double y2, double z2) {
        return Block.makeCuboidShape(x1, y1, z1, x2, y2, z2);
    }

    public static VoxelShape makeCuboidShaoe90(double x1, double y1, double z1, double x2, double y2, double z2) {
        return makeCuboidShaoe0(z1, y1, 16 - x1, z2, y2, 16 - x2);
    }

    public static VoxelShape makeCuboidShaoe180(double x1, double y1, double z1, double x2, double y2, double z2) {
        return makeCuboidShaoe0(16 - x1, y1, 16 - z1, 16 - x2, y2, 16 - z2);
    }

    public static VoxelShape makeCuboidShaoe270(double x1, double y1, double z1, double x2, double y2, double z2) {
        return makeCuboidShaoe180(z1, y1, 16 - x1, z2, y2, 16 - x2);
    }

    public static VoxelShape addCuboidShaoe0(double x1, double y1, double z1, double x2, double y2, double z2) {
        return makeCuboidShaoe0(x1, y1, z1, x1 + x2, y1 + y2, z1 + z2);
    }

    public static VoxelShape addCuboidShaoe90(double x1, double y1, double z1, double x2, double y2, double z2) {
        return makeCuboidShaoe90(x1, y1, z1, x1 + x2, y1 + y2, z1 + z2);
    }

    public static VoxelShape addCuboidShaoe180(double x1, double y1, double z1, double x2, double y2, double z2) {
        return makeCuboidShaoe180(x1, y1, z1, x1 + x2, y1 + y2, z1 + z2);
    }

    public static VoxelShape addCuboidShaoe270(double x1, double y1, double z1, double x2, double y2, double z2) {
        return makeCuboidShaoe270(x1, y1, z1, x1 + x2, y1 + y2, z1 + z2);
    }
}

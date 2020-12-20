package red.felnull.imp.block.voxelshape;

import net.minecraft.util.Direction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import red.felnull.otyacraftengine.util.IKSGVoxelShapeUtil;

public class CassetteDeckVoxelShape {
    private static final VoxelShape NORTH_PART1 = IKSGVoxelShapeUtil.makeCuboidShaoe0(13, 0, 3, 15, 0.5, 5);
    private static final VoxelShape NORTH_PART2 = IKSGVoxelShapeUtil.makeCuboidShaoe0(1, 0, 3, 3, 0.5, 5);
    private static final VoxelShape NORTH_PART3 = IKSGVoxelShapeUtil.makeCuboidShaoe0(1, 0, 11, 3, 0.5, 13);
    private static final VoxelShape NORTH_PART4 = IKSGVoxelShapeUtil.makeCuboidShaoe0(13, 0, 11, 15, 0.5, 13);
    private static final VoxelShape NORTH_PART5 = IKSGVoxelShapeUtil.makeCuboidShaoe0(0, 0.5, 2.5, 16, 5.5, 13.5);
    private static final VoxelShape NORTH_PART6 = IKSGVoxelShapeUtil.makeCuboidShaoe0(9.5, 3.2, 2.3, 10.3, 3.5, 2.5);
    private static final VoxelShape NORTH_PART7 = IKSGVoxelShapeUtil.makeCuboidShaoe0(7.6, 3.2, 2.3, 8.4, 3.5, 2.5);
    private static final VoxelShape NORTH_PART8 = IKSGVoxelShapeUtil.makeCuboidShaoe0(6.7, 3.2, 2.3, 7.5, 3.5, 2.5);
    private static final VoxelShape NORTH_PART9 = IKSGVoxelShapeUtil.makeCuboidShaoe0(5.7, 3.2, 2.3, 6.5, 3.5, 2.5);
    private static final VoxelShape NORTH_PART10 = IKSGVoxelShapeUtil.makeCuboidShaoe0(8.5, 3.2, 2.3, 9.3, 3.5, 2.5);
    private static final VoxelShape NORTH_PART11 = IKSGVoxelShapeUtil.makeCuboidShaoe0(10, 1, 2.3, 11, 1.3, 2.5);
    private static final VoxelShape NORTH_PART12 = IKSGVoxelShapeUtil.makeCuboidShaoe0(5, 1, 2.3, 6, 1.3, 2.5);
    private static final VoxelShape NORTH_PART13 = IKSGVoxelShapeUtil.makeCuboidShaoe0(8.5, 2.85, 2.3, 11, 3.05, 2.5);
    private static final VoxelShape NORTH_PART14 = IKSGVoxelShapeUtil.makeCuboidShaoe0(5, 2.85, 2.3, 7.5, 3.05, 2.5);
    private static final VoxelShape NORTH_PART15 = IKSGVoxelShapeUtil.makeCuboidShaoe0(7.5, 1.5, 2.2, 8.5, 2.5, 2.5);
    private static final VoxelShape NORTH_PART16 = IKSGVoxelShapeUtil.makeCuboidShaoe0(8.5, 2, 2.3, 11, 2.7, 2.5);
    private static final VoxelShape NORTH_PART17 = IKSGVoxelShapeUtil.makeCuboidShaoe0(5, 2, 2.3, 7.5, 2.7, 2.5);
    private static final VoxelShape NORTH_PART18 = IKSGVoxelShapeUtil.makeCuboidShaoe0(14, 0.75, 2.3, 14.5, 1.25, 2.5);
    private static final VoxelShape NORTH_PART19 = IKSGVoxelShapeUtil.makeCuboidShaoe0(6, 4, 2.3, 6.2, 4.2, 2.5);
    private static final VoxelShape NORTH_PART20 = IKSGVoxelShapeUtil.makeCuboidShaoe0(9.8, 4, 2.3, 10, 4.2, 2.5);
    private static final VoxelShape NORTH_PART21 = IKSGVoxelShapeUtil.makeCuboidShaoe0(0.5, 5.5, 3, 2.5, 6, 5);
    private static final VoxelShape NORTH_PART22 = IKSGVoxelShapeUtil.makeCuboidShaoe0(1, 7, 3.5, 2, 7.5, 4.5);
    private static final VoxelShape NORTH_PART23 = IKSGVoxelShapeUtil.makeCuboidShaoe0(1.25, 7.5, 3.75, 1.75, 8.5, 4.25);
    private static final VoxelShape NORTH_PART24 = IKSGVoxelShapeUtil.makeCuboidShaoe0(2, 6, 3, 2.5, 6.5, 3.5);
    private static final VoxelShape NORTH_PART25 = IKSGVoxelShapeUtil.makeCuboidShaoe0(0.5, 6, 3, 1, 6.5, 3.5);
    private static final VoxelShape NORTH_PART26 = IKSGVoxelShapeUtil.makeCuboidShaoe0(0.5, 6, 4.5, 1, 6.5, 5);
    private static final VoxelShape NORTH_PART27 = IKSGVoxelShapeUtil.makeCuboidShaoe0(1, 6.5, 3, 2, 7, 3.5);
    private static final VoxelShape NORTH_PART28 = IKSGVoxelShapeUtil.makeCuboidShaoe0(2, 6.5, 3.5, 2.5, 7, 4.5);
    private static final VoxelShape NORTH_PART29 = IKSGVoxelShapeUtil.makeCuboidShaoe0(1, 6.5, 4.5, 2, 7, 5);
    private static final VoxelShape NORTH_PART30 = IKSGVoxelShapeUtil.makeCuboidShaoe0(0.5, 6.5, 3.5, 1, 7, 4.5);
    private static final VoxelShape NORTH_PART31 = IKSGVoxelShapeUtil.makeCuboidShaoe0(2, 6, 4.5, 2.5, 6.5, 5);
    public static final VoxelShape NORTH_AXIS_AABB = VoxelShapes.or(NORTH_PART1, NORTH_PART2, NORTH_PART3, NORTH_PART4, NORTH_PART5, NORTH_PART6, NORTH_PART7, NORTH_PART8, NORTH_PART9, NORTH_PART10, NORTH_PART11, NORTH_PART12, NORTH_PART13, NORTH_PART14, NORTH_PART15, NORTH_PART16, NORTH_PART17, NORTH_PART18, NORTH_PART19, NORTH_PART20, NORTH_PART21, NORTH_PART22, NORTH_PART23, NORTH_PART24, NORTH_PART25, NORTH_PART26, NORTH_PART27, NORTH_PART28, NORTH_PART29, NORTH_PART30, NORTH_PART31);
    public static final VoxelShape SOUTH_AXIS_AABB = IKSGVoxelShapeUtil.rotateDirection(NORTH_AXIS_AABB, Direction.SOUTH);
    public static final VoxelShape EAST_AXIS_AABB = IKSGVoxelShapeUtil.rotateDirection(NORTH_AXIS_AABB, Direction.EAST);
    public static final VoxelShape WEST_AXIS_AABB = IKSGVoxelShapeUtil.rotateDirection(NORTH_AXIS_AABB, Direction.WEST);
}

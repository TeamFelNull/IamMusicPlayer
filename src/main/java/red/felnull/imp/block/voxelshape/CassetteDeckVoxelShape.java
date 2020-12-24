package red.felnull.imp.block.voxelshape;

import net.minecraft.util.Direction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import red.felnull.otyacraftengine.util.IKSGVoxelShapeUtil;

public class CassetteDeckVoxelShape {
    private static final VoxelShape NORTH_PART1 = IKSGVoxelShapeUtil.makeCuboidShaoe0(1.0f, 0.0f, 4.0f, 2.0f, 0.1f, 5.0f);
    private static final VoxelShape NORTH_PART2 = IKSGVoxelShapeUtil.makeCuboidShaoe0(14.0f, 0.0f, 4.0f, 15.0f, 0.1f, 5.0f);
    private static final VoxelShape NORTH_PART3 = IKSGVoxelShapeUtil.makeCuboidShaoe0(14.0f, 0.0f, 11.0f, 15.0f, 0.1f, 12.0f);
    private static final VoxelShape NORTH_PART4 = IKSGVoxelShapeUtil.makeCuboidShaoe0(1.0f, 0.0f, 11.0f, 2.0f, 0.1f, 12.0f);
    private static final VoxelShape NORTH_PART5 = IKSGVoxelShapeUtil.makeCuboidShaoe0(0.5f, 0.1f, 3.925f, 15.5f, 3.1f, 12.5f);
    private static final VoxelShape NORTH_PART6 = IKSGVoxelShapeUtil.makeCuboidShaoe0(0.5f, 0.1f, 3.5f, 15.5f, 3.1f, 3.925f);
    private static final VoxelShape NORTH_PART7 = IKSGVoxelShapeUtil.makeCuboidShaoe0(9.825f, 1.55f, 3.475f, 9.875f, 2.975f, 3.5f);
    private static final VoxelShape NORTH_PART8 = IKSGVoxelShapeUtil.makeCuboidShaoe0(6.125f, 2.925f, 3.475f, 9.825f, 2.975f, 3.5f);
    private static final VoxelShape NORTH_PART9 = IKSGVoxelShapeUtil.makeCuboidShaoe0(6.125f, 1.55f, 3.475f, 9.825f, 1.6f, 3.5f);
    private static final VoxelShape NORTH_PART10 = IKSGVoxelShapeUtil.makeCuboidShaoe0(6.075f, 1.55f, 3.475f, 6.125f, 2.975f, 3.5f);
    private static final VoxelShape NORTH_PART11 = IKSGVoxelShapeUtil.makeCuboidShaoe0(4.65f, 1.75f, 3.4f, 5.45f, 2.55f, 3.5f);
    private static final VoxelShape NORTH_PART12 = IKSGVoxelShapeUtil.makeCuboidShaoe0(11.0f, 3.7f, 8.8f, 11.05f, 4.4f, 10.2f);
    private static final VoxelShape NORTH_PART13 = IKSGVoxelShapeUtil.makeCuboidShaoe0(6.5f, 4.7f, 8.5f, 8.5f, 5.2f, 10.5f);
    private static final VoxelShape NORTH_PART14 = IKSGVoxelShapeUtil.makeCuboidShaoe0(6.5f, 5.7f, 9.5f, 7.0f, 6.2f, 10.0f);
    private static final VoxelShape NORTH_PART15 = IKSGVoxelShapeUtil.makeCuboidShaoe0(6.5f, 5.2f, 10.0f, 7.0f, 5.7f, 10.5f);
    private static final VoxelShape NORTH_PART16 = IKSGVoxelShapeUtil.makeCuboidShaoe0(7.0f, 6.2f, 9.5f, 7.5f, 6.7f, 10.0f);
    private static final VoxelShape NORTH_PART17 = IKSGVoxelShapeUtil.makeCuboidShaoe0(7.0f, 5.7f, 10.0f, 7.5f, 6.2f, 10.5f);
    private static final VoxelShape NORTH_PART18 = IKSGVoxelShapeUtil.makeCuboidShaoe0(8.0f, 5.7f, 9.5f, 8.5f, 6.2f, 10.0f);
    private static final VoxelShape NORTH_PART19 = IKSGVoxelShapeUtil.makeCuboidShaoe0(7.5f, 6.2f, 9.5f, 8.0f, 6.7f, 10.0f);
    private static final VoxelShape NORTH_PART20 = IKSGVoxelShapeUtil.makeCuboidShaoe0(7.5f, 5.7f, 10.0f, 8.0f, 6.2f, 10.5f);
    private static final VoxelShape NORTH_PART21 = IKSGVoxelShapeUtil.makeCuboidShaoe0(8.0f, 5.2f, 10.0f, 8.5f, 5.7f, 10.5f);
    private static final VoxelShape NORTH_PART22 = IKSGVoxelShapeUtil.makeCuboidShaoe0(7.25f, 6.7f, 9.25f, 7.75f, 7.7f, 9.75f);
    private static final VoxelShape NORTH_PART23 = IKSGVoxelShapeUtil.makeCuboidShaoe0(7.5f, 6.2f, 9.0f, 8.0f, 6.7f, 9.5f);
    private static final VoxelShape NORTH_PART24 = IKSGVoxelShapeUtil.makeCuboidShaoe0(8.0f, 5.2f, 8.5f, 8.5f, 5.7f, 9.0f);
    private static final VoxelShape NORTH_PART25 = IKSGVoxelShapeUtil.makeCuboidShaoe0(7.5f, 5.7f, 8.5f, 8.0f, 6.2f, 9.0f);
    private static final VoxelShape NORTH_PART26 = IKSGVoxelShapeUtil.makeCuboidShaoe0(8.0f, 5.7f, 9.0f, 8.5f, 6.2f, 9.5f);
    private static final VoxelShape NORTH_PART27 = IKSGVoxelShapeUtil.makeCuboidShaoe0(6.5f, 5.7f, 9.0f, 7.0f, 6.2f, 9.5f);
    private static final VoxelShape NORTH_PART28 = IKSGVoxelShapeUtil.makeCuboidShaoe0(7.0f, 6.2f, 9.0f, 7.5f, 6.7f, 9.5f);
    private static final VoxelShape NORTH_PART29 = IKSGVoxelShapeUtil.makeCuboidShaoe0(7.0f, 5.7f, 8.5f, 7.5f, 6.2f, 9.0f);
    private static final VoxelShape NORTH_PART30 = IKSGVoxelShapeUtil.makeCuboidShaoe0(6.5f, 5.2f, 8.5f, 7.0f, 5.7f, 9.0f);
    private static final VoxelShape NORTH_PART31 = IKSGVoxelShapeUtil.makeCuboidShaoe0(10.0f, 3.1f, 10.5f, 11.0f, 3.2f, 11.5f);
    private static final VoxelShape NORTH_PART32 = IKSGVoxelShapeUtil.makeCuboidShaoe0(10.0f, 3.1f, 7.5f, 11.0f, 3.2f, 8.5f);
    private static final VoxelShape NORTH_PART33 = IKSGVoxelShapeUtil.makeCuboidShaoe0(4.0f, 3.1f, 7.5f, 5.0f, 3.2f, 8.5f);
    private static final VoxelShape NORTH_PART34 = IKSGVoxelShapeUtil.makeCuboidShaoe0(4.0f, 3.1f, 10.5f, 5.0f, 3.2f, 11.5f);
    private static final VoxelShape NORTH_PART35 = IKSGVoxelShapeUtil.makeCuboidShaoe0(4.0f, 3.2f, 7.5f, 11.0f, 4.7f, 11.5f);

    public static final VoxelShape NORTH_AXIS_AABB = VoxelShapes.or(NORTH_PART1, NORTH_PART2, NORTH_PART3, NORTH_PART4, NORTH_PART5, NORTH_PART6, NORTH_PART7, NORTH_PART8, NORTH_PART9, NORTH_PART10, NORTH_PART11, NORTH_PART12, NORTH_PART13, NORTH_PART14, NORTH_PART15, NORTH_PART16, NORTH_PART17, NORTH_PART18, NORTH_PART19, NORTH_PART20, NORTH_PART21, NORTH_PART22, NORTH_PART23, NORTH_PART24, NORTH_PART25, NORTH_PART26, NORTH_PART27, NORTH_PART28, NORTH_PART29, NORTH_PART30, NORTH_PART31, NORTH_PART32, NORTH_PART33, NORTH_PART34, NORTH_PART35);
    public static final VoxelShape SOUTH_AXIS_AABB = IKSGVoxelShapeUtil.rotateDirection(NORTH_AXIS_AABB, Direction.SOUTH);
    public static final VoxelShape EAST_AXIS_AABB = IKSGVoxelShapeUtil.rotateDirection(NORTH_AXIS_AABB, Direction.EAST);
    public static final VoxelShape WEST_AXIS_AABB = IKSGVoxelShapeUtil.rotateDirection(NORTH_AXIS_AABB, Direction.WEST);
}

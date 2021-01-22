package red.felnull.imp.block.voxelshape;

import net.minecraft.util.Direction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import red.felnull.otyacraftengine.util.IKSGVoxelShapeUtil;

public class CassetteStorageVoxelShape {
    private static final VoxelShape NORTH_PART1 = IKSGVoxelShapeUtil.makeCuboidShaoe0(0.45f, 0.0f, 4.9f, 1.85f, 16.0f, 11.1f);
    private static final VoxelShape NORTH_PART2 = IKSGVoxelShapeUtil.makeCuboidShaoe0(14.25f, 0.0f, 4.9f, 15.65f, 16.0f, 11.1f);
    private static final VoxelShape NORTH_PART3 = IKSGVoxelShapeUtil.makeCuboidShaoe0(1.85f, 0.0f, 4.9f, 14.3f, 16.0f, 5.9f);
    private static final VoxelShape NORTH_PART4 = IKSGVoxelShapeUtil.makeCuboidShaoe0(1.85f, 0.0f, 5.9f, 14.25f, 0.6f, 11.1f);
    private static final VoxelShape NORTH_PART5 = IKSGVoxelShapeUtil.makeCuboidShaoe0(1.85f, 15.4f, 5.9f, 14.25f, 16.0f, 11.1f);

    public static final VoxelShape NORTH_AXIS_AABB = VoxelShapes.or(NORTH_PART1, NORTH_PART2, NORTH_PART3, NORTH_PART4, NORTH_PART5);
    public static final VoxelShape SOUTH_AXIS_AABB = IKSGVoxelShapeUtil.rotateDirection(NORTH_AXIS_AABB, Direction.SOUTH);
    public static final VoxelShape EAST_AXIS_AABB = IKSGVoxelShapeUtil.rotateDirection(NORTH_AXIS_AABB, Direction.EAST);
    public static final VoxelShape WEST_AXIS_AABB = IKSGVoxelShapeUtil.rotateDirection(NORTH_AXIS_AABB, Direction.WEST);

    private static final VoxelShape NORTH_WALL_PART1 = IKSGVoxelShapeUtil.makeCuboidShaoe0(0.45f, 0.0f, -0.1f, 1.85f, 16.0f, 6.2f);
    private static final VoxelShape NORTH_WALL_PART2 = IKSGVoxelShapeUtil.makeCuboidShaoe0(14.25f, 0.0f, -0.1f, 15.65f, 16.0f, 6.2f);
    private static final VoxelShape NORTH_WALL_PART3 = IKSGVoxelShapeUtil.makeCuboidShaoe0(1.85f, 0.0f, -0.1f, 14.3f, 16.0f, 1.0f);
    private static final VoxelShape NORTH_WALL_PART4 = IKSGVoxelShapeUtil.makeCuboidShaoe0(1.85f, 0.0f, 0.9f, 14.25f, 0.6f, 6.2f);
    private static final VoxelShape NORTH_WALL_PART5 = IKSGVoxelShapeUtil.makeCuboidShaoe0(1.85f, 15.4f, 0.9f, 14.25f, 16.0f, 6.2f);

    public static final VoxelShape NORTH_WALL_AXIS_AABB = VoxelShapes.or(NORTH_WALL_PART1, NORTH_WALL_PART2, NORTH_WALL_PART3, NORTH_WALL_PART4, NORTH_WALL_PART5);
    public static final VoxelShape SOUTH_WALL_AXIS_AABB = IKSGVoxelShapeUtil.rotateDirection(NORTH_WALL_AXIS_AABB, Direction.SOUTH);
    public static final VoxelShape EAST_WALL_AXIS_AABB = IKSGVoxelShapeUtil.rotateDirection(NORTH_WALL_AXIS_AABB, Direction.EAST);
    public static final VoxelShape WEST_WALL_AXIS_AABB = IKSGVoxelShapeUtil.rotateDirection(NORTH_WALL_AXIS_AABB, Direction.WEST);


}

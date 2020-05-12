package net.morimori.imp.block;

import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.morimori.imp.util.VoxelShapeHelper;

public class CassetteDeckVoxelShape {
	private static final VoxelShape WEST_PART1 = VoxelShapeHelper.addCuboidShaoe0(1, 0, 3, 2, 0.5d, 2);
	private static final VoxelShape WEST_PART2 = VoxelShapeHelper.addCuboidShaoe0(1, 0, 11, 2, 0.5d, 2);
	private static final VoxelShape WEST_PART3 = VoxelShapeHelper.addCuboidShaoe0(13, 0, 3, 2, 0.5d, 2);
	private static final VoxelShape WEST_PART4 = VoxelShapeHelper.addCuboidShaoe0(13, 0, 11, 2, 0.5d, 2);
	private static final VoxelShape WEST_PART5 = VoxelShapeHelper.addCuboidShaoe0(0, 0.5d, 2.5d, 16, 5, 11);
	private static final VoxelShape WEST_PART6 = VoxelShapeHelper.addCuboidShaoe0(5, 1, 2.3d, 1, 0.3d, 0.2d);
	private static final VoxelShape WEST_PART7 = VoxelShapeHelper.addCuboidShaoe0(5, 2, 2.3d, 2.5d, 0.7d, 0.2d);
	private static final VoxelShape WEST_PART8 = VoxelShapeHelper.addCuboidShaoe0(5, 2.85d, 2.3d, 2.5d, 0.2d, 0.2d);
	private static final VoxelShape WEST_PART9 = VoxelShapeHelper.addCuboidShaoe0(5.7d, 3.2d, 2.3d, 0.8d, 0.3d, 0.2d);
	private static final VoxelShape WEST_PART10 = VoxelShapeHelper.addCuboidShaoe0(6.7d, 3.2d, 2.3d, 0.8d, 0.3d, 0.2d);
	private static final VoxelShape WEST_PART11 = VoxelShapeHelper.addCuboidShaoe0(7.6d, 3.2d, 2.3d, 0.8d, 0.3d, 0.2d);
	private static final VoxelShape WEST_PART12 = VoxelShapeHelper.addCuboidShaoe0(8.5d, 3.2d, 2.3d, 0.8d, 0.3d, 0.2d);
	private static final VoxelShape WEST_PART13 = VoxelShapeHelper.addCuboidShaoe0(9.5d, 3.2d, 2.3d, 0.8d, 0.3d, 0.2d);
	private static final VoxelShape WEST_PART14 = VoxelShapeHelper.addCuboidShaoe0(8.5d, 2.85d, 2.3d, 2.5d, 0.2d, 0.2d);
	private static final VoxelShape WEST_PART15 = VoxelShapeHelper.addCuboidShaoe0(8.5d, 2, 2.3d, 2.5d, 0.7d, 0.2d);
	private static final VoxelShape WEST_PART16 = VoxelShapeHelper.addCuboidShaoe0(7.5d, 1.5d, 2.2d, 1, 1, 0.3d);
	private static final VoxelShape WEST_PART17 = VoxelShapeHelper.addCuboidShaoe0(10, 1, 2.3d, 1, 0.3d, 0.2d);
	private static final VoxelShape WEST_PART18 = VoxelShapeHelper.addCuboidShaoe0(14, 0.75d, 2.3d, 0.5d, 0.5d, 0.2d);
	private static final VoxelShape WEST_PART19 = VoxelShapeHelper.addCuboidShaoe0(0.5d, 5.5d, 3, 2, 0.5d, 2);
	private static final VoxelShape WEST_PART20 = VoxelShapeHelper.addCuboidShaoe0(0.5d, 6, 3, 0.5d, 0.5d, 0.5d);
	private static final VoxelShape WEST_PART21 = VoxelShapeHelper.addCuboidShaoe0(0.5d, 6, 4.5d, 0.5d, 0.5d, 0.5d);
	private static final VoxelShape WEST_PART22 = VoxelShapeHelper.addCuboidShaoe0(2, 6, 3, 0.5d, 0.5d, 0.5d);
	private static final VoxelShape WEST_PART23 = VoxelShapeHelper.addCuboidShaoe0(2, 6, 4.5d, 0.5d, 0.5d, 0.5d);
	private static final VoxelShape WEST_PART24 = VoxelShapeHelper.addCuboidShaoe0(1, 6.5d, 3, 1, 0.5d, 0.5d);
	private static final VoxelShape WEST_PART25 = VoxelShapeHelper.addCuboidShaoe0(0.5d, 6.5d, 3.5d, 0.5d, 0.5d, 1);
	private static final VoxelShape WEST_PART26 = VoxelShapeHelper.addCuboidShaoe0(1, 6.5d, 4.5d, 1, 0.5d, 0.5d);
	private static final VoxelShape WEST_PART27 = VoxelShapeHelper.addCuboidShaoe0(2, 6.5d, 3.5d, 0.5d, 0.5d, 1);
	private static final VoxelShape WEST_PART28 = VoxelShapeHelper.addCuboidShaoe0(1, 7, 3.5d, 1, 0.5d, 1);
	private static final VoxelShape WEST_PART29 = VoxelShapeHelper.addCuboidShaoe0(1.25, 7.5d, 3.75d, 0.5d, 1, 0.5d);

	protected static final VoxelShape WEST_AXIS_AABB = VoxelShapes.or(WEST_PART1, WEST_PART2, WEST_PART3, WEST_PART4,
			WEST_PART5, WEST_PART6, WEST_PART7, WEST_PART8, WEST_PART9, WEST_PART10, WEST_PART11, WEST_PART12,
			WEST_PART13, WEST_PART14, WEST_PART15, WEST_PART16, WEST_PART17, WEST_PART18, WEST_PART19, WEST_PART20,
			WEST_PART21, WEST_PART22, WEST_PART23, WEST_PART24, WEST_PART25, WEST_PART26, WEST_PART27, WEST_PART28,
			WEST_PART29);

	private static final VoxelShape EAST_PART1 = VoxelShapeHelper.addCuboidShaoe180(1, 0, 3, 2, 0.5d, 2);
	private static final VoxelShape EAST_PART2 = VoxelShapeHelper.addCuboidShaoe180(1, 0, 11, 2, 0.5d, 2);
	private static final VoxelShape EAST_PART3 = VoxelShapeHelper.addCuboidShaoe180(13, 0, 3, 2, 0.5d, 2);
	private static final VoxelShape EAST_PART4 = VoxelShapeHelper.addCuboidShaoe180(13, 0, 11, 2, 0.5d, 2);
	private static final VoxelShape EAST_PART5 = VoxelShapeHelper.addCuboidShaoe180(0, 0.5d, 2.5d, 16, 5, 11);
	private static final VoxelShape EAST_PART6 = VoxelShapeHelper.addCuboidShaoe180(5, 1, 2.3d, 1, 0.3d, 0.2d);
	private static final VoxelShape EAST_PART7 = VoxelShapeHelper.addCuboidShaoe180(5, 2, 2.3d, 2.5d, 0.7d, 0.2d);
	private static final VoxelShape EAST_PART8 = VoxelShapeHelper.addCuboidShaoe180(5, 2.85d, 2.3d, 2.5d, 0.2d, 0.2d);
	private static final VoxelShape EAST_PART9 = VoxelShapeHelper.addCuboidShaoe180(5.7d, 3.2d, 2.3d, 0.8d, 0.3d, 0.2d);
	private static final VoxelShape EAST_PART10 = VoxelShapeHelper.addCuboidShaoe180(6.7d, 3.2d, 2.3d, 0.8d, 0.3d,
			0.2d);
	private static final VoxelShape EAST_PART11 = VoxelShapeHelper.addCuboidShaoe180(7.6d, 3.2d, 2.3d, 0.8d, 0.3d,
			0.2d);
	private static final VoxelShape EAST_PART12 = VoxelShapeHelper.addCuboidShaoe180(8.5d, 3.2d, 2.3d, 0.8d, 0.3d,
			0.2d);
	private static final VoxelShape EAST_PART13 = VoxelShapeHelper.addCuboidShaoe180(9.5d, 3.2d, 2.3d, 0.8d, 0.3d,
			0.2d);
	private static final VoxelShape EAST_PART14 = VoxelShapeHelper.addCuboidShaoe180(8.5d, 2.85d, 2.3d, 2.5d, 0.2d,
			0.2d);
	private static final VoxelShape EAST_PART15 = VoxelShapeHelper.addCuboidShaoe180(8.5d, 2, 2.3d, 2.5d, 0.7d, 0.2d);
	private static final VoxelShape EAST_PART16 = VoxelShapeHelper.addCuboidShaoe180(7.5d, 1.5d, 2.2d, 1, 1, 0.3d);
	private static final VoxelShape EAST_PART17 = VoxelShapeHelper.addCuboidShaoe180(10, 1, 2.3d, 1, 0.3d, 0.2d);
	private static final VoxelShape EAST_PART18 = VoxelShapeHelper.addCuboidShaoe180(14, 0.75d, 2.3d, 0.5d, 0.5d, 0.2d);
	private static final VoxelShape EAST_PART19 = VoxelShapeHelper.addCuboidShaoe180(0.5d, 5.5d, 3, 2, 0.5d, 2);
	private static final VoxelShape EAST_PART20 = VoxelShapeHelper.addCuboidShaoe180(0.5d, 6, 3, 0.5d, 0.5d, 0.5d);
	private static final VoxelShape EAST_PART21 = VoxelShapeHelper.addCuboidShaoe180(0.5d, 6, 4.5d, 0.5d, 0.5d, 0.5d);
	private static final VoxelShape EAST_PART22 = VoxelShapeHelper.addCuboidShaoe180(2, 6, 3, 0.5d, 0.5d, 0.5d);
	private static final VoxelShape EAST_PART23 = VoxelShapeHelper.addCuboidShaoe180(2, 6, 4.5d, 0.5d, 0.5d, 0.5d);
	private static final VoxelShape EAST_PART24 = VoxelShapeHelper.addCuboidShaoe180(1, 6.5d, 3, 1, 0.5d, 0.5d);
	private static final VoxelShape EAST_PART25 = VoxelShapeHelper.addCuboidShaoe180(0.5d, 6.5d, 3.5d, 0.5d, 0.5d, 1);
	private static final VoxelShape EAST_PART26 = VoxelShapeHelper.addCuboidShaoe180(1, 6.5d, 4.5d, 1, 0.5d, 0.5d);
	private static final VoxelShape EAST_PART27 = VoxelShapeHelper.addCuboidShaoe180(2, 6.5d, 3.5d, 0.5d, 0.5d, 1);
	private static final VoxelShape EAST_PART28 = VoxelShapeHelper.addCuboidShaoe180(1, 7, 3.5d, 1, 0.5d, 1);
	private static final VoxelShape EAST_PART29 = VoxelShapeHelper.addCuboidShaoe180(1.25, 7.5d, 3.75d, 0.5d, 1, 0.5d);

	protected static final VoxelShape EAST_AXIS_AABB = VoxelShapes.or(EAST_PART1, EAST_PART2, EAST_PART3, EAST_PART4,
			EAST_PART5, EAST_PART6, EAST_PART7, EAST_PART8, EAST_PART9, EAST_PART10, EAST_PART11, EAST_PART12,
			EAST_PART13, EAST_PART14, EAST_PART15, EAST_PART16, EAST_PART17, EAST_PART18, EAST_PART19, EAST_PART20,
			EAST_PART21, EAST_PART22, EAST_PART23, EAST_PART24, EAST_PART25, EAST_PART26, EAST_PART27, EAST_PART28,
			EAST_PART29);

	private static final VoxelShape NORTH_PART1 = fix270(1, 0, 3, 2, 0.5d, 2);
	private static final VoxelShape NORTH_PART2 = fix270(1, 0, 11, 2, 0.5d, 2);
	private static final VoxelShape NORTH_PART3 = fix270(13, 0, 3, 2, 0.5d, 2);
	private static final VoxelShape NORTH_PART4 = fix270(13, 0, 11, 2, 0.5d, 2);
	private static final VoxelShape NORTH_PART5 = fix270(0, 0.5d, 2.5d, 16, 5, 11);
	private static final VoxelShape NORTH_PART6 = fix270(5, 1, 2.3d, 1, 0.3d, 0.2d);
	private static final VoxelShape NORTH_PART7 = fix270(5, 2, 2.3d, 2.5d, 0.7d, 0.2d);
	private static final VoxelShape NORTH_PART8 = fix270(5, 2.85d, 2.3d, 2.5d, 0.2d, 0.2d);
	private static final VoxelShape NORTH_PART9 = fix270(5.7d, 3.2d, 2.3d, 0.8d, 0.3d,
			0.2d);
	private static final VoxelShape NORTH_PART10 = fix270(6.7d, 3.2d, 2.3d, 0.8d, 0.3d,
			0.2d);
	private static final VoxelShape NORTH_PART11 = fix270(7.6d, 3.2d, 2.3d, 0.8d, 0.3d,
			0.2d);
	private static final VoxelShape NORTH_PART12 = fix270(8.5d, 3.2d, 2.3d, 0.8d, 0.3d,
			0.2d);
	private static final VoxelShape NORTH_PART13 = fix270(9.5d, 3.2d, 2.3d, 0.8d, 0.3d,
			0.2d);
	private static final VoxelShape NORTH_PART14 = fix270(8.5d, 2.85d, 2.3d, 2.5d, 0.2d,
			0.2d);
	private static final VoxelShape NORTH_PART15 = fix270(8.5d, 2, 2.3d, 2.5d, 0.7d, 0.2d);
	private static final VoxelShape NORTH_PART16 = fix270(7.5d, 1.5d, 2.2d, 1, 1, 0.3d);
	private static final VoxelShape NORTH_PART17 = fix270(10, 1, 2.3d, 1, 0.3d, 0.2d);
	private static final VoxelShape NORTH_PART18 = fix270(14, 0.75d, 2.3d, 0.5d, 0.5d,
			0.2d);
	private static final VoxelShape NORTH_PART19 = fix270(0.5d, 5.5d, 3, 2, 0.5d, 2);
	private static final VoxelShape NORTH_PART20 = fix270(0.5d, 6, 3, 0.5d, 0.5d, 0.5d);
	private static final VoxelShape NORTH_PART21 = fix270(0.5d, 6, 4.5d, 0.5d, 0.5d, 0.5d);
	private static final VoxelShape NORTH_PART22 = fix270(2, 6, 3, 0.5d, 0.5d, 0.5d);
	private static final VoxelShape NORTH_PART23 = fix270(2, 6, 4.5d, 0.5d, 0.5d, 0.5d);
	private static final VoxelShape NORTH_PART24 = fix270(1, 6.5d, 3, 1, 0.5d, 0.5d);
	private static final VoxelShape NORTH_PART25 = fix270(0.5d, 6.5d, 3.5d, 0.5d, 0.5d, 1);
	private static final VoxelShape NORTH_PART26 = fix270(1, 6.5d, 4.5d, 1, 0.5d, 0.5d);
	private static final VoxelShape NORTH_PART27 = fix270(2, 6.5d, 3.5d, 0.5d, 0.5d, 1);
	private static final VoxelShape NORTH_PART28 = fix270(1, 7, 3.5d, 1, 0.5d, 1);
	private static final VoxelShape NORTH_PART29 = fix270(1.25, 7.5d, 3.75d, 0.5d, 1, 0.5d);

	protected static final VoxelShape NORTH_AXIS_AABB = VoxelShapes.or(NORTH_PART1, NORTH_PART2, NORTH_PART3,
			NORTH_PART4,
			NORTH_PART5, NORTH_PART6, NORTH_PART7, NORTH_PART8, NORTH_PART9, NORTH_PART10, NORTH_PART11, NORTH_PART12,
			NORTH_PART13, NORTH_PART14, NORTH_PART15, NORTH_PART16, NORTH_PART17, NORTH_PART18, NORTH_PART19,
			NORTH_PART20,
			NORTH_PART21, NORTH_PART22, NORTH_PART23, NORTH_PART24, NORTH_PART25, NORTH_PART26, NORTH_PART27,
			NORTH_PART28,
			NORTH_PART29);

	private static final VoxelShape SOUTH_PART1 = fix90(1, 0, 3, 2, 0.5d, 2);
	private static final VoxelShape SOUTH_PART2 = fix90(1, 0, 11, 2, 0.5d, 2);
	private static final VoxelShape SOUTH_PART3 = fix90(13, 0, 3, 2, 0.5d, 2);
	private static final VoxelShape SOUTH_PART4 = fix90(13, 0, 11, 2, 0.5d, 2);
	private static final VoxelShape SOUTH_PART5 = fix90(0, 0.5d, 2.5d, 16, 5, 11);
	private static final VoxelShape SOUTH_PART6 = fix90(5, 1, 2.3d, 1, 0.3d, 0.2d);
	private static final VoxelShape SOUTH_PART7 = fix90(5, 2, 2.3d, 2.5d, 0.7d, 0.2d);
	private static final VoxelShape SOUTH_PART8 = fix90(5, 2.85d, 2.3d, 2.5d, 0.2d, 0.2d);
	private static final VoxelShape SOUTH_PART9 = fix90(5.7d, 3.2d, 2.3d, 0.8d, 0.3d, 0.2d);
	private static final VoxelShape SOUTH_PART10 = fix90(6.7d, 3.2d, 2.3d, 0.8d, 0.3d,
			0.2d);
	private static final VoxelShape SOUTH_PART11 = fix90(7.6d, 3.2d, 2.3d, 0.8d, 0.3d,
			0.2d);
	private static final VoxelShape SOUTH_PART12 = fix90(8.5d, 3.2d, 2.3d, 0.8d, 0.3d,
			0.2d);
	private static final VoxelShape SOUTH_PART13 = fix90(9.5d, 3.2d, 2.3d, 0.8d, 0.3d,
			0.2d);
	private static final VoxelShape SOUTH_PART14 = fix90(8.5d, 2.85d, 2.3d, 2.5d, 0.2d,
			0.2d);
	private static final VoxelShape SOUTH_PART15 = fix90(8.5d, 2, 2.3d, 2.5d, 0.7d, 0.2d);
	private static final VoxelShape SOUTH_PART16 = fix90(7.5d, 1.5d, 2.2d, 1, 1, 0.3d);
	private static final VoxelShape SOUTH_PART17 = fix90(10, 1, 2.3d, 1, 0.3d, 0.2d);
	private static final VoxelShape SOUTH_PART18 = fix90(14, 0.75d, 2.3d, 0.5d, 0.5d, 0.2d);
	private static final VoxelShape SOUTH_PART19 = fix90(0.5d, 5.5d, 3, 2, 0.5d, 2);
	private static final VoxelShape SOUTH_PART20 = fix90(0.5d, 6, 3, 0.5d, 0.5d, 0.5d);
	private static final VoxelShape SOUTH_PART21 = fix90(0.5d, 6, 4.5d, 0.5d, 0.5d, 0.5d);
	private static final VoxelShape SOUTH_PART22 = fix90(2, 6, 3, 0.5d, 0.5d, 0.5d);
	private static final VoxelShape SOUTH_PART23 = fix90(2, 6, 4.5d, 0.5d, 0.5d, 0.5d);
	private static final VoxelShape SOUTH_PART24 = fix90(1, 6.5d, 3, 1, 0.5d, 0.5d);
	private static final VoxelShape SOUTH_PART25 = fix90(0.5d, 6.5d, 3.5d, 0.5d, 0.5d, 1);
	private static final VoxelShape SOUTH_PART26 = fix90(1, 6.5d, 4.5d, 1, 0.5d, 0.5d);
	private static final VoxelShape SOUTH_PART27 = fix90(2, 6.5d, 3.5d, 0.5d, 0.5d, 1);
	private static final VoxelShape SOUTH_PART28 = fix90(1, 7, 3.5d, 1, 0.5d, 1);
	private static final VoxelShape SOUTH_PART29 = fix90(1.25, 7.5d, 3.75d, 0.5d, 1, 0.5d);

	protected static final VoxelShape SOUTH_AXIS_AABB = VoxelShapes.or(SOUTH_PART1, SOUTH_PART2, SOUTH_PART3,
			SOUTH_PART4,
			SOUTH_PART5, SOUTH_PART6, SOUTH_PART7, SOUTH_PART8, SOUTH_PART9, SOUTH_PART10, SOUTH_PART11, SOUTH_PART12,
			SOUTH_PART13, SOUTH_PART14, SOUTH_PART15, SOUTH_PART16, SOUTH_PART17, SOUTH_PART18, SOUTH_PART19,
			SOUTH_PART20,
			SOUTH_PART21, SOUTH_PART22, SOUTH_PART23, SOUTH_PART24, SOUTH_PART25, SOUTH_PART26, SOUTH_PART27,
			SOUTH_PART28,
			SOUTH_PART29);

	private static VoxelShape fix270(double x1, double y1, double z1, double x2, double y2, double z2) {

		return VoxelShapeHelper.addCuboidShaoe270(16 - x1, y1, z1, -x2, y2, z2);
	}
	private static VoxelShape fix90(double x1, double y1, double z1, double x2, double y2, double z2) {

		return VoxelShapeHelper.addCuboidShaoe90(16 - x1, y1, z1, -x2, y2, z2);
	}
}

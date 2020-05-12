package net.morimori.imp.block;

import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.morimori.imp.util.VoxelShapeHelper;

public class SoundfileUploaderVoxelShape {
	private static final VoxelShape WEST_PART1 = VoxelShapeHelper.makeCuboidShaoe0(0, 0, 3, 1, 0.5d, 4);
	private static final VoxelShape WEST_PART2 = VoxelShapeHelper.makeCuboidShaoe0(3, 0, 3, 4, 0.5d, 4);
	private static final VoxelShape WEST_PART3 = VoxelShapeHelper.makeCuboidShaoe0(0, 0, 12, 1, 0.5d, 13);
	private static final VoxelShape WEST_PART4 = VoxelShapeHelper.makeCuboidShaoe0(3, 0, 12, 4, 0.5d, 13);
	private static final VoxelShape WEST_PART5 = VoxelShapeHelper.makeCuboidShaoe0(0, 0.5d, 3, 4, 12.5d, 13);
	private static final VoxelShape WEST_PART6 = VoxelShapeHelper.makeCuboidShaoe0(6, 0, 3, 15, 0.5d, 8);
	private static final VoxelShape WEST_PART7 = VoxelShapeHelper.makeCuboidShaoe0(9, 0, 10, 12, 0.5d, 13);
	private static final VoxelShape WEST_PART8 = VoxelShapeHelper.makeCuboidShaoe0(10, 0.5d, 11, 11, 3, 12);
	private static final VoxelShape WEST_PART9 = VoxelShapeHelper.makeCuboidShaoe0(5, 3, 11, 16, 10, 12);
	private static final VoxelShape WEST_PART10 = VoxelShapeHelper.makeCuboidShaoe0(1, 12.5d, 7, 3, 13, 9);
	private static final VoxelShape WEST_PART11 = VoxelShapeHelper.makeCuboidShaoe0(1, 13, 7, 1.5d, 13.5d, 7.5d);
	private static final VoxelShape WEST_PART12 = VoxelShapeHelper.makeCuboidShaoe0(2.5d, 13, 7, 3d, 13.5d, 7.5d);
	private static final VoxelShape WEST_PART13 = VoxelShapeHelper.makeCuboidShaoe0(1, 13, 8.5d, 1.5d, 13.5d, 9);
	private static final VoxelShape WEST_PART14 = VoxelShapeHelper.makeCuboidShaoe0(2.5d, 13, 8.5d, 3d, 13.5d, 9);
	private static final VoxelShape WEST_PART15 = VoxelShapeHelper.addCuboidShaoe0(1, 13.5d, 7.5d, 0.5d, 0.5d, 1);
	private static final VoxelShape WEST_PART16 = VoxelShapeHelper.addCuboidShaoe0(2.5, 13.5d, 7.5d, 0.5d, 0.5d, 1);
	private static final VoxelShape WEST_PART17 = VoxelShapeHelper.addCuboidShaoe0(1.5d, 13.5d, 7, 1, 0.5d, 0.5d);
	private static final VoxelShape WEST_PART18 = VoxelShapeHelper.addCuboidShaoe0(1.5d, 13.5d, 8.5d, 1, 0.5d, 0.5d);
	private static final VoxelShape WEST_PART19 = VoxelShapeHelper.addCuboidShaoe0(1.5d, 14d, 7.5d, 1, 0.5d, 1);
	private static final VoxelShape WEST_PART20 = VoxelShapeHelper.addCuboidShaoe0(1.75d, 14.5d, 7.75d, 0.5d, 1, 0.5d);

	protected static final VoxelShape WEST_AXIS_AABB = VoxelShapes.or(WEST_PART1, WEST_PART2, WEST_PART3, WEST_PART4,
			WEST_PART5, WEST_PART6, WEST_PART7, WEST_PART8, WEST_PART9, WEST_PART10, WEST_PART11, WEST_PART12,
			WEST_PART13, WEST_PART14, WEST_PART15, WEST_PART16, WEST_PART17, WEST_PART18, WEST_PART19, WEST_PART20);

	private static final VoxelShape EAST_PART1 = VoxelShapeHelper.makeCuboidShaoe180(0, 0, 3, 1, 0.5d, 4);
	private static final VoxelShape EAST_PART2 = VoxelShapeHelper.makeCuboidShaoe180(3, 0, 3, 4, 0.5d, 4);
	private static final VoxelShape EAST_PART3 = VoxelShapeHelper.makeCuboidShaoe180(0, 0, 12, 1, 0.5d, 13);
	private static final VoxelShape EAST_PART4 = VoxelShapeHelper.makeCuboidShaoe180(3, 0, 12, 4, 0.5d, 13);
	private static final VoxelShape EAST_PART5 = VoxelShapeHelper.makeCuboidShaoe180(0, 0.5d, 3, 4, 12.5d, 13);
	private static final VoxelShape EAST_PART6 = VoxelShapeHelper.makeCuboidShaoe180(6, 0, 3, 15, 0.5d, 8);
	private static final VoxelShape EAST_PART7 = VoxelShapeHelper.makeCuboidShaoe180(9, 0, 10, 12, 0.5d, 13);
	private static final VoxelShape EAST_PART8 = VoxelShapeHelper.makeCuboidShaoe180(10, 0.5d, 11, 11, 3, 12);
	private static final VoxelShape EAST_PART9 = VoxelShapeHelper.makeCuboidShaoe180(5, 3, 11, 16, 10, 12);
	private static final VoxelShape EAST_PART10 = VoxelShapeHelper.makeCuboidShaoe180(1, 12.5d, 7, 3, 13, 9);
	private static final VoxelShape EAST_PART11 = VoxelShapeHelper.makeCuboidShaoe180(1, 13, 7, 1.5d, 13.5d, 7.5d);
	private static final VoxelShape EAST_PART12 = VoxelShapeHelper.makeCuboidShaoe180(2.5d, 13, 7, 3d, 13.5d, 7.5d);
	private static final VoxelShape EAST_PART13 = VoxelShapeHelper.makeCuboidShaoe180(1, 13, 8.5d, 1.5d, 13.5d, 9);
	private static final VoxelShape EAST_PART14 = VoxelShapeHelper.makeCuboidShaoe180(2.5d, 13, 8.5d, 3d, 13.5d, 9);
	private static final VoxelShape EAST_PART15 = VoxelShapeHelper.addCuboidShaoe180(1, 13.5d, 7.5d, 0.5d, 0.5d, 1);
	private static final VoxelShape EAST_PART16 = VoxelShapeHelper.addCuboidShaoe180(2.5, 13.5d, 7.5d, 0.5d, 0.5d, 1);
	private static final VoxelShape EAST_PART17 = VoxelShapeHelper.addCuboidShaoe180(1.5d, 13.5d, 7, 1, 0.5d, 0.5d);
	private static final VoxelShape EAST_PART18 = VoxelShapeHelper.addCuboidShaoe180(1.5d, 13.5d, 8.5d, 1, 0.5d, 0.5d);
	private static final VoxelShape EAST_PART19 = VoxelShapeHelper.addCuboidShaoe180(1.5d, 14d, 7.5d, 1, 0.5d, 1);
	private static final VoxelShape EAST_PART20 = VoxelShapeHelper.addCuboidShaoe180(1.75d, 14.5d, 7.75d, 0.5d, 1,
			0.5d);

	protected static final VoxelShape EAST_AXIS_AABB = VoxelShapes.or(EAST_PART1, EAST_PART2, EAST_PART3, EAST_PART4,
			EAST_PART5, EAST_PART6, EAST_PART7, EAST_PART8, EAST_PART9, EAST_PART10, EAST_PART11, EAST_PART12,
			EAST_PART13, EAST_PART14, EAST_PART15, EAST_PART16, EAST_PART17, EAST_PART18, EAST_PART19, EAST_PART20);

	private static final VoxelShape SOUTH_PART1 = fixCuboidShaoe1(0, 0, 3, 1, 0.5d, 4);
	private static final VoxelShape SOUTH_PART2 = fixCuboidShaoe1(3, 0, 3, 4, 0.5d, 4);
	private static final VoxelShape SOUTH_PART3 = fixCuboidShaoe1(0, 0, 12, 1, 0.5d, 13);
	private static final VoxelShape SOUTH_PART4 = fixCuboidShaoe1(3, 0, 12, 4, 0.5d, 13);
	private static final VoxelShape SOUTH_PART5 = fixCuboidShaoe1(0, 0.5d, 3, 4, 12.5d, 13);
	private static final VoxelShape SOUTH_PART6 = fixCuboidShaoe1(6, 0, 3, 15, 0.5d, 8);
	private static final VoxelShape SOUTH_PART7 = fixCuboidShaoe1(9, 0, 10, 12, 0.5d, 13);
	private static final VoxelShape SOUTH_PART8 = fixCuboidShaoe1(10, 0.5d, 11, 11, 3, 12);
	private static final VoxelShape SOUTH_PART9 = fixCuboidShaoe1(5, 3, 11, 16, 10, 12);
	private static final VoxelShape SOUTH_PART10 = fixCuboidShaoe1(1, 12.5d, 7, 3, 13, 9);
	private static final VoxelShape SOUTH_PART11 = fixCuboidShaoe1(1, 13, 7, 1.5d, 13.5d, 7.5d);
	private static final VoxelShape SOUTH_PART12 = fixCuboidShaoe1(2.5d, 13, 7, 3d, 13.5d, 7.5d);
	private static final VoxelShape SOUTH_PART13 = fixCuboidShaoe1(1, 13, 8.5d, 1.5d, 13.5d, 9);
	private static final VoxelShape SOUTH_PART14 = fixCuboidShaoe1(2.5d, 13, 8.5d, 3d, 13.5d, 9);
	private static final VoxelShape SOUTH_PART15 = VoxelShapeHelper.addCuboidShaoe270(1, 13.5d, 7.5d, 0.5d, 0.5d, 1);
	private static final VoxelShape SOUTH_PART16 = VoxelShapeHelper.addCuboidShaoe270(2.5, 13.5d, 7.5d, 0.5d, 0.5d, 1);
	private static final VoxelShape SOUTH_PART17 = VoxelShapeHelper.addCuboidShaoe270(1.5d, 13.5d, 7, 1, 0.5d, 0.5d);
	private static final VoxelShape SOUTH_PART18 = VoxelShapeHelper.addCuboidShaoe270(1.5d, 13.5d, 8.5d, 1, 0.5d, 0.5d);
	private static final VoxelShape SOUTH_PART19 = VoxelShapeHelper.addCuboidShaoe270(1.5d, 14d, 7.5d, 1, 0.5d, 1);
	private static final VoxelShape SOUTH_PART20 = VoxelShapeHelper.addCuboidShaoe270(1.75d, 14.5d, 7.75d, 0.5d, 1,
			0.5d);

	protected static final VoxelShape SOUTH_AXIS_AABB = VoxelShapes.or(SOUTH_PART1, SOUTH_PART2, SOUTH_PART3,
			SOUTH_PART4, SOUTH_PART5, SOUTH_PART6, SOUTH_PART7, SOUTH_PART8, SOUTH_PART9, SOUTH_PART10, SOUTH_PART11,
			SOUTH_PART12, SOUTH_PART13, SOUTH_PART14, SOUTH_PART15, SOUTH_PART16, SOUTH_PART17, SOUTH_PART18,
			SOUTH_PART19, SOUTH_PART20);

	private static final VoxelShape NORTH_PART1 = fixCuboidShaoe2(0, 0, 3, 1, 0.5d, 4);
	private static final VoxelShape NORTH_PART2 = fixCuboidShaoe2(3, 0, 3, 4, 0.5d, 4);
	private static final VoxelShape NORTH_PART3 = fixCuboidShaoe2(0, 0, 12, 1, 0.5d, 13);
	private static final VoxelShape NORTH_PART4 = fixCuboidShaoe2(3, 0, 12, 4, 0.5d, 13);
	private static final VoxelShape NORTH_PART5 = fixCuboidShaoe2(0, 0.5d, 3, 4, 12.5d, 13);
	private static final VoxelShape NORTH_PART6 = fixCuboidShaoe2(6, 0, 3, 15, 0.5d, 8);
	private static final VoxelShape NORTH_PART7 = fixCuboidShaoe2(9, 0, 10, 12, 0.5d, 13);
	private static final VoxelShape NORTH_PART8 = fixCuboidShaoe2(10, 0.5d, 11, 11, 3, 12);
	private static final VoxelShape NORTH_PART9 = fixCuboidShaoe2(5, 3, 11, 16, 10, 12);
	private static final VoxelShape NORTH_PART10 = fixCuboidShaoe2(1, 12.5d, 7, 3, 13, 9);
	private static final VoxelShape NORTH_PART11 = fixCuboidShaoe2(1, 13, 7, 1.5d, 13.5d, 7.5d);
	private static final VoxelShape NORTH_PART12 = fixCuboidShaoe2(2.5d, 13, 7, 3d, 13.5d, 7.5d);
	private static final VoxelShape NORTH_PART13 = fixCuboidShaoe2(1, 13, 8.5d, 1.5d, 13.5d, 9);
	private static final VoxelShape NORTH_PART14 = fixCuboidShaoe2(2.5d, 13, 8.5d, 3d, 13.5d, 9);
	private static final VoxelShape NORTH_PART15 = VoxelShapeHelper.addCuboidShaoe90(1, 13.5d, 7.5d, 0.5d, 0.5d, 1);
	private static final VoxelShape NORTH_PART16 = VoxelShapeHelper.addCuboidShaoe90(2.5, 13.5d, 7.5d, 0.5d, 0.5d, 1);
	private static final VoxelShape NORTH_PART17 = VoxelShapeHelper.addCuboidShaoe90(1.5d, 13.5d, 7, 1, 0.5d, 0.5d);
	private static final VoxelShape NORTH_PART18 = VoxelShapeHelper.addCuboidShaoe90(1.5d, 13.5d, 8.5d, 1, 0.5d, 0.5d);
	private static final VoxelShape NORTH_PART19 = VoxelShapeHelper.addCuboidShaoe90(1.5d, 14d, 7.5d, 1, 0.5d, 1);
	private static final VoxelShape NORTH_PART20 = VoxelShapeHelper
			.addCuboidShaoe90(1.75d, 14.5d, 7.75d, 0.5d, 1,
					0.5d);

	protected static final VoxelShape NORTH_AXIS_AABB = VoxelShapes.or(NORTH_PART1, NORTH_PART2, NORTH_PART3,
			NORTH_PART4, NORTH_PART5, NORTH_PART6, NORTH_PART7, NORTH_PART8, NORTH_PART9, NORTH_PART10, NORTH_PART11,
			NORTH_PART12, NORTH_PART13, NORTH_PART14, NORTH_PART15, NORTH_PART16, NORTH_PART17, NORTH_PART18,
			NORTH_PART19, NORTH_PART20);

	private static VoxelShape fixCuboidShaoe1(double x1, double y1, double z1, double x2, double y2, double z2) {
		return VoxelShapeHelper.makeCuboidShaoe90(16 - x1, y1, z1, 16 - x2, y2, z2);
	}
	private static VoxelShape fixCuboidShaoe2(double x1, double y1, double z1, double x2, double y2, double z2) {
		return VoxelShapeHelper.makeCuboidShaoe270(16 - x1, y1, z1, 16 - x2, y2, z2);
	}

}

package net.morimori.imp.block;

import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.morimori.imp.util.VoxelShapeHelper;

public class BoomboxVoxelShape {

	private static final VoxelShape SOUTH_PART1 = fixCuboidShaoe1(0, 0, 5, 16, 9, 11);
	private static final VoxelShape SOUTH_PART2 = fixCuboidShaoe1(1.5d, 1.5d, 4, 5.5d, 5.5d, 5);
	private static final VoxelShape SOUTH_PART3 = fixCuboidShaoe1(10.5d, 1.5d, 4, 14.5d, 5.5d, 5);
	private static final VoxelShape SOUTH_PART4 = fixCuboidShaoe1(1, 5.8d, 4.9d, 15, 7.8d, 5);
	private static final VoxelShape SOUTH_PART5 = fixCuboidShaoe1(1, 9, 6, 2, 13, 7);
	private static final VoxelShape SOUTH_PART6 = fixCuboidShaoe1(14, 9, 6, 15, 13, 7);
	private static final VoxelShape SOUTH_PART7 = fixCuboidShaoe1(2, 11.5d, 6.25d, 14, 13, 6.75d);
	private static final VoxelShape SOUTH_PART8 = fixCuboidShaoe1(3.2d, 9, 6.5d, 4, 9.9d, 7.3d);
	private static final VoxelShape SOUTH_PART9 = fixCuboidShaoe1(4.2d, 9, 6.5d, 5, 9.9d, 7.3d);
	private static final VoxelShape SOUTH_PART10 = fixCuboidShaoe1(5.2d, 9, 6.5d, 6, 9.9d, 7.3d);
	private static final VoxelShape SOUTH_PART11 = fixCuboidShaoe1(9.2d, 9, 6.5d, 10, 9.9d, 7.3d);
	private static final VoxelShape SOUTH_PART12 = fixCuboidShaoe1(10.2d, 9, 6.5d, 11, 9.9d, 7.3d);
	private static final VoxelShape SOUTH_PART13 = fixCuboidShaoe1(11.2d, 9, 6.5d, 12, 9.9d, 7.3d);
	private static final VoxelShape SOUTH_PART14 = fixCuboidShaoe1(12.2d, 9, 6.5d, 13, 9.9d, 7.3d);
	private static final VoxelShape SOUTH_PART15 = fixCuboidShaoe1(2.5d, 9, 7.5d, 6.5d, 9.2d, 9);
	private static final VoxelShape SOUTH_PART16 = fixCuboidShaoe1(2, 3, 11, 5, 7.2d, 11.2d);
	private static final VoxelShape SOUTH_PART17 = fixCuboidShaoe1(2, 3, 11.2d, 5, 3.2d, 11.4d);
	private static final VoxelShape SOUTH_PART18 = fixCuboidShaoe1(2, 3.4d, 11.2d, 5, 3.6d, 11.4d);
	private static final VoxelShape SOUTH_PART19 = fixCuboidShaoe1(2, 3.8d, 11.2d, 5, 4, 11.4d);
	private static final VoxelShape SOUTH_PART20 = fixCuboidShaoe1(2, 4.2d, 11.2d, 5, 4.4d, 11.4d);
	private static final VoxelShape SOUTH_PART21 = fixCuboidShaoe1(2, 4.6d, 11.2d, 5, 4.8d, 11.4d);
	private static final VoxelShape SOUTH_PART22 = fixCuboidShaoe1(2, 5.0d, 11.2d, 5, 5.2d, 11.4d);
	private static final VoxelShape SOUTH_PART23 = fixCuboidShaoe1(2, 5.4d, 11.2d, 5, 5.6d, 11.4d);
	private static final VoxelShape SOUTH_PART24 = fixCuboidShaoe1(2, 5.8d, 11.2d, 5, 6, 11.4d);
	private static final VoxelShape SOUTH_PART25 = fixCuboidShaoe1(2, 6.2d, 11.2d, 5, 6.4d, 11.4d);
	private static final VoxelShape SOUTH_PART26 = fixCuboidShaoe1(2, 6.6d, 11.2d, 5, 6.8d, 11.4d);
	private static final VoxelShape SOUTH_PART27 = fixCuboidShaoe1(2, 7, 11.2d, 5, 7.2d, 11.4d);
	private static final VoxelShape SOUTH_PART28 = fixCuboidShaoe1(14, 3, 11, 11, 7.2d, 11.2d);
	private static final VoxelShape SOUTH_PART29 = fixCuboidShaoe1(14, 3, 11.2d, 11, 3.2d, 11.4d);
	private static final VoxelShape SOUTH_PART30 = fixCuboidShaoe1(14, 3.4d, 11.2d, 11, 3.6d, 11.4d);
	private static final VoxelShape SOUTH_PART31 = fixCuboidShaoe1(14, 3.8d, 11.2d, 11, 4, 11.4d);
	private static final VoxelShape SOUTH_PART32 = fixCuboidShaoe1(14, 4.2d, 11.2d, 11, 4.4d, 11.4d);
	private static final VoxelShape SOUTH_PART33 = fixCuboidShaoe1(14, 4.6d, 11.2d, 11, 4.8d, 11.4d);
	private static final VoxelShape SOUTH_PART34 = fixCuboidShaoe1(14, 5.0d, 11.2d, 11, 5.2d, 11.4d);
	private static final VoxelShape SOUTH_PART35 = fixCuboidShaoe1(14, 5.4d, 11.2d, 11, 5.6d, 11.4d);
	private static final VoxelShape SOUTH_PART36 = fixCuboidShaoe1(14, 5.8d, 11.2d, 11, 6, 11.4d);
	private static final VoxelShape SOUTH_PART37 = fixCuboidShaoe1(14, 6.2d, 11.2d, 11, 6.4d, 11.4d);
	private static final VoxelShape SOUTH_PART38 = fixCuboidShaoe1(14, 6.6d, 11.2d, 11, 6.8d, 11.4d);
	private static final VoxelShape SOUTH_PART39 = fixCuboidShaoe1(14, 7, 11.2d, 11, 7.2d, 11.4d);

	protected static final VoxelShape SOUTH_AXIS_AABB = VoxelShapes.or(SOUTH_PART1, SOUTH_PART2, SOUTH_PART3,
			SOUTH_PART5, SOUTH_PART6, SOUTH_PART7, SOUTH_PART8, SOUTH_PART9, SOUTH_PART10, SOUTH_PART11, SOUTH_PART4,
			SOUTH_PART12, SOUTH_PART13, SOUTH_PART14, SOUTH_PART15, SOUTH_PART16, SOUTH_PART17, SOUTH_PART18,
			SOUTH_PART19, SOUTH_PART20, SOUTH_PART21, SOUTH_PART22, SOUTH_PART23, SOUTH_PART24, SOUTH_PART25,
			SOUTH_PART26, SOUTH_PART27, SOUTH_PART28, SOUTH_PART29, SOUTH_PART30, SOUTH_PART31, SOUTH_PART32,
			SOUTH_PART33, SOUTH_PART34, SOUTH_PART35, SOUTH_PART36, SOUTH_PART37, SOUTH_PART38, SOUTH_PART39);

	private static final VoxelShape NORTH_PART1 = fixCuboidShaoe2(0, 0, 5, 16, 9, 11);
	private static final VoxelShape NORTH_PART2 = fixCuboidShaoe2(1.5d, 1.5d, 4, 5.5d, 5.5d, 5);
	private static final VoxelShape NORTH_PART3 = fixCuboidShaoe2(10.5d, 1.5d, 4, 14.5d, 5.5d, 5);
	private static final VoxelShape NORTH_PART4 = fixCuboidShaoe2(1, 5.8d, 4.9d, 15, 7.8d, 5);
	private static final VoxelShape NORTH_PART5 = fixCuboidShaoe2(1, 9, 6, 2, 13, 7);
	private static final VoxelShape NORTH_PART6 = fixCuboidShaoe2(14, 9, 6, 15, 13, 7);
	private static final VoxelShape NORTH_PART7 = fixCuboidShaoe2(2, 11.5d, 6.25d, 14, 13, 6.75d);
	private static final VoxelShape NORTH_PART8 = fixCuboidShaoe2(3.2d, 9, 6.5d, 4, 9.9d, 7.3d);
	private static final VoxelShape NORTH_PART9 = fixCuboidShaoe2(4.2d, 9, 6.5d, 5, 9.9d, 7.3d);
	private static final VoxelShape NORTH_PART10 = fixCuboidShaoe2(5.2d, 9, 6.5d, 6, 9.9d, 7.3d);
	private static final VoxelShape NORTH_PART11 = fixCuboidShaoe2(9.2d, 9, 6.5d, 10, 9.9d, 7.3d);
	private static final VoxelShape NORTH_PART12 = fixCuboidShaoe2(10.2d, 9, 6.5d, 11, 9.9d, 7.3d);
	private static final VoxelShape NORTH_PART13 = fixCuboidShaoe2(11.2d, 9, 6.5d, 12, 9.9d, 7.3d);
	private static final VoxelShape NORTH_PART14 = fixCuboidShaoe2(12.2d, 9, 6.5d, 13, 9.9d, 7.3d);
	private static final VoxelShape NORTH_PART15 = fixCuboidShaoe2(2.5d, 9, 7.5d, 6.5d, 9.2d, 9);
	private static final VoxelShape NORTH_PART16 = fixCuboidShaoe2(2, 3, 11, 5, 7.2d, 11.2d);
	private static final VoxelShape NORTH_PART17 = fixCuboidShaoe2(2, 3, 11.2d, 5, 3.2d, 11.4d);
	private static final VoxelShape NORTH_PART18 = fixCuboidShaoe2(2, 3.4d, 11.2d, 5, 3.6d, 11.4d);
	private static final VoxelShape NORTH_PART19 = fixCuboidShaoe2(2, 3.8d, 11.2d, 5, 4, 11.4d);
	private static final VoxelShape NORTH_PART20 = fixCuboidShaoe2(2, 4.2d, 11.2d, 5, 4.4d, 11.4d);
	private static final VoxelShape NORTH_PART21 = fixCuboidShaoe2(2, 4.6d, 11.2d, 5, 4.8d, 11.4d);
	private static final VoxelShape NORTH_PART22 = fixCuboidShaoe2(2, 5.0d, 11.2d, 5, 5.2d, 11.4d);
	private static final VoxelShape NORTH_PART23 = fixCuboidShaoe2(2, 5.4d, 11.2d, 5, 5.6d, 11.4d);
	private static final VoxelShape NORTH_PART24 = fixCuboidShaoe2(2, 5.8d, 11.2d, 5, 6, 11.4d);
	private static final VoxelShape NORTH_PART25 = fixCuboidShaoe2(2, 6.2d, 11.2d, 5, 6.4d, 11.4d);
	private static final VoxelShape NORTH_PART26 = fixCuboidShaoe2(2, 6.6d, 11.2d, 5, 6.8d, 11.4d);
	private static final VoxelShape NORTH_PART27 = fixCuboidShaoe2(2, 7, 11.2d, 5, 7.2d, 11.4d);
	private static final VoxelShape NORTH_PART28 = fixCuboidShaoe2(14, 3, 11, 11, 7.2d, 11.2d);
	private static final VoxelShape NORTH_PART29 = fixCuboidShaoe2(14, 3, 11.2d, 11, 3.2d, 11.4d);
	private static final VoxelShape NORTH_PART30 = fixCuboidShaoe2(14, 3.4d, 11.2d, 11, 3.6d,
			11.4d);
	private static final VoxelShape NORTH_PART31 = fixCuboidShaoe2(14, 3.8d, 11.2d, 11, 4, 11.4d);
	private static final VoxelShape NORTH_PART32 = fixCuboidShaoe2(14, 4.2d, 11.2d, 11, 4.4d,
			11.4d);
	private static final VoxelShape NORTH_PART33 = fixCuboidShaoe2(14, 4.6d, 11.2d, 11, 4.8d,
			11.4d);
	private static final VoxelShape NORTH_PART34 = fixCuboidShaoe2(14, 5.0d, 11.2d, 11, 5.2d,
			11.4d);
	private static final VoxelShape NORTH_PART35 = fixCuboidShaoe2(14, 5.4d, 11.2d, 11, 5.6d,
			11.4d);
	private static final VoxelShape NORTH_PART36 = fixCuboidShaoe2(14, 5.8d, 11.2d, 11, 6, 11.4d);
	private static final VoxelShape NORTH_PART37 = fixCuboidShaoe2(14, 6.2d, 11.2d, 11, 6.4d,
			11.4d);
	private static final VoxelShape NORTH_PART38 = fixCuboidShaoe2(14, 6.6d, 11.2d, 11, 6.8d,
			11.4d);
	private static final VoxelShape NORTH_PART39 = fixCuboidShaoe2(14, 7, 11.2d, 11, 7.2d, 11.4d);

	protected static final VoxelShape NORTH_AXIS_AABB = VoxelShapes.or(NORTH_PART1, NORTH_PART2, NORTH_PART3,
			NORTH_PART5, NORTH_PART6, NORTH_PART7, NORTH_PART8, NORTH_PART9, NORTH_PART10, NORTH_PART11, NORTH_PART4,
			NORTH_PART12, NORTH_PART13, NORTH_PART14, NORTH_PART15, NORTH_PART16, NORTH_PART17, NORTH_PART18,
			NORTH_PART19, NORTH_PART20, NORTH_PART21, NORTH_PART22, NORTH_PART23, NORTH_PART24, NORTH_PART25,
			NORTH_PART26, NORTH_PART27, NORTH_PART28, NORTH_PART29, NORTH_PART30, NORTH_PART31, NORTH_PART32,
			NORTH_PART33, NORTH_PART34, NORTH_PART35, NORTH_PART36, NORTH_PART37, NORTH_PART38, NORTH_PART39);

	private static final VoxelShape EAST_PART1 = fixCuboidShaoe3(0, 0, 5, 16, 9, 11);
	private static final VoxelShape EAST_PART2 = fixCuboidShaoe3(1.5d, 1.5d, 4, 5.5d, 5.5d, 5);
	private static final VoxelShape EAST_PART3 = fixCuboidShaoe3(10.5d, 1.5d, 4, 14.5d, 5.5d, 5);
	private static final VoxelShape EAST_PART4 = fixCuboidShaoe3(1, 5.8d, 4.9d, 15, 7.8d, 5);
	private static final VoxelShape EAST_PART5 = fixCuboidShaoe3(1, 9, 6, 2, 13, 7);
	private static final VoxelShape EAST_PART6 = fixCuboidShaoe3(14, 9, 6, 15, 13, 7);
	private static final VoxelShape EAST_PART7 = fixCuboidShaoe3(2, 11.5d, 6.25d, 14, 13, 6.75d);
	private static final VoxelShape EAST_PART8 = fixCuboidShaoe3(3.2d, 9, 6.5d, 4, 9.9d, 7.3d);
	private static final VoxelShape EAST_PART9 = fixCuboidShaoe3(4.2d, 9, 6.5d, 5, 9.9d, 7.3d);
	private static final VoxelShape EAST_PART10 = fixCuboidShaoe3(5.2d, 9, 6.5d, 6, 9.9d, 7.3d);
	private static final VoxelShape EAST_PART11 = fixCuboidShaoe3(9.2d, 9, 6.5d, 10, 9.9d, 7.3d);
	private static final VoxelShape EAST_PART12 = fixCuboidShaoe3(10.2d, 9, 6.5d, 11, 9.9d, 7.3d);
	private static final VoxelShape EAST_PART13 = fixCuboidShaoe3(11.2d, 9, 6.5d, 12, 9.9d, 7.3d);
	private static final VoxelShape EAST_PART14 = fixCuboidShaoe3(12.2d, 9, 6.5d, 13, 9.9d, 7.3d);
	private static final VoxelShape EAST_PART15 = fixCuboidShaoe3(2.5d, 9, 7.5d, 6.5d, 9.2d, 9);
	private static final VoxelShape EAST_PART16 = fixCuboidShaoe3(2, 3, 11, 5, 7.2d, 11.2d);
	private static final VoxelShape EAST_PART17 = fixCuboidShaoe3(2, 3, 11.2d, 5, 3.2d, 11.4d);
	private static final VoxelShape EAST_PART18 = fixCuboidShaoe3(2, 3.4d, 11.2d, 5, 3.6d, 11.4d);
	private static final VoxelShape EAST_PART19 = fixCuboidShaoe3(2, 3.8d, 11.2d, 5, 4, 11.4d);
	private static final VoxelShape EAST_PART20 = fixCuboidShaoe3(2, 4.2d, 11.2d, 5, 4.4d, 11.4d);
	private static final VoxelShape EAST_PART21 = fixCuboidShaoe3(2, 4.6d, 11.2d, 5, 4.8d, 11.4d);
	private static final VoxelShape EAST_PART22 = fixCuboidShaoe3(2, 5.0d, 11.2d, 5, 5.2d, 11.4d);
	private static final VoxelShape EAST_PART23 = fixCuboidShaoe3(2, 5.4d, 11.2d, 5, 5.6d, 11.4d);
	private static final VoxelShape EAST_PART24 = fixCuboidShaoe3(2, 5.8d, 11.2d, 5, 6, 11.4d);
	private static final VoxelShape EAST_PART25 = fixCuboidShaoe3(2, 6.2d, 11.2d, 5, 6.4d, 11.4d);
	private static final VoxelShape EAST_PART26 = fixCuboidShaoe3(2, 6.6d, 11.2d, 5, 6.8d, 11.4d);
	private static final VoxelShape EAST_PART27 = fixCuboidShaoe3(2, 7, 11.2d, 5, 7.2d, 11.4d);
	private static final VoxelShape EAST_PART28 = fixCuboidShaoe3(14, 3, 11, 11, 7.2d, 11.2d);
	private static final VoxelShape EAST_PART29 = fixCuboidShaoe3(14, 3, 11.2d, 11, 3.2d, 11.4d);
	private static final VoxelShape EAST_PART30 = fixCuboidShaoe3(14, 3.4d, 11.2d, 11, 3.6d, 11.4d);
	private static final VoxelShape EAST_PART31 = fixCuboidShaoe3(14, 3.8d, 11.2d, 11, 4, 11.4d);
	private static final VoxelShape EAST_PART32 = fixCuboidShaoe3(14, 4.2d, 11.2d, 11, 4.4d, 11.4d);
	private static final VoxelShape EAST_PART33 = fixCuboidShaoe3(14, 4.6d, 11.2d, 11, 4.8d, 11.4d);
	private static final VoxelShape EAST_PART34 = fixCuboidShaoe3(14, 5.0d, 11.2d, 11, 5.2d, 11.4d);
	private static final VoxelShape EAST_PART35 = fixCuboidShaoe3(14, 5.4d, 11.2d, 11, 5.6d, 11.4d);
	private static final VoxelShape EAST_PART36 = fixCuboidShaoe3(14, 5.8d, 11.2d, 11, 6, 11.4d);
	private static final VoxelShape EAST_PART37 = fixCuboidShaoe3(14, 6.2d, 11.2d, 11, 6.4d, 11.4d);
	private static final VoxelShape EAST_PART38 = fixCuboidShaoe3(14, 6.6d, 11.2d, 11, 6.8d, 11.4d);
	private static final VoxelShape EAST_PART39 = fixCuboidShaoe3(14, 7, 11.2d, 11, 7.2d, 11.4d);

	protected static final VoxelShape EAST_AXIS_AABB = VoxelShapes.or(EAST_PART1, EAST_PART2, EAST_PART3,
			EAST_PART5, EAST_PART6, EAST_PART7, EAST_PART8, EAST_PART9, EAST_PART10, EAST_PART11, EAST_PART4,
			EAST_PART12, EAST_PART13, EAST_PART14, EAST_PART15, EAST_PART16, EAST_PART17, EAST_PART18,
			EAST_PART19, EAST_PART20, EAST_PART21, EAST_PART22, EAST_PART23, EAST_PART24, EAST_PART25,
			EAST_PART26, EAST_PART27, EAST_PART28, EAST_PART29, EAST_PART30, EAST_PART31, EAST_PART32,
			EAST_PART33, EAST_PART34, EAST_PART35, EAST_PART36, EAST_PART37, EAST_PART38, EAST_PART39);

	private static final VoxelShape WEST_PART1 = fixCuboidShaoe4(0, 0, 5, 16, 9, 11);
	private static final VoxelShape WEST_PART2 = fixCuboidShaoe4(1.5d, 1.5d, 4, 5.5d, 5.5d, 5);
	private static final VoxelShape WEST_PART3 = fixCuboidShaoe4(10.5d, 1.5d, 4, 14.5d, 5.5d, 5);
	private static final VoxelShape WEST_PART4 = fixCuboidShaoe4(1, 5.8d, 4.9d, 15, 7.8d, 5);
	private static final VoxelShape WEST_PART5 = fixCuboidShaoe4(1, 9, 6, 2, 13, 7);
	private static final VoxelShape WEST_PART6 = fixCuboidShaoe4(14, 9, 6, 15, 13, 7);
	private static final VoxelShape WEST_PART7 = fixCuboidShaoe4(2, 11.5d, 6.25d, 14, 13, 6.75d);
	private static final VoxelShape WEST_PART8 = fixCuboidShaoe4(3.2d, 9, 6.5d, 4, 9.9d, 7.3d);
	private static final VoxelShape WEST_PART9 = fixCuboidShaoe4(4.2d, 9, 6.5d, 5, 9.9d, 7.3d);
	private static final VoxelShape WEST_PART10 = fixCuboidShaoe4(5.2d, 9, 6.5d, 6, 9.9d, 7.3d);
	private static final VoxelShape WEST_PART11 = fixCuboidShaoe4(9.2d, 9, 6.5d, 10, 9.9d, 7.3d);
	private static final VoxelShape WEST_PART12 = fixCuboidShaoe4(10.2d, 9, 6.5d, 11, 9.9d, 7.3d);
	private static final VoxelShape WEST_PART13 = fixCuboidShaoe4(11.2d, 9, 6.5d, 12, 9.9d, 7.3d);
	private static final VoxelShape WEST_PART14 = fixCuboidShaoe4(12.2d, 9, 6.5d, 13, 9.9d, 7.3d);
	private static final VoxelShape WEST_PART15 = fixCuboidShaoe4(2.5d, 9, 7.5d, 6.5d, 9.2d, 9);
	private static final VoxelShape WEST_PART16 = fixCuboidShaoe4(2, 3, 11, 5, 7.2d, 11.2d);
	private static final VoxelShape WEST_PART17 = fixCuboidShaoe4(2, 3, 11.2d, 5, 3.2d, 11.4d);
	private static final VoxelShape WEST_PART18 = fixCuboidShaoe4(2, 3.4d, 11.2d, 5, 3.6d, 11.4d);
	private static final VoxelShape WEST_PART19 = fixCuboidShaoe4(2, 3.8d, 11.2d, 5, 4, 11.4d);
	private static final VoxelShape WEST_PART20 = fixCuboidShaoe4(2, 4.2d, 11.2d, 5, 4.4d, 11.4d);
	private static final VoxelShape WEST_PART21 = fixCuboidShaoe4(2, 4.6d, 11.2d, 5, 4.8d, 11.4d);
	private static final VoxelShape WEST_PART22 = fixCuboidShaoe4(2, 5.0d, 11.2d, 5, 5.2d, 11.4d);
	private static final VoxelShape WEST_PART23 = fixCuboidShaoe4(2, 5.4d, 11.2d, 5, 5.6d, 11.4d);
	private static final VoxelShape WEST_PART24 = fixCuboidShaoe4(2, 5.8d, 11.2d, 5, 6, 11.4d);
	private static final VoxelShape WEST_PART25 = fixCuboidShaoe4(2, 6.2d, 11.2d, 5, 6.4d, 11.4d);
	private static final VoxelShape WEST_PART26 = fixCuboidShaoe4(2, 6.6d, 11.2d, 5, 6.8d, 11.4d);
	private static final VoxelShape WEST_PART27 = fixCuboidShaoe4(2, 7, 11.2d, 5, 7.2d, 11.4d);
	private static final VoxelShape WEST_PART28 = fixCuboidShaoe4(14, 3, 11, 11, 7.2d, 11.2d);
	private static final VoxelShape WEST_PART29 = fixCuboidShaoe4(14, 3, 11.2d, 11, 3.2d, 11.4d);
	private static final VoxelShape WEST_PART30 = fixCuboidShaoe4(14, 3.4d, 11.2d, 11, 3.6d, 11.4d);
	private static final VoxelShape WEST_PART31 = fixCuboidShaoe4(14, 3.8d, 11.2d, 11, 4, 11.4d);
	private static final VoxelShape WEST_PART32 = fixCuboidShaoe4(14, 4.2d, 11.2d, 11, 4.4d, 11.4d);
	private static final VoxelShape WEST_PART33 = fixCuboidShaoe4(14, 4.6d, 11.2d, 11, 4.8d, 11.4d);
	private static final VoxelShape WEST_PART34 = fixCuboidShaoe4(14, 5.0d, 11.2d, 11, 5.2d, 11.4d);
	private static final VoxelShape WEST_PART35 = fixCuboidShaoe4(14, 5.4d, 11.2d, 11, 5.6d, 11.4d);
	private static final VoxelShape WEST_PART36 = fixCuboidShaoe4(14, 5.8d, 11.2d, 11, 6, 11.4d);
	private static final VoxelShape WEST_PART37 = fixCuboidShaoe4(14, 6.2d, 11.2d, 11, 6.4d, 11.4d);
	private static final VoxelShape WEST_PART38 = fixCuboidShaoe4(14, 6.6d, 11.2d, 11, 6.8d, 11.4d);
	private static final VoxelShape WEST_PART39 = fixCuboidShaoe4(14, 7, 11.2d, 11, 7.2d, 11.4d);

	protected static final VoxelShape WEST_AXIS_AABB = VoxelShapes.or(WEST_PART1, WEST_PART2, WEST_PART3,
			WEST_PART5, WEST_PART6, WEST_PART7, WEST_PART8, WEST_PART9, WEST_PART10, WEST_PART11, WEST_PART4,
			WEST_PART12, WEST_PART13, WEST_PART14, WEST_PART15, WEST_PART16, WEST_PART17, WEST_PART18,
			WEST_PART19, WEST_PART20, WEST_PART21, WEST_PART22, WEST_PART23, WEST_PART24, WEST_PART25,
			WEST_PART26, WEST_PART27, WEST_PART28, WEST_PART29, WEST_PART30, WEST_PART31, WEST_PART32,
			WEST_PART33, WEST_PART34, WEST_PART35, WEST_PART36, WEST_PART37, WEST_PART38, WEST_PART39);

	private static VoxelShape fixCuboidShaoe1(double x1, double y1, double z1, double x2, double y2, double z2) {
		return VoxelShapeHelper.makeCuboidShaoe0(z1, y1, x1, z2, y2, x2);
	}

	private static VoxelShape fixCuboidShaoe2(double x1, double y1, double z1, double x2, double y2, double z2) {
		return VoxelShapeHelper.makeCuboidShaoe90(16 - x1, y1, 16 - z1, 16 - x2, y2, 16 - z2);
	}

	private static VoxelShape fixCuboidShaoe3(double x1, double y1, double z1, double x2, double y2, double z2) {
		return VoxelShapeHelper.makeCuboidShaoe180(16 - x1, y1, z1, 16 - x2, y2, z2);
	}

	private static VoxelShape fixCuboidShaoe4(double x1, double y1, double z1, double x2, double y2, double z2) {
		return VoxelShapeHelper.makeCuboidShaoe180(x1, y1, 16 - z1, x2, y2, 16 - z2);
	}
}

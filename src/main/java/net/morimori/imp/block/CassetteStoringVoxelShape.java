package net.morimori.imp.block;

import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.morimori.imp.util.VoxelShapeHelper;

public class CassetteStoringVoxelShape {
	private static final VoxelShape EAST_PART1 = VoxelShapeHelper.makeCuboidShaoe0(0.45, 0.6, 0.85, 15.65, 15.4, 1.45);
	private static final VoxelShape EAST_PART2 = VoxelShapeHelper.makeCuboidShaoe0(0.45, 0, 0.85, 15.65, 0.6, 7.05);
	private static final VoxelShape EAST_PART3 = VoxelShapeHelper.makeCuboidShaoe0(0.45, 15.4, 0.85, 15.65, 16, 7.05);
	private static final VoxelShape EAST_PART4 = VoxelShapeHelper.makeCuboidShaoe0(0.45, 0.6, 1.45, 1.05, 15.4, 7.05);
	private static final VoxelShape EAST_PART5 = VoxelShapeHelper.makeCuboidShaoe0(15.05, 0.6, 1.45, 15.65, 15.4, 7.05);
	private static final VoxelShape EAST_PART6 = VoxelShapeHelper.makeCuboidShaoe0(1.05, 0.6, 6.65, 1.85, 15.4, 7.05);
	private static final VoxelShape EAST_PART7 = VoxelShapeHelper.makeCuboidShaoe0(1.85, 0.6, 6.65, 7.45, 1.4, 7.05);
	private static final VoxelShape EAST_PART8 = VoxelShapeHelper.makeCuboidShaoe0(8.65, 0.6, 6.65, 14.25, 1.4, 7.05);
	private static final VoxelShape EAST_PART9 = VoxelShapeHelper.makeCuboidShaoe0(14.25, 0.6, 6.65, 15.05, 15.4, 7.05);
	private static final VoxelShape EAST_PART10 = VoxelShapeHelper.makeCuboidShaoe0(7.45, 0.6, 6.65, 8.65, 15.4, 7.05);
	private static final VoxelShape EAST_PART11 = VoxelShapeHelper.makeCuboidShaoe0(8.65, 14.6, 6.65, 14.25, 15.4,
			7.05);
	private static final VoxelShape EAST_PART12 = VoxelShapeHelper.makeCuboidShaoe0(1.85, 14.6, 6.65, 7.45, 15.4, 7.05);
	private static final VoxelShape EAST_PART13 = VoxelShapeHelper.makeCuboidShaoe0(1.45, 1.08, 1.85, 1.85, 2.04, 6.65);
	private static final VoxelShape EAST_PART14 = VoxelShapeHelper.makeCuboidShaoe0(1.45, 4.28, 1.85, 1.85, 5.24, 6.65);
	private static final VoxelShape EAST_PART15 = VoxelShapeHelper.makeCuboidShaoe0(1.45, 2.68, 1.85, 1.85, 3.64, 6.65);
	private static final VoxelShape EAST_PART16 = VoxelShapeHelper.makeCuboidShaoe0(1.45, 13.88, 1.85, 1.85, 14.84,
			6.65);
	private static final VoxelShape EAST_PART17 = VoxelShapeHelper.makeCuboidShaoe0(1.45, 12.28, 1.85, 1.85, 13.24,
			6.65);
	private static final VoxelShape EAST_PART18 = VoxelShapeHelper.makeCuboidShaoe0(1.45, 10.68, 1.85, 1.85, 11.64,
			6.65);
	private static final VoxelShape EAST_PART19 = VoxelShapeHelper.makeCuboidShaoe0(1.45, 9.08, 1.85, 1.85, 10.04,
			6.65);
	private static final VoxelShape EAST_PART20 = VoxelShapeHelper.makeCuboidShaoe0(1.45, 7.48, 1.85, 1.85, 8.44, 6.65);
	private static final VoxelShape EAST_PART21 = VoxelShapeHelper.makeCuboidShaoe0(1.45, 5.88, 1.85, 1.85, 6.84, 6.65);
	private static final VoxelShape EAST_PART22 = VoxelShapeHelper.makeCuboidShaoe0(1.85, 1.4, 1.85, 2.25, 1.72, 7.05);
	private static final VoxelShape EAST_PART23 = VoxelShapeHelper.makeCuboidShaoe0(1.85, 7.8, 1.85, 2.25, 8.12, 7.05);
	private static final VoxelShape EAST_PART24 = VoxelShapeHelper.makeCuboidShaoe0(1.85, 6.2, 1.85, 2.25, 6.52, 7.05);
	private static final VoxelShape EAST_PART25 = VoxelShapeHelper.makeCuboidShaoe0(1.85, 4.6, 1.85, 2.25, 4.92, 7.05);
	private static final VoxelShape EAST_PART26 = VoxelShapeHelper.makeCuboidShaoe0(1.85, 14.2, 1.85, 2.25, 14.52,
			7.05);
	private static final VoxelShape EAST_PART27 = VoxelShapeHelper.makeCuboidShaoe0(1.85, 12.6, 1.85, 2.25, 12.92,
			7.05);
	private static final VoxelShape EAST_PART28 = VoxelShapeHelper.makeCuboidShaoe0(1.85, 11, 1.85, 2.25, 11.32, 7.05);
	private static final VoxelShape EAST_PART29 = VoxelShapeHelper.makeCuboidShaoe0(1.85, 9.4, 1.85, 2.25, 9.72, 7.05);
	private static final VoxelShape EAST_PART30 = VoxelShapeHelper.makeCuboidShaoe0(1.85, 3, 1.85, 2.25, 3.32, 7.05);
	private static final VoxelShape EAST_PART31 = VoxelShapeHelper.makeCuboidShaoe0(8.25, 1.08, 1.85, 8.65, 2.04, 6.65);
	private static final VoxelShape EAST_PART32 = VoxelShapeHelper.makeCuboidShaoe0(8.25, 4.28, 1.85, 8.65, 5.24, 6.65);
	private static final VoxelShape EAST_PART33 = VoxelShapeHelper.makeCuboidShaoe0(8.25, 2.68, 1.85, 8.65, 3.64, 6.65);
	private static final VoxelShape EAST_PART34 = VoxelShapeHelper.makeCuboidShaoe0(8.25, 13.88, 1.85, 8.65, 14.84,
			6.65);
	private static final VoxelShape EAST_PART35 = VoxelShapeHelper.makeCuboidShaoe0(8.25, 12.28, 1.85, 8.65, 13.24,
			6.65);
	private static final VoxelShape EAST_PART36 = VoxelShapeHelper.makeCuboidShaoe0(8.25, 10.68, 1.85, 8.65, 11.64,
			6.65);
	private static final VoxelShape EAST_PART37 = VoxelShapeHelper.makeCuboidShaoe0(8.25, 9.08, 1.85, 8.65, 10.04,
			6.65);
	private static final VoxelShape EAST_PART38 = VoxelShapeHelper.makeCuboidShaoe0(8.25, 7.48, 1.85, 8.65, 8.44, 6.65);
	private static final VoxelShape EAST_PART39 = VoxelShapeHelper.makeCuboidShaoe0(8.25, 5.88, 1.85, 8.65, 6.84, 6.65);
	private static final VoxelShape EAST_PART40 = VoxelShapeHelper.makeCuboidShaoe0(8.65, 1.4, 1.85, 9.05, 1.72, 7.05);
	private static final VoxelShape EAST_PART41 = VoxelShapeHelper.makeCuboidShaoe0(8.65, 7.8, 1.85, 9.05, 8.12, 7.05);
	private static final VoxelShape EAST_PART42 = VoxelShapeHelper.makeCuboidShaoe0(8.65, 6.2, 1.85, 9.05, 6.52, 7.05);
	private static final VoxelShape EAST_PART43 = VoxelShapeHelper.makeCuboidShaoe0(8.65, 4.6, 1.85, 9.05, 4.92, 7.05);
	private static final VoxelShape EAST_PART44 = VoxelShapeHelper.makeCuboidShaoe0(8.65, 14.2, 1.85, 9.05, 14.52,
			7.05);
	private static final VoxelShape EAST_PART45 = VoxelShapeHelper.makeCuboidShaoe0(8.65, 12.6, 1.85, 9.05, 12.92,
			7.05);
	private static final VoxelShape EAST_PART46 = VoxelShapeHelper.makeCuboidShaoe0(8.65, 11, 1.85, 9.05, 11.32, 7.05);
	private static final VoxelShape EAST_PART47 = VoxelShapeHelper.makeCuboidShaoe0(8.65, 9.4, 1.85, 9.05, 9.72, 7.05);
	private static final VoxelShape EAST_PART48 = VoxelShapeHelper.makeCuboidShaoe0(8.65, 3, 1.85, 9.05, 3.32, 7.05);
	private static final VoxelShape EAST_PART49 = VoxelShapeHelper.makeCuboidShaoe0(7.45, 1.08, 1.85, 7.85, 2.04, 6.65);
	private static final VoxelShape EAST_PART50 = VoxelShapeHelper.makeCuboidShaoe0(7.45, 4.28, 1.85, 7.85, 5.24, 6.65);
	private static final VoxelShape EAST_PART51 = VoxelShapeHelper.makeCuboidShaoe0(7.45, 2.68, 1.85, 7.85, 3.64, 6.65);
	private static final VoxelShape EAST_PART52 = VoxelShapeHelper.makeCuboidShaoe0(7.45, 13.88, 1.85, 7.85, 14.84,
			6.65);
	private static final VoxelShape EAST_PART53 = VoxelShapeHelper.makeCuboidShaoe0(7.45, 12.28, 1.85, 7.85, 13.24,
			6.65);
	private static final VoxelShape EAST_PART54 = VoxelShapeHelper.makeCuboidShaoe0(7.45, 10.68, 1.85, 7.85, 11.64,
			6.65);
	private static final VoxelShape EAST_PART55 = VoxelShapeHelper.makeCuboidShaoe0(7.45, 9.08, 1.85, 7.85, 10.04,
			6.65);
	private static final VoxelShape EAST_PART56 = VoxelShapeHelper.makeCuboidShaoe0(7.45, 7.48, 1.85, 7.85, 8.44, 6.65);
	private static final VoxelShape EAST_PART57 = VoxelShapeHelper.makeCuboidShaoe0(7.45, 5.88, 1.85, 7.85, 6.84, 6.65);
	private static final VoxelShape EAST_PART58 = VoxelShapeHelper.makeCuboidShaoe0(7.05, 1.4, 1.85, 7.45, 1.72, 7.05);
	private static final VoxelShape EAST_PART59 = VoxelShapeHelper.makeCuboidShaoe0(7.05, 7.8, 1.85, 7.45, 8.12, 7.05);
	private static final VoxelShape EAST_PART60 = VoxelShapeHelper.makeCuboidShaoe0(7.05, 6.2, 1.85, 7.45, 6.52, 7.05);
	private static final VoxelShape EAST_PART61 = VoxelShapeHelper.makeCuboidShaoe0(7.05, 4.6, 1.85, 7.45, 4.92, 7.05);
	private static final VoxelShape EAST_PART62 = VoxelShapeHelper.makeCuboidShaoe0(7.05, 14.2, 1.85, 7.45, 14.52,
			7.05);
	private static final VoxelShape EAST_PART63 = VoxelShapeHelper.makeCuboidShaoe0(7.05, 12.6, 1.85, 7.45, 12.92,
			7.05);
	private static final VoxelShape EAST_PART64 = VoxelShapeHelper.makeCuboidShaoe0(7.05, 11, 1.85, 7.45, 11.32, 7.05);
	private static final VoxelShape EAST_PART65 = VoxelShapeHelper.makeCuboidShaoe0(7.05, 9.4, 1.85, 7.45, 9.72, 7.05);
	private static final VoxelShape EAST_PART66 = VoxelShapeHelper.makeCuboidShaoe0(7.05, 3, 1.85, 7.45, 3.32, 7.05);
	private static final VoxelShape EAST_PART67 = VoxelShapeHelper.makeCuboidShaoe0(14.25, 1.08, 1.85, 14.65, 2.04,
			6.65);
	private static final VoxelShape EAST_PART68 = VoxelShapeHelper.makeCuboidShaoe0(14.25, 4.28, 1.85, 14.65, 5.24,
			6.65);
	private static final VoxelShape EAST_PART69 = VoxelShapeHelper.makeCuboidShaoe0(14.25, 2.68, 1.85, 14.65, 3.64,
			6.65);
	private static final VoxelShape EAST_PART70 = VoxelShapeHelper.makeCuboidShaoe0(14.25, 13.88, 1.85, 14.65, 14.84,
			6.65);
	private static final VoxelShape EAST_PART71 = VoxelShapeHelper.makeCuboidShaoe0(14.25, 12.28, 1.85, 14.65, 13.24,
			6.65);
	private static final VoxelShape EAST_PART72 = VoxelShapeHelper.makeCuboidShaoe0(14.25, 10.68, 1.85, 14.65, 11.64,
			6.65);
	private static final VoxelShape EAST_PART73 = VoxelShapeHelper.makeCuboidShaoe0(14.25, 9.08, 1.85, 14.65, 10.04,
			6.65);
	private static final VoxelShape EAST_PART74 = VoxelShapeHelper.makeCuboidShaoe0(14.25, 7.48, 1.85, 14.65, 8.44,
			6.65);
	private static final VoxelShape EAST_PART75 = VoxelShapeHelper.makeCuboidShaoe0(14.25, 5.88, 1.85, 14.65, 6.84,
			6.65);
	private static final VoxelShape EAST_PART76 = VoxelShapeHelper.makeCuboidShaoe0(13.85, 1.4, 1.85, 14.25, 1.72,
			7.05);
	private static final VoxelShape EAST_PART77 = VoxelShapeHelper.makeCuboidShaoe0(13.85, 7.8, 1.85, 14.25, 8.12,
			7.05);
	private static final VoxelShape EAST_PART78 = VoxelShapeHelper.makeCuboidShaoe0(13.85, 6.2, 1.85, 14.25, 6.52,
			7.05);
	private static final VoxelShape EAST_PART79 = VoxelShapeHelper.makeCuboidShaoe0(13.85, 4.6, 1.85, 14.25, 4.92,
			7.05);
	private static final VoxelShape EAST_PART80 = VoxelShapeHelper.makeCuboidShaoe0(13.85, 14.2, 1.85, 14.25, 14.52,
			7.05);
	private static final VoxelShape EAST_PART81 = VoxelShapeHelper.makeCuboidShaoe0(13.85, 12.6, 1.85, 14.25, 12.92,
			7.05);
	private static final VoxelShape EAST_PART82 = VoxelShapeHelper.makeCuboidShaoe0(13.85, 11, 1.85, 14.25, 11.32,
			7.05);
	private static final VoxelShape EAST_PART83 = VoxelShapeHelper.makeCuboidShaoe0(13.85, 9.4, 1.85, 14.25, 9.72,
			7.05);
	private static final VoxelShape EAST_PART84 = VoxelShapeHelper.makeCuboidShaoe0(13.85, 3, 1.85, 14.25, 3.32, 7.05);
	private static final VoxelShape EAST_PART85 = VoxelShapeHelper.makeCuboidShaoe0(1.05, 0.6, 1.45, 2.25, 15.4, 1.85);
	private static final VoxelShape EAST_PART86 = VoxelShapeHelper.makeCuboidShaoe0(13.85, 0.6, 1.45, 15.05, 15.4,
			1.85);
	private static final VoxelShape EAST_PART87 = VoxelShapeHelper.makeCuboidShaoe0(7.45, 0.6, 1.45, 8.65, 15.4, 1.85);
	private static final VoxelShape EAST_PART88 = VoxelShapeHelper.makeCuboidShaoe0(1.05, 0.6, 1.85, 1.45, 15.4, 6.65);
	private static final VoxelShape EAST_PART89 = VoxelShapeHelper.makeCuboidShaoe0(14.65, 0.6, 1.85, 15.05, 15.4,
			6.65);
	private static final VoxelShape EAST_PART90 = VoxelShapeHelper.makeCuboidShaoe0(10.81, 7.96, 1.69, 11.69, 13.88,
			1.85);
	private static final VoxelShape EAST_PART91 = VoxelShapeHelper.makeCuboidShaoe0(10.81, 2.04, 1.69, 11.69, 7.96,
			1.85);
	private static final VoxelShape EAST_PART92 = VoxelShapeHelper.makeCuboidShaoe0(4.41, 7.96, 1.69, 5.29, 13.88,
			1.85);
	private static final VoxelShape EAST_PART93 = VoxelShapeHelper.makeCuboidShaoe0(4.41, 2.04, 1.69, 5.29, 7.96, 1.85);
	private static final VoxelShape EAST_PART94 = VoxelShapeHelper.makeCuboidShaoe0(2.65, 3.64, 1.45, 7.05, 4.28, 1.61);
	private static final VoxelShape EAST_PART95 = VoxelShapeHelper.makeCuboidShaoe0(2.65, 2.04, 1.45, 7.05, 2.68, 1.61);
	private static final VoxelShape EAST_PART96 = VoxelShapeHelper.makeCuboidShaoe0(2.65, 13.24, 1.45, 7.05, 13.88,
			1.61);
	private static final VoxelShape EAST_PART97 = VoxelShapeHelper.makeCuboidShaoe0(2.65, 11.64, 1.45, 7.05, 12.28,
			1.61);
	private static final VoxelShape EAST_PART98 = VoxelShapeHelper.makeCuboidShaoe0(2.65, 10.04, 1.45, 7.05, 10.68,
			1.61);
	private static final VoxelShape EAST_PART99 = VoxelShapeHelper.makeCuboidShaoe0(2.65, 8.44, 1.45, 7.05, 9.08, 1.61);
	private static final VoxelShape EAST_PART100 = VoxelShapeHelper.makeCuboidShaoe0(2.65, 6.84, 1.45, 7.05, 7.48,
			1.61);
	private static final VoxelShape EAST_PART101 = VoxelShapeHelper.makeCuboidShaoe0(2.65, 5.24, 1.45, 7.05, 5.88,
			1.61);
	private static final VoxelShape EAST_PART102 = VoxelShapeHelper.makeCuboidShaoe0(9.05, 3.64, 1.45, 13.45, 4.28,
			1.61);
	private static final VoxelShape EAST_PART103 = VoxelShapeHelper.makeCuboidShaoe0(9.05, 2.04, 1.45, 13.45, 2.68,
			1.61);
	private static final VoxelShape EAST_PART104 = VoxelShapeHelper.makeCuboidShaoe0(9.05, 13.24, 1.45, 13.45, 13.88,
			1.61);
	private static final VoxelShape EAST_PART105 = VoxelShapeHelper.makeCuboidShaoe0(9.05, 11.64, 1.45, 13.45, 12.28,
			1.61);
	private static final VoxelShape EAST_PART106 = VoxelShapeHelper.makeCuboidShaoe0(9.05, 10.04, 1.45, 13.45, 10.68,
			1.61);
	private static final VoxelShape EAST_PART107 = VoxelShapeHelper.makeCuboidShaoe0(9.05, 8.44, 1.45, 13.45, 9.08,
			1.61);
	private static final VoxelShape EAST_PART108 = VoxelShapeHelper.makeCuboidShaoe0(9.05, 6.84, 1.45, 13.45, 7.48,
			1.61);
	private static final VoxelShape EAST_PART109 = VoxelShapeHelper.makeCuboidShaoe0(9.05, 5.24, 1.45, 13.45, 5.88,
			1.61);
	private static final VoxelShape EAST_PART110 = VoxelShapeHelper.makeCuboidShaoe0(7.85, 0.6, 1.85, 8.25, 15.4, 6.65);
	protected static final VoxelShape EAST_AXIS_AABB = VoxelShapes.or(EAST_PART1, EAST_PART2, EAST_PART3, EAST_PART4,
			EAST_PART5, EAST_PART6, EAST_PART7, EAST_PART8, EAST_PART9, EAST_PART10, EAST_PART11, EAST_PART12,
			EAST_PART13, EAST_PART14, EAST_PART15, EAST_PART16, EAST_PART17, EAST_PART18, EAST_PART19, EAST_PART20,
			EAST_PART21, EAST_PART22, EAST_PART23, EAST_PART24, EAST_PART25, EAST_PART26, EAST_PART27, EAST_PART28,
			EAST_PART29, EAST_PART30, EAST_PART31, EAST_PART32, EAST_PART33, EAST_PART34, EAST_PART35, EAST_PART36,
			EAST_PART37, EAST_PART38, EAST_PART39, EAST_PART40, EAST_PART41, EAST_PART42, EAST_PART43, EAST_PART44,
			EAST_PART45, EAST_PART46, EAST_PART47, EAST_PART48, EAST_PART49, EAST_PART50, EAST_PART51, EAST_PART52,
			EAST_PART53, EAST_PART54, EAST_PART55, EAST_PART56, EAST_PART57, EAST_PART58, EAST_PART59, EAST_PART60,
			EAST_PART61, EAST_PART62, EAST_PART63, EAST_PART64, EAST_PART65, EAST_PART66, EAST_PART67, EAST_PART68,
			EAST_PART69, EAST_PART70, EAST_PART71, EAST_PART72, EAST_PART73, EAST_PART74, EAST_PART75, EAST_PART76,
			EAST_PART77, EAST_PART78, EAST_PART79, EAST_PART80, EAST_PART81, EAST_PART82, EAST_PART83, EAST_PART84,
			EAST_PART85, EAST_PART86, EAST_PART87, EAST_PART88, EAST_PART89, EAST_PART90, EAST_PART91, EAST_PART92,
			EAST_PART93, EAST_PART94, EAST_PART95, EAST_PART96, EAST_PART97, EAST_PART98, EAST_PART99, EAST_PART100,
			EAST_PART101, EAST_PART102, EAST_PART103, EAST_PART104, EAST_PART105, EAST_PART106, EAST_PART107,
			EAST_PART108, EAST_PART109, EAST_PART110);

	private static final VoxelShape WEST_PART1 = VoxelShapeHelper.makeCuboidShaoe180(0.45, 0.6, 0.85, 15.65, 15.4,
			1.45);
	private static final VoxelShape WEST_PART2 = VoxelShapeHelper.makeCuboidShaoe180(0.45, 0, 0.85, 15.65, 0.6, 7.05);
	private static final VoxelShape WEST_PART3 = VoxelShapeHelper.makeCuboidShaoe180(0.45, 15.4, 0.85, 15.65, 16, 7.05);
	private static final VoxelShape WEST_PART4 = VoxelShapeHelper.makeCuboidShaoe180(0.45, 0.6, 1.45, 1.05, 15.4, 7.05);
	private static final VoxelShape WEST_PART5 = VoxelShapeHelper.makeCuboidShaoe180(15.05, 0.6, 1.45, 15.65, 15.4,
			7.05);
	private static final VoxelShape WEST_PART6 = VoxelShapeHelper.makeCuboidShaoe180(1.05, 0.6, 6.65, 1.85, 15.4, 7.05);
	private static final VoxelShape WEST_PART7 = VoxelShapeHelper.makeCuboidShaoe180(1.85, 0.6, 6.65, 7.45, 1.4, 7.05);
	private static final VoxelShape WEST_PART8 = VoxelShapeHelper.makeCuboidShaoe180(8.65, 0.6, 6.65, 14.25, 1.4, 7.05);
	private static final VoxelShape WEST_PART9 = VoxelShapeHelper.makeCuboidShaoe180(14.25, 0.6, 6.65, 15.05, 15.4,
			7.05);
	private static final VoxelShape WEST_PART10 = VoxelShapeHelper.makeCuboidShaoe180(7.45, 0.6, 6.65, 8.65, 15.4,
			7.05);
	private static final VoxelShape WEST_PART11 = VoxelShapeHelper.makeCuboidShaoe180(8.65, 14.6, 6.65, 14.25, 15.4,
			7.05);
	private static final VoxelShape WEST_PART12 = VoxelShapeHelper.makeCuboidShaoe180(1.85, 14.6, 6.65, 7.45, 15.4,
			7.05);
	private static final VoxelShape WEST_PART13 = VoxelShapeHelper.makeCuboidShaoe180(1.45, 1.08, 1.85, 1.85, 2.04,
			6.65);
	private static final VoxelShape WEST_PART14 = VoxelShapeHelper.makeCuboidShaoe180(1.45, 4.28, 1.85, 1.85, 5.24,
			6.65);
	private static final VoxelShape WEST_PART15 = VoxelShapeHelper.makeCuboidShaoe180(1.45, 2.68, 1.85, 1.85, 3.64,
			6.65);
	private static final VoxelShape WEST_PART16 = VoxelShapeHelper.makeCuboidShaoe180(1.45, 13.88, 1.85, 1.85, 14.84,
			6.65);
	private static final VoxelShape WEST_PART17 = VoxelShapeHelper.makeCuboidShaoe180(1.45, 12.28, 1.85, 1.85, 13.24,
			6.65);
	private static final VoxelShape WEST_PART18 = VoxelShapeHelper.makeCuboidShaoe180(1.45, 10.68, 1.85, 1.85, 11.64,
			6.65);
	private static final VoxelShape WEST_PART19 = VoxelShapeHelper.makeCuboidShaoe180(1.45, 9.08, 1.85, 1.85, 10.04,
			6.65);
	private static final VoxelShape WEST_PART20 = VoxelShapeHelper.makeCuboidShaoe180(1.45, 7.48, 1.85, 1.85, 8.44,
			6.65);
	private static final VoxelShape WEST_PART21 = VoxelShapeHelper.makeCuboidShaoe180(1.45, 5.88, 1.85, 1.85, 6.84,
			6.65);
	private static final VoxelShape WEST_PART22 = VoxelShapeHelper.makeCuboidShaoe180(1.85, 1.4, 1.85, 2.25, 1.72,
			7.05);
	private static final VoxelShape WEST_PART23 = VoxelShapeHelper.makeCuboidShaoe180(1.85, 7.8, 1.85, 2.25, 8.12,
			7.05);
	private static final VoxelShape WEST_PART24 = VoxelShapeHelper.makeCuboidShaoe180(1.85, 6.2, 1.85, 2.25, 6.52,
			7.05);
	private static final VoxelShape WEST_PART25 = VoxelShapeHelper.makeCuboidShaoe180(1.85, 4.6, 1.85, 2.25, 4.92,
			7.05);
	private static final VoxelShape WEST_PART26 = VoxelShapeHelper.makeCuboidShaoe180(1.85, 14.2, 1.85, 2.25, 14.52,
			7.05);
	private static final VoxelShape WEST_PART27 = VoxelShapeHelper.makeCuboidShaoe180(1.85, 12.6, 1.85, 2.25, 12.92,
			7.05);
	private static final VoxelShape WEST_PART28 = VoxelShapeHelper.makeCuboidShaoe180(1.85, 11, 1.85, 2.25, 11.32,
			7.05);
	private static final VoxelShape WEST_PART29 = VoxelShapeHelper.makeCuboidShaoe180(1.85, 9.4, 1.85, 2.25, 9.72,
			7.05);
	private static final VoxelShape WEST_PART30 = VoxelShapeHelper.makeCuboidShaoe180(1.85, 3, 1.85, 2.25, 3.32, 7.05);
	private static final VoxelShape WEST_PART31 = VoxelShapeHelper.makeCuboidShaoe180(8.25, 1.08, 1.85, 8.65, 2.04,
			6.65);
	private static final VoxelShape WEST_PART32 = VoxelShapeHelper.makeCuboidShaoe180(8.25, 4.28, 1.85, 8.65, 5.24,
			6.65);
	private static final VoxelShape WEST_PART33 = VoxelShapeHelper.makeCuboidShaoe180(8.25, 2.68, 1.85, 8.65, 3.64,
			6.65);
	private static final VoxelShape WEST_PART34 = VoxelShapeHelper.makeCuboidShaoe180(8.25, 13.88, 1.85, 8.65, 14.84,
			6.65);
	private static final VoxelShape WEST_PART35 = VoxelShapeHelper.makeCuboidShaoe180(8.25, 12.28, 1.85, 8.65, 13.24,
			6.65);
	private static final VoxelShape WEST_PART36 = VoxelShapeHelper.makeCuboidShaoe180(8.25, 10.68, 1.85, 8.65, 11.64,
			6.65);
	private static final VoxelShape WEST_PART37 = VoxelShapeHelper.makeCuboidShaoe180(8.25, 9.08, 1.85, 8.65, 10.04,
			6.65);
	private static final VoxelShape WEST_PART38 = VoxelShapeHelper.makeCuboidShaoe180(8.25, 7.48, 1.85, 8.65, 8.44,
			6.65);
	private static final VoxelShape WEST_PART39 = VoxelShapeHelper.makeCuboidShaoe180(8.25, 5.88, 1.85, 8.65, 6.84,
			6.65);
	private static final VoxelShape WEST_PART40 = VoxelShapeHelper.makeCuboidShaoe180(8.65, 1.4, 1.85, 9.05, 1.72,
			7.05);
	private static final VoxelShape WEST_PART41 = VoxelShapeHelper.makeCuboidShaoe180(8.65, 7.8, 1.85, 9.05, 8.12,
			7.05);
	private static final VoxelShape WEST_PART42 = VoxelShapeHelper.makeCuboidShaoe180(8.65, 6.2, 1.85, 9.05, 6.52,
			7.05);
	private static final VoxelShape WEST_PART43 = VoxelShapeHelper.makeCuboidShaoe180(8.65, 4.6, 1.85, 9.05, 4.92,
			7.05);
	private static final VoxelShape WEST_PART44 = VoxelShapeHelper.makeCuboidShaoe180(8.65, 14.2, 1.85, 9.05, 14.52,
			7.05);
	private static final VoxelShape WEST_PART45 = VoxelShapeHelper.makeCuboidShaoe180(8.65, 12.6, 1.85, 9.05, 12.92,
			7.05);
	private static final VoxelShape WEST_PART46 = VoxelShapeHelper.makeCuboidShaoe180(8.65, 11, 1.85, 9.05, 11.32,
			7.05);
	private static final VoxelShape WEST_PART47 = VoxelShapeHelper.makeCuboidShaoe180(8.65, 9.4, 1.85, 9.05, 9.72,
			7.05);
	private static final VoxelShape WEST_PART48 = VoxelShapeHelper.makeCuboidShaoe180(8.65, 3, 1.85, 9.05, 3.32, 7.05);
	private static final VoxelShape WEST_PART49 = VoxelShapeHelper.makeCuboidShaoe180(7.45, 1.08, 1.85, 7.85, 2.04,
			6.65);
	private static final VoxelShape WEST_PART50 = VoxelShapeHelper.makeCuboidShaoe180(7.45, 4.28, 1.85, 7.85, 5.24,
			6.65);
	private static final VoxelShape WEST_PART51 = VoxelShapeHelper.makeCuboidShaoe180(7.45, 2.68, 1.85, 7.85, 3.64,
			6.65);
	private static final VoxelShape WEST_PART52 = VoxelShapeHelper.makeCuboidShaoe180(7.45, 13.88, 1.85, 7.85, 14.84,
			6.65);
	private static final VoxelShape WEST_PART53 = VoxelShapeHelper.makeCuboidShaoe180(7.45, 12.28, 1.85, 7.85, 13.24,
			6.65);
	private static final VoxelShape WEST_PART54 = VoxelShapeHelper.makeCuboidShaoe180(7.45, 10.68, 1.85, 7.85, 11.64,
			6.65);
	private static final VoxelShape WEST_PART55 = VoxelShapeHelper.makeCuboidShaoe180(7.45, 9.08, 1.85, 7.85, 10.04,
			6.65);
	private static final VoxelShape WEST_PART56 = VoxelShapeHelper.makeCuboidShaoe180(7.45, 7.48, 1.85, 7.85, 8.44,
			6.65);
	private static final VoxelShape WEST_PART57 = VoxelShapeHelper.makeCuboidShaoe180(7.45, 5.88, 1.85, 7.85, 6.84,
			6.65);
	private static final VoxelShape WEST_PART58 = VoxelShapeHelper.makeCuboidShaoe180(7.05, 1.4, 1.85, 7.45, 1.72,
			7.05);
	private static final VoxelShape WEST_PART59 = VoxelShapeHelper.makeCuboidShaoe180(7.05, 7.8, 1.85, 7.45, 8.12,
			7.05);
	private static final VoxelShape WEST_PART60 = VoxelShapeHelper.makeCuboidShaoe180(7.05, 6.2, 1.85, 7.45, 6.52,
			7.05);
	private static final VoxelShape WEST_PART61 = VoxelShapeHelper.makeCuboidShaoe180(7.05, 4.6, 1.85, 7.45, 4.92,
			7.05);
	private static final VoxelShape WEST_PART62 = VoxelShapeHelper.makeCuboidShaoe180(7.05, 14.2, 1.85, 7.45, 14.52,
			7.05);
	private static final VoxelShape WEST_PART63 = VoxelShapeHelper.makeCuboidShaoe180(7.05, 12.6, 1.85, 7.45, 12.92,
			7.05);
	private static final VoxelShape WEST_PART64 = VoxelShapeHelper.makeCuboidShaoe180(7.05, 11, 1.85, 7.45, 11.32,
			7.05);
	private static final VoxelShape WEST_PART65 = VoxelShapeHelper.makeCuboidShaoe180(7.05, 9.4, 1.85, 7.45, 9.72,
			7.05);
	private static final VoxelShape WEST_PART66 = VoxelShapeHelper.makeCuboidShaoe180(7.05, 3, 1.85, 7.45, 3.32, 7.05);
	private static final VoxelShape WEST_PART67 = VoxelShapeHelper.makeCuboidShaoe180(14.25, 1.08, 1.85, 14.65, 2.04,
			6.65);
	private static final VoxelShape WEST_PART68 = VoxelShapeHelper.makeCuboidShaoe180(14.25, 4.28, 1.85, 14.65, 5.24,
			6.65);
	private static final VoxelShape WEST_PART69 = VoxelShapeHelper.makeCuboidShaoe180(14.25, 2.68, 1.85, 14.65, 3.64,
			6.65);
	private static final VoxelShape WEST_PART70 = VoxelShapeHelper.makeCuboidShaoe180(14.25, 13.88, 1.85, 14.65, 14.84,
			6.65);
	private static final VoxelShape WEST_PART71 = VoxelShapeHelper.makeCuboidShaoe180(14.25, 12.28, 1.85, 14.65, 13.24,
			6.65);
	private static final VoxelShape WEST_PART72 = VoxelShapeHelper.makeCuboidShaoe180(14.25, 10.68, 1.85, 14.65, 11.64,
			6.65);
	private static final VoxelShape WEST_PART73 = VoxelShapeHelper.makeCuboidShaoe180(14.25, 9.08, 1.85, 14.65, 10.04,
			6.65);
	private static final VoxelShape WEST_PART74 = VoxelShapeHelper.makeCuboidShaoe180(14.25, 7.48, 1.85, 14.65, 8.44,
			6.65);
	private static final VoxelShape WEST_PART75 = VoxelShapeHelper.makeCuboidShaoe180(14.25, 5.88, 1.85, 14.65, 6.84,
			6.65);
	private static final VoxelShape WEST_PART76 = VoxelShapeHelper.makeCuboidShaoe180(13.85, 1.4, 1.85, 14.25, 1.72,
			7.05);
	private static final VoxelShape WEST_PART77 = VoxelShapeHelper.makeCuboidShaoe180(13.85, 7.8, 1.85, 14.25, 8.12,
			7.05);
	private static final VoxelShape WEST_PART78 = VoxelShapeHelper.makeCuboidShaoe180(13.85, 6.2, 1.85, 14.25, 6.52,
			7.05);
	private static final VoxelShape WEST_PART79 = VoxelShapeHelper.makeCuboidShaoe180(13.85, 4.6, 1.85, 14.25, 4.92,
			7.05);
	private static final VoxelShape WEST_PART80 = VoxelShapeHelper.makeCuboidShaoe180(13.85, 14.2, 1.85, 14.25, 14.52,
			7.05);
	private static final VoxelShape WEST_PART81 = VoxelShapeHelper.makeCuboidShaoe180(13.85, 12.6, 1.85, 14.25, 12.92,
			7.05);
	private static final VoxelShape WEST_PART82 = VoxelShapeHelper.makeCuboidShaoe180(13.85, 11, 1.85, 14.25, 11.32,
			7.05);
	private static final VoxelShape WEST_PART83 = VoxelShapeHelper.makeCuboidShaoe180(13.85, 9.4, 1.85, 14.25, 9.72,
			7.05);
	private static final VoxelShape WEST_PART84 = VoxelShapeHelper.makeCuboidShaoe180(13.85, 3, 1.85, 14.25, 3.32,
			7.05);
	private static final VoxelShape WEST_PART85 = VoxelShapeHelper.makeCuboidShaoe180(1.05, 0.6, 1.45, 2.25, 15.4,
			1.85);
	private static final VoxelShape WEST_PART86 = VoxelShapeHelper.makeCuboidShaoe180(13.85, 0.6, 1.45, 15.05, 15.4,
			1.85);
	private static final VoxelShape WEST_PART87 = VoxelShapeHelper.makeCuboidShaoe180(7.45, 0.6, 1.45, 8.65, 15.4,
			1.85);
	private static final VoxelShape WEST_PART88 = VoxelShapeHelper.makeCuboidShaoe180(1.05, 0.6, 1.85, 1.45, 15.4,
			6.65);
	private static final VoxelShape WEST_PART89 = VoxelShapeHelper.makeCuboidShaoe180(14.65, 0.6, 1.85, 15.05, 15.4,
			6.65);
	private static final VoxelShape WEST_PART90 = VoxelShapeHelper.makeCuboidShaoe180(10.81, 7.96, 1.69, 11.69, 13.88,
			1.85);
	private static final VoxelShape WEST_PART91 = VoxelShapeHelper.makeCuboidShaoe180(10.81, 2.04, 1.69, 11.69, 7.96,
			1.85);
	private static final VoxelShape WEST_PART92 = VoxelShapeHelper.makeCuboidShaoe180(4.41, 7.96, 1.69, 5.29, 13.88,
			1.85);
	private static final VoxelShape WEST_PART93 = VoxelShapeHelper.makeCuboidShaoe180(4.41, 2.04, 1.69, 5.29, 7.96,
			1.85);
	private static final VoxelShape WEST_PART94 = VoxelShapeHelper.makeCuboidShaoe180(2.65, 3.64, 1.45, 7.05, 4.28,
			1.61);
	private static final VoxelShape WEST_PART95 = VoxelShapeHelper.makeCuboidShaoe180(2.65, 2.04, 1.45, 7.05, 2.68,
			1.61);
	private static final VoxelShape WEST_PART96 = VoxelShapeHelper.makeCuboidShaoe180(2.65, 13.24, 1.45, 7.05, 13.88,
			1.61);
	private static final VoxelShape WEST_PART97 = VoxelShapeHelper.makeCuboidShaoe180(2.65, 11.64, 1.45, 7.05, 12.28,
			1.61);
	private static final VoxelShape WEST_PART98 = VoxelShapeHelper.makeCuboidShaoe180(2.65, 10.04, 1.45, 7.05, 10.68,
			1.61);
	private static final VoxelShape WEST_PART99 = VoxelShapeHelper.makeCuboidShaoe180(2.65, 8.44, 1.45, 7.05, 9.08,
			1.61);
	private static final VoxelShape WEST_PART100 = VoxelShapeHelper.makeCuboidShaoe180(2.65, 6.84, 1.45, 7.05, 7.48,
			1.61);
	private static final VoxelShape WEST_PART101 = VoxelShapeHelper.makeCuboidShaoe180(2.65, 5.24, 1.45, 7.05, 5.88,
			1.61);
	private static final VoxelShape WEST_PART102 = VoxelShapeHelper.makeCuboidShaoe180(9.05, 3.64, 1.45, 13.45, 4.28,
			1.61);
	private static final VoxelShape WEST_PART103 = VoxelShapeHelper.makeCuboidShaoe180(9.05, 2.04, 1.45, 13.45, 2.68,
			1.61);
	private static final VoxelShape WEST_PART104 = VoxelShapeHelper.makeCuboidShaoe180(9.05, 13.24, 1.45, 13.45, 13.88,
			1.61);
	private static final VoxelShape WEST_PART105 = VoxelShapeHelper.makeCuboidShaoe180(9.05, 11.64, 1.45, 13.45, 12.28,
			1.61);
	private static final VoxelShape WEST_PART106 = VoxelShapeHelper.makeCuboidShaoe180(9.05, 10.04, 1.45, 13.45, 10.68,
			1.61);
	private static final VoxelShape WEST_PART107 = VoxelShapeHelper.makeCuboidShaoe180(9.05, 8.44, 1.45, 13.45, 9.08,
			1.61);
	private static final VoxelShape WEST_PART108 = VoxelShapeHelper.makeCuboidShaoe180(9.05, 6.84, 1.45, 13.45, 7.48,
			1.61);
	private static final VoxelShape WEST_PART109 = VoxelShapeHelper.makeCuboidShaoe180(9.05, 5.24, 1.45, 13.45, 5.88,
			1.61);
	private static final VoxelShape WEST_PART110 = VoxelShapeHelper.makeCuboidShaoe180(7.85, 0.6, 1.85, 8.25, 15.4,
			6.65);
	protected static final VoxelShape WEST_AXIS_AABB = VoxelShapes.or(WEST_PART1, WEST_PART2, WEST_PART3, WEST_PART4,
			WEST_PART5, WEST_PART6, WEST_PART7, WEST_PART8, WEST_PART9, WEST_PART10, WEST_PART11, WEST_PART12,
			WEST_PART13, WEST_PART14, WEST_PART15, WEST_PART16, WEST_PART17, WEST_PART18, WEST_PART19, WEST_PART20,
			WEST_PART21, WEST_PART22, WEST_PART23, WEST_PART24, WEST_PART25, WEST_PART26, WEST_PART27, WEST_PART28,
			WEST_PART29, WEST_PART30, WEST_PART31, WEST_PART32, WEST_PART33, WEST_PART34, WEST_PART35, WEST_PART36,
			WEST_PART37, WEST_PART38, WEST_PART39, WEST_PART40, WEST_PART41, WEST_PART42, WEST_PART43, WEST_PART44,
			WEST_PART45, WEST_PART46, WEST_PART47, WEST_PART48, WEST_PART49, WEST_PART50, WEST_PART51, WEST_PART52,
			WEST_PART53, WEST_PART54, WEST_PART55, WEST_PART56, WEST_PART57, WEST_PART58, WEST_PART59, WEST_PART60,
			WEST_PART61, WEST_PART62, WEST_PART63, WEST_PART64, WEST_PART65, WEST_PART66, WEST_PART67, WEST_PART68,
			WEST_PART69, WEST_PART70, WEST_PART71, WEST_PART72, WEST_PART73, WEST_PART74, WEST_PART75, WEST_PART76,
			WEST_PART77, WEST_PART78, WEST_PART79, WEST_PART80, WEST_PART81, WEST_PART82, WEST_PART83, WEST_PART84,
			WEST_PART85, WEST_PART86, WEST_PART87, WEST_PART88, WEST_PART89, WEST_PART90, WEST_PART91, WEST_PART92,
			WEST_PART93, WEST_PART94, WEST_PART95, WEST_PART96, WEST_PART97, WEST_PART98, WEST_PART99, WEST_PART100,
			WEST_PART101, WEST_PART102, WEST_PART103, WEST_PART104, WEST_PART105, WEST_PART106, WEST_PART107,
			WEST_PART108, WEST_PART109, WEST_PART110);



	private static final VoxelShape  NORTH_PART1 = fixCuboidShaoe1(0.45, 0.6, 0.85, 15.65, 15.4,
			1.45);
	private static final VoxelShape  NORTH_PART2 = fixCuboidShaoe1(0.45, 0, 0.85, 15.65, 0.6, 7.05);
	private static final VoxelShape  NORTH_PART3 = fixCuboidShaoe1(0.45, 15.4, 0.85, 15.65, 16, 7.05);
	private static final VoxelShape  NORTH_PART4 = fixCuboidShaoe1(0.45, 0.6, 1.45, 1.05, 15.4, 7.05);
	private static final VoxelShape  NORTH_PART5 = fixCuboidShaoe1(15.05, 0.6, 1.45, 15.65, 15.4,
			7.05);
	private static final VoxelShape  NORTH_PART6 = fixCuboidShaoe1(1.05, 0.6, 6.65, 1.85, 15.4, 7.05);
	private static final VoxelShape  NORTH_PART7 = fixCuboidShaoe1(1.85, 0.6, 6.65, 7.45, 1.4, 7.05);
	private static final VoxelShape  NORTH_PART8 = fixCuboidShaoe1(8.65, 0.6, 6.65, 14.25, 1.4, 7.05);
	private static final VoxelShape  NORTH_PART9 = fixCuboidShaoe1(14.25, 0.6, 6.65, 15.05, 15.4,
			7.05);
	private static final VoxelShape  NORTH_PART10 = fixCuboidShaoe1(7.45, 0.6, 6.65, 8.65, 15.4,
			7.05);
	private static final VoxelShape  NORTH_PART11 = fixCuboidShaoe1(8.65, 14.6, 6.65, 14.25, 15.4,
			7.05);
	private static final VoxelShape  NORTH_PART12 = fixCuboidShaoe1(1.85, 14.6, 6.65, 7.45, 15.4,
			7.05);
	private static final VoxelShape  NORTH_PART13 = fixCuboidShaoe1(1.45, 1.08, 1.85, 1.85, 2.04,
			6.65);
	private static final VoxelShape  NORTH_PART14 = fixCuboidShaoe1(1.45, 4.28, 1.85, 1.85, 5.24,
			6.65);
	private static final VoxelShape  NORTH_PART15 = fixCuboidShaoe1(1.45, 2.68, 1.85, 1.85, 3.64,
			6.65);
	private static final VoxelShape  NORTH_PART16 = fixCuboidShaoe1(1.45, 13.88, 1.85, 1.85, 14.84,
			6.65);
	private static final VoxelShape  NORTH_PART17 = fixCuboidShaoe1(1.45, 12.28, 1.85, 1.85, 13.24,
			6.65);
	private static final VoxelShape  NORTH_PART18 = fixCuboidShaoe1(1.45, 10.68, 1.85, 1.85, 11.64,
			6.65);
	private static final VoxelShape  NORTH_PART19 = fixCuboidShaoe1(1.45, 9.08, 1.85, 1.85, 10.04,
			6.65);
	private static final VoxelShape  NORTH_PART20 = fixCuboidShaoe1(1.45, 7.48, 1.85, 1.85, 8.44,
			6.65);
	private static final VoxelShape  NORTH_PART21 = fixCuboidShaoe1(1.45, 5.88, 1.85, 1.85, 6.84,
			6.65);
	private static final VoxelShape  NORTH_PART22 = fixCuboidShaoe1(1.85, 1.4, 1.85, 2.25, 1.72,
			7.05);
	private static final VoxelShape  NORTH_PART23 = fixCuboidShaoe1(1.85, 7.8, 1.85, 2.25, 8.12,
			7.05);
	private static final VoxelShape  NORTH_PART24 = fixCuboidShaoe1(1.85, 6.2, 1.85, 2.25, 6.52,
			7.05);
	private static final VoxelShape  NORTH_PART25 = fixCuboidShaoe1(1.85, 4.6, 1.85, 2.25, 4.92,
			7.05);
	private static final VoxelShape  NORTH_PART26 = fixCuboidShaoe1(1.85, 14.2, 1.85, 2.25, 14.52,
			7.05);
	private static final VoxelShape  NORTH_PART27 = fixCuboidShaoe1(1.85, 12.6, 1.85, 2.25, 12.92,
			7.05);
	private static final VoxelShape  NORTH_PART28 = fixCuboidShaoe1(1.85, 11, 1.85, 2.25, 11.32,
			7.05);
	private static final VoxelShape  NORTH_PART29 = fixCuboidShaoe1(1.85, 9.4, 1.85, 2.25, 9.72,
			7.05);
	private static final VoxelShape  NORTH_PART30 = fixCuboidShaoe1(1.85, 3, 1.85, 2.25, 3.32, 7.05);
	private static final VoxelShape  NORTH_PART31 = fixCuboidShaoe1(8.25, 1.08, 1.85, 8.65, 2.04,
			6.65);
	private static final VoxelShape  NORTH_PART32 = fixCuboidShaoe1(8.25, 4.28, 1.85, 8.65, 5.24,
			6.65);
	private static final VoxelShape  NORTH_PART33 = fixCuboidShaoe1(8.25, 2.68, 1.85, 8.65, 3.64,
			6.65);
	private static final VoxelShape  NORTH_PART34 = fixCuboidShaoe1(8.25, 13.88, 1.85, 8.65, 14.84,
			6.65);
	private static final VoxelShape  NORTH_PART35 = fixCuboidShaoe1(8.25, 12.28, 1.85, 8.65, 13.24,
			6.65);
	private static final VoxelShape  NORTH_PART36 = fixCuboidShaoe1(8.25, 10.68, 1.85, 8.65, 11.64,
			6.65);
	private static final VoxelShape  NORTH_PART37 = fixCuboidShaoe1(8.25, 9.08, 1.85, 8.65, 10.04,
			6.65);
	private static final VoxelShape  NORTH_PART38 = fixCuboidShaoe1(8.25, 7.48, 1.85, 8.65, 8.44,
			6.65);
	private static final VoxelShape  NORTH_PART39 = fixCuboidShaoe1(8.25, 5.88, 1.85, 8.65, 6.84,
			6.65);
	private static final VoxelShape  NORTH_PART40 = fixCuboidShaoe1(8.65, 1.4, 1.85, 9.05, 1.72,
			7.05);
	private static final VoxelShape  NORTH_PART41 = fixCuboidShaoe1(8.65, 7.8, 1.85, 9.05, 8.12,
			7.05);
	private static final VoxelShape  NORTH_PART42 = fixCuboidShaoe1(8.65, 6.2, 1.85, 9.05, 6.52,
			7.05);
	private static final VoxelShape  NORTH_PART43 = fixCuboidShaoe1(8.65, 4.6, 1.85, 9.05, 4.92,
			7.05);
	private static final VoxelShape  NORTH_PART44 = fixCuboidShaoe1(8.65, 14.2, 1.85, 9.05, 14.52,
			7.05);
	private static final VoxelShape  NORTH_PART45 = fixCuboidShaoe1(8.65, 12.6, 1.85, 9.05, 12.92,
			7.05);
	private static final VoxelShape  NORTH_PART46 = fixCuboidShaoe1(8.65, 11, 1.85, 9.05, 11.32,
			7.05);
	private static final VoxelShape  NORTH_PART47 = fixCuboidShaoe1(8.65, 9.4, 1.85, 9.05, 9.72,
			7.05);
	private static final VoxelShape  NORTH_PART48 = fixCuboidShaoe1(8.65, 3, 1.85, 9.05, 3.32, 7.05);
	private static final VoxelShape  NORTH_PART49 = fixCuboidShaoe1(7.45, 1.08, 1.85, 7.85, 2.04,
			6.65);
	private static final VoxelShape  NORTH_PART50 = fixCuboidShaoe1(7.45, 4.28, 1.85, 7.85, 5.24,
			6.65);
	private static final VoxelShape  NORTH_PART51 = fixCuboidShaoe1(7.45, 2.68, 1.85, 7.85, 3.64,
			6.65);
	private static final VoxelShape  NORTH_PART52 = fixCuboidShaoe1(7.45, 13.88, 1.85, 7.85, 14.84,
			6.65);
	private static final VoxelShape  NORTH_PART53 = fixCuboidShaoe1(7.45, 12.28, 1.85, 7.85, 13.24,
			6.65);
	private static final VoxelShape  NORTH_PART54 = fixCuboidShaoe1(7.45, 10.68, 1.85, 7.85, 11.64,
			6.65);
	private static final VoxelShape  NORTH_PART55 = fixCuboidShaoe1(7.45, 9.08, 1.85, 7.85, 10.04,
			6.65);
	private static final VoxelShape  NORTH_PART56 = fixCuboidShaoe1(7.45, 7.48, 1.85, 7.85, 8.44,
			6.65);
	private static final VoxelShape  NORTH_PART57 = fixCuboidShaoe1(7.45, 5.88, 1.85, 7.85, 6.84,
			6.65);
	private static final VoxelShape  NORTH_PART58 = fixCuboidShaoe1(7.05, 1.4, 1.85, 7.45, 1.72,
			7.05);
	private static final VoxelShape  NORTH_PART59 = fixCuboidShaoe1(7.05, 7.8, 1.85, 7.45, 8.12,
			7.05);
	private static final VoxelShape  NORTH_PART60 = fixCuboidShaoe1(7.05, 6.2, 1.85, 7.45, 6.52,
			7.05);
	private static final VoxelShape  NORTH_PART61 = fixCuboidShaoe1(7.05, 4.6, 1.85, 7.45, 4.92,
			7.05);
	private static final VoxelShape  NORTH_PART62 = fixCuboidShaoe1(7.05, 14.2, 1.85, 7.45, 14.52,
			7.05);
	private static final VoxelShape  NORTH_PART63 = fixCuboidShaoe1(7.05, 12.6, 1.85, 7.45, 12.92,
			7.05);
	private static final VoxelShape  NORTH_PART64 = fixCuboidShaoe1(7.05, 11, 1.85, 7.45, 11.32,
			7.05);
	private static final VoxelShape  NORTH_PART65 = fixCuboidShaoe1(7.05, 9.4, 1.85, 7.45, 9.72,
			7.05);
	private static final VoxelShape  NORTH_PART66 = fixCuboidShaoe1(7.05, 3, 1.85, 7.45, 3.32, 7.05);
	private static final VoxelShape  NORTH_PART67 = fixCuboidShaoe1(14.25, 1.08, 1.85, 14.65, 2.04,
			6.65);
	private static final VoxelShape  NORTH_PART68 = fixCuboidShaoe1(14.25, 4.28, 1.85, 14.65, 5.24,
			6.65);
	private static final VoxelShape  NORTH_PART69 = fixCuboidShaoe1(14.25, 2.68, 1.85, 14.65, 3.64,
			6.65);
	private static final VoxelShape  NORTH_PART70 = fixCuboidShaoe1(14.25, 13.88, 1.85, 14.65, 14.84,
			6.65);
	private static final VoxelShape  NORTH_PART71 = fixCuboidShaoe1(14.25, 12.28, 1.85, 14.65, 13.24,
			6.65);
	private static final VoxelShape  NORTH_PART72 = fixCuboidShaoe1(14.25, 10.68, 1.85, 14.65, 11.64,
			6.65);
	private static final VoxelShape  NORTH_PART73 = fixCuboidShaoe1(14.25, 9.08, 1.85, 14.65, 10.04,
			6.65);
	private static final VoxelShape  NORTH_PART74 = fixCuboidShaoe1(14.25, 7.48, 1.85, 14.65, 8.44,
			6.65);
	private static final VoxelShape  NORTH_PART75 = fixCuboidShaoe1(14.25, 5.88, 1.85, 14.65, 6.84,
			6.65);
	private static final VoxelShape  NORTH_PART76 = fixCuboidShaoe1(13.85, 1.4, 1.85, 14.25, 1.72,
			7.05);
	private static final VoxelShape  NORTH_PART77 = fixCuboidShaoe1(13.85, 7.8, 1.85, 14.25, 8.12,
			7.05);
	private static final VoxelShape  NORTH_PART78 = fixCuboidShaoe1(13.85, 6.2, 1.85, 14.25, 6.52,
			7.05);
	private static final VoxelShape  NORTH_PART79 = fixCuboidShaoe1(13.85, 4.6, 1.85, 14.25, 4.92,
			7.05);
	private static final VoxelShape  NORTH_PART80 = fixCuboidShaoe1(13.85, 14.2, 1.85, 14.25, 14.52,
			7.05);
	private static final VoxelShape  NORTH_PART81 = fixCuboidShaoe1(13.85, 12.6, 1.85, 14.25, 12.92,
			7.05);
	private static final VoxelShape  NORTH_PART82 = fixCuboidShaoe1(13.85, 11, 1.85, 14.25, 11.32,
			7.05);
	private static final VoxelShape  NORTH_PART83 = fixCuboidShaoe1(13.85, 9.4, 1.85, 14.25, 9.72,
			7.05);
	private static final VoxelShape  NORTH_PART84 = fixCuboidShaoe1(13.85, 3, 1.85, 14.25, 3.32,
			7.05);
	private static final VoxelShape  NORTH_PART85 = fixCuboidShaoe1(1.05, 0.6, 1.45, 2.25, 15.4,
			1.85);
	private static final VoxelShape  NORTH_PART86 = fixCuboidShaoe1(13.85, 0.6, 1.45, 15.05, 15.4,
			1.85);
	private static final VoxelShape  NORTH_PART87 = fixCuboidShaoe1(7.45, 0.6, 1.45, 8.65, 15.4,
			1.85);
	private static final VoxelShape  NORTH_PART88 = fixCuboidShaoe1(1.05, 0.6, 1.85, 1.45, 15.4,
			6.65);
	private static final VoxelShape  NORTH_PART89 = fixCuboidShaoe1(14.65, 0.6, 1.85, 15.05, 15.4,
			6.65);
	private static final VoxelShape  NORTH_PART90 = fixCuboidShaoe1(10.81, 7.96, 1.69, 11.69, 13.88,
			1.85);
	private static final VoxelShape  NORTH_PART91 = fixCuboidShaoe1(10.81, 2.04, 1.69, 11.69, 7.96,
			1.85);
	private static final VoxelShape  NORTH_PART92 = fixCuboidShaoe1(4.41, 7.96, 1.69, 5.29, 13.88,
			1.85);
	private static final VoxelShape  NORTH_PART93 = fixCuboidShaoe1(4.41, 2.04, 1.69, 5.29, 7.96,
			1.85);
	private static final VoxelShape  NORTH_PART94 = fixCuboidShaoe1(2.65, 3.64, 1.45, 7.05, 4.28,
			1.61);
	private static final VoxelShape  NORTH_PART95 = fixCuboidShaoe1(2.65, 2.04, 1.45, 7.05, 2.68,
			1.61);
	private static final VoxelShape  NORTH_PART96 = fixCuboidShaoe1(2.65, 13.24, 1.45, 7.05, 13.88,
			1.61);
	private static final VoxelShape  NORTH_PART97 = fixCuboidShaoe1(2.65, 11.64, 1.45, 7.05, 12.28,
			1.61);
	private static final VoxelShape  NORTH_PART98 = fixCuboidShaoe1(2.65, 10.04, 1.45, 7.05, 10.68,
			1.61);
	private static final VoxelShape  NORTH_PART99 = fixCuboidShaoe1(2.65, 8.44, 1.45, 7.05, 9.08,
			1.61);
	private static final VoxelShape  NORTH_PART100 = fixCuboidShaoe1(2.65, 6.84, 1.45, 7.05, 7.48,
			1.61);
	private static final VoxelShape  NORTH_PART101 = fixCuboidShaoe1(2.65, 5.24, 1.45, 7.05, 5.88,
			1.61);
	private static final VoxelShape  NORTH_PART102 = fixCuboidShaoe1(9.05, 3.64, 1.45, 13.45, 4.28,
			1.61);
	private static final VoxelShape  NORTH_PART103 = fixCuboidShaoe1(9.05, 2.04, 1.45, 13.45, 2.68,
			1.61);
	private static final VoxelShape  NORTH_PART104 = fixCuboidShaoe1(9.05, 13.24, 1.45, 13.45, 13.88,
			1.61);
	private static final VoxelShape  NORTH_PART105 = fixCuboidShaoe1(9.05, 11.64, 1.45, 13.45, 12.28,
			1.61);
	private static final VoxelShape  NORTH_PART106 = fixCuboidShaoe1(9.05, 10.04, 1.45, 13.45, 10.68,
			1.61);
	private static final VoxelShape  NORTH_PART107 = fixCuboidShaoe1(9.05, 8.44, 1.45, 13.45, 9.08,
			1.61);
	private static final VoxelShape  NORTH_PART108 = fixCuboidShaoe1(9.05, 6.84, 1.45, 13.45, 7.48,
			1.61);
	private static final VoxelShape  NORTH_PART109 = fixCuboidShaoe1(9.05, 5.24, 1.45, 13.45, 5.88,
			1.61);
	private static final VoxelShape  NORTH_PART110 = fixCuboidShaoe1(7.85, 0.6, 1.85, 8.25, 15.4,
			6.65);
	protected static final VoxelShape  NORTH_AXIS_AABB = VoxelShapes.or( NORTH_PART1,  NORTH_PART2,  NORTH_PART3,  NORTH_PART4,
			 NORTH_PART5,  NORTH_PART6,  NORTH_PART7,  NORTH_PART8,  NORTH_PART9,  NORTH_PART10,  NORTH_PART11,  NORTH_PART12,
			 NORTH_PART13,  NORTH_PART14,  NORTH_PART15,  NORTH_PART16,  NORTH_PART17,  NORTH_PART18,  NORTH_PART19,  NORTH_PART20,
			 NORTH_PART21,  NORTH_PART22,  NORTH_PART23,  NORTH_PART24,  NORTH_PART25,  NORTH_PART26,  NORTH_PART27,  NORTH_PART28,
			 NORTH_PART29,  NORTH_PART30,  NORTH_PART31,  NORTH_PART32,  NORTH_PART33,  NORTH_PART34,  NORTH_PART35,  NORTH_PART36,
			 NORTH_PART37,  NORTH_PART38,  NORTH_PART39,  NORTH_PART40,  NORTH_PART41,  NORTH_PART42,  NORTH_PART43,  NORTH_PART44,
			 NORTH_PART45,  NORTH_PART46,  NORTH_PART47,  NORTH_PART48,  NORTH_PART49,  NORTH_PART50,  NORTH_PART51,  NORTH_PART52,
			 NORTH_PART53,  NORTH_PART54,  NORTH_PART55,  NORTH_PART56,  NORTH_PART57,  NORTH_PART58,  NORTH_PART59,  NORTH_PART60,
			 NORTH_PART61,  NORTH_PART62,  NORTH_PART63,  NORTH_PART64,  NORTH_PART65,  NORTH_PART66,  NORTH_PART67,  NORTH_PART68,
			 NORTH_PART69,  NORTH_PART70,  NORTH_PART71,  NORTH_PART72,  NORTH_PART73,  NORTH_PART74,  NORTH_PART75,  NORTH_PART76,
			 NORTH_PART77,  NORTH_PART78,  NORTH_PART79,  NORTH_PART80,  NORTH_PART81,  NORTH_PART82,  NORTH_PART83,  NORTH_PART84,
			 NORTH_PART85,  NORTH_PART86,  NORTH_PART87,  NORTH_PART88,  NORTH_PART89,  NORTH_PART90,  NORTH_PART91,  NORTH_PART92,
			 NORTH_PART93,  NORTH_PART94,  NORTH_PART95,  NORTH_PART96,  NORTH_PART97,  NORTH_PART98,  NORTH_PART99,  NORTH_PART100,
			 NORTH_PART101,  NORTH_PART102,  NORTH_PART103,  NORTH_PART104,  NORTH_PART105,  NORTH_PART106,  NORTH_PART107,
			 NORTH_PART108,  NORTH_PART109,  NORTH_PART110);



	private static final VoxelShape SOUTH_PART1 = fixCuboidShaoe2(0.45, 0.6, 0.85, 15.65, 15.4,
			1.45);
	private static final VoxelShape SOUTH_PART2 = fixCuboidShaoe2(0.45, 0, 0.85, 15.65, 0.6, 7.05);
	private static final VoxelShape SOUTH_PART3 = fixCuboidShaoe2(0.45, 15.4, 0.85, 15.65, 16, 7.05);
	private static final VoxelShape SOUTH_PART4 = fixCuboidShaoe2(0.45, 0.6, 1.45, 1.05, 15.4, 7.05);
	private static final VoxelShape SOUTH_PART5 = fixCuboidShaoe2(15.05, 0.6, 1.45, 15.65, 15.4,
			7.05);
	private static final VoxelShape SOUTH_PART6 = fixCuboidShaoe2(1.05, 0.6, 6.65, 1.85, 15.4, 7.05);
	private static final VoxelShape SOUTH_PART7 = fixCuboidShaoe2(1.85, 0.6, 6.65, 7.45, 1.4, 7.05);
	private static final VoxelShape SOUTH_PART8 = fixCuboidShaoe2(8.65, 0.6, 6.65, 14.25, 1.4, 7.05);
	private static final VoxelShape SOUTH_PART9 = fixCuboidShaoe2(14.25, 0.6, 6.65, 15.05, 15.4,
			7.05);
	private static final VoxelShape SOUTH_PART10 = fixCuboidShaoe2(7.45, 0.6, 6.65, 8.65, 15.4,
			7.05);
	private static final VoxelShape SOUTH_PART11 = fixCuboidShaoe2(8.65, 14.6, 6.65, 14.25, 15.4,
			7.05);
	private static final VoxelShape SOUTH_PART12 = fixCuboidShaoe2(1.85, 14.6, 6.65, 7.45, 15.4,
			7.05);
	private static final VoxelShape SOUTH_PART13 = fixCuboidShaoe2(1.45, 1.08, 1.85, 1.85, 2.04,
			6.65);
	private static final VoxelShape SOUTH_PART14 = fixCuboidShaoe2(1.45, 4.28, 1.85, 1.85, 5.24,
			6.65);
	private static final VoxelShape SOUTH_PART15 = fixCuboidShaoe2(1.45, 2.68, 1.85, 1.85, 3.64,
			6.65);
	private static final VoxelShape SOUTH_PART16 = fixCuboidShaoe2(1.45, 13.88, 1.85, 1.85, 14.84,
			6.65);
	private static final VoxelShape SOUTH_PART17 = fixCuboidShaoe2(1.45, 12.28, 1.85, 1.85, 13.24,
			6.65);
	private static final VoxelShape SOUTH_PART18 = fixCuboidShaoe2(1.45, 10.68, 1.85, 1.85, 11.64,
			6.65);
	private static final VoxelShape SOUTH_PART19 = fixCuboidShaoe2(1.45, 9.08, 1.85, 1.85, 10.04,
			6.65);
	private static final VoxelShape SOUTH_PART20 = fixCuboidShaoe2(1.45, 7.48, 1.85, 1.85, 8.44,
			6.65);
	private static final VoxelShape SOUTH_PART21 = fixCuboidShaoe2(1.45, 5.88, 1.85, 1.85, 6.84,
			6.65);
	private static final VoxelShape SOUTH_PART22 = fixCuboidShaoe2(1.85, 1.4, 1.85, 2.25, 1.72,
			7.05);
	private static final VoxelShape SOUTH_PART23 = fixCuboidShaoe2(1.85, 7.8, 1.85, 2.25, 8.12,
			7.05);
	private static final VoxelShape SOUTH_PART24 = fixCuboidShaoe2(1.85, 6.2, 1.85, 2.25, 6.52,
			7.05);
	private static final VoxelShape SOUTH_PART25 = fixCuboidShaoe2(1.85, 4.6, 1.85, 2.25, 4.92,
			7.05);
	private static final VoxelShape SOUTH_PART26 = fixCuboidShaoe2(1.85, 14.2, 1.85, 2.25, 14.52,
			7.05);
	private static final VoxelShape SOUTH_PART27 = fixCuboidShaoe2(1.85, 12.6, 1.85, 2.25, 12.92,
			7.05);
	private static final VoxelShape SOUTH_PART28 = fixCuboidShaoe2(1.85, 11, 1.85, 2.25, 11.32,
			7.05);
	private static final VoxelShape SOUTH_PART29 = fixCuboidShaoe2(1.85, 9.4, 1.85, 2.25, 9.72,
			7.05);
	private static final VoxelShape SOUTH_PART30 = fixCuboidShaoe2(1.85, 3, 1.85, 2.25, 3.32, 7.05);
	private static final VoxelShape SOUTH_PART31 = fixCuboidShaoe2(8.25, 1.08, 1.85, 8.65, 2.04,
			6.65);
	private static final VoxelShape SOUTH_PART32 = fixCuboidShaoe2(8.25, 4.28, 1.85, 8.65, 5.24,
			6.65);
	private static final VoxelShape SOUTH_PART33 = fixCuboidShaoe2(8.25, 2.68, 1.85, 8.65, 3.64,
			6.65);
	private static final VoxelShape SOUTH_PART34 = fixCuboidShaoe2(8.25, 13.88, 1.85, 8.65, 14.84,
			6.65);
	private static final VoxelShape SOUTH_PART35 = fixCuboidShaoe2(8.25, 12.28, 1.85, 8.65, 13.24,
			6.65);
	private static final VoxelShape SOUTH_PART36 = fixCuboidShaoe2(8.25, 10.68, 1.85, 8.65, 11.64,
			6.65);
	private static final VoxelShape SOUTH_PART37 = fixCuboidShaoe2(8.25, 9.08, 1.85, 8.65, 10.04,
			6.65);
	private static final VoxelShape SOUTH_PART38 = fixCuboidShaoe2(8.25, 7.48, 1.85, 8.65, 8.44,
			6.65);
	private static final VoxelShape SOUTH_PART39 = fixCuboidShaoe2(8.25, 5.88, 1.85, 8.65, 6.84,
			6.65);
	private static final VoxelShape SOUTH_PART40 = fixCuboidShaoe2(8.65, 1.4, 1.85, 9.05, 1.72,
			7.05);
	private static final VoxelShape SOUTH_PART41 = fixCuboidShaoe2(8.65, 7.8, 1.85, 9.05, 8.12,
			7.05);
	private static final VoxelShape SOUTH_PART42 = fixCuboidShaoe2(8.65, 6.2, 1.85, 9.05, 6.52,
			7.05);
	private static final VoxelShape SOUTH_PART43 = fixCuboidShaoe2(8.65, 4.6, 1.85, 9.05, 4.92,
			7.05);
	private static final VoxelShape SOUTH_PART44 = fixCuboidShaoe2(8.65, 14.2, 1.85, 9.05, 14.52,
			7.05);
	private static final VoxelShape SOUTH_PART45 = fixCuboidShaoe2(8.65, 12.6, 1.85, 9.05, 12.92,
			7.05);
	private static final VoxelShape SOUTH_PART46 = fixCuboidShaoe2(8.65, 11, 1.85, 9.05, 11.32,
			7.05);
	private static final VoxelShape SOUTH_PART47 = fixCuboidShaoe2(8.65, 9.4, 1.85, 9.05, 9.72,
			7.05);
	private static final VoxelShape SOUTH_PART48 = fixCuboidShaoe2(8.65, 3, 1.85, 9.05, 3.32, 7.05);
	private static final VoxelShape SOUTH_PART49 = fixCuboidShaoe2(7.45, 1.08, 1.85, 7.85, 2.04,
			6.65);
	private static final VoxelShape SOUTH_PART50 = fixCuboidShaoe2(7.45, 4.28, 1.85, 7.85, 5.24,
			6.65);
	private static final VoxelShape SOUTH_PART51 = fixCuboidShaoe2(7.45, 2.68, 1.85, 7.85, 3.64,
			6.65);
	private static final VoxelShape SOUTH_PART52 = fixCuboidShaoe2(7.45, 13.88, 1.85, 7.85, 14.84,
			6.65);
	private static final VoxelShape SOUTH_PART53 = fixCuboidShaoe2(7.45, 12.28, 1.85, 7.85, 13.24,
			6.65);
	private static final VoxelShape SOUTH_PART54 = fixCuboidShaoe2(7.45, 10.68, 1.85, 7.85, 11.64,
			6.65);
	private static final VoxelShape SOUTH_PART55 = fixCuboidShaoe2(7.45, 9.08, 1.85, 7.85, 10.04,
			6.65);
	private static final VoxelShape SOUTH_PART56 = fixCuboidShaoe2(7.45, 7.48, 1.85, 7.85, 8.44,
			6.65);
	private static final VoxelShape SOUTH_PART57 = fixCuboidShaoe2(7.45, 5.88, 1.85, 7.85, 6.84,
			6.65);
	private static final VoxelShape SOUTH_PART58 = fixCuboidShaoe2(7.05, 1.4, 1.85, 7.45, 1.72,
			7.05);
	private static final VoxelShape SOUTH_PART59 = fixCuboidShaoe2(7.05, 7.8, 1.85, 7.45, 8.12,
			7.05);
	private static final VoxelShape SOUTH_PART60 = fixCuboidShaoe2(7.05, 6.2, 1.85, 7.45, 6.52,
			7.05);
	private static final VoxelShape SOUTH_PART61 = fixCuboidShaoe2(7.05, 4.6, 1.85, 7.45, 4.92,
			7.05);
	private static final VoxelShape SOUTH_PART62 = fixCuboidShaoe2(7.05, 14.2, 1.85, 7.45, 14.52,
			7.05);
	private static final VoxelShape SOUTH_PART63 = fixCuboidShaoe2(7.05, 12.6, 1.85, 7.45, 12.92,
			7.05);
	private static final VoxelShape SOUTH_PART64 = fixCuboidShaoe2(7.05, 11, 1.85, 7.45, 11.32,
			7.05);
	private static final VoxelShape SOUTH_PART65 = fixCuboidShaoe2(7.05, 9.4, 1.85, 7.45, 9.72,
			7.05);
	private static final VoxelShape SOUTH_PART66 = fixCuboidShaoe2(7.05, 3, 1.85, 7.45, 3.32, 7.05);
	private static final VoxelShape SOUTH_PART67 = fixCuboidShaoe2(14.25, 1.08, 1.85, 14.65, 2.04,
			6.65);
	private static final VoxelShape SOUTH_PART68 = fixCuboidShaoe2(14.25, 4.28, 1.85, 14.65, 5.24,
			6.65);
	private static final VoxelShape SOUTH_PART69 = fixCuboidShaoe2(14.25, 2.68, 1.85, 14.65, 3.64,
			6.65);
	private static final VoxelShape SOUTH_PART70 = fixCuboidShaoe2(14.25, 13.88, 1.85, 14.65, 14.84,
			6.65);
	private static final VoxelShape SOUTH_PART71 = fixCuboidShaoe2(14.25, 12.28, 1.85, 14.65, 13.24,
			6.65);
	private static final VoxelShape SOUTH_PART72 = fixCuboidShaoe2(14.25, 10.68, 1.85, 14.65, 11.64,
			6.65);
	private static final VoxelShape SOUTH_PART73 = fixCuboidShaoe2(14.25, 9.08, 1.85, 14.65, 10.04,
			6.65);
	private static final VoxelShape SOUTH_PART74 = fixCuboidShaoe2(14.25, 7.48, 1.85, 14.65, 8.44,
			6.65);
	private static final VoxelShape SOUTH_PART75 = fixCuboidShaoe2(14.25, 5.88, 1.85, 14.65, 6.84,
			6.65);
	private static final VoxelShape SOUTH_PART76 = fixCuboidShaoe2(13.85, 1.4, 1.85, 14.25, 1.72,
			7.05);
	private static final VoxelShape SOUTH_PART77 = fixCuboidShaoe2(13.85, 7.8, 1.85, 14.25, 8.12,
			7.05);
	private static final VoxelShape SOUTH_PART78 = fixCuboidShaoe2(13.85, 6.2, 1.85, 14.25, 6.52,
			7.05);
	private static final VoxelShape SOUTH_PART79 = fixCuboidShaoe2(13.85, 4.6, 1.85, 14.25, 4.92,
			7.05);
	private static final VoxelShape SOUTH_PART80 = fixCuboidShaoe2(13.85, 14.2, 1.85, 14.25, 14.52,
			7.05);
	private static final VoxelShape SOUTH_PART81 = fixCuboidShaoe2(13.85, 12.6, 1.85, 14.25, 12.92,
			7.05);
	private static final VoxelShape SOUTH_PART82 = fixCuboidShaoe2(13.85, 11, 1.85, 14.25, 11.32,
			7.05);
	private static final VoxelShape SOUTH_PART83 = fixCuboidShaoe2(13.85, 9.4, 1.85, 14.25, 9.72,
			7.05);
	private static final VoxelShape SOUTH_PART84 = fixCuboidShaoe2(13.85, 3, 1.85, 14.25, 3.32,
			7.05);
	private static final VoxelShape SOUTH_PART85 = fixCuboidShaoe2(1.05, 0.6, 1.45, 2.25, 15.4,
			1.85);
	private static final VoxelShape SOUTH_PART86 = fixCuboidShaoe2(13.85, 0.6, 1.45, 15.05, 15.4,
			1.85);
	private static final VoxelShape SOUTH_PART87 = fixCuboidShaoe2(7.45, 0.6, 1.45, 8.65, 15.4,
			1.85);
	private static final VoxelShape SOUTH_PART88 = fixCuboidShaoe2(1.05, 0.6, 1.85, 1.45, 15.4,
			6.65);
	private static final VoxelShape SOUTH_PART89 = fixCuboidShaoe2(14.65, 0.6, 1.85, 15.05, 15.4,
			6.65);
	private static final VoxelShape SOUTH_PART90 = fixCuboidShaoe2(10.81, 7.96, 1.69, 11.69, 13.88,
			1.85);
	private static final VoxelShape SOUTH_PART91 = fixCuboidShaoe2(10.81, 2.04, 1.69, 11.69, 7.96,
			1.85);
	private static final VoxelShape SOUTH_PART92 = fixCuboidShaoe2(4.41, 7.96, 1.69, 5.29, 13.88,
			1.85);
	private static final VoxelShape SOUTH_PART93 = fixCuboidShaoe2(4.41, 2.04, 1.69, 5.29, 7.96,
			1.85);
	private static final VoxelShape SOUTH_PART94 = fixCuboidShaoe2(2.65, 3.64, 1.45, 7.05, 4.28,
			1.61);
	private static final VoxelShape SOUTH_PART95 = fixCuboidShaoe2(2.65, 2.04, 1.45, 7.05, 2.68,
			1.61);
	private static final VoxelShape SOUTH_PART96 = fixCuboidShaoe2(2.65, 13.24, 1.45, 7.05, 13.88,
			1.61);
	private static final VoxelShape SOUTH_PART97 = fixCuboidShaoe2(2.65, 11.64, 1.45, 7.05, 12.28,
			1.61);
	private static final VoxelShape SOUTH_PART98 = fixCuboidShaoe2(2.65, 10.04, 1.45, 7.05, 10.68,
			1.61);
	private static final VoxelShape SOUTH_PART99 = fixCuboidShaoe2(2.65, 8.44, 1.45, 7.05, 9.08,
			1.61);
	private static final VoxelShape SOUTH_PART100 = fixCuboidShaoe2(2.65, 6.84, 1.45, 7.05, 7.48,
			1.61);
	private static final VoxelShape SOUTH_PART101 = fixCuboidShaoe2(2.65, 5.24, 1.45, 7.05, 5.88,
			1.61);
	private static final VoxelShape SOUTH_PART102 = fixCuboidShaoe2(9.05, 3.64, 1.45, 13.45, 4.28,
			1.61);
	private static final VoxelShape SOUTH_PART103 = fixCuboidShaoe2(9.05, 2.04, 1.45, 13.45, 2.68,
			1.61);
	private static final VoxelShape SOUTH_PART104 = fixCuboidShaoe2(9.05, 13.24, 1.45, 13.45, 13.88,
			1.61);
	private static final VoxelShape SOUTH_PART105 = fixCuboidShaoe2(9.05, 11.64, 1.45, 13.45, 12.28,
			1.61);
	private static final VoxelShape SOUTH_PART106 = fixCuboidShaoe2(9.05, 10.04, 1.45, 13.45, 10.68,
			1.61);
	private static final VoxelShape SOUTH_PART107 = fixCuboidShaoe2(9.05, 8.44, 1.45, 13.45, 9.08,
			1.61);
	private static final VoxelShape SOUTH_PART108 = fixCuboidShaoe2(9.05, 6.84, 1.45, 13.45, 7.48,
			1.61);
	private static final VoxelShape SOUTH_PART109 = fixCuboidShaoe2(9.05, 5.24, 1.45, 13.45, 5.88,
			1.61);
	private static final VoxelShape SOUTH_PART110 = fixCuboidShaoe2(7.85, 0.6, 1.85, 8.25, 15.4,
			6.65);
	protected static final VoxelShape SOUTH_AXIS_AABB = VoxelShapes.or(SOUTH_PART1, SOUTH_PART2, SOUTH_PART3, SOUTH_PART4,
			SOUTH_PART5, SOUTH_PART6, SOUTH_PART7, SOUTH_PART8, SOUTH_PART9, SOUTH_PART10, SOUTH_PART11, SOUTH_PART12,
			SOUTH_PART13, SOUTH_PART14, SOUTH_PART15, SOUTH_PART16, SOUTH_PART17, SOUTH_PART18, SOUTH_PART19, SOUTH_PART20,
			SOUTH_PART21, SOUTH_PART22, SOUTH_PART23, SOUTH_PART24, SOUTH_PART25, SOUTH_PART26, SOUTH_PART27, SOUTH_PART28,
			SOUTH_PART29, SOUTH_PART30, SOUTH_PART31, SOUTH_PART32, SOUTH_PART33, SOUTH_PART34, SOUTH_PART35, SOUTH_PART36,
			SOUTH_PART37, SOUTH_PART38, SOUTH_PART39, SOUTH_PART40, SOUTH_PART41, SOUTH_PART42, SOUTH_PART43, SOUTH_PART44,
			SOUTH_PART45, SOUTH_PART46, SOUTH_PART47, SOUTH_PART48, SOUTH_PART49, SOUTH_PART50, SOUTH_PART51, SOUTH_PART52,
			SOUTH_PART53, SOUTH_PART54, SOUTH_PART55, SOUTH_PART56, SOUTH_PART57, SOUTH_PART58, SOUTH_PART59, SOUTH_PART60,
			SOUTH_PART61, SOUTH_PART62, SOUTH_PART63, SOUTH_PART64, SOUTH_PART65, SOUTH_PART66, SOUTH_PART67, SOUTH_PART68,
			SOUTH_PART69, SOUTH_PART70, SOUTH_PART71, SOUTH_PART72, SOUTH_PART73, SOUTH_PART74, SOUTH_PART75, SOUTH_PART76,
			SOUTH_PART77, SOUTH_PART78, SOUTH_PART79, SOUTH_PART80, SOUTH_PART81, SOUTH_PART82, SOUTH_PART83, SOUTH_PART84,
			SOUTH_PART85, SOUTH_PART86, SOUTH_PART87, SOUTH_PART88, SOUTH_PART89, SOUTH_PART90, SOUTH_PART91, SOUTH_PART92,
			SOUTH_PART93, SOUTH_PART94, SOUTH_PART95, SOUTH_PART96, SOUTH_PART97, SOUTH_PART98, SOUTH_PART99, SOUTH_PART100,
			SOUTH_PART101, SOUTH_PART102, SOUTH_PART103, SOUTH_PART104, SOUTH_PART105, SOUTH_PART106, SOUTH_PART107,
			SOUTH_PART108, SOUTH_PART109, SOUTH_PART110);


	private static VoxelShape fixCuboidShaoe1(double x1, double y1, double z1, double x2, double y2, double z2) {
		return VoxelShapeHelper.makeCuboidShaoe90(16 - x1, y1, z1, 16 - x2, y2, z2);
	}

	private static VoxelShape fixCuboidShaoe2(double x1, double y1, double z1, double x2, double y2, double z2) {
		return VoxelShapeHelper.makeCuboidShaoe270(16 - x1, y1, z1, 16 - x2, y2, z2);
	}
}

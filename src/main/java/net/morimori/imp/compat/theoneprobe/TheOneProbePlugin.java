package net.morimori.imp.compat.theoneprobe;

import java.util.function.Function;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.IProgressStyle;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.morimori.imp.IamMusicPlayer;
import net.morimori.imp.sound.INewSoundPlayer;
import net.morimori.imp.util.SoundHelper;

public class TheOneProbePlugin implements Function<ITheOneProbe, Void> {

	@Override
	public Void apply(ITheOneProbe t) {

		t.registerProvider(new IProbeInfoProvider() {

			@Override
			public String getID() {

				return IamMusicPlayer.MODID + ":imp";
			}

			@Override
			public void addProbeInfo(ProbeMode arg0, IProbeInfo intfo, PlayerEntity playerIn, World worldIn,
					BlockState state, IProbeHitData arg5) {
				TileEntity te = worldIn.getTileEntity(arg5.getPos());

				if (te instanceof INewSoundPlayer) {
					IProgressStyle prgstyle = intfo.defaultProgressStyle().filledColor(191970).suffix("%");

					float posi = ((INewSoundPlayer) te).getPosition();
					float allle = SoundHelper.getSoundLength(((INewSoundPlayer) te).getSound(), playerIn.getServer());

					int par = Math.round(posi / allle * 100);

					intfo.progress(par, 100, prgstyle);
				}

			}
		});

		return null;
	}

}

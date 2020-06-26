package net.morimori.imp.compat.theoneprobe;

import java.util.function.Function;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.api.TextStyleClass;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.morimori.imp.IamMusicPlayer;
import net.morimori.imp.sound.INewSoundPlayer;
import net.morimori.imp.util.SoundHelper;
import net.morimori.imp.util.StringHelper;

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

					if (((INewSoundPlayer) te).canPlayed()) {
						float posi = ((INewSoundPlayer) te).getPosition();
						float allle = SoundHelper.getSoundLength(((INewSoundPlayer) te).getSound(),
								playerIn.getServer());
						int par = Math.round(posi / allle * 100);

						intfo.text(TextStyleClass.OK + ((INewSoundPlayer) te).getSound().name,
								intfo.defaultTextStyle());

						intfo.text(StringHelper.getTimeDisplay(((INewSoundPlayer) te).getPosition()) + " / "
								+ StringHelper
										.getTimeDisplay(SoundHelper.getSoundLength(((INewSoundPlayer) te).getSound(),
												playerIn.getServer())));
						intfo.progress(par, 100, intfo.defaultProgressStyle().filledColor(0xff191970)
					              .alternateFilledColor(0xff0000cd).suffix("%"));
					}
				}
			}
		});

		return null;
	}

}

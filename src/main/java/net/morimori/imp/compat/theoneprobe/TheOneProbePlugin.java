package net.morimori.imp.compat.theoneprobe;

import java.util.function.Function;

import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IIconStyle;
import mcjty.theoneprobe.api.ILayoutStyle;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.api.TextStyleClass;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.morimori.imp.IamMusicPlayer;
import net.morimori.imp.sound.INewSoundPlayer;
import net.morimori.imp.sound.WorldPlayListSoundData;
import net.morimori.imp.tileentity.BoomboxTileEntity;
import net.morimori.imp.tileentity.CassetteDeckTileEntity;
import net.morimori.imp.util.PictuerUtil;
import net.morimori.imp.util.SoundHelper;
import net.morimori.imp.util.StringHelper;
import net.morimori.imp.util.TextureHelper;

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

					ILayoutStyle alignment = intfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER);
					IProbeInfo horizontal = intfo.horizontal(alignment);

					if (te instanceof BoomboxTileEntity) {
						ItemStack cas = ((BoomboxTileEntity) te).getCassette();
						if (!cas.isEmpty()) {
							WorldPlayListSoundData wplsd = WorldPlayListSoundData.getWorldPlayListData(cas);
							if (worldIn.isRemote) {

								ResourceLocation location = wplsd.getAlbumImage();

								int pxsize = 0;
								int pysize = 0;
								if (TextureHelper.isImageNotExists(wplsd.getSoundData().album_image_uuid)) {
									pxsize = 128;
									pysize = 128;
								} else {
									pxsize = PictuerUtil.getImage(wplsd.getSoundData().album_image_uuid).getWidth();
									pysize = PictuerUtil.getImage(wplsd.getSoundData().album_image_uuid).getHeight();
								}
								pxsize /= 8;
								pysize /= 8;

								IIconStyle iconStyle = intfo.defaultIconStyle().width(pxsize + 4).height(pysize)
										.textureWidth(pxsize).textureHeight(pysize);
								horizontal.icon(location, 0, 0, pxsize, pysize, iconStyle)
										.text(TextStyleClass.OK + SoundHelper.getSoundName(cas));
							}
						}

					}
					if (te instanceof CassetteDeckTileEntity) {
						ItemStack cas = ((CassetteDeckTileEntity) te).getPlayCassette();
						if (!cas.isEmpty()) {
							WorldPlayListSoundData wplsd = WorldPlayListSoundData.getWorldPlayListData(cas);

							ResourceLocation location = wplsd.getAlbumImage();

							int pxsize = 0;
							int pysize = 0;
							if (TextureHelper.isImageNotExists(wplsd.getSoundData().album_image_uuid)) {
								pxsize = 128;
								pysize = 128;
							} else {
								pxsize = PictuerUtil.getImage(wplsd.getSoundData().album_image_uuid).getWidth();
								pysize = PictuerUtil.getImage(wplsd.getSoundData().album_image_uuid).getHeight();
							}
							pxsize /= 8;
							pysize /= 8;

							IIconStyle iconStyle = intfo.defaultIconStyle().width(pxsize + 4).height(pysize)
									.textureWidth(pxsize).textureHeight(pysize);
							horizontal.icon(location, 0, 0, pxsize, pysize, iconStyle)
									.text(TextStyleClass.OK + SoundHelper.getSoundName(cas));
						}

					}

					if (((INewSoundPlayer) te).canPlayed()) {
						float posi = ((INewSoundPlayer) te).getPosition();
						float allle = SoundHelper.getSoundLength(((INewSoundPlayer) te).getSound(),
								playerIn.getServer());
						int par = Math.round(posi / allle * 100);

						intfo.text(StringHelper.getTimeDisplay(((INewSoundPlayer) te).getPosition()) + " / "
								+ StringHelper
										.getTimeDisplay(SoundHelper.getSoundLength(((INewSoundPlayer) te).getSound(),
												playerIn.getServer())));
						intfo.progress(par, 100, intfo.defaultProgressStyle().filledColor(191970).suffix("%"));
					}
				}
			}
		});

		return null;
	}

}

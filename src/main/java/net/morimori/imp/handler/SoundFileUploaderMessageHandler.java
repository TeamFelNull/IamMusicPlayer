package net.morimori.imp.handler;

import java.nio.file.Paths;
import java.util.function.Supplier;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.NetworkEvent;
import net.morimori.imp.block.IMPBlocks;
import net.morimori.imp.block.SoundfileUploaderBlock;
import net.morimori.imp.client.screen.SoundFileUploaderMonitorTextures;
import net.morimori.imp.client.screen.SoundFileUploaderWindwos;
import net.morimori.imp.file.PlayList;
import net.morimori.imp.file.ServerFileSender;
import net.morimori.imp.file.ServerSoundFileReceiver;
import net.morimori.imp.packet.SoundFileUploaderMessage;
import net.morimori.imp.tileentity.SoundFileUploaderTileEntity;
import net.morimori.imp.util.PlayerHelper;

public class SoundFileUploaderMessageHandler {
	public static void reversiveMessage(SoundFileUploaderMessage message, Supplier<NetworkEvent.Context> ctx) {
		ServerPlayerEntity SPE = ctx.get().getSender();
		if (SPE.world.getDimension().getType().getId() != message.dim)
			return;

		BlockState state = SPE.world.getBlockState(message.pos);

		if (state.getBlock() != IMPBlocks.SOUNDFILE_UPLOADER)
			return;

		if (message.state == 0) {
			SPE.world.setBlockState(message.pos, state.cycle(SoundfileUploaderBlock.ON));
			SPE.world.setBlockState(message.pos,
					SPE.world.getBlockState(message.pos).with(
							SoundfileUploaderBlock.SOUNDFILE_UPLOADER_WINDWOS,
							SoundFileUploaderWindwos.DESKTOP));
		} else if (message.state == 1) {
			SPE.world.setBlockState(message.pos,
					state.with(SoundfileUploaderBlock.SOUNDFILE_UPLOADER_MONITOR, SoundFileUploaderMonitorTextures.YJ));
		} else if (message.state == 2) {
			SPE.world.setBlockState(message.pos,
					SPE.world.getBlockState(message.pos).with(
							SoundfileUploaderBlock.SOUNDFILE_UPLOADER_WINDWOS,
							SoundFileUploaderWindwos.CLIENT_FILESELECT));
		} else if (message.state == 3) {
			SPE.world.setBlockState(message.pos,
					SPE.world.getBlockState(message.pos).with(
							SoundfileUploaderBlock.SOUNDFILE_UPLOADER_WINDWOS,
							SoundFileUploaderWindwos.DESKTOP));
		} else if (message.state == 4) {
			SPE.world.setBlockState(message.pos,
					SPE.world.getBlockState(message.pos).with(
							SoundfileUploaderBlock.SOUNDFILE_UPLOADER_WINDWOS,
							SoundFileUploaderWindwos.UPLOAD_FILE));
		} else if (message.state == 5) {
			if (SPE.world.getTileEntity(message.pos) instanceof SoundFileUploaderTileEntity) {
				SoundFileUploaderTileEntity sfit = (SoundFileUploaderTileEntity) SPE.world
						.getTileEntity(message.pos);
				sfit.setUsePlayerUUID(SPE);
			}
		} else if (message.state == 6) {
			if (SPE.world.getTileEntity(message.pos) instanceof SoundFileUploaderTileEntity) {
				SoundFileUploaderTileEntity sfit = (SoundFileUploaderTileEntity) SPE.world
						.getTileEntity(message.pos);
				sfit.setUsePlayerUUID("");
			}
		} else if (message.state == 7) {
			if (SPE.world.getTileEntity(message.pos) instanceof SoundFileUploaderTileEntity) {
				SoundFileUploaderTileEntity sfit = (SoundFileUploaderTileEntity) SPE.world
						.getTileEntity(message.pos);
				sfit.setFliePath(message.string);
			}
		} else if (message.state == 8) {
			if (SPE.world.getTileEntity(message.pos) instanceof SoundFileUploaderTileEntity) {
				SoundFileUploaderTileEntity sfit = (SoundFileUploaderTileEntity) SPE.world
						.getTileEntity(message.pos);
				sfit.setFliePath("");
			}
		} else if (message.state == 9) {
			SPE.world.setBlockState(message.pos,
					SPE.world.getBlockState(message.pos).with(
							SoundfileUploaderBlock.SOUNDFILE_UPLOADER_WINDWOS,
							SoundFileUploaderWindwos.SERVER_FILESELECT));
		} else if (message.state == 10) {
			PlayList.syncPlayerServerWorldList(SPE);
		} else if (message.state == 11) {
			PlayList.syncdEveryoneServerWorldList(SPE);
		} else if (message.state == 12) {
			SPE.world.setBlockState(message.pos,
					state.with(SoundfileUploaderBlock.SOUNDFILE_UPLOADER_MONITOR,
							SoundFileUploaderMonitorTextures.GABADADDY));
		} else if (message.state == 13) {
			SPE.world.setBlockState(message.pos,
					SPE.world.getBlockState(message.pos).with(
							SoundfileUploaderBlock.SOUNDFILE_UPLOADER_WINDWOS,
							SoundFileUploaderWindwos.UPLOAD_COFIN));
		} else if (message.state == 14) {
			SPE.world.setBlockState(message.pos,
					SPE.world.getBlockState(message.pos).with(
							SoundfileUploaderBlock.SOUNDFILE_UPLOADER_WINDWOS,
							SoundFileUploaderWindwos.EDIT_FILE));
		} else if (message.state == 15) {
			ServerSoundFileReceiver.receiveStop(PlayerHelper.getUUID(SPE), message.string);
		} else if (message.state == 16) {
			ServerFileSender.startSender(PlayerHelper.getUUID(SPE), Paths.get(message.string), true,
					SPE.getServer());
		} else if (message.state == 17) {
			ServerFileSender.stopSend(PlayerHelper.getUUID(SPE), Integer.parseInt(message.string));
		} else if (message.state == 18) {
			PlayList.deleteWorldPlayListSoundFile(ctx.get().getSender().getServer(), message.string);
		} else if (message.state == 19) {
			if (SPE.world.getTileEntity(message.pos) instanceof SoundFileUploaderTileEntity) {
				SoundFileUploaderTileEntity sfit = (SoundFileUploaderTileEntity) SPE.world
						.getTileEntity(message.pos);
				sfit.playerstager.put(PlayerHelper.getUUID(SPE), message.string);

			}
		}
	}
}

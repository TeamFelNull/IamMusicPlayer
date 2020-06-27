package red.felnull.imp.handler;

import java.nio.file.Paths;
import java.util.function.Supplier;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.NetworkEvent;
import red.felnull.imp.block.IMPBlocks;
import red.felnull.imp.block.SoundfileUploaderBlock;
import red.felnull.imp.client.screen.SoundFileUploaderMonitorTextures;
import red.felnull.imp.client.screen.SoundFileUploaderWindwos;
import red.felnull.imp.file.PlayList;
import red.felnull.imp.file.ServerFileSender;
import red.felnull.imp.file.ServerSoundFileReceiver;
import red.felnull.imp.packet.SoundFileUploaderMessage;
import red.felnull.imp.tileentity.SoundFileUploaderTileEntity;
import red.felnull.imp.util.PlayerHelper;

public class SoundFileUploaderMessageHandler {
    public static void reversiveMessage(SoundFileUploaderMessage message, Supplier<NetworkEvent.Context> ctx) {
        ServerPlayerEntity SPE = ctx.get().getSender();
        if (0 != message.dim)
            return;

        BlockState state = SPE.world.getBlockState(message.pos);

        if (state.getBlock() != IMPBlocks.SOUNDFILE_UPLOADER)
            return;

        if (message.state == 0) {
            SPE.world.setBlockState(message.pos, state.func_235896_a_(SoundfileUploaderBlock.ON));
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
            PlayList.deleteWorldPlayListSoundFile(ctx.get().getSender().getServer(), message.string.split(":")[0], message.string.split(":")[1]);
        } else if (message.state == 19) {
            if (SPE.world.getTileEntity(message.pos) instanceof SoundFileUploaderTileEntity) {
                SoundFileUploaderTileEntity sfit = (SoundFileUploaderTileEntity) SPE.world
                        .getTileEntity(message.pos);
                sfit.playerstager.put(PlayerHelper.getUUID(SPE), message.string);

            }
        }
    }
}

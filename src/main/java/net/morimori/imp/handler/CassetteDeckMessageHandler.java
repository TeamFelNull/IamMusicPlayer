package net.morimori.imp.handler;

import java.util.function.Supplier;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.NetworkEvent;
import net.morimori.imp.block.CassetteDeckBlock;
import net.morimori.imp.block.CassetteDeckStates;
import net.morimori.imp.block.IMPBlocks;
import net.morimori.imp.file.PlayList;
import net.morimori.imp.packet.CassetteDeckMessage;
import net.morimori.imp.tileentity.CassetteDeckTileEntity;
import net.morimori.imp.util.PlayerHelper;

public class CassetteDeckMessageHandler {
    public static void reversiveMessage(CassetteDeckMessage message, Supplier<NetworkEvent.Context> ctx) {
        ServerPlayerEntity SPE = ctx.get().getSender();
        if (SPE.world.getDimension().getType().getId() != message.dim)
            return;

        BlockState state = SPE.world.getBlockState(message.pos);

        if (state.getBlock() != IMPBlocks.CASSETTE_DECK)
            return;

        if (message.state == 0) {
            SPE.world.setBlockState(message.pos,
                    state.with(CassetteDeckBlock.CASSETTE_DECK_STATES, CassetteDeckStates.RECORD));
        } else if (message.state == 1) {
            SPE.world.setBlockState(message.pos,
                    state.with(CassetteDeckBlock.CASSETTE_DECK_STATES, CassetteDeckStates.PLAY));
        } else if (message.state == 2) {
            SPE.world.setBlockState(message.pos,
                    state.with(CassetteDeckBlock.CASSETTE_DECK_STATES, CassetteDeckStates.NONE));
        } else if (message.state == 3) {
            SPE.world.setBlockState(message.pos,
                    state.with(CassetteDeckBlock.CASSETTE_DECK_STATES, CassetteDeckStates.DELETE));
        } else if (message.state == 4) {
            SPE.world.setBlockState(message.pos,
                    state.with(CassetteDeckBlock.CASSETTE_DECK_STATES, CassetteDeckStates.COPY));
        } else if (message.state == 5) {
            PlayList.syncPlayerServerWorldList(SPE);
        } else if (message.state == 6) {
            PlayList.syncdEveryoneServerWorldList(SPE);
        } else if (message.state == 7) {
            CassetteDeckTileEntity sfit = (CassetteDeckTileEntity) SPE.world
                    .getTileEntity(message.pos);
            sfit.setFolderNameAndFileName(message.string.split(":")[0], message.string.split(":")[1]);
        } else if (message.state == 8) {
            if (SPE.world.getTileEntity(message.pos) instanceof CassetteDeckTileEntity) {
                CassetteDeckTileEntity sfit = (CassetteDeckTileEntity) SPE.world
                        .getTileEntity(message.pos);
                sfit.playerstager.put(PlayerHelper.getUUID(SPE), message.string);

            }
        }
    }

}

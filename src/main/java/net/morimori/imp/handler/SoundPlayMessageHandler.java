package net.morimori.imp.handler;

import java.util.function.Supplier;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.NetworkEvent;
import net.morimori.imp.packet.SoundPlayMessage;
import net.morimori.imp.tileentity.CassetteDeckTileEntity;
import net.morimori.imp.util.PlayerHelper;

public class SoundPlayMessageHandler {
	public static void reversiveMessage(SoundPlayMessage message, Supplier<NetworkEvent.Context> ctx) {
		ServerPlayerEntity SPE = ctx.get().getSender();
		if (SPE.world.getDimension().getType().getId() != message.dim)
			return;

		//		BlockState state = SPE.world.getBlockState(message.pos);

		if (message.state == 0) {
			/*	if (SPE.world.getTileEntity(message.pos) instanceof BoomboxTileEntity) {
					BoomboxTileEntity sfit = (BoomboxTileEntity) SPE.world
							.getTileEntity(message.pos);
					if (!sfit.isLoopPlay()) {
						sfit.lisnFinishedPlayers.add(PlayerHelper.getUUID(SPE));
					}
				}*/
			if (SPE.world.getTileEntity(message.pos) instanceof CassetteDeckTileEntity) {
				CassetteDeckTileEntity sfit = (CassetteDeckTileEntity) SPE.world
						.getTileEntity(message.pos);
				if (!sfit.isLoopPlay()) {
					sfit.lisnFinishedPlayers.add(PlayerHelper.getUUID(SPE));
				}
			}

		}
	}
}

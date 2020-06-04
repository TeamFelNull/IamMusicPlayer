package net.morimori.imp.client.handler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import net.morimori.imp.IkisugiMusicPlayer;
import net.morimori.imp.packet.ServerClientDataSyncMessage;
import net.morimori.imp.util.FileHelper;
import net.morimori.imp.util.FileLoader;
import net.morimori.imp.util.TextureHelper;

public class ServerClientDataSyncMessageHandler {
	private static Minecraft mc = Minecraft.getInstance();
	private static TextureManager tm = mc.getTextureManager();

	public static void reversiveMessage(ServerClientDataSyncMessage message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().setPacketHandled(true);

		if (message.data != null) {
			if (message.id == 0) {
				FileLoader.fileBytesWriter(message.data,
						FileHelper.getClientPictuerCashPath().resolve(message.st + ".png"));

				ResourceLocation imagelocation = new ResourceLocation(IkisugiMusicPlayer.MODID,
						"pictuer/" + message.st);
				try {
					ByteArrayInputStream bis = new ByteArrayInputStream(message.data);
					NativeImage NI = NativeImage.read(bis);
					tm.func_229263_a_(imagelocation, new DynamicTexture(NI));
					TextureHelper.pictuers.put(message.st, imagelocation);
				} catch (IOException e) {

				}
			}
		}
	}
}

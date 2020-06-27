package red.felnull.imp.client.handler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.packet.ServerClientDataSyncMessage;
import red.felnull.imp.util.FileHelper;
import red.felnull.imp.util.FileLoader;
import red.felnull.imp.util.TextureHelper;

public class ServerClientDataSyncMessageHandler {

	public static void reversiveMessage(ServerClientDataSyncMessage message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().setPacketHandled(true);
		setTextuer(message, ctx);
	}

	@OnlyIn(Dist.CLIENT)
	public static void setTextuer(ServerClientDataSyncMessage message, Supplier<NetworkEvent.Context> ctx) {

		if (message.data != null) {
			if (message.id == 0) {
				FileLoader.fileBytesWriter(message.data,
						FileHelper.getClientPictuerCashPath().resolve(message.st + ".png"));

				ResourceLocation imagelocation = new ResourceLocation(IamMusicPlayer.MODID,
						"pictuer/" + message.st);
				try {
					ByteArrayInputStream bis = new ByteArrayInputStream(message.data);
					NativeImage NI = NativeImage.read(bis);
					Minecraft mc = IamMusicPlayer.proxy.getMinecraft();
					mc.textureManager.loadTexture(imagelocation, new DynamicTexture(NI));
					TextureHelper.pictuers.put(message.st, imagelocation);
				} catch (IOException e) {

				}
			}
		}
	}
}

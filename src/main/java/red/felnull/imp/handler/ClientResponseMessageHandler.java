package red.felnull.imp.handler;

import java.io.File;
import java.nio.file.Path;
import java.util.function.Supplier;

import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import red.felnull.imp.file.ImagePictuers;
import red.felnull.imp.file.ServerFileSender;
import red.felnull.imp.packet.ClientResponseMessage;
import red.felnull.imp.packet.PacketHandler;
import red.felnull.imp.packet.ServerClientDataSyncMessage;
import red.felnull.imp.packet.ServerResponseMessage;
import red.felnull.imp.util.FileHelper;
import red.felnull.imp.util.PlayerHelper;

public class ClientResponseMessageHandler {
    public static void reversiveMessage(ClientResponseMessage message, Supplier<NetworkEvent.Context> ctx) {

        if (message.num == 0) {
            ServerFileSender.responseWaits.get(PlayerHelper.getUUID(ctx.get().getSender())).put(message.id, false);
        } else if (message.num == 1) {
            ServerFileSender.startSender(PlayerHelper.getUUID(ctx.get().getSender()),
                    FileHelper.getWorldPlayListDataPath(ctx.get().getSender().getServer()).resolve(message.st), false,
                    ctx.get().getSender().getServer());
        } else if (message.num == 2) {

            if (ServerFileSender.canSending(PlayerHelper.getUUID(ctx.get().getSender()))) {
                PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> ctx.get().getSender()),
                        new ServerResponseMessage(1, ""));
            } else {
                PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> ctx.get().getSender()),
                        new ServerResponseMessage(2, ""));
            }

        } else if (message.num == 3) {

            File picfile = FileHelper.getWorldPictuerPath(ctx.get().getSender().getServer())
                    .resolve(message.st + ".png").toFile();

            if (picfile.exists() && picfile != null) {
                byte[] picbyte = ImagePictuers.readPictuer(message.st, ctx.get().getSender().getServer());

                PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> ctx.get().getSender()),
                        new ServerClientDataSyncMessage(0, message.st, picbyte));
            }
        } else if (message.num == 4) {
            if (!message.st.isEmpty() && !message.st.equals(":")) {
                Path path = FileHelper.getWorldPlayerPlayListDataPath(ctx.get().getSender().getServer(),
                        message.st.split(":")[0]).resolve(message.st.split(":")[1]);

                ServerFileSender.startSender(PlayerHelper.getUUID(ctx.get().getSender()), path, false,
                        ctx.get().getSender().getServer());
            }
        }
    }
}

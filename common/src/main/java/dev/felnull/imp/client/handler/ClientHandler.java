package dev.felnull.imp.client.handler;

import dev.architectury.event.events.client.ClientPlayerEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.jetbrains.annotations.Nullable;

public class ClientHandler {
    private static final Minecraft mc = Minecraft.getInstance();

    public static void init() {
        ClientPlayerEvent.CLIENT_PLAYER_QUIT.register(new ClientPlayerEvent.ClientPlayerQuit() {
            @Override
            public void quit(@Nullable LocalPlayer localPlayer) {
                if (mc.player != null && mc.player == localPlayer) {
                    System.out.println("test");
                }
            }
        });
    }
}

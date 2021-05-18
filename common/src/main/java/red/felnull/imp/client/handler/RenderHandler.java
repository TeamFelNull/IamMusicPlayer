package red.felnull.imp.client.handler;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.client.music.subtitle.SubtitleManager;
import red.felnull.imp.client.music.subtitle.SubtitleSystem;
import red.felnull.otyacraftengine.api.event.TickEvent;

public class RenderHandler {
    private static final Minecraft mc = Minecraft.getInstance();

    public static void onRender(TickEvent.RenderTickEvent e) {
        if (IamMusicPlayer.CONFIG.subtitleSystem == SubtitleSystem.OVERLAY && e.phase == TickEvent.Phase.END && mc.level != null && !mc.options.hideGui) {
            PoseStack stack = new PoseStack();
            stack.pushPose();
            stack.translate(0, 0, 1000);
            SubtitleManager.getInstance().overlay.render(stack);
            stack.popPose();
        }
    }
}

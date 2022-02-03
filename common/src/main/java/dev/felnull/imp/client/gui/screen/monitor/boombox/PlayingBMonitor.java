package dev.felnull.imp.client.gui.screen.monitor.boombox;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.blockentity.BoomboxBlockEntity;
import dev.felnull.imp.client.gui.screen.BoomboxScreen;
import dev.felnull.imp.item.CassetteTapeItem;
import dev.felnull.imp.music.resource.Music;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.network.chat.TextComponent;

public class PlayingBMonitor extends BoomboxMonitor {
    public PlayingBMonitor(BoomboxBlockEntity.MonitorType monitorType, BoomboxScreen screen) {
        super(monitorType, screen);
    }

    @Override
    public void render(PoseStack poseStack, float f, int mouseX, int mouseY) {
        super.render(poseStack, f, mouseX, mouseY);

        if (!getCassetteTape().isEmpty()) {
            Music music = CassetteTapeItem.getMusic(getCassetteTape());
            if (music != null) {
                int sx = 2;
                if (!music.getImage().isEmpty()) {
                    getPlayImageRenderer().draw(music.getImage(), poseStack, getStartX() + 1, getStartY() + 1, height - 2);
                    sx += height - 2;
                }
                drawSmartText(poseStack, new TextComponent(OERenderUtil.getWidthString(music.getName(), width - sx - 2, "...")), getStartX() + sx, getStartY() + 3);
            } else {

            }
        }
    }
}

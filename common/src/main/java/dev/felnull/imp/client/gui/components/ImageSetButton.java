package dev.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.client.gui.IIMPSmartRender;
import dev.felnull.imp.client.gui.screen.monitor.MusicManagerMonitor;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class ImageSetButton extends Button implements IIMPSmartRender {
    private final ImageSetType type;

    public ImageSetButton(int x, int y, ImageSetType type, OnPress onPress, Screen screen) {
        super(x, y, 33, 15, type.getName(), onPress, (button, poseStack, i, j) -> screen.renderTooltip(poseStack, type.getName(), i, j));
        this.type = type;
    }

    public static enum ImageSetType {
        FILE_OPEN("file_open"),
        PLAYER_FACE("player_face"),
        DELETE("delete"),
        URL("url");
        private final Component name;

        private ImageSetType(String name) {
            this.name = new TranslatableComponent("imp.button.imageSet." + name);
        }

        public Component getName() {
            return name;
        }
    }

    @Override
    public void renderButton(PoseStack poseStack, int mx, int my, float f) {
        drawSmartButtonBox(poseStack, x, y, width, height, isHovered());
        if (type == ImageSetType.PLAYER_FACE) {
            OERenderUtil.drawPlayerFace(poseStack, mc.player.getGameProfile().getId(), (float) x + ((float) width - 11f) / 2f, (float) y + ((float) height - 11f) / 2f, 11);
        } else {
            OERenderUtil.drawTexture(MusicManagerMonitor.WIDGETS_TEXTURE, poseStack, (float) x + ((float) width - 11f) / 2f, (float) y + ((float) height - 11f) / 2f, 73 + (type == ImageSetType.URL ? 22 : type == ImageSetType.DELETE ? 11 : 0), 19, 11, 11);
        }
        if (this.isHovered())
            this.renderToolTip(poseStack, mx, my);
    }
}

package dev.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class ImageSetButton extends SmartButton {
    private final ImageSetType type;

    public ImageSetButton(int x, int y, ImageSetType type, OnPress onPress, Screen screen) {
        super(x, y, 33, 15, type.getName(), onPress, (button, poseStack, i, j) -> screen.renderTooltip(poseStack, type.getName(), i, j));
        this.type = type;
    }

    public static enum ImageSetType {
        FILE_OPEN("file_open"),
        PLAYER_FACE("player_face"),
        DELETE("delete");
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
        super.renderButton(poseStack, mx, my, f);
        if (type == ImageSetType.PLAYER_FACE) {
            OERenderUtil.drawPlayerFace(poseStack, mc.player.getGameProfile().getId(), (float) x + ((float) width - 11f) / 2f, (float) y + ((float) height - 11f) / 2f, 11);
        } else {

        }
    }
}

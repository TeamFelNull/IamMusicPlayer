package dev.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.client.gui.IIMPSmartRender;
import dev.felnull.imp.client.gui.screen.monitor.music_manager.MusicManagerMonitor;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ImageSetButton extends IMPButton implements IIMPSmartRender {
    private final ImageSetType type;

    public ImageSetButton(int x, int y, ImageSetType type, OnPress onPress, Screen screen) {
        super(x, y, 33, 15, type.getName(), onPress, Tooltip.create(type.getName()));
        this.type = type;
    }

    public static enum ImageSetType {
        FILE_OPEN("file_open"),
        PLAYER_FACE("player_face"),
        DELETE("delete");
        //        URL("url");
        private final Component name;

        private ImageSetType(String name) {
            this.name = Component.translatable("imp.button.imageSet." + name);
        }

        public Component getName() {
            return name;
        }
    }

    @Override
    public void renderButton(PoseStack poseStack, int mx, int my, float f) {
        drawSmartButtonBox(poseStack, getX(), getY(), width, height, isHoveredOrFocused());
        if (type == ImageSetType.PLAYER_FACE) {
            OERenderUtils.drawPlayerFace(poseStack, mc.player.getGameProfile().getId(), (float) getX() + ((float) width - 11f) / 2f, (float) getY() + ((float) height - 11f) / 2f, 11);
        } else {
            OERenderUtils.drawTexture(MusicManagerMonitor.WIDGETS_TEXTURE, poseStack, (float) getX() + ((float) width - 11f) / 2f, (float) getY() + ((float) height - 11f) / 2f, 73 + (type == ImageSetType.DELETE ? 11 : 0), 19, 11, 11);
        }
        /*if (this.isHoveredOrFocused())
            this.renderToolTip(poseStack, mx, my);*/
    }
}

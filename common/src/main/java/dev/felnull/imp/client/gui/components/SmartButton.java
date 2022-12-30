package dev.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.client.gui.IIMPSmartRender;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class SmartButton extends IMPButton implements IIMPSmartRender {
    private boolean center = true;
    private ResourceLocation iconTexture;
    private int iconStX, iconStY, iconWidth, iconHeight, textureWidth, textureHeight;
    private boolean hideText;

    public void setCenter(boolean center) {
        this.center = center;
    }

    public SmartButton(int x, int y, int w, int h, Component text, OnPress onPress) {
        super(x, y, w, h, text, onPress);
    }

    public void setHideText(boolean hideText) {
        this.hideText = hideText;
    }

    @Override
    public void renderButton(PoseStack poseStack, int mx, int my, float f) {
        drawSmartButtonBox(poseStack, getX(), getY(), width, height, this.getYImage(this.isHoveredOrFocused()));
        float fx = center ? (float) (width - mc.font.width(getMessage())) / 2f : 2;
        float fy = (float) (height - 7) / 2f;

        if (iconTexture != null) {
            float itx = hideText ? (width - iconWidth) / 2f : 2;
            float ity = (float) (height - iconHeight) / 2f;
            fx += itx + iconWidth;
            OERenderUtils.drawTexture(iconTexture, poseStack, getX() + itx, getY() + ity, iconStX, iconStY, iconWidth, iconHeight, textureWidth, textureHeight);
        }

        if (!hideText)
            drawSmartText(poseStack, getMessage(), getX() + fx, getY() + fy);

        /*if (this.isHoveredOrFocused())
            this.renderToolTip(poseStack, mx, my);*/
    }

    public void setIcon(ResourceLocation location, int stX, int stY, int w, int h) {
        setIcon(location, stX, stY, w, h, 256, 256);
    }

    public void setIcon(ResourceLocation location, int stX, int stY, int w, int h, int texW, int texH) {
        setCenter(false);
        this.iconTexture = location;
        this.iconStX = stX;
        this.iconStY = stY;
        this.iconWidth = w;
        this.iconHeight = h;
        this.textureWidth = texW;
        this.textureHeight = texH;
    }

}

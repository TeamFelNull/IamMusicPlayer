package dev.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.client.gui.IIMPSmartRender;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class SmartButton extends Button implements IIMPSmartRender {
    private boolean center = true;
    private ResourceLocation iconTexture;
    private int iconStX, iconStY, iconWidth, iconHeight, textureWidth, textureHeight;

    public SmartButton(int x, int y, int w, int h, Component text, OnPress onPress, OnTooltip onTooltip) {
        super(x, y, w, h, text, onPress, onTooltip);
    }

    public void setCenter(boolean center) {
        this.center = center;
    }

    public SmartButton(int x, int y, int w, int h, Component text, OnPress onPress) {
        super(x, y, w, h, text, onPress);
    }

    @Override
    public void renderButton(PoseStack poseStack, int mx, int my, float f) {
        drawSmartButtonBox(poseStack, x, y, width, height, this.getYImage(this.isHovered()));
        float fx = center ? (float) (width - mc.font.width(getMessage())) / 2f : 2;
        float fy = (float) (height - 7) / 2f;

        if (iconTexture != null) {
            float itx = 2;
            float ity = (float) (height - iconHeight) / 2f;
            fx += itx + iconWidth;
            OERenderUtil.drawTexture(iconTexture, poseStack, x + itx, y + ity, iconStX, iconStY, iconWidth, iconHeight, textureWidth, textureHeight);
        }

        drawSmartText(poseStack, getMessage(), x + fx, y + fy);

        if (this.isHovered())
            this.renderToolTip(poseStack, mx, my);
    }

    public void setIcon(ResourceLocation location, int stX, int stY, int w, int h) {
        setIcon(location, stX, stY, w, w, 256, 256);
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

package dev.felnull.imp.client.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.client.gui.screen.IMPBaseContainerScreen;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public class PowerButton extends ImageButton {
    private final IMPBaseContainerScreen<?> screen;
    private final ResourceLocation resourceLocation;
    private final int xTexStart;
    private final int yTexStart;
    private final int textureWidth;
    private final int textureHeight;

    public PowerButton(IMPBaseContainerScreen<?> screen, int x, int y, int width, int height, int xTexStart, int yTexStart, ResourceLocation resourceLocation, int textureWidth, int textureHeight) {
        super(x, y, width, height, xTexStart, yTexStart, height, resourceLocation, textureWidth, textureHeight, button -> onPower(screen), new TranslatableComponent("imp.button.power"));
        this.screen = screen;
        this.resourceLocation = resourceLocation;
        this.xTexStart = xTexStart;
        this.yTexStart = yTexStart;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    @Override
    public void renderButton(PoseStack poseStack, int i, int j, float f) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, this.resourceLocation);
        int tx = this.xTexStart;
        int ty = this.yTexStart;

        if (this.isHoveredOrFocused())
            ty += this.height;

        if (screen.isPowered())
            tx += this.width;

        RenderSystem.enableDepthTest();
        blit(poseStack, this.x, this.y, (float) tx, (float) ty, this.width, this.height, this.textureWidth, this.textureHeight);
        if (this.isHoveredOrFocused())
            this.renderToolTip(poseStack, i, j);
    }

    private static void onPower(IMPBaseContainerScreen<?> screen) {
        screen.insPower(!screen.isPowered());
    }
}

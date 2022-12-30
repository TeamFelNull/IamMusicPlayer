package dev.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.block.BoomboxData;
import dev.felnull.imp.client.gui.screen.BoomboxScreen;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class BoomboxButton extends IMPButton {
    private final BoomboxData.ButtonType type;
    private final Supplier<BoomboxData.Buttons> buttons;
    private final BooleanSupplier downShift;

    public BoomboxButton(int x, int y, BoomboxData.ButtonType type, OnPress onPress, Supplier<BoomboxData.Buttons> buttons) {
        this(x, y, type, onPress, buttons, () -> false);
    }

    public BoomboxButton(int x, int y, BoomboxData.ButtonType type, OnPress onPress, Supplier<BoomboxData.Buttons> buttons, BooleanSupplier downShift) {
        super(x, y, 19, 13, type.getComponent(), onPress);
        this.type = type;
        this.buttons = buttons;
        this.downShift = downShift;
    }

    @Override
    public void renderButton(PoseStack poseStack, int i, int j, float f) {
        int r = isHoveredOrFocused() ? 1 : 0;
        if (type.getState(buttons.get()))
            r += 2;
        OERenderUtils.drawTexture(BoomboxScreen.BG_TEXTURE, poseStack, getX(), getY(), r * 19, 175, 19, 13);

        float zx = ((float) width - 9f) / 2f;
        float zy = ((float) height - 9f) / 2f;
        OERenderUtils.drawTexture(BoomboxScreen.BG_TEXTURE, poseStack, getX() + zx, getY() + zy, (type.ordinal() - 1) * 9, 188 + (downShift.getAsBoolean() ? 9 : 0), 9, 9);
    }

    public BoomboxData.ButtonType getType() {
        return type;
    }

}

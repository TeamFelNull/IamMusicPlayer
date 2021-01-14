package red.felnull.imp.client.handler;


import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.OptionsScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.client.gui.screen.IMPAbstractEquipmentScreen;
import red.felnull.imp.client.gui.screen.IMPOptionsScreen;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;
import red.felnull.otyacraftengine.util.IKSGStyles;

public class RenderHandler {
    public static final ResourceLocation IMP_OPTIONS_LOGO = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/options_logo.png");

    @SubscribeEvent
    public static void onGuiDraowEvent(GuiScreenEvent.InitGuiEvent.Post e) {
        if (e.getGui() instanceof OptionsScreen) {
            Button feedback = (Button) e.getWidgetList().stream().filter(n -> n instanceof Button && n.getMessage() instanceof TranslationTextComponent && ((TranslationTextComponent) n.getMessage()).getKey().equals("options.sounds")).findAny().orElseGet(null);
            feedback.setWidth(130);

            e.addWidget(new IMPOptionsButton(e.getGui().width / 2 + 5 + 130, e.getGui().height / 6 + 48 - 6, n -> {
                IamMusicPlayer.proxy.getMinecraft().displayGuiScreen(new IMPOptionsScreen(e.getGui()));
            }));
        }
    }

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent e) {
        e.getPlayer().sendStatusMessage(IKSGStyles.withStyle(new TranslationTextComponent("message.login.impalpha"), IKSGStyles.withColor(TextFormatting.RED)), false);
    }

    private static class IMPOptionsButton extends Button {
        public IMPOptionsButton(int xIn, int yIn, IPressable onPressIn) {
            super(xIn, yIn, 20, 20, new TranslationTextComponent("narrator.button.impoptions"), onPressIn);
        }

        @Override
        public void renderButton(MatrixStack matrix, int mouseX, int mouseY, float parTic) {
            IKSGRenderUtil.guiBindAndBlit(IMPAbstractEquipmentScreen.EQUIPMENT_WIDGETS_TEXTURES, matrix, this.x, this.y, 0, 118 + (this.isHovered() ? 20 : 0), 20, 20, 256, 256);
            IKSGRenderUtil.guiBindAndBlit(IMP_OPTIONS_LOGO, matrix, this.x + 3, this.y + 3, 0, 0, 14, 14, 14, 14);
        }
    }
}

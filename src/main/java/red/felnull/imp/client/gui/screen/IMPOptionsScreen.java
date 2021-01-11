package red.felnull.imp.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TranslationTextComponent;
import red.felnull.otyacraftengine.client.gui.screen.IkisugiScreen;

public class IMPOptionsScreen extends IkisugiScreen {
    protected final Screen parentScreen;

    public IMPOptionsScreen(Screen previousScreen) {
        super(new TranslationTextComponent("impoptions.title"));
        this.parentScreen = previousScreen;
    }

    @Override
    public void initByIKSG() {
        super.initByIKSG();
        this.addWidgetByIKSG(new Button(this.width / 2 - 100, this.height / 6 + 168, 200, 20, DialogTexts.GUI_DONE, (p_213104_1_) -> {
            this.minecraft.displayGuiScreen(this.parentScreen);
        }));
    }

    @Override
    public void renderByIKSG(MatrixStack matrix, int mouseX, int mouseY, float parTick) {
        this.renderBackgroundByIKSG(matrix);
        drawCenteredString(matrix, this.font, this.title, this.width / 2, 15, 16777215);
        super.renderByIKSG(matrix, mouseX, mouseY, parTick);
    }

    @Override
    public void onClose() {
        System.out.println("Save");
    }

    @Override
    public void closeScreen() {
        this.minecraft.displayGuiScreen(this.parentScreen);
    }
}

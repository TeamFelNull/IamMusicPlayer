package red.felnull.imp.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import red.felnull.imp.client.config.ClientConfig;
import red.felnull.imp.client.gui.widget.MusicVolumeSlider;
import red.felnull.imp.client.music.ClientWorldMusicManager;
import red.felnull.imp.ffmpeg.FFmpegDownloader;
import red.felnull.imp.ffmpeg.FFmpegManeger;
import red.felnull.otyacraftengine.client.gui.IkisugiDialogTexts;
import red.felnull.otyacraftengine.client.gui.screen.IkisugiScreen;

public class IMPOptionsScreen extends IkisugiScreen {
    protected final Screen parentScreen;
    private MusicVolumeSlider musicVolumeSlider;

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

        IFormattableTextComponent iformattabletextcomponent = new TranslationTextComponent("imp.stereoEnabled").appendString(":");
        this.addWidgetByIKSG(new Button(this.width / 2 - 155 + 160, this.height / 6 - 12, 150, 20, iformattabletextcomponent.deepCopy().append(ClientWorldMusicManager.instance().isStereoEnabled() ? IkisugiDialogTexts.ON : IkisugiDialogTexts.OFF), (n) -> {
            ClientWorldMusicManager.instance().setStereoEnabled(!ClientWorldMusicManager.instance().isStereoEnabled());
            n.setMessage(iformattabletextcomponent.deepCopy().append(ClientWorldMusicManager.instance().isStereoEnabled() ? IkisugiDialogTexts.ON : IkisugiDialogTexts.OFF));
        }));

        this.musicVolumeSlider = this.addWidgetByIKSG(new MusicVolumeSlider(this.width / 2 - 155, this.height / 6 - 12, 150));

        this.addWidgetByIKSG(new Button(this.width / 2 - 155, this.height / 6 - 12 + 24, 150, 20, new TranslationTextComponent("imp.ffmpegTest"), (n) -> {
            FFmpegManeger.instance().startFFmpegEncodeTest(true);
        }));

        this.addWidgetByIKSG(new Button(this.width / 2 - 155 + 160, this.height / 6 - 12 + 24, 150, 20, new TranslationTextComponent("imp.ffmpegReLoading"), (n) -> {
            FFmpegManeger.instance().reload();
        }));
    }

    @Override
    public void renderByIKSG(MatrixStack matrix, int mouseX, int mouseY, float parTick) {
        this.renderBackgroundByIKSG(matrix);
        drawCenterStringByIKSG(matrix, this.title, this.width / 2, 15, 16777215);
        super.renderByIKSG(matrix, mouseX, mouseY, parTick);
    }

    @Override
    public void onClose() {
        ClientConfig.MusicVolume.set(ClientWorldMusicManager.instance().getMusicVolume());
        ClientConfig.StereoEnabled.set(ClientWorldMusicManager.instance().isStereoEnabled());
    }

    @Override
    public void closeScreen() {
        this.minecraft.displayGuiScreen(this.parentScreen);
    }
}

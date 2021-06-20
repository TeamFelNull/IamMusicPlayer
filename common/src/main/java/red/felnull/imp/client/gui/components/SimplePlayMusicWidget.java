package red.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.TextComponent;
import red.felnull.imp.client.gui.screen.monitor.MSDBaseMonitor;
import red.felnull.imp.client.music.MusicEngine;
import red.felnull.imp.music.info.MusicPlayInfo;
import red.felnull.imp.music.info.tracker.FixedMusicTracker;
import red.felnull.imp.music.resource.MusicSource;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

import java.util.UUID;

public class SimplePlayMusicWidget extends AbstractWidget implements IMSDSmartRender {
    private MusicSource playMusic;
    private boolean playing;
    private boolean lastPlaying;
    private boolean isHoveredPlayButton;

    public SimplePlayMusicWidget(int x, int y, MusicSource playMusic) {
        super(x, y, 95, 15, new TextComponent("Play Music"));
        this.playMusic = playMusic;
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {
        this.defaultButtonNarrationText(narrationElementOutput);
    }

    @Override
    public void render(PoseStack poseStack, int mx, int my, float parTick) {
        if (this.visible) {
            this.isHoveredPlayButton = mx >= this.x && my >= this.y && mx < this.x + 15 && my < this.y + this.height;
        }
        super.render(poseStack, mx, my, parTick);

        if (!lastPlaying && playing) {
            musicPlayStart();
        }

        lastPlaying = playing;
    }

    @Override
    public void renderButton(PoseStack poseStack, int mx, int my, float parTick) {

        int bk = this.getYImage(this.isHoveredPlayButton());
        drawSmartButtonBox(poseStack, this.x, this.y, 15, 15, bk);

        fillGray(poseStack, x + 15, y, 80, 15);

        IKSGRenderUtil.drawBindTextuer(MSDBaseMonitor.MSD_WIDGETS, poseStack, x + 3, y + 2, 45, 30 + (playing ? 11 : 0), 9, 11);

    }

    public void musicPlayStart() {
        if (playMusic == null)
            return;

        MusicEngine engine = MusicEngine.getInstance();
        UUID id = UUID.randomUUID();
        engine.ready(id, playMusic, 0);
        engine.play(id, 0, new MusicPlayInfo(new FixedMusicTracker(getMinecraft().player.position(), 1, 10)));
    }

    public void musicPlayStop() {

    }

    @Override
    public void onClick(double x, double y) {
        super.onClick(x, y);
        if (isHoveredPlayButton()) {
            playing = !playing;
        }
    }

    public MusicSource getPlayMusic() {
        return playMusic;
    }

    public boolean isHoveredPlayButton() {
        return this.isHovered() && this.isHoveredPlayButton;
    }

    public void setPlayMusic(MusicSource playMusic) {
        if (!this.playMusic.equals(playMusic))
            playing = false;
        this.playMusic = playMusic;
    }
}

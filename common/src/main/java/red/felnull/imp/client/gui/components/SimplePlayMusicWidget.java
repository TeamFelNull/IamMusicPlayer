package red.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.TextComponent;
import red.felnull.imp.client.gui.screen.monitor.MSDBaseMonitor;
import red.felnull.imp.client.music.MusicEngine;
import red.felnull.imp.music.info.MusicPlayInfo;
import red.felnull.imp.music.info.tracker.EntityMusicTracker;
import red.felnull.imp.music.resource.MusicSource;
import red.felnull.imp.util.StringUtils;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

import java.util.UUID;

public class SimplePlayMusicWidget extends AbstractWidget implements IMSDSmartRender {
    private final UUID uuid;
    private MusicSource playMusic;
    private boolean isHoveredPlayButton;
    private boolean isHoveredLine;

    public SimplePlayMusicWidget(int x, int y, MusicSource playMusic, UUID uuid) {
        super(x, y, 95, 15, new TextComponent("Play Music"));
        this.playMusic = playMusic;
        this.uuid = uuid;
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {
        this.defaultButtonNarrationText(narrationElementOutput);
    }

    @Override
    public void render(PoseStack poseStack, int mx, int my, float parTick) {
        if (this.visible) {
            this.isHoveredPlayButton = mx >= this.x && my >= this.y && mx < this.x + 15 && my < this.y + this.height;
            this.isHoveredLine = mx >= this.x + 16 && my >= this.y + 12 && mx < this.x + 94 && my < this.y + 14;
        }
        super.render(poseStack, mx, my, parTick);
    }

    public boolean isPlaying() {
        MusicEngine engine = MusicEngine.getInstance();
        return engine.isPlaying(uuid) && engine.getPlyingMusic(uuid) != null;
    }

    public boolean isExist() {
        MusicEngine engine = MusicEngine.getInstance();
        return engine.isExist(uuid);
    }

    @Override
    public void renderButton(PoseStack poseStack, int mx, int my, float parTick) {

        int bk = this.getYImage(this.isHoveredPlayButton());
        drawSmartButtonBox(poseStack, this.x, this.y, 15, 15, bk);

        fillGray(poseStack, x + 15, y, 80, 15);
        MusicEngine engine = MusicEngine.getInstance();

        IKSGRenderUtil.drawBindTextuer(MSDBaseMonitor.MSD_WIDGETS, poseStack, x + 3, y + 2, 45, 30 + (isPlaying() ? 11 : 0), 9, 11);

        long ct = 0;
        long at = playMusic != null ? playMusic.getDuration() : 0;

        if (isPlaying()) {
            MusicEngine.MusicPlayingEntry entry = engine.getPlyingMusic(uuid);
            ct = entry.musicPlayer.getPosition();
            at = entry.musicPlayer.getMusicSource().getDuration();
        }

        if (ct > at)
            ct = 0;

        drawPrettyString(poseStack, new TextComponent(StringUtils.getTimeNotationPercentage(ct, at)), x + 16, y + 2, 0);

        float plpar = (float) ct / at;
        fillMediumGray(poseStack, x + 16, y + 12, 78, 2);
        fillGreen(poseStack, x + 16, y + 12, 78 * plpar, 2);


        if (playMusic == null)
            musicPlayStop();
    }

    public void musicPlayStart() {
        musicPlayStart(0);
    }

    public void musicPlayStart(long pos) {
        if (playMusic == null)
            return;


        MusicEngine engine = MusicEngine.getInstance();
        engine.readyAndPlay(uuid, playMusic, pos, new MusicPlayInfo(new EntityMusicTracker(getMinecraft().player.position(), 1, 10, getMinecraft().player)), false);
    }

    public void musicPlayStop() {
        MusicEngine engine = MusicEngine.getInstance();
        engine.stop(uuid);
    }

    @Override
    public void onClick(double x, double y) {
        super.onClick(x, y);
        if (isHoveredPlayButton()) {
            if (!isExist()) {
                musicPlayStart();
            } else {
                musicPlayStop();
            }
        }
        if (isHoveredLine()) {
            float plpar = (float) ((x - this.x - 16d) / 78d);
            musicPlayStart((long) (getPlayMusic().getDuration() * plpar));
        }
    }

    public MusicSource getPlayMusic() {
        return playMusic;
    }

    public boolean isHoveredPlayButton() {
        return this.isHovered() && this.isHoveredPlayButton;
    }

    public boolean isHoveredLine() {
        return this.isHovered() && this.isHoveredLine;
    }

    public void setPlayMusic(MusicSource playMusic) {
        this.playMusic = playMusic;
    }
}

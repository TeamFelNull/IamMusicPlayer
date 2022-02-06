package dev.felnull.imp.client.gui.screen.monitor.cassette_deck;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.blockentity.CassetteDeckBlockEntity;
import dev.felnull.imp.client.gui.components.SmartButton;
import dev.felnull.imp.client.gui.components.WritePlayListFixedButtonsList;
import dev.felnull.imp.client.gui.screen.CassetteDeckScreen;
import dev.felnull.imp.client.gui.screen.monitor.music_manager.MusicManagerMonitor;
import dev.felnull.imp.util.IMPItemUtil;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;

public class WriteCDMonitor extends CassetteDeckMonitor {
    protected static final ResourceLocation WRITE_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/cassette_deck/monitor/write.png");
    private SmartButton backButton;
    private SmartButton writeButton;

    public WriteCDMonitor(CassetteDeckBlockEntity.MonitorType monitorType, CassetteDeckScreen screen) {
        super(monitorType, screen);
    }

    @Override
    public void init(int leftPos, int topPos) {
        super.init(leftPos, topPos);
        this.backButton = this.addRenderWidget(new SmartButton(getStartX() + 1, getStartY() + 44, 14, 11, new TranslatableComponent("imp.button.back"), n -> {
            insMonitor(CassetteDeckBlockEntity.MonitorType.MENU);
        }));
        this.backButton.setHideText(true);
        this.backButton.setIcon(MusicManagerMonitor.WIDGETS_TEXTURE, 10, 123, 8, 8);

        this.writeButton = this.addRenderWidget(new SmartButton(getStartX() + 164, getStartY() + 44, 35, 11, new TranslatableComponent("imp.button.writeStart"), n -> {

        }));
        this.writeButton.setHideText(true);
        this.writeButton.setIcon(MusicManagerMonitor.WIDGETS_TEXTURE, 10, 131, 20, 8);
        this.writeButton.active = canWriteStart();

        this.addRenderWidget(new WritePlayListFixedButtonsList(getStartX() + 1, getStartY() + 1, new ArrayList<>(), (fixedButtonsList, playList, i, i1) -> {

        }));

    }

    private boolean canWriteStart() {
        var tape = getScreen().getCassetteTape();
        return !tape.isEmpty() && IMPItemUtil.isCassetteTape(tape);
    }

    @Override
    public void tick() {
        super.tick();
        this.writeButton.active = canWriteStart();
    }

    @Override
    public void render(PoseStack poseStack, float f, int mouseX, int mouseY) {
        super.render(poseStack, f, mouseX, mouseY);
        OERenderUtil.drawTexture(WRITE_TEXTURE, poseStack, getStartX(), getStartY(), 0f, 0f, width, height, width, height);
    }
}

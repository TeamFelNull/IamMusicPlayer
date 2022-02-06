package dev.felnull.imp.client.gui.screen.monitor.cassette_deck;

import dev.felnull.imp.blockentity.CassetteDeckBlockEntity;
import dev.felnull.imp.client.gui.components.SmartButton;
import dev.felnull.imp.client.gui.screen.CassetteDeckScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class MenuCDMonitor extends CassetteDeckMonitor {
    private static final Component WRITE = new TranslatableComponent("imp.button.write");
    private static final Component PLAYBACK = new TranslatableComponent("imp.button.playback");

    public MenuCDMonitor(CassetteDeckBlockEntity.MonitorType monitorType, CassetteDeckScreen screen) {
        super(monitorType, screen);
    }

    @Override
    public void init(int leftPos, int topPos) {
        super.init(leftPos, topPos);
        this.addRenderWidget(new SmartButton(getStartX() + (width - 100) / 2, getStartY() + (height - 14 * 3) / 2, 100, 14, WRITE, n -> {
            insMonitor(CassetteDeckBlockEntity.MonitorType.WRITE);
        }));

        this.addRenderWidget(new SmartButton(getStartX() + (width - 100) / 2, getStartY() + (height - 14 * 3) / 2 + 14 * 2, 100, 14, PLAYBACK, n -> {
            insMonitor(CassetteDeckBlockEntity.MonitorType.PLAYBACK);
        }));
    }
}

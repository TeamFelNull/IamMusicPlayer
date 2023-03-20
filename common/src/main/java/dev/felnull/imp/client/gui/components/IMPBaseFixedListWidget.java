package dev.felnull.imp.client.gui.components;

import dev.felnull.imp.client.gui.IIMPSmartRender;
import dev.felnull.imp.client.gui.screen.monitor.music_manager.MusicManagerMonitor;
import dev.felnull.otyacraftengine.client.gui.TextureRegion;
import dev.felnull.otyacraftengine.client.gui.components.FixedListWidget;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

public abstract class IMPBaseFixedListWidget<E> extends FixedListWidget<E> implements IIMPSmartRender {
    private static final TextureRegion IMP_BASE_TEXTURE = TextureRegion.relative(MusicManagerMonitor.WIDGETS_TEXTURE, 0, 20, 18, 42);

    public IMPBaseFixedListWidget(int x, int y, int width, int height, @NotNull Component message, int entryShowCount, @NotNull List<E> entryList, @NotNull Function<E, Component> entryName, @Nullable PressEntry<E> onPressEntry, boolean selectable, @Nullable FixedListWidget<E> old) {
        super(x, y, width, height, message, entryShowCount, entryList, entryName, onPressEntry, selectable, IMP_BASE_TEXTURE, old);
    }

    protected int getYImage(boolean hoverd) {
        int i = 1;
        if (!this.active) {
            i = 0;
        } else if (hoverd) {
            i = 2;
        }

        return 46 + i * 20;
    }
}

package dev.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.client.gui.IIMPSmartRender;
import dev.felnull.imp.client.gui.screen.monitor.music_manager.MusicManagerMonitor;
import dev.felnull.imp.music.resource.IIMPComparable;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.Comparator;
import java.util.List;

public abstract class SortButton extends Button implements IIMPSmartRender {
    protected final boolean longed;

    private SortButton(int x, int y, int w, int tx, Component component, OnPress onPress, boolean longed, Screen screen) {
        super(x, y, w, 9, component, onPress, longed ? NO_TOOLTIP : (button, poseStack, px, py) -> screen.renderTooltip(poseStack, getText(button), px, py));
        // super(x, y, w, 9, tx, 30, 9, MusicManagerMonitor.WIDGETS_TEXTURE, 256, 256, onPress, longed ? NO_TOOLTIP : (button, poseStack, px, py) -> screen.renderTooltip(poseStack, getText(button), px, py), Component.translatable("imp.button.sort"));
        this.longed = longed;
    }

    private static Component getText(Button button) {
        if (button instanceof SortTypeButton sortButton) {
            return Component.translatable("imp.sortType." + sortButton.getSortType().getName());
        } else if (button instanceof OrderTypeButton orderButton) {
            return Component.translatable("imp.orderType." + orderButton.getOrderType().getName());
        }
        return Component.literal("none");
    }

    @Override
    public void onPress() {
        cycle();
        super.onPress();
    }

    @Override
    public void renderButton(PoseStack poseStack, int mx, int my, float f) {
        drawSmartButtonBox(poseStack, x, y, width, height, isHoveredOrFocused());
        if (this.isHoveredOrFocused())
            this.renderToolTip(poseStack, mx, my);
    }

    abstract public void cycle();

    public static enum SortType {
        NAME("name", (o1, o2) -> {
            return o1.getCompareName().compareTo(o2.getCompareName());
        }), PLAYER("player", (o1, o2) -> {
            return o1.getComparePlayerName().compareTo(o2.getComparePlayerName());
        }), CREATE_DATE("create_date", (o1, o2) -> (int) (o1.getCompareDate() - o2.getCompareDate()));
        private final String name;
        private final Comparator<IIMPComparable> comparator;
        private final Comparator<IIMPComparable> rcomparator;

        private SortType(String name, Comparator<IIMPComparable> comparator) {
            this.name = name;
            this.comparator = comparator;
            this.rcomparator = comparator.reversed();
        }

        public String getName() {
            return name;
        }

        public Comparator<IIMPComparable> getComparator(OrderType orderType) {
            return orderType.isReverse() ? rcomparator : comparator;
        }

    }

    public static enum OrderType {
        ASCENDING("ascending", false), DESCENDING("descending", true);
        private final String name;
        private final boolean reverse;

        private OrderType(String name, boolean reverse) {
            this.name = name;
            this.reverse = reverse;
        }

        public boolean isReverse() {
            return reverse;
        }

        public String getName() {
            return name;
        }
    }

    public static class SortTypeButton extends SortButton {
        private SortType type = SortType.NAME;

        public SortTypeButton(int x, int y, OnPress onPress, boolean longed, Screen screen) {
            super(x, y, longed ? 97 : 9, longed ? 61 : 52, Component.translatable("imp.button.sort"), onPress, longed, screen);
        }

        @Override
        public void cycle() {
            type = SortType.values()[(type.ordinal() + 1) % SortType.values().length];
        }

        public SortType getSortType() {
            return type;
        }

        public <T extends IIMPComparable> List<T> sort(List<T> target, OrderTypeButton orderTypeButton) {
            return target.stream().sorted(getSortType().getComparator(orderTypeButton.getOrderType())).toList();
        }

        @Override
        public void renderButton(PoseStack poseStack, int mx, int my, float f) {
            super.renderButton(poseStack, mx, my, f);
            OERenderUtils.drawTexture(MusicManagerMonitor.WIDGETS_TEXTURE, poseStack, x + 1, y + 1, 73 + getSortType().ordinal() * 7, 0, 7, 7);
            if (longed) {
                drawSmartText(poseStack, SortButton.getText(this), x + 9, y + 1);
            }
        }
    }

    public static class OrderTypeButton extends SortButton {
        private OrderType type = OrderType.DESCENDING;

        public OrderTypeButton(int x, int y, OnPress onPress, boolean longed, Screen screen) {
            super(x, y, longed ? 88 : 9, longed ? 158 : 52, Component.translatable("imp.button.order"), onPress, longed, screen);
        }

        @Override
        public void cycle() {
            type = OrderType.values()[(type.ordinal() + 1) % OrderType.values().length];
        }

        public OrderType getOrderType() {
            return type;
        }

        @Override
        public void renderButton(PoseStack poseStack, int mx, int my, float f) {
            super.renderButton(poseStack, mx, my, f);
            OERenderUtils.drawTexture(MusicManagerMonitor.WIDGETS_TEXTURE, poseStack, x + 1, y + 1, 73 + getOrderType().ordinal() * 7, 7, 7, 7);
            if (longed) {
                drawSmartText(poseStack, SortButton.getText(this), x + 9, y + 1);
            }
        }
    }
}

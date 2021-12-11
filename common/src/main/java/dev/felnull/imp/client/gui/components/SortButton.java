package dev.felnull.imp.client.gui.components;

import dev.felnull.imp.client.gui.screen.monitor.MusicManagerMonitor;
import dev.felnull.imp.music.resource.IIMPComparable;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.Comparator;

public class SortButton extends ImageButton {
    private SortButton(int x, int y, OnPress onPress, boolean longed, Screen screen) {
        super(x, y, 9, 9, 52, 30, 9, MusicManagerMonitor.WIDGETS_TEXTURE, 256, 256, onPress, longed ? NO_TOOLTIP : (button, poseStack, px, py) -> screen.renderTooltip(poseStack, getText((SortButton) button), px, py), new TranslatableComponent("imp.button.sort"));
    }

    private static Component getText(Button button) {
        if (button instanceof SortTypeButton sortButton) {
            return new TranslatableComponent("imp.sortType." + sortButton.getSortType().getName());
        } else if (button instanceof OrderTypeButton orderButton) {
            return new TranslatableComponent("imp.orderType." + orderButton.getOrderType().getName());
        }
        return new TextComponent("none");
    }


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
            super(x, y, onPress, longed, screen);
        }

        public SortType getSortType() {
            return type;
        }
    }

    public static class OrderTypeButton extends SortButton {
        private OrderType type = OrderType.DESCENDING;

        public OrderTypeButton(int x, int y, OnPress onPress, boolean longed, Screen screen) {
            super(x, y, onPress, longed, screen);
        }

        public OrderType getOrderType() {
            return type;
        }
    }
}

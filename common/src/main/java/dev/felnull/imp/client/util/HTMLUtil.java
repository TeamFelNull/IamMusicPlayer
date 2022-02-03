package dev.felnull.imp.client.util;

import dev.felnull.fnjl.util.FNStringUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

public class HTMLUtil {
    public static Component toComponent(String text) {
        text = FNStringUtil.decodeHTMLSpecialCharacter(text);
        var doc = Jsoup.parse(text);
        var body = doc.body();
        var els = body.getAllElements();
        var tx = FNStringUtil.decodeHTMLSpecialCharacter(els.get(els.size() - 1).html());

        if (tx.isEmpty())
            return null;

        long jbkc = tx.chars().filter(n -> n == '　').count();
        if (jbkc == tx.length())
            return null;

        long bkc = tx.chars().filter(n -> n == ' ').count();
        if (bkc == tx.length())
            return null;

        var comp = new TextComponent(tx);
        for (org.jsoup.nodes.Element el : els) {
            var en = el.tagName();
            try {
                comp = toDecoComponent(comp, en, body.select(en));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return comp;
    }

    private static TextComponent toDecoComponent(TextComponent component, String name, Elements element) {
        if ("font".equals(name)) {
            var at = element.attr("color");
            if (at.isEmpty())
                return component;
            var cf = ChatFormatting.getByName(at);
            if (cf != null)
                return (TextComponent) component.withStyle(cf);
            if (!at.startsWith("#"))
                return component;
            int col = Integer.parseInt(at.substring(1), 16);
            return (TextComponent) component.withStyle(Style.EMPTY.withColor(col));
        }
        if ("b".equals(name))
            return (TextComponent) component.withStyle(Style.EMPTY.withBold(true));
        if ("i".equals(name))
            return (TextComponent) component.withStyle(Style.EMPTY.withItalic(true));
        if ("u".equals(name))
            return (TextComponent) component.withStyle(Style.EMPTY.withUnderlined(true));
        if ("s".equals(name) || "strike".equals(name))
            return (TextComponent) component.withStyle(Style.EMPTY.withStrikethrough(true));
        return component;
    }
}

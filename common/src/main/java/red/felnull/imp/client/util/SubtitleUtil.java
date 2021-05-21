package red.felnull.imp.client.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubtitleUtil {
    private static final Pattern codesPattern = Pattern.compile("<(.*?)>");
    private static final Pattern colorCordPattern = Pattern.compile("\"#(.*?)\"");

    public static List<Component> getHTMLSubtitleComponents(String text) {
        List<Component> comps = new ArrayList<>();

        List<String> codes = getCodes(text);

        Map<String, String> cstrs = new HashMap<>();

        codes.stream().filter(n -> n.charAt(0) != '/').forEach(n -> {
            String fs = "/" + n.split(" ")[0];
            Pattern cdPattern = Pattern.compile("<+" + n + "+>(.*?)<+" + fs + "+>");
            Matcher cm = cdPattern.matcher(text);
            if (cm.find())
                cstrs.put(n, cm.group(1));
        });

        List<Map.Entry<String, Set<String>>> outs = new ArrayList<>();
        List<String> nnts = new ArrayList<>();

        cstrs.entrySet().stream().filter(n -> getFirstCodes(n.getValue()).isEmpty()).forEach(n -> {
            nnts.add(n.getKey());
            Set<String> ll = new HashSet<>();
            ll.add(n.getKey());
            outs.add(new AbstractMap.SimpleEntry<>(n.getValue(), ll));
        });

        cstrs.entrySet().stream().filter(n -> !nnts.contains(n.getKey())).forEach(n -> {
            List<String> cods = getFirstCodes(n.getValue());
            int num = -1;
            for (Map.Entry<String, Set<String>> out : outs) {
                for (String cod : cods) {
                    if (out.getValue().contains(cod)) {
                        break;
                    }
                }
                num++;
            }
            outs.get(num).getValue().add(n.getKey());
            outs.get(num).getValue().addAll(cods);
        });

        outs.forEach(n -> {
            TextComponent tc = new TextComponent(convertDisplayableSubtitle(n.getKey()));
            for (String s : n.getValue()) {
                Style style = getStyleByCode(s);
                if (style != null)
                    tc.withStyle(style);
            }
            comps.add(tc);
        });

        return comps;
    }

    public static String convertDisplayableSubtitle(String text) {
        text = text.replace("\u200B", "");
        text = text.replace("\u3000", " ");
        text = text.replace("&lt;", "<");
        text = text.replace("&gt;", ">");
        text = text.replace("&amp;", "&");
        text = text.replace("&quot;", "\"");
        text = text.replace("&#39;", "'");
        text = text.replace("&nbsp;", " ");
        return text;
    }

    private static Style getStyleByCode(String code) {

        Style style = Style.EMPTY;

        String[] codes = code.split(" ");
        String firstCode = codes[0];

        if (firstCode.equals("font")) {
            for (int i = 1; i < codes.length; i++) {
                String[] fonts = codes[1].split("=");
                if (fonts[0].equals("color")) {
                    Matcher matcher = colorCordPattern.matcher(fonts[1]);
                    if (matcher.find()) {
                        int c = Integer.parseInt(matcher.group(1), 16);
                        style.withColor(c);
                    }
                }
            }
        }
        return style;
    }

    private static List<String> getCodes(String text) {
        Matcher matcher = codesPattern.matcher(text);
        List<String> codes = new ArrayList<>();
        while (matcher.find()) {
            String ms = matcher.group(1);
            if (!ms.isEmpty())
                codes.add(ms);
        }
        return codes;
    }

    public static List<String> getFirstCodes(String text) {
        List<String> codes = getCodes(text);
        List<String> ocodes = new ArrayList<>();
        codes.stream().filter(n -> n.charAt(0) != '/').forEach(n -> {
            String fs = "/" + n.split(" ")[0];
            Pattern cdPattern = Pattern.compile("<+" + n + "+>(.*?)<+" + fs + "+>");
            Matcher cm = cdPattern.matcher(text);
            if (cm.find())
                ocodes.add(n);
        });
        return ocodes;
    }
}

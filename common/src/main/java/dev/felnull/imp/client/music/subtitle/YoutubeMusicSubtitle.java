package dev.felnull.imp.client.music.subtitle;

import com.github.kiulian.downloader.downloader.request.RequestSubtitlesDownload;
import com.github.kiulian.downloader.model.Extension;
import com.github.kiulian.downloader.model.subtitles.SubtitlesInfo;
import dev.felnull.fnjl.util.FNStringUtil;
import dev.felnull.fnjl.util.FNURLUtil;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.client.util.HTMLUtil;
import dev.felnull.imp.client.util.YoutubeDownloaderUtil;
import dev.felnull.imp.music.resource.MusicSource;
import dev.felnull.otyacraftengine.client.util.OEClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class YoutubeMusicSubtitle implements IMusicSubtitle {
    private final List<SubtitlesInfo> subtitlesInfos;
    private final List<SubtitleEntry> subs = new ArrayList<>();
    private final List<Component> currentSubs = new ArrayList<>();

    public YoutubeMusicSubtitle(MusicSource source) {
        this.subtitlesInfos = YoutubeDownloaderUtil.getSubtitle(source.getIdentifier());
    }

    @Override
    public boolean isExist() {
        return !subtitlesInfos.isEmpty();
    }

    @Override
    public void load() throws Exception {
        subs.addAll(loadSubtitle(getSubtitleURL()));
        subs.sort((o1, o2) -> Math.toIntExact(o1.st() - o2.st()));
    }

    private URL getSubtitleURL() throws MalformedURLException {
        String lng = IamMusicPlayer.CONFIG.youtubeSubtitleLanguage.getCode();
        if (lng.isEmpty())
            lng = OEClientUtil.getGoogleCodeByLanguage(Minecraft.getInstance().getLanguageManager().getSelected());
        String urlst = null;
        for (SubtitlesInfo subtitlesInfo : subtitlesInfos) {
            if (subtitlesInfo.getLanguage().equals(lng)) {
                urlst = subtitlesInfo.getUrl();
                break;
            }
        }
        if (urlst == null) {
            Optional<SubtitlesInfo> sub = subtitlesInfos.stream().filter(n -> n.getLanguage().equals(YoutubeSubtitleEnum.ENGLISH.getCode())).findFirst();
            SubtitlesInfo si = sub.orElseGet(() -> subtitlesInfos.get(0));
            RequestSubtitlesDownload request = new RequestSubtitlesDownload(si).formatTo(Extension.TRANSCRIPT_V1).translateTo(lng);
            urlst = request.getDownloadUrl();
        }
        return new URL(urlst);
    }

    private List<SubtitleEntry> loadSubtitle(URL url) throws ParserConfigurationException, IOException, SAXException {
        List<SubtitleEntry> sb = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document subDocument = builder.parse(FNURLUtil.getStream(url));
        NodeList subListNL = subDocument.getChildNodes();

        for (int i = 0; i < subListNL.getLength(); i++) {
            Node node = subListNL.item(i);
            if ("transcript".equals(node.getNodeName())) {
                NodeList nodeList2 = node.getChildNodes();
                for (int k = 0; k < nodeList2.getLength(); k++) {
                    Node node2 = nodeList2.item(k);
                    try {
                        NamedNodeMap att = node2.getAttributes();
                        float st = Float.parseFloat(att.getNamedItem("start").getNodeValue());
                        float dr = Float.parseFloat(att.getNamedItem("dur").getNodeValue());
                        String text = node2.getTextContent();
                        for (Component component : toComponent(text)) {
                            if (component != null)
                                sb.add(new SubtitleEntry((long) (st * 1000), (long) (dr * 1000), component));
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        return sb;
    }


    private static List<Component> toComponent(String text) {
        List<Component> ls = new ArrayList<>();
        text = FNStringUtil.decodeHTMLSpecialCharacter(text);
        String[] txts = text.split("\n");
        for (String txt : txts) {
            if (!txt.isEmpty())
                ls.add(HTMLUtil.toComponent(txt));
        }
        return ls;
    }


    @Override
    public List<Component> getSubtitle(long last, long current) {
        currentSubs.clear();
        boolean nu = false;
        for (SubtitleEntry sub : subs) {
            long st = sub.st();
            long ft = sub.st() + sub.dr();
            if ((st <= last && ft >= last) && (st <= current && ft >= current)) {
                currentSubs.add(sub.text());
                nu = true;
                continue;
            }
            if (nu)
                break;
        }
        return currentSubs;
    }

    private static record SubtitleEntry(long st, long dr, Component text) {
    }
}

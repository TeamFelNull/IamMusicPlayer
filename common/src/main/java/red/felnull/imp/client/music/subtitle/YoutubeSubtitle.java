package red.felnull.imp.client.music.subtitle;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import red.felnull.imp.client.music.player.IMusicPlayer;
import red.felnull.otyacraftengine.util.IKSGURLUtil;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class YoutubeSubtitle implements IMusicSubtitle {
    private final String videoID;
    private final List<SubtitleManager.MusicSubtitleEntry> subtitleEntries = new ArrayList<>();
    private final IMusicPlayer musicPlayer;
    private boolean loaded;

    public YoutubeSubtitle(IMusicPlayer musicPlayer, String videoID) {
        this.videoID = videoID;
        this.musicPlayer = musicPlayer;
    }

    @Override
    public void init() throws Exception {
        subtitleEntries.clear();
        SubtitleManager manager = SubtitleManager.getInstance();
        URL langList = new URL("https://video.google.com/timedtext?hl=en&type=list&v=" + videoID);

        InputStream langListStream = IKSGURLUtil.getURLStream(langList);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document langListDocument = builder.parse(langListStream);

        List<String> langCodes = new ArrayList<>();
        NodeList langListNL = langListDocument.getChildNodes();
        for (int i = 0; i < langListNL.getLength(); i++) {
            Node node = langListNL.item(i);
            if ("transcript_list".equals(node.getNodeName())) {
                NodeList nodeList2 = node.getChildNodes();
                for (int k = 0; k < nodeList2.getLength(); k++) {
                    Node node2 = nodeList2.item(k);
                    langCodes.add(node2.getAttributes().getNamedItem("lang_code").getNodeValue());
                }
            }
        }

        Optional<String> langCode = manager.getDefaultSubtitle(langCodes);

        if (!langCode.isPresent())
            return;

        URL subUrl = new URL("https://www.youtube.com/api/timedtext?lang=" + langCode.get() + "&v=" + videoID);
        InputStream subStream = IKSGURLUtil.getURLStream(subUrl);
        Document subDocument = builder.parse(subStream);

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
                        text = text.replace("\u3000", " ");
                        SubtitleManager.MusicSubtitleEntry entry = new SubtitleManager.MusicSubtitleEntry((long) (st * 1000), (long) (dr * 1000), text);
                        subtitleEntries.add(entry);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        loaded = true;
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }

    @Override
    public IMusicPlayer getMusicPlayer() {
        return musicPlayer;
    }

    @Override
    public List<SubtitleManager.MusicSubtitleEntry> getSubtitles() {
        return subtitleEntries;
    }
}

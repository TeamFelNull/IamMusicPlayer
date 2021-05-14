package red.felnull.imp.client.music.subtitle;

public class YoutubeSubtitle implements IMusicSubtitle {
    private final String videoID;

    public YoutubeSubtitle(String videoID) {
        this.videoID = videoID;
    }

    @Override
    public void init() {
        String langListUrl = "https://video.google.com/timedtext?hl=en&type=list&v=" + videoID;

        String subUrl = "https://www.youtube.com/api/timedtext?lang=" + "ja" + "&v=" + videoID;
    }
}

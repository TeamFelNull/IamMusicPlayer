package red.felnull.imp.ffmpeg;

import ws.schild.jave.process.ProcessLocator;

public class IMPFFMPEGLocator implements ProcessLocator {
    private final String path;

    public IMPFFMPEGLocator(String path) {
        this.path = path;
    }

    @Override
    public String getExecutablePath() {
        return path;
    }
}

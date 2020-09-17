package red.felnull.imp.client.data;

import red.felnull.imp.musicplayer.PlayImage;

public class MusicUploadData {
    private UploadState state;
    private float progress;
    private PlayImage image;

    public MusicUploadData(PlayImage image) {
        this.state = UploadState.PREPARATION;
        this.progress = 0f;
        this.image = image;
    }

    public float getProgress() {
        return progress;
    }

    public UploadState getState() {
        return state;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public void setState(UploadState state) {
        this.state = state;
    }

    public PlayImage getImage() {
        return image;
    }

    public enum UploadState {
        PREPARATION(false),
        CONVERTING(true),
        COMPRESSING(false),
        SENDING(true),
        UNZIPPING(false),
        COMPLETION(false),
        ERROR(false);

        private boolean progressble;

        private UploadState(boolean progressble) {
            this.progressble = progressble;
        }

        public boolean isProgressble() {
            return progressble;
        }
    }
}

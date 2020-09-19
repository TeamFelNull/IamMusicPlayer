package red.felnull.imp.client.data;

import net.minecraft.util.text.TranslationTextComponent;
import red.felnull.imp.musicplayer.PlayImage;

public class MusicUploadData {
    private final String name;
    private UploadState state;
    private float progress;
    private PlayImage image;
    private byte[] imageData;

    public MusicUploadData(String name, PlayImage image, byte[] imageData) {
        this.name = name;
        this.state = UploadState.PREPARATION;
        this.progress = 0f;
        this.image = image;
        this.imageData = imageData;
    }

    public float getProgress() {
        return progress;
    }

    public UploadState getState() {
        return state;
    }

    public String getName() {
        return name;
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

    public byte[] getImageData() {
        return imageData;
    }

    public enum UploadState {
        PREPARATION(false, new TranslationTextComponent("uploadstate.preparation")),
        CONVERTING(true, new TranslationTextComponent("uploadstate.converting")),
        COMPRESSING(false, new TranslationTextComponent("uploadstate.compressing")),
        SENDING(true, new TranslationTextComponent("uploadstate.sending")),
        UNZIPPING(false, new TranslationTextComponent("uploadstate.unzipping")),
        COMPLETION(false, new TranslationTextComponent("uploadstate.completion")),
        ERROR(false, new TranslationTextComponent("uploadstate.error"));

        private boolean progressble;
        private TranslationTextComponent localized;

        private UploadState(boolean progressble, TranslationTextComponent localized) {
            this.localized = localized;
            this.progressble = progressble;
        }

        public TranslationTextComponent getLocalized() {
            return localized;
        }

        public boolean isProgressble() {
            return progressble;
        }
    }
}

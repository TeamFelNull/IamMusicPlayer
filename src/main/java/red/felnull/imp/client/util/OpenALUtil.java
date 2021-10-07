package red.felnull.imp.client.util;


import static org.lwjgl.openal.AL10.*;

public class OpenALUtil {
    public static void checkErrorThrower() {
        switch (alGetError()) {
            case AL_NO_ERROR:
                break;
            case AL_INVALID_NAME:
                throw new IllegalStateException("Invalid name parameter.");
            case AL_INVALID_ENUM:
                throw new IllegalStateException("Invalid enumerated parameter value.");
            case AL_INVALID_VALUE:
                throw new IllegalStateException("Invalid parameter parameter value.");
            case AL_INVALID_OPERATION:
                throw new IllegalStateException("Invalid operation.");
            case AL_OUT_OF_MEMORY:
                throw new IllegalStateException("Unable to allocate memory.");
        }
    }

    public static int getOpenALFormat(int channel, int bit) {
        if (channel == 1) {
            if (bit == 8) {
                return AL_FORMAT_MONO8;
            }
            if (bit == 16) {
                return AL_FORMAT_MONO16;
            }
        } else if (channel == 2) {
            if (bit == 8) {
                return AL_FORMAT_STEREO8;
            }

            if (bit == 16) {
                return AL_FORMAT_STEREO16;
            }
        }
        return AL_FORMAT_MONO16;
    }
}
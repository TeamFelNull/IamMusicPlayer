package net.morimori.imp.sound;

public class SoundThread extends Thread {

    public static boolean check;

    public SoundThread() {

    }

    @Override
    public void run() {

        while (true) {

            check = true;

            ClientSoundPlayer.INSTANS.tick();
            try {
                sleep(50);
            } catch (InterruptedException e) {
            }

        }
    }
}

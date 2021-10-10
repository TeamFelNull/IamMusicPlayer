package red.felnull.imp.handler;

/*
public class WorldMusicServerHandler {
    public static int sendByteLength = 32 * 1024;
    public static final Map<String, byte[]> WORLDMUSIC_DATA = new HashMap<>();

    @SubscribeEvent
    public static void onClientResponse(ResponseEvent.Client e) {
        if (e.getLocation().equals(IMPWorldData.WORLDMUSICFILEDATA)) {
            if (e.getId() == 0) {
                try {
                    File musicfile = PathUtils.getWorldMusic(e.getMessage()).toFile();
                    if (musicfile.exists()) {
                        WorldMusicFileDataInfo wmfd = new WorldMusicFileDataInfo(MusicUtils.getMillisecondDuration(musicfile), musicfile.length(), MusicUtils.getMP3MillisecondPerFrame(musicfile));
                        ResponseSender.sendToClient(e.getPlayer(), IMPWorldData.WORLDMUSICFILEDATA, 0, e.getMessage(), wmfd.write(new CompoundNBT()));
                    } else {
                        ResponseSender.sendToClient(e.getPlayer(), IMPWorldData.WORLDMUSICFILEDATA, 1, e.getMessage(), new CompoundNBT());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    ResponseSender.sendToClient(e.getPlayer(), IMPWorldData.WORLDMUSICFILEDATA, 1, e.getMessage(), new CompoundNBT());
                }
            } else if (e.getId() == 1) {
                ByteSendThread bst = new ByteSendThread(e.getPlayer(), e.getMessage(), e.getData().getInt("begin"), e.getData().getString("byteuuid"));
                bst.start();
            }
        }
    }

    public static class ByteSendThread extends Thread {
        private final ServerPlayerEntity playerEntity;
        private final String uuid;
        private final String beginUUID;
        private final int begin;

        public ByteSendThread(ServerPlayerEntity player, String uuid, int begin, String beginUUID) {
            this.playerEntity = player;
            this.uuid = uuid;
            this.begin = begin;
            this.beginUUID = beginUUID;
        }

        @Override
        public void run() {
            try {
                byte[] data = null;
                if (WORLDMUSIC_DATA.containsKey(uuid)) {
                    data = WORLDMUSIC_DATA.get(uuid);
                } else {
                    File musicfile = PathUtils.getWorldMusic(uuid).toFile();
                    if (musicfile.exists()) {
                        data = Files.readAllBytes(musicfile.toPath());
                        WORLDMUSIC_DATA.put(uuid, data);
                    }
                }
                if (data == null)
                    throw new FileNotFoundException();


                int nocori = data.length - begin;

                byte[] sendData = new byte[Math.min(sendByteLength, nocori)];
                System.arraycopy(data, begin, sendData, 0, sendData.length);
                PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> playerEntity), new WorldMusicSendByteMessage(beginUUID, sendData));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
*/
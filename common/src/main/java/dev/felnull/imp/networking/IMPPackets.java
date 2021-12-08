package dev.felnull.imp.networking;

import dev.architectury.networking.NetworkManager;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.client.handler.ClientMessageHandler;
import dev.felnull.imp.handler.ServerMessageHandler;
import dev.felnull.imp.music.resource.Music;
import dev.felnull.imp.music.resource.MusicPlayList;
import dev.felnull.imp.util.IMPNbtUtil;
import dev.felnull.otyacraftengine.networking.PacketMessage;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IMPPackets {
    public static final ResourceLocation MUSIC_SYNC = new ResourceLocation(IamMusicPlayer.MODID, "music_sync");

    public static void init() {
        NetworkManager.registerReceiver(NetworkManager.c2s(), MUSIC_SYNC, (friendlyByteBuf, packetContext) -> ServerMessageHandler.onMusicSyncRequestMessage(new MusicSyncRequestMessage(friendlyByteBuf), packetContext));
    }

    public static void clientInit() {
        NetworkManager.registerReceiver(NetworkManager.s2c(), MUSIC_SYNC, (friendlyByteBuf, packetContext) -> ClientMessageHandler.onMusicSyncResponseMessage(new MusicSyncResponseMessage(friendlyByteBuf), packetContext));
    }

    public static class MusicSyncResponseMessage implements PacketMessage {
        public final MusicSyncType syncType;
        public final UUID syncId;
        public final List<MusicPlayList> playLists;
        public final List<Music> musics;

        public MusicSyncResponseMessage(FriendlyByteBuf bf) {
            this.syncType = MusicSyncType.getById(bf.readInt());
            this.syncId = bf.readUUID();
            this.playLists = new ArrayList<>();
            IMPNbtUtil.readMusicPlayLists(bf.readNbt(), "PlayLists", playLists);
            this.musics = new ArrayList<>();
            IMPNbtUtil.writeMusics(bf.readNbt(), "Musics", musics);
        }

        public MusicSyncResponseMessage(MusicSyncType syncType, UUID syncId, List<MusicPlayList> playLists, List<Music> musics) {
            this.syncType = syncType;
            this.syncId = syncId;
            this.playLists = playLists;
            this.musics = musics;
        }

        @Override
        public FriendlyByteBuf toFBB() {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeInt(syncType.getId());
            buf.writeUUID(syncId);
            buf.writeNbt(IMPNbtUtil.writeMusicPlayLists(new CompoundTag(), "PlayLists", playLists));
            buf.writeNbt(IMPNbtUtil.writeMusics(new CompoundTag(), "Musics", musics));
            return buf;
        }
    }

    public static class MusicSyncRequestMessage implements PacketMessage {
        public final MusicSyncType syncType;
        public final UUID syncId;

        public MusicSyncRequestMessage(FriendlyByteBuf bf) {
            this(MusicSyncType.getById(bf.readInt()), bf.readUUID());
        }

        public MusicSyncRequestMessage(MusicSyncType syncType, UUID syncId) {
            this.syncType = syncType;
            this.syncId = syncId;
        }

        @Override
        public FriendlyByteBuf toFBB() {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeInt(syncType.getId());
            buf.writeUUID(syncId);
            return buf;
        }
    }

    public static enum MusicSyncType {
        NONE(0),
        PLAYLIST_CAN_JOIN(1),
        PLAYLIST_MY_LIST(2),
        MUSIC_BY_PLAYLIST(3),
        UPDATE(4);
        private final int id;

        private MusicSyncType(int id) {
            this.id = id;
        }

        private int getId() {
            return id;
        }

        private static MusicSyncType getById(int id) {
            for (MusicSyncType type : values()) {
                if (type.getId() == id)
                    return type;
            }
            return NONE;
        }
    }
}

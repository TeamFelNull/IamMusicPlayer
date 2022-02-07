package dev.felnull.imp.networking;

import dev.architectury.networking.NetworkManager;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.client.handler.ClientMessageHandler;
import dev.felnull.imp.handler.ServerMessageHandler;
import dev.felnull.imp.music.MusicPlaybackInfo;
import dev.felnull.imp.music.resource.ImageInfo;
import dev.felnull.imp.music.resource.Music;
import dev.felnull.imp.music.resource.MusicPlayList;
import dev.felnull.imp.music.resource.MusicSource;
import dev.felnull.imp.util.IMPNbtUtil;
import dev.felnull.otyacraftengine.networking.BlockEntityExistence;
import dev.felnull.otyacraftengine.networking.PacketMessage;
import dev.felnull.otyacraftengine.util.OENbtUtil;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IMPPackets {
    public static final ResourceLocation MUSIC_SYNC = new ResourceLocation(IamMusicPlayer.MODID, "music_sync");
    public static final ResourceLocation MUSIC_PLAYLIST_ADD = new ResourceLocation(IamMusicPlayer.MODID, "music_playlist_add");
    public static final ResourceLocation MUSIC_ADD = new ResourceLocation(IamMusicPlayer.MODID, "music_add");
    public static final ResourceLocation MUSIC_READY = new ResourceLocation(IamMusicPlayer.MODID, "music_ring");
    public static final ResourceLocation MUSIC_READY_RESULT = new ResourceLocation(IamMusicPlayer.MODID, "music_ready_result");

    public static void init() {
        NetworkManager.registerReceiver(NetworkManager.c2s(), MUSIC_SYNC, (friendlyByteBuf, packetContext) -> ServerMessageHandler.onMusicSyncRequestMessage(new MusicSyncRequestMessage(friendlyByteBuf), packetContext));
        NetworkManager.registerReceiver(NetworkManager.c2s(), MUSIC_PLAYLIST_ADD, (friendlyByteBuf, packetContext) -> ServerMessageHandler.onMusicPlayListAddMessage(new MusicPlayListAddMessage(friendlyByteBuf), packetContext));
        NetworkManager.registerReceiver(NetworkManager.c2s(), MUSIC_ADD, (friendlyByteBuf, packetContext) -> ServerMessageHandler.onMusicAddMessage(new MusicAddMessage(friendlyByteBuf), packetContext));
        NetworkManager.registerReceiver(NetworkManager.c2s(), MUSIC_READY_RESULT, (friendlyByteBuf, packetContext) -> ServerMessageHandler.onMusicReadyResultMessage(new MusicReadyResultMessage(friendlyByteBuf), packetContext));
    }

    public static void clientInit() {
        NetworkManager.registerReceiver(NetworkManager.s2c(), MUSIC_SYNC, (friendlyByteBuf, packetContext) -> ClientMessageHandler.onMusicSyncResponseMessage(new MusicSyncResponseMessage(friendlyByteBuf), packetContext));
        NetworkManager.registerReceiver(NetworkManager.s2c(), MUSIC_READY, (friendlyByteBuf, packetContext) -> ClientMessageHandler.onMusicReadyResponseMessage(new MusicReadyMessage(friendlyByteBuf), packetContext));
    }

    public static class MusicReadyResultMessage implements PacketMessage {
        public final UUID waitID;
        public final UUID uuid;
        public final boolean result;
        public final boolean retry;

        public MusicReadyResultMessage(FriendlyByteBuf bf) {
            this.waitID = bf.readUUID();
            this.uuid = bf.readUUID();
            this.result = bf.readBoolean();
            this.retry = bf.readBoolean();
        }

        public MusicReadyResultMessage(UUID waitID, UUID uuid, boolean result, boolean retry) {
            this.waitID = waitID;
            this.uuid = uuid;
            this.result = result;
            this.retry = retry;
        }

        @Override
        public FriendlyByteBuf toFBB() {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeUUID(waitID);
            buf.writeUUID(uuid);
            buf.writeBoolean(result);
            buf.writeBoolean(retry);
            return buf;
        }
    }

    public static class MusicReadyMessage implements PacketMessage {
        public final UUID waitId;
        public final UUID uuid;
        public final MusicSource source;
        public final MusicPlaybackInfo playbackInfo;
        public final long position;

        public MusicReadyMessage(FriendlyByteBuf bf) {
            this.waitId = bf.readUUID();
            this.uuid = bf.readUUID();
            this.source = OENbtUtil.readSerializable(bf.readNbt(), "ms", new MusicSource());
            this.playbackInfo = OENbtUtil.readSerializable(bf.readNbt(), "pbi", new MusicPlaybackInfo());
            this.position = bf.readLong();
        }

        public MusicReadyMessage(UUID waitId, UUID uuid, MusicSource source, MusicPlaybackInfo playbackInfo, long position) {
            this.waitId = waitId;
            this.uuid = uuid;
            this.source = source;
            this.playbackInfo = playbackInfo;
            this.position = position;
        }

        @Override
        public FriendlyByteBuf toFBB() {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeUUID(this.waitId);
            buf.writeUUID(this.uuid);
            buf.writeNbt(OENbtUtil.writeSerializable(new CompoundTag(), "ms", this.source));
            buf.writeNbt(OENbtUtil.writeSerializable(new CompoundTag(), "pbi", this.playbackInfo));
            buf.writeLong(this.position);
            return buf;
        }
    }

    public static class MusicAddMessage implements PacketMessage {
        public final UUID playlist;
        public final String name;
        public final String author;
        public final ImageInfo image;
        public final MusicSource source;

        public MusicAddMessage(FriendlyByteBuf bf) {
            this.playlist = bf.readUUID();
            this.name = bf.readUtf();
            this.author = bf.readUtf();
            this.image = OENbtUtil.readSerializable(bf.readNbt(), "Image", new ImageInfo());
            this.source = OENbtUtil.readSerializable(bf.readNbt(), "Source", new MusicSource());
        }

        public MusicAddMessage(UUID playlist, String name, String author, ImageInfo image, MusicSource source) {
            this.playlist = playlist;
            this.name = name;
            this.author = author;
            this.image = image;
            this.source = source;
        }

        @Override
        public FriendlyByteBuf toFBB() {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeUUID(playlist);
            buf.writeUtf(this.name);
            buf.writeUtf(this.author);
            buf.writeNbt(OENbtUtil.writeSerializable(new CompoundTag(), "Image", image));
            buf.writeNbt(OENbtUtil.writeSerializable(new CompoundTag(), "Source", source));
            return buf;
        }
    }

    public static class MusicPlayListAddMessage implements PacketMessage {
        public final String name;
        public final ImageInfo image;
        public final boolean publiced;
        public final boolean initMember;
        public final List<UUID> invitePlayers;
        public final BlockEntityExistence blockEntityExistence;

        public MusicPlayListAddMessage(FriendlyByteBuf bf) {
            this.name = bf.readUtf();
            this.image = OENbtUtil.readSerializable(bf.readNbt(), "Image", new ImageInfo());
            this.publiced = bf.readBoolean();
            this.initMember = bf.readBoolean();
            this.invitePlayers = new ArrayList<>();
            OENbtUtil.readUUIDList(bf.readNbt(), "InvitePlayers", invitePlayers);
            this.blockEntityExistence = BlockEntityExistence.readFBB(bf);
        }

        public MusicPlayListAddMessage(String name, ImageInfo image, boolean publiced, boolean initMember, List<UUID> invitePlayers, BlockEntityExistence blockEntityExistence) {
            this.name = name;
            this.image = image;
            this.publiced = publiced;
            this.initMember = initMember;
            this.invitePlayers = invitePlayers;
            this.blockEntityExistence = blockEntityExistence;
        }

        @Override
        public FriendlyByteBuf toFBB() {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeUtf(this.name);
            buf.writeNbt(OENbtUtil.writeSerializable(new CompoundTag(), "Image", image));
            buf.writeBoolean(publiced);
            buf.writeBoolean(initMember);
            buf.writeNbt(OENbtUtil.writeUUIDList(new CompoundTag(), "InvitePlayers", invitePlayers));
            blockEntityExistence.writeFBB(buf);
            return buf;
        }
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
            IMPNbtUtil.readMusics(bf.readNbt(), "Musics", musics);
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
            buf.writeInt(syncType.ordinal());
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
            buf.writeInt(syncType.ordinal());
            buf.writeUUID(syncId);
            return buf;
        }
    }

    public static enum MusicSyncType {
        NONE,
        PLAYLIST_CAN_JOIN,
        PLAYLIST_MY_LIST,
        MUSIC_BY_PLAYLIST,
        UPDATE;

        private static MusicSyncType getById(int id) {
            if (values().length > id)
                return values()[id];
            return NONE;
        }
    }
}

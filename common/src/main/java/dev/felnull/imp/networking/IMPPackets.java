package dev.felnull.imp.networking;

import dev.architectury.networking.NetworkManager;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.client.handler.ClientMessageHandler;
import dev.felnull.imp.music.MusicPlaybackInfo;
import dev.felnull.imp.music.resource.*;
import dev.felnull.imp.server.handler.ServerMessageHandler;
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
    public static final ResourceLocation MUSIC_PLAYLIST_EDIT = new ResourceLocation(IamMusicPlayer.MODID, "music_playlist_edit");
    public static final ResourceLocation MUSIC_PLAYLIST_CHANGE_AUTHORITY = new ResourceLocation(IamMusicPlayer.MODID, "music_playlist_change_authority");
    public static final ResourceLocation MUSIC_ADD = new ResourceLocation(IamMusicPlayer.MODID, "music_add");
    public static final ResourceLocation MUSIC_EDIT = new ResourceLocation(IamMusicPlayer.MODID, "music_edit");
    public static final ResourceLocation MUSIC_OR_PLAYLIST_DELETE = new ResourceLocation(IamMusicPlayer.MODID, "music_or_playlist_delete");
    public static final ResourceLocation MUSIC_RING_READY = new ResourceLocation(IamMusicPlayer.MODID, "music_ring_ready");
    public static final ResourceLocation MUSIC_RING_READY_RESULT = new ResourceLocation(IamMusicPlayer.MODID, "music_ring_ready_result");
    public static final ResourceLocation MUSIC_RING_STATE = new ResourceLocation(IamMusicPlayer.MODID, "music_ring_state");
    public static final ResourceLocation MUSIC_RING_UPDATE_RESULT = new ResourceLocation(IamMusicPlayer.MODID, "music_ring_update_result");
    public static final ResourceLocation MULTIPLE_MUSIC_ADD = new ResourceLocation(IamMusicPlayer.MODID, "multiple_music_add");

    public static void init() {
        NetworkManager.registerReceiver(NetworkManager.c2s(), MUSIC_SYNC, (friendlyByteBuf, packetContext) -> ServerMessageHandler.onMusicSyncRequestMessage(new MusicSyncRequestMessage(friendlyByteBuf), packetContext));
        NetworkManager.registerReceiver(NetworkManager.c2s(), MUSIC_PLAYLIST_ADD, (friendlyByteBuf, packetContext) -> ServerMessageHandler.onMusicPlayListAddMessage(new MusicPlayListMessage(friendlyByteBuf), packetContext));
        NetworkManager.registerReceiver(NetworkManager.c2s(), MUSIC_PLAYLIST_EDIT, (friendlyByteBuf, packetContext) -> ServerMessageHandler.onMusicPlayListEditMessage(new MusicPlayListMessage(friendlyByteBuf), packetContext));
        NetworkManager.registerReceiver(NetworkManager.c2s(), MUSIC_PLAYLIST_CHANGE_AUTHORITY, (friendlyByteBuf, packetContext) -> ServerMessageHandler.onMusicPlayListChangeAuthority(new MusicPlayListChangeAuthority(friendlyByteBuf), packetContext));
        NetworkManager.registerReceiver(NetworkManager.c2s(), MUSIC_ADD, (friendlyByteBuf, packetContext) -> ServerMessageHandler.onMusicAddMessage(new MusicMessage(friendlyByteBuf), packetContext));
        NetworkManager.registerReceiver(NetworkManager.c2s(), MUSIC_EDIT, (friendlyByteBuf, packetContext) -> ServerMessageHandler.onMusicEditMessage(new MusicMessage(friendlyByteBuf), packetContext));
        NetworkManager.registerReceiver(NetworkManager.c2s(), MUSIC_RING_READY_RESULT, (friendlyByteBuf, packetContext) -> ServerMessageHandler.onMusicReadyResultMessage(new MusicRingReadyResultMessage(friendlyByteBuf), packetContext));
        NetworkManager.registerReceiver(NetworkManager.c2s(), MUSIC_RING_UPDATE_RESULT, (friendlyByteBuf, packetContext) -> ServerMessageHandler.onMusicUpdateResultMessage(new MusicRingUpdateResultMessage(friendlyByteBuf), packetContext));
        NetworkManager.registerReceiver(NetworkManager.c2s(), MUSIC_OR_PLAYLIST_DELETE, (friendlyByteBuf, packetContext) -> ServerMessageHandler.onMusicOrPlayListDeleteMessage(new MusicOrPlayListDeleteMessage(friendlyByteBuf), packetContext));
        NetworkManager.registerReceiver(NetworkManager.c2s(), MULTIPLE_MUSIC_ADD, (friendlyByteBuf, packetContext) -> ServerMessageHandler.onMultipleMusicAdd(new MultipleMusicAddMessage(friendlyByteBuf), packetContext));
    }

    public static void clientInit() {
        NetworkManager.registerReceiver(NetworkManager.s2c(), MUSIC_SYNC, (friendlyByteBuf, packetContext) -> ClientMessageHandler.onMusicSyncResponseMessage(new MusicSyncResponseMessage(friendlyByteBuf), packetContext));
        NetworkManager.registerReceiver(NetworkManager.s2c(), MUSIC_RING_READY, (friendlyByteBuf, packetContext) -> ClientMessageHandler.onMusicRingReadyResponseMessage(new MusicReadyMessage(friendlyByteBuf), packetContext));
        NetworkManager.registerReceiver(NetworkManager.s2c(), MUSIC_RING_STATE, (friendlyByteBuf, packetContext) -> ClientMessageHandler.onMusicRingStateResponseMessage(new MusicRingStateMessage(friendlyByteBuf), packetContext));
    }

    public static class MusicPlayListChangeAuthority implements PacketMessage {
        public final UUID playlist;
        public final UUID player;
        public final AuthorityInfo.AuthorityType authorityType;
        public final BlockEntityExistence blockEntityExistence;

        public MusicPlayListChangeAuthority(FriendlyByteBuf bf) {
            this.playlist = bf.readUUID();
            this.player = bf.readUUID();
            this.authorityType = AuthorityInfo.AuthorityType.getByName(bf.readUtf());
            this.blockEntityExistence = BlockEntityExistence.readFBB(bf);
        }

        public MusicPlayListChangeAuthority(UUID playlist, UUID player, AuthorityInfo.AuthorityType authorityType, BlockEntityExistence blockEntityExistence) {
            this.playlist = playlist;
            this.player = player;
            this.authorityType = authorityType;
            this.blockEntityExistence = blockEntityExistence;
        }

        @Override
        public FriendlyByteBuf toFBB() {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeUUID(playlist);
            buf.writeUUID(player);
            buf.writeUtf(authorityType.getName());
            this.blockEntityExistence.writeFBB(buf);
            return buf;
        }
    }

    public static class MultipleMusicAddMessage implements PacketMessage {
        public final UUID playlist;
        public final List<Music> musics;
        public final BlockEntityExistence blockEntityExistence;

        public MultipleMusicAddMessage(FriendlyByteBuf bf) {
            this.playlist = bf.readUUID();
            this.musics = new ArrayList<>();
            IMPNbtUtil.readMusics(bf.readNbt(), "Musics", musics);
            this.blockEntityExistence = BlockEntityExistence.readFBB(bf);
        }

        public MultipleMusicAddMessage(UUID playlist, List<Music> musics, BlockEntityExistence blockEntityExistence) {
            this.playlist = playlist;
            this.musics = musics;
            this.blockEntityExistence = blockEntityExistence;
        }

        @Override
        public FriendlyByteBuf toFBB() {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeUUID(this.playlist);
            buf.writeNbt(IMPNbtUtil.writeMusics(new CompoundTag(), "Musics", musics));
            this.blockEntityExistence.writeFBB(buf);
            return buf;
        }
    }

    public static class MusicOrPlayListDeleteMessage implements PacketMessage {
        public final UUID playListID;
        public final UUID musicID;
        public final BlockEntityExistence blockEntityExistence;
        public final boolean music;

        public MusicOrPlayListDeleteMessage(FriendlyByteBuf bf) {
            this.playListID = bf.readUUID();
            this.musicID = bf.readUUID();
            this.blockEntityExistence = BlockEntityExistence.readFBB(bf);
            this.music = bf.readBoolean();
        }

        public MusicOrPlayListDeleteMessage(UUID playListID, UUID musicID, BlockEntityExistence blockEntityExistence, boolean music) {
            this.playListID = playListID;
            this.musicID = musicID;
            this.blockEntityExistence = blockEntityExistence;
            this.music = music;
        }

        @Override
        public FriendlyByteBuf toFBB() {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeUUID(this.playListID);
            buf.writeUUID(this.musicID);
            this.blockEntityExistence.writeFBB(buf);
            buf.writeBoolean(this.music);
            return buf;
        }
    }


    public static class MusicRingUpdateResultMessage implements PacketMessage {
        public final UUID uuid;
        public final UUID waitId;
        public final int state;

        public MusicRingUpdateResultMessage(FriendlyByteBuf bf) {
            this.uuid = bf.readUUID();
            this.waitId = bf.readUUID();
            this.state = bf.readInt();

        }

        public MusicRingUpdateResultMessage(UUID uuid, UUID waitId, int state) {
            this.uuid = uuid;
            this.waitId = waitId;
            this.state = state;
        }

        @Override
        public FriendlyByteBuf toFBB() {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeUUID(this.uuid);
            buf.writeUUID(this.waitId);
            buf.writeInt(this.state);
            return buf;
        }
    }

    public static class MusicRingStateMessage implements PacketMessage {
        public final UUID uuid;
        public final UUID waitId;
        public final int num;
        public final long elapsed;
        public final MusicPlaybackInfo playbackInfo;

        public MusicRingStateMessage(FriendlyByteBuf bf) {
            this.uuid = bf.readUUID();
            this.waitId = bf.readUUID();
            this.num = bf.readInt();
            this.elapsed = bf.readLong();
            this.playbackInfo = OENbtUtil.readSerializable(bf.readNbt(), "pbi", new MusicPlaybackInfo());
        }

        public MusicRingStateMessage(UUID uuid, UUID waitId, int num) {
            this(uuid, waitId, num, 0, MusicPlaybackInfo.EMPTY);
        }

        public MusicRingStateMessage(UUID uuid, UUID waitId, int num, long elapsed, MusicPlaybackInfo playbackInfo) {
            this.uuid = uuid;
            this.waitId = waitId;
            this.num = num;
            this.elapsed = elapsed;
            this.playbackInfo = playbackInfo;
        }

        @Override
        public FriendlyByteBuf toFBB() {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeUUID(uuid);
            buf.writeUUID(waitId);
            buf.writeInt(num);
            buf.writeLong(elapsed);
            buf.writeNbt(OENbtUtil.writeSerializable(new CompoundTag(), "pbi", this.playbackInfo));
            return buf;
        }
    }

    public static class MusicRingReadyResultMessage implements PacketMessage {
        public final UUID waitID;
        public final UUID uuid;
        public final boolean result;
        public final boolean retry;
        public final long elapsed;

        public MusicRingReadyResultMessage(FriendlyByteBuf bf) {
            this.waitID = bf.readUUID();
            this.uuid = bf.readUUID();
            this.result = bf.readBoolean();
            this.retry = bf.readBoolean();
            this.elapsed = bf.readLong();
        }

        public MusicRingReadyResultMessage(UUID waitID, UUID uuid, boolean result, boolean retry, long elapsed) {
            this.waitID = waitID;
            this.uuid = uuid;
            this.result = result;
            this.retry = retry;
            this.elapsed = elapsed;
        }

        @Override
        public FriendlyByteBuf toFBB() {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeUUID(waitID);
            buf.writeUUID(uuid);
            buf.writeBoolean(result);
            buf.writeBoolean(retry);
            buf.writeLong(elapsed);
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

    public static class MusicMessage implements PacketMessage {
        public final UUID uuid;
        public final UUID playlist;
        public final String name;
        public final String author;
        public final ImageInfo image;
        public final MusicSource source;
        public final BlockEntityExistence blockEntityExistence;

        public MusicMessage(FriendlyByteBuf bf) {
            this.uuid = bf.readUUID();
            this.playlist = bf.readUUID();
            this.name = bf.readUtf();
            this.author = bf.readUtf();
            this.image = OENbtUtil.readSerializable(bf.readNbt(), "Image", new ImageInfo());
            this.source = OENbtUtil.readSerializable(bf.readNbt(), "Source", new MusicSource());
            this.blockEntityExistence = BlockEntityExistence.readFBB(bf);
        }

        public MusicMessage(UUID playlist, String name, String author, ImageInfo image, MusicSource source, BlockEntityExistence blockEntityExistence) {
            this(UUID.randomUUID(), playlist, name, author, image, source, blockEntityExistence);
        }

        public MusicMessage(UUID uuid, UUID playlist, String name, String author, ImageInfo image, MusicSource source, BlockEntityExistence blockEntityExistence) {
            this.uuid = uuid;
            this.playlist = playlist;
            this.name = name;
            this.author = author;
            this.image = image;
            this.source = source;
            this.blockEntityExistence = blockEntityExistence;
        }

        @Override
        public FriendlyByteBuf toFBB() {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeUUID(this.uuid);
            buf.writeUUID(this.playlist);
            buf.writeUtf(this.name);
            buf.writeUtf(this.author);
            buf.writeNbt(OENbtUtil.writeSerializable(new CompoundTag(), "Image", image));
            buf.writeNbt(OENbtUtil.writeSerializable(new CompoundTag(), "Source", source));
            blockEntityExistence.writeFBB(buf);
            return buf;
        }
    }

    public static class MusicPlayListMessage implements PacketMessage {
        public final UUID uuid;
        public final String name;
        public final ImageInfo image;
        public final boolean publiced;
        public final boolean initMember;
        public final List<UUID> invitePlayers;
        public final BlockEntityExistence blockEntityExistence;
        public final List<Music> importMusics;

        public MusicPlayListMessage(FriendlyByteBuf bf) {
            this.uuid = bf.readUUID();
            this.name = bf.readUtf();
            this.image = OENbtUtil.readSerializable(bf.readNbt(), "Image", new ImageInfo());
            this.publiced = bf.readBoolean();
            this.initMember = bf.readBoolean();
            this.invitePlayers = new ArrayList<>();
            OENbtUtil.readUUIDList(bf.readNbt(), "InvitePlayers", invitePlayers);
            this.blockEntityExistence = BlockEntityExistence.readFBB(bf);
            this.importMusics = new ArrayList<>();
            IMPNbtUtil.readMusics(bf.readNbt(), "ImportMusics", importMusics);
        }

        public MusicPlayListMessage(String name, ImageInfo image, boolean publiced, boolean initMember, List<UUID> invitePlayers, BlockEntityExistence blockEntityExistence, List<Music> importMusics) {
            this(UUID.randomUUID(), name, image, publiced, initMember, invitePlayers, blockEntityExistence, importMusics);
        }

        public MusicPlayListMessage(UUID uuid, String name, ImageInfo image, boolean publiced, boolean initMember, List<UUID> invitePlayers, BlockEntityExistence blockEntityExistence, List<Music> importMusics) {
            this.uuid = uuid;
            this.name = name;
            this.image = image;
            this.publiced = publiced;
            this.initMember = initMember;
            this.invitePlayers = invitePlayers;
            this.blockEntityExistence = blockEntityExistence;
            this.importMusics = importMusics;
        }

        @Override
        public FriendlyByteBuf toFBB() {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeUUID(this.uuid);
            buf.writeUtf(this.name);
            buf.writeNbt(OENbtUtil.writeSerializable(new CompoundTag(), "Image", image));
            buf.writeBoolean(publiced);
            buf.writeBoolean(initMember);
            buf.writeNbt(OENbtUtil.writeUUIDList(new CompoundTag(), "InvitePlayers", invitePlayers));
            blockEntityExistence.writeFBB(buf);
            buf.writeNbt(IMPNbtUtil.writeMusics(new CompoundTag(), "ImportMusics", importMusics));
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

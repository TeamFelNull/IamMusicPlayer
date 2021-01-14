package red.felnull.imp.music;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.PacketDistributor;
import red.felnull.imp.data.IMPWorldData;
import red.felnull.imp.music.resource.PlayMusic;
import red.felnull.imp.packet.MusicRingMessage;
import red.felnull.imp.packet.PacketHandler;
import red.felnull.otyacraftengine.api.ResponseSender;
import red.felnull.otyacraftengine.util.IKSGPlayerUtil;
import red.felnull.otyacraftengine.util.IKSGServerUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class WorldMusicRinger {
    private final List<UUID> playingPlayers = new ArrayList<>();
    private final List<UUID> loadingPlayers = new ArrayList<>();
    private final List<UUID> loadWaitingPlayers = new ArrayList<>();
    private final List<UUID> waitingMiddlePlayers = new ArrayList<>();
    private final List<UUID> regularConfirmationPlayers = new ArrayList<>();
    private final UUID uuid;
    private final ResourceLocation dimension;
    private final PlayMusic playMusic;
    private final IWorldRingWhether whether;
    private long lastUpdateTime;
    private long ringStartTime;
    private long ringStartElapsedTime;
    private boolean playWaitingPrev;
    private boolean playWaiting;
    private long waitTime;
    private boolean playing;
    private boolean ringin;

    public WorldMusicRinger(UUID uuid, ResourceLocation dimension, PlayMusic playMusic, IWorldRingWhether whether) {
        this.uuid = uuid;
        this.dimension = dimension;
        this.playMusic = playMusic;
        this.whether = whether;
        this.lastUpdateTime = System.currentTimeMillis();
    }

    public void play() {
        if (whether.canMusicPlay())
            whether.musicPlayed();

        ringStartElapsedTime = getCurrentMusicPlayPosition();

        if (IKSGServerUtil.getOnlinePlayers().stream().anyMatch(this::canListen)) {
            playWaitingPrev = true;
            playWaiting = true;
        } else {
            playing = true;
            ringin = true;
            ringStartTime = System.currentTimeMillis();
        }
    }

    public void stop() {
        pause();
        whether.musicStoped();
    }

    public long getCurrentMusicPlayPosition() {
        return whether.getCurrentMusicPlayPosition();
    }

    public void pause() {
        playing = false;
        ringin = false;
        playingPlayers.stream().filter(n -> IKSGServerUtil.isOnlinePlayer(n.toString())).forEach(n -> {
            ResponseSender.sendToClient(n.toString(), IKSGServerUtil.getMinecraftServer(), IMPWorldData.WORLD_RINGD, 1, uuid.toString(), new CompoundNBT());
        });
        playingPlayers.clear();
        loadingPlayers.clear();
        loadWaitingPlayers.clear();
        waitingMiddlePlayers.clear();
        regularConfirmationPlayers.clear();
    }

    public void playerPause(UUID plyaerUUID) {
        ResponseSender.sendToClient(plyaerUUID.toString(), IKSGServerUtil.getMinecraftServer(), IMPWorldData.WORLD_RINGD, 1, uuid.toString(), new CompoundNBT());
        playingPlayers.remove(plyaerUUID);
        loadingPlayers.remove(plyaerUUID);
        loadWaitingPlayers.remove(plyaerUUID);
        waitingMiddlePlayers.remove(plyaerUUID);
        regularConfirmationPlayers.remove(plyaerUUID);
    }

    public boolean isRelatedPlayer(UUID playerUUID) {
        boolean flag1 = playingPlayers.contains(playerUUID);
        boolean flag2 = loadingPlayers.contains(playerUUID);
        boolean flag3 = loadWaitingPlayers.contains(playerUUID);
        boolean flag4 = waitingMiddlePlayers.contains(playerUUID);
        boolean flag5 = regularConfirmationPlayers.contains(playerUUID);
        return flag1 || flag2 || flag3 || flag4 || flag5;
    }

    public boolean tick() {

        if (playMusic == null) {
            stop();
        }

        if (!whether.canMusicPlay() || playMusic == null)
            return true;

        if (playWaiting)
            waitTime += System.currentTimeMillis() - lastUpdateTime;
        else
            waitTime = 0;

        Stream<ServerPlayerEntity> listenPlayers = IKSGServerUtil.getOnlinePlayers().stream().filter(this::canListen);

        if (playWaiting && playWaitingPrev) {
            listenPlayers.filter(n -> !(loadingPlayers.contains(UUID.fromString(IKSGPlayerUtil.getUUID(n))) || loadWaitingPlayers.contains(UUID.fromString(IKSGPlayerUtil.getUUID(n))) || playingPlayers.contains(UUID.fromString(IKSGPlayerUtil.getUUID(n))) || waitingMiddlePlayers.contains(UUID.fromString(IKSGPlayerUtil.getUUID(n))))).forEach(n -> {
                loadingPlayers.add(UUID.fromString(IKSGPlayerUtil.getUUID(n)));
                PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> n), new MusicRingMessage(uuid, getMusicPos(), getPlayMusic(), getCurrentMusicPlayPosition()));
                playWaitingPrev = false;
            });
        } else if (playWaiting && loadingPlayers.isEmpty()) {
            loadWaitingPlayers.stream().filter(n -> canListen(IKSGPlayerUtil.getPlayerByUUID(n.toString()))).forEach(n -> {
                ResponseSender.sendToClient(n.toString(), IKSGServerUtil.getMinecraftServer(), IMPWorldData.WORLD_RINGD, 0, uuid.toString(), new CompoundNBT());
            });
            playingPlayers.addAll(loadWaitingPlayers);
            playWaiting = false;
            ringin = true;
            ringStartTime = System.currentTimeMillis();
        }

        if (ringin) {
            long cur = ringStartElapsedTime + System.currentTimeMillis() - ringStartTime;
            if (cur >= playMusic.getLengthInMilliseconds()) {
                stop();
                whether.setCurrentMusicPlayPosition(0);
            } else {
                whether.setCurrentMusicPlayPosition(cur);
            }

            IKSGServerUtil.getOnlinePlayers().stream().filter(this::canListen).forEach(n -> {
                if (!playingPlayers.contains(UUID.fromString(IKSGPlayerUtil.getUUID(n))) && !waitingMiddlePlayers.contains(UUID.fromString(IKSGPlayerUtil.getUUID(n)))) {
                    PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> n), new MusicRingMessage(uuid, getMusicPos(), getPlayMusic(), getCurrentMusicPlayPosition()));
                    waitingMiddlePlayers.add(UUID.fromString(IKSGPlayerUtil.getUUID(n)));
                }
            });

            IKSGServerUtil.getOnlinePlayers().stream().filter(n -> isRelatedPlayer(UUID.fromString(IKSGPlayerUtil.getUUID(n)))).filter(n -> !canListen(n)).forEach(n -> playerPause(UUID.fromString(IKSGPlayerUtil.getUUID(n))));

        }

        lastUpdateTime = System.currentTimeMillis();

        return false;
    }

    public boolean isPlayWaiting() {
        return playWaiting;
    }

    public PlayMusic getPlayMusic() {
        return playMusic;
    }

    public Vector3d getMusicPos() {
        return whether.getMusicPos();
    }

    public float getMusicVolume() {
        return whether.getMusicVolume();
    }

    public ResourceLocation getDimension() {
        return dimension;
    }

    public float getListenRange() {

        return 30f * getMusicVolume();
    }

    private boolean canListen(ServerPlayerEntity player) {
        return player.world.getDimensionKey().getLocation().equals(getDimension()) && Math.sqrt(player.getDistanceSq(getMusicPos())) <= getListenRange();
    }

    public void musicLoadingFinish(UUID playerUUID) {
        if (loadingPlayers.contains(playerUUID)) {
            loadingPlayers.remove(playerUUID);
            loadWaitingPlayers.add(playerUUID);
        } else {
            if (canListen(IKSGPlayerUtil.getPlayerByUUID(playerUUID.toString()))) {
                ResponseSender.sendToClient(playerUUID.toString(), IKSGServerUtil.getMinecraftServer(), IMPWorldData.WORLD_RINGD, 2, uuid.toString(), new CompoundNBT());
                playingPlayers.add(playerUUID);
            }
        }
    }

    public void musicLoadingNotFinishRegularConfirmation(UUID playerUUID) {
        if (loadingPlayers.contains(playerUUID)) {
            loadingPlayers.remove(playerUUID);
        }
        regularConfirmationPlayers.add(playerUUID);
    }
}

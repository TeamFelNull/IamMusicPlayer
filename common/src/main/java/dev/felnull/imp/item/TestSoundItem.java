package dev.felnull.imp.item;

import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import dev.felnull.imp.client.music.MusicEngine;
import dev.felnull.imp.client.util.LavaPlayerUtil;
import dev.felnull.imp.music.MusicPlayManager;
import dev.felnull.imp.music.MusicPlaybackInfo;
import dev.felnull.imp.music.resource.MusicSource;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class TestSoundItem extends Item {

    public TestSoundItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);

        if (level.isClientSide()) {
            UUID id = UUID.randomUUID();
            MusicEngine me = MusicEngine.getInstance();
            var apm = LavaPlayerUtil.createAudioPlayerManager();
            apm.registerSourceManager(new YoutubeAudioSourceManager());
            var idi = itemStack.getHoverName().getString();
            var track = LavaPlayerUtil.loadTrackNonThrow(apm, idi);
            var plb = new MusicPlaybackInfo(MusicPlayManager.FIXED_TRACKER, MusicPlayManager.createFixedTracker(player.position()), 1, 30);
            var ms = new MusicSource("youtube", idi, track.get().getDuration());
            me.loadAddMusicPlayer(id, plb, ms, 0, (result, time, player1, retry) -> {
                player.displayClientMessage(new TextComponent(result + ":" + time + ":" + retry), false);
                me.playMusicPlayer(id, 0);
            });
        }

        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide);
    }
}

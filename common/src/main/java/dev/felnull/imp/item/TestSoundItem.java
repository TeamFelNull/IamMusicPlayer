package dev.felnull.imp.item;

import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import dev.felnull.imp.client.music.MusicEngine;
import dev.felnull.imp.client.util.LavaPlayerUtil;
import dev.felnull.imp.music.MusicPlayManager;
import dev.felnull.imp.music.MusicPlaybackInfo;
import dev.felnull.imp.music.resource.MusicSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class TestSoundItem extends Item {
    private static final UUID id = UUID.randomUUID();

    public TestSoundItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);

        if (level.isClientSide()) {
            MusicEngine me = MusicEngine.getInstance();
            List<Creeper> list = level.getEntitiesOfClass(Creeper.class, player.getBoundingBox().inflate(2.0D), Objects::nonNull);
            if (!list.isEmpty()) {
                var c = list.get(0);
                System.out.println(c);

                if (!player.isCrouching()) {
                    var apm = LavaPlayerUtil.createAudioPlayerManager();
                    apm.registerSourceManager(new YoutubeAudioSourceManager());
                    var idi = itemStack.getHoverName().getString();
                    var track = LavaPlayerUtil.loadTrackNonThrow(apm, idi);
                    var tag = new CompoundTag();
                    var p = c.position();
                    tag.putDouble("x", p.x);
                    tag.putDouble("y", p.y);
                    tag.putDouble("z", p.z);
                    tag.putInt("id", c.getId());
                    var plb = new MusicPlaybackInfo(MusicPlayManager.ENTITY_TRACKER, tag, 1, 30);
                    var ms = new MusicSource("youtube", idi, track.get().getDuration());
                    me.loadAddMusicPlayer(id, plb, ms, 0, (result, time, player1, retry) -> {
                        System.out.println(result + ":" + time + ":" + retry);
                    });
                }
            } else {
                me.playMusicPlayer(id, 0);
            }
/*
            var apm = LavaPlayerUtil.createAudioPlayerManager();
            apm.registerSourceManager(new YoutubeAudioSourceManager());
            long st = System.currentTimeMillis();

            var track = LavaPlayerUtil.loadTrack(apm, "_KFAhsZjfn8");
            track.ifPresent(n -> {
                player.displayClientMessage(new TextComponent(n.getInfo().title + ":" + (System.currentTimeMillis() - st) + "ms"), false);
            });
*/
        }

        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide);
    }
}

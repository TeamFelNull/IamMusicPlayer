package dev.felnull.imp.item;

import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import dev.felnull.imp.client.util.LavaPlayerUtil;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TestSoundItem extends Item {
    public TestSoundItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);

        if (level.isClientSide()) {
            //player.displayClientMessage(new TextComponent(LavaPlayerUtil.loadTrack()), false);
            /*  UUID id = UUID.randomUUID();
            MusicEngine me = MusicEngine.getInstance();
            if (!player.isCrouching()) {
                me.addMusicPlayer(id, new MusicPlaybackInfo(MusicPlayManager.FIXED_TRACKER, new CompoundTag()), null);
            } else {
                me.removeAllMusicPlayer();
            }*/

            var apm = LavaPlayerUtil.createAudioPlayerManager();
            apm.registerSourceManager(new YoutubeAudioSourceManager());

            var track = LavaPlayerUtil.loadTrack(apm, "6uAjJ3m2ZIk");

            track.ifPresent(n -> {
                player.displayClientMessage(new TextComponent(n.getInfo().title), false);
            });
        }

        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide);
    }
}

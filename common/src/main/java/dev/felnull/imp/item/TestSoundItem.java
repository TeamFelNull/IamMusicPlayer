package dev.felnull.imp.item;

import com.google.gson.Gson;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.otyacraftengine.util.OEVoxelShapeUtil;
import dev.felnull.otyacraftengine.vsg.TentativeVoxelShapeGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestSoundItem extends Item {

    public TestSoundItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);

        if (level.isClientSide()) {
          /*  UUID id = UUID.randomUUID();
            MusicEngine me = MusicEngine.getInstance();
            var apm = LavaPlayerUtil.createAudioPlayerManager();
            apm.registerSourceManager(new YoutubeAudioSourceManager());
            apm.registerSourceManager(new HttpAudioSourceManager());
            var idi = itemStack.getHoverName().getString();
            var track = LavaPlayerUtil.loadTrackNonThrow(apm, idi);
            var plb = new MusicPlaybackInfo(MusicPlayManager.FIXED_TRACKER, MusicPlayManager.createFixedTracker(player.position()), 1, 30);
            if (track.isPresent()) {
                var ms = new MusicSource("", idi, track.get().getInfo().isStream ? -1 : track.get().getDuration());
                me.loadAddMusicPlayer(id, plb, ms, 0, (result, time, player1, retry) -> {
                    player.displayClientMessage(new TextComponent(result + ":" + time + ":" + retry), false);
                    me.playMusicPlayer(id, 0);
                });
            }*/
            var shpe = TentativeVoxelShapeGenerator.generate(OEVoxelShapeUtil.getShapeFromResource(new ResourceLocation(IamMusicPlayer.MODID, "boombox")), OEVoxelShapeUtil.getShapeFromResource(new ResourceLocation(IamMusicPlayer.MODID, "boombox_base")));
            try {
                Files.writeString(Paths.get("shape.json"), new Gson().toJson(shpe));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide);
    }
}

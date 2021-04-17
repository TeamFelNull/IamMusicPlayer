package red.felnull.imp.item;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import red.felnull.imp.data.MusicSaveData;
import red.felnull.imp.data.resource.AdministratorInformation;
import red.felnull.imp.data.resource.ImageLocation;
import red.felnull.imp.music.resource.*;
import red.felnull.otyacraftengine.data.WorldDataManager;

import java.util.*;

public class TestSoundItem extends Item {
    public TestSoundItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        if (level.isClientSide()) {
            player.displayClientMessage(new TextComponent("Ikisugi"), false);
        } else {
            UUID uuid = UUID.randomUUID();

            Map<UUID, AdministratorInformation.AuthorityType> players = new HashMap<>();
            players.put(player.getGameProfile().getId(), AdministratorInformation.AuthorityType.READ_ONLY);

            Music ms = new Music(uuid, "ikisugi", 114514, new MusicLocation(MusicLocation.LocationType.URL, "https://cdn.discordapp.com/attachments/358878159615164416/831304524001837086/pigstep.mp3"), new MusicDetailed("yj", "ikisugi", "1919", "a", "115"), new ImageLocation(ImageLocation.ImageType.STRING, "TEST"), new AdministratorInformation(false, players));
            WorldDataManager.getInstance().getSaveData(MusicSaveData.class).addMusic(ms);

            UUID uuidpl = UUID.randomUUID();
            List<UUID> adpl = new ArrayList<>();
            adpl.add(uuid);
            MusicPlayList mpl = new MusicPlayList(uuidpl, "aikisugiList", new MusicPlayListDetailed(false), new ImageLocation(ImageLocation.ImageType.STRING, "TEST"), new AdministratorInformation(false, players), adpl);
            WorldDataManager.getInstance().getSaveData(MusicSaveData.class).addPlayList(mpl);
        }
        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide);
    }
}

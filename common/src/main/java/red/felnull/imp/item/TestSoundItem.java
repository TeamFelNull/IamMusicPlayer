package red.felnull.imp.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.music.resource.MusicLocation;
import red.felnull.imp.packet.MusicClientInstructionMessage;
import red.felnull.otyacraftengine.util.IKSGPacketUtil;

import java.util.UUID;

public class TestSoundItem extends Item {
    public TestSoundItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        if (level.isClientSide()) {
            //  player.displayClientMessage(new TextComponent(itemStack.getHoverName().getString()), false);
            // TestMusic.playStart(itemStack.getHoverName().getString());
           /* IMusicPlayerLoader loader = MusicPlayerRegistry.getLoader(new ResourceLocation(IamMusicPlayer.MODID, "youtube"));
            IMusicPlayer musicPlayer = loader.createMusicPlayer(itemStack.getHoverName().getString());
            try {
                musicPlayer.ready(0);
            } catch (Exception e) {
                e.printStackTrace();
            }

            musicPlayer.setSelfPosition(player.position());
            musicPlayer.linearAttenuation(32f);

            musicPlayer.play(0);*/

        } else {
            IKSGPacketUtil.sendToClientPacket((ServerPlayer) player, new MusicClientInstructionMessage(MusicClientInstructionMessage.Type.READY, UUID.randomUUID(), 0, new MusicLocation(new ResourceLocation(IamMusicPlayer.MODID, "http"), itemStack.getHoverName().getString())));
     /*       Map<UUID, AdministratorInformation.AuthorityType> players = new HashMap<>();
            players.put(player.getGameProfile().getId(), AdministratorInformation.AuthorityType.READ_ONLY);

            UUID uuidpl = UUID.randomUUID();
            MusicPlayList mpl = new MusicPlayList(uuidpl, "aikisugiList", new MusicPlayListDetailed(false), new ImageLocation(ImageLocation.ImageType.STRING, "TEST"), new AdministratorInformation(false, players), new ArrayList<>());
            MusicManager.getInstance().addPlayList(mpl);

            UUID uuid = UUID.randomUUID();

            Music ms = new Music(uuid, "ikisugi", 114514, new MusicLocation(MusicLocation.LocationType.URL, "https://cdn.discordapp.com/attachments/358878159615164416/831304524001837086/pigstep.mp3"), new MusicDetailed("yj", "ikisugi", "1919", "a", "115"), new ImageLocation(ImageLocation.ImageType.STRING, "TEST"), new AdministratorInformation(false, players));
            MusicManager.getInstance().addMusic(uuidpl, ms);*/
        }
        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide);
    }
}

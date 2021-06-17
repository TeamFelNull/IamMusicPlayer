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
import red.felnull.imp.music.resource.MusicSource;
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
         /*   IMPSyncClientManager.getInstance().getMyPlayLists().forEach(n->{
                player.displayClientMessage(new TextComponent(n.getName()),false);
            });*/
        } else {
            if (!player.isCrouching()) {
                IKSGPacketUtil.sendToClientPacket((ServerPlayer) player, new MusicClientInstructionMessage(MusicClientInstructionMessage.Type.READY, UUID.randomUUID(), 0, new MusicSource(new ResourceLocation(IamMusicPlayer.MODID, "youtube"), itemStack.getHoverName().getString(), 1)));
            } else {
                IKSGPacketUtil.sendToClientPacket((ServerPlayer) player, new MusicClientInstructionMessage(MusicClientInstructionMessage.Type.READY, UUID.randomUUID(), 0, new MusicSource(new ResourceLocation(IamMusicPlayer.MODID, "http"), itemStack.getHoverName().getString(), 1)));
            }
        }
        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide);
    }
}

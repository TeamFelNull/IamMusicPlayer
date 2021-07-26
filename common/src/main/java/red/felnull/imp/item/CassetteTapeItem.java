package red.felnull.imp.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import red.felnull.imp.data.resource.ImageInfo;
import red.felnull.imp.music.MusicManager;
import red.felnull.imp.music.resource.Music;
import red.felnull.imp.music.resource.dynamic.DynamicMusicList;
import red.felnull.otyacraftengine.item.IkisugiItem;

import java.util.List;
import java.util.Optional;

public class CassetteTapeItem extends IkisugiItem implements DyeableLeatherItem {
    private final BaseType type;

    public CassetteTapeItem(Properties properties, BaseType type) {
        super(properties.stacksTo(1));
        this.type = type;
    }

    public BaseType getType() {
        return type;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        if (!level.isClientSide()) {
            List<Music> musics = MusicManager.getInstance().getPlayerAllMusics(player.getGameProfile().getId());
            DynamicMusicList musicList = new DynamicMusicList("TEST", new ImageInfo(ImageInfo.ImageType.PLAYER_FACE, "MoriMori_0317_jp"), musics);

            setMusicList(itemStack, musicList);

        }
        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }

    public static ItemStack setMusicList(ItemStack stack, DynamicMusicList musicList) {
        stack.getOrCreateTag().put("MusicList", musicList.save(new CompoundTag()));
        return stack;
    }

    public static DynamicMusicList getMusicList(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("MusicList")) {
            return new DynamicMusicList(stack.getTag().getCompound("MusicList"));
        }
        return null;
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack itemStack) {

        if (getMusicList(itemStack) != null) {
            return Optional.of(new CassetteTapeTooltip(getMusicList(itemStack)));
        }

        return super.getTooltipImage(itemStack);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        if (getMusicList(itemStack) != null) {
            list.add(new TextComponent("test"));
        }
    }

    public static enum BaseType {
        NORMAL, GLASS
    }
}

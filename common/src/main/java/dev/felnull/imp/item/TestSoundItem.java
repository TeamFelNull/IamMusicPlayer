package dev.felnull.imp.item;

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

/*
        if (!level.isClientSide()) {
            var shp = TentativeVoxelShapeGenerator.generate(OEVoxelShapeUtil.getShapeFromResource(new ResourceLocation(IamMusicPlayer.MODID, "test/boombox_no_raised_shape_fast"), TestSoundItem.class), OEVoxelShapeUtil.getShapeFromResource(new ResourceLocation(IamMusicPlayer.MODID, "test/boombox_no_raised_shape"), TestSoundItem.class));
            try {
                Files.writeString(Paths.get("./test_shape.json"), new Gson().toJson(shp));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/

        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide);
    }
}

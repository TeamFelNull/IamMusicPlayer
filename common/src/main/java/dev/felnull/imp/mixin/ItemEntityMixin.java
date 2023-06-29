package dev.felnull.imp.mixin;

import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.item.BoomboxItem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @Shadow
    public abstract ItemStack getItem();

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;tick()V", ordinal = 0))
    private void tick(CallbackInfo ci) {
        if (IamMusicPlayer.getConfig().dropItemRing && getItem().getItem() instanceof BoomboxItem) {
            var ths = (ItemEntity) (Object) this;
            BoomboxItem.tick(ths.level(), ths, getItem(), true);
        }
    }
}

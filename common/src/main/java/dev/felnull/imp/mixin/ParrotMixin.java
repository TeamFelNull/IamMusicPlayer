package dev.felnull.imp.mixin;

import dev.felnull.imp.entity.IRingerPartyParrot;
import net.minecraft.world.entity.animal.Parrot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(Parrot.class)
public class ParrotMixin implements IRingerPartyParrot {
    private UUID ringerUUID;

    @Inject(method = "isPartyParrot", at = @At("RETURN"), cancellable = true)
    private void isPartyParrot(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValue() || ringerUUID != null);
    }

    @Override
    public void setRingerUUID(UUID uuid) {
        this.ringerUUID = uuid;
    }

    @Override
    public UUID getRingerUUID() {
        return ringerUUID;
    }
}

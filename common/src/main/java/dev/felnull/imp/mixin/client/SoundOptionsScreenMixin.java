package dev.felnull.imp.mixin.client;

import dev.felnull.imp.client.IamMusicPlayerClient;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.screens.SoundOptionsScreen;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SoundOptionsScreen.class)
public class SoundOptionsScreenMixin {
    @Inject(method = "getAllSoundOptionsExceptMaster", at = @At("RETURN"), cancellable = true)
    private void getAllSoundOptionsExceptMaster(CallbackInfoReturnable<OptionInstance<?>[]> cir) {
        cir.setReturnValue(ArrayUtils.add(cir.getReturnValue(), IamMusicPlayerClient.IMP_VOLUME_OPTION));
    }
}

package dev.felnull.imp.mixin.client;

import com.mojang.blaze3d.audio.Library;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Library.class)
public class LibraryMixin {
    /*@Inject(method = "getDebugString", at = @At("RETURN"), cancellable = true)
    private void getDebugString(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue(cir.getReturnValue() + " " + MusicEngineOld.getInstance().getDebugString());
    }*/
}

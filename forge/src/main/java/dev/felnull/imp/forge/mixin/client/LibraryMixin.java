package dev.felnull.imp.forge.mixin.client;

import com.mojang.blaze3d.audio.Library;
import dev.felnull.imp.client.music.MusicEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Library.class)
public class LibraryMixin {
    @Inject(method = "getDebugString", at = @At("RETURN"), cancellable = true)
    private void getDebugString(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue(cir.getReturnValue() + " " + MusicEngine.getInstance().getDebugString());
    }
}

package dev.felnull.imp.mixin.client;

import dev.felnull.imp.client.music.MusicEngine;
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundEngine.class)
public class SoundEngineMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(boolean bl, CallbackInfo ci) {
        MusicEngine.getInstance().tick();
    }

    @Inject(method = "reload", at = @At("HEAD"))
    private void reload(CallbackInfo ci) {
        MusicEngine.getInstance().destroy();
    }

    @Inject(method = "reload", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sounds/SoundEngine;destroy()V"))
    private void reloadFlag(CallbackInfo ci) {
        MusicEngine.getInstance().reloadFlag = true;
    }

    @Inject(method = "stopAll", at = @At("HEAD"))
    private void stopAll(CallbackInfo ci) {
        MusicEngine.getInstance().stopAll();
    }
}

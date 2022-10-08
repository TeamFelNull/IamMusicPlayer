package dev.felnull.imp.mixin.client;

import dev.felnull.imp.client.music.MusicEngineOld;
import dev.felnull.imp.client.nmusic.MusicEngine;
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundEngine.class)
public class SoundEngineMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(boolean bl, CallbackInfo ci) {
        MusicEngineOld.getInstance().tick();
        MusicEngine.getInstance().tick();
    }

    @Inject(method = "reload", at = @At("HEAD"))
    private void reload(CallbackInfo ci) {
        MusicEngineOld.getInstance().reload();
        MusicEngine.getInstance().destroy();
    }

    @Inject(method = "reload", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sounds/SoundEngine;destroy()V"))
    private void reloadFlag(CallbackInfo ci) {
        MusicEngineOld.getInstance().reloadFlag = true;
        MusicEngine.getInstance().reloadFlag = true;
    }

    @Inject(method = "stopAll", at = @At("HEAD"))
    private void stopAll(CallbackInfo ci) {
        MusicEngineOld.getInstance().stop();
        MusicEngine.getInstance().stop();
    }
}

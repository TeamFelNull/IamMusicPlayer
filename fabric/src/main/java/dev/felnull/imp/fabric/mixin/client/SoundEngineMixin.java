package dev.felnull.imp.fabric.mixin.client;

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
        MusicEngine.getInstance().tick(bl);
    }

    @Inject(method = "pause", at = @At("HEAD"))
    private void pause(CallbackInfo ci) {
        MusicEngine.getInstance().pause();
    }

    @Inject(method = "resume", at = @At("HEAD"))
    private void resume(CallbackInfo ci) {
        MusicEngine.getInstance().resume();
    }

    @Inject(method = "reload", at = @At("HEAD"))
    private void reload(CallbackInfo ci) {
        MusicEngine.getInstance().reload();
    }

    @Inject(method = "stopAll", at = @At("HEAD"))
    private void stopAll(CallbackInfo ci) {
        MusicEngine.getInstance().stop();
    }
}

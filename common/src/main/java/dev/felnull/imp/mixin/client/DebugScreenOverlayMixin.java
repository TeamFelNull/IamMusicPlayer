package dev.felnull.imp.mixin.client;

import dev.felnull.imp.client.nmusic.MusicEngine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(DebugScreenOverlay.class)
public class DebugScreenOverlayMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "getGameInformation", at = @At("RETURN"))
    private void getGameInformation(CallbackInfoReturnable<List<String>> cir) {
        if (!this.minecraft.showOnlyReducedInfo())
            cir.getReturnValue().add(MusicEngine.getInstance().getDebugString());
    }
}

package com.tmquan2508.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.tmquan2508.MeteorNetherCrackerAddon;

@Mixin(MinecraftClient.class)
public abstract class MeteorNetherCrackerMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void onGameLoaded(RunArgs args, CallbackInfo ci) {
        MeteorNetherCrackerAddon.LOG.info("Hello from ExampleMixin!");
    }
}
